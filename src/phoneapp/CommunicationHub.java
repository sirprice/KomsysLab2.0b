package phoneapp;

import phoneapp.states.ClientSipState;
import phoneapp.states.Free;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by cj on 2016-10-05.
 */
public class CommunicationHub implements Runnable {

    private AtomicReference<ClientSipState> currentState;
    private AtomicBoolean running = new AtomicBoolean(true);
    private int listeningPort;
    //private ClientSipState currentState = new Free();
    private static final String DELIMITERS = " ";
    private ConcurrentHashMap<String, SignalInvoker> signalList = new ConcurrentHashMap<String, SignalInvoker>();

    private ServerSocket serverSocket = null;
    public CommunicationHub(int port) throws IOException {
        this.listeningPort = (port <= 0) ? 5060 : port;
        this.currentState = new AtomicReference<>();
        this.currentState.set(new Free());
        this.serverSocket = new ServerSocket(port);
        registrateAllInSignals();
    }

    private void registrateAllInSignals() {
        //signalList.put("INVITE",new phoneapp.Invoker.InvokeInvite());
        signalList.put("TRO",new Invoker.InvokeTRO());
        signalList.put("200",new Invoker.InvokeOK());
        signalList.put("ACK",new Invoker.InvokeAck());
        signalList.put("BUSY",new Invoker.InvokeBusy());
        signalList.put("INVALID",new Invoker.InvokeInvalid());
    }

    public void startServer() {
        Thread th = new Thread(this);
        th.start();
    }

    public void shutdownServer() {
        running.set(false);
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SignalInvoker invokeSignal(String signal) {
        SignalInvoker invoker = signalList.get(signal);
        if (invoker == null) {
            invoker = signalList.get("INVALID");
        }
        return invoker;
    }

    private Object lockCurrentState = new Object();

    public void sendInvite(String ip) {
        System.out.println("Starting call");
        try (Socket socket = new Socket(ip,listeningPort)) {
            System.out.println("sending invite");
            ClientSipState oldState = this.currentState.get();
            ClientSipState newState = oldState.sendInvite(socket, "INVITE");
            boolean b = this.currentState.compareAndSet(oldState, newState);
            if (!b || oldState == newState) {
                return;
            }
            handelOpenConnection(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endCall() {
        ClientSipState oldState = this.currentState.get();
        ClientSipState newState = oldState.sendBye("");
        boolean b = this.currentState.compareAndSet(oldState, newState);
        if (!b || oldState == newState) {
            return;
        }
    }

    private void handelOpenConnection(Socket socket) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Opening connection");
        boolean connected = currentState.get().isConnceted();
        while (connected) {
            String msg = input.readLine();
            ClientSipState oldState = this.currentState.get();
            SignalInvoker signalInvoker = evaluateCommand(msg);
            ClientSipState newState = signalInvoker.invoke(oldState, msg);

            boolean b = this.currentState.compareAndSet(oldState, newState);
            if (!b) {
                return;
            }

            connected = currentState.get().isConnceted();
        }
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
            System.out.println("handeling Incoming connection");
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = input.readLine();
            // this always should be from a new connection
            ClientSipState oldState = this.currentState.get();
            ClientSipState newState = oldState.recieveInvite(socket, msg);
            boolean b = this.currentState.compareAndSet(oldState, newState);
            if (!b || oldState == newState) {
                return;
            }
            handelOpenConnection(socket);

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
        try {

        while (running.get()) {
            try {
                System.out.println("waiting for connection...");
                Socket incomingConnection = serverSocket.accept();
                System.out.println("Incoming connection!");
                handleIncomingConncetion(incomingConnection);
//                if(!currentState.isConnected()) {
//                    handleIncomingConncetion(incomingConnection);
//                }else {
//                    // TODO: 2016-10-05 handle busy
//                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }finally {
            try {
                if(serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}