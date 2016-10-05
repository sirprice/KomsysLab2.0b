import com.sun.xml.internal.bind.v2.TODO;
import states.ClientSipState;
import states.Free;
import sun.misc.Signal;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cj on 2016-10-05.
 */
public class CommunicationHub implements Runnable {

    private static final String DELIMITERS = " ";
    private ConcurrentHashMap<String, SignalInvoker> signalList = new ConcurrentHashMap<String, SignalInvoker>();

    private ServerSocket serverSocket = null;
    public CommunicationHub(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        registrateAllInSignals();
    }

    private void registrateAllInSignals() {
        signalList.put("INVOKE",new Invoker.InvokeInvite());
        signalList.put("TRO",new Invoker.InvokeTRO());
        signalList.put("200",new Invoker.InvokeOK());
        signalList.put("ACK",new Invoker.InvokeAck());
        signalList.put("BUSY",new Invoker.InvokeBusy());
        signalList.put("INVALID",new Invoker.InvokeInvalid());
    }

    private ClientSipState currentState = new Free();

    public SignalInvoker invokeSignal(String signal) {
        SignalInvoker invoker = signalList.get(signal);
        if (invoker == null) {
            invoker = signalList.get("INVALID");
        }
        return invoker;
    }



    public SignalInvoker evaluateCommand(String msg) {
        StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITERS);
        String cmd = null;
        String body = null;

        if (tokenizer.hasMoreTokens()) {
            cmd = tokenizer.nextToken();
            System.out.println("EvaluateCommand: Command = "+cmd);
        }
        while (tokenizer.hasMoreTokens()){
            body += tokenizer.nextToken() +" ";
        }
        System.out.println("EvaluateCommand: Body = "+body);
        return invokeSignal(cmd);
    }

    private void handleIncomingConncetion(Socket socket) {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            if (currentState.setSessionSocket(socket) == false) {
                return;
            }
            while (currentState.isConnected()) {
                String msg = input.readLine();
                SignalInvoker signalInvoker = evaluateCommand(msg);
                currentState = signalInvoker.invoke(currentState, msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (true) {
            try {
                Socket incomingConnection = serverSocket.accept();
                handleIncomingConncetion(incomingConnection);
                if(!currentState.isConnected()) {
                    handleIncomingConncetion(incomingConnection);
                }else {
                    // TODO: 2016-10-05 handle busy
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
