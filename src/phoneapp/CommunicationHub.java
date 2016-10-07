package phoneapp;

import phoneapp.states.ClientSipState;
import phoneapp.states.Free;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    private ExecutorService threadPool;
    private static final String DELIMITERS = " \n";
    private ConcurrentHashMap<String, SignalInvoker> signalList = new ConcurrentHashMap<String, SignalInvoker>();

    private ServerSocket serverSocket = null;

    public CommunicationHub(int port) throws IOException {
        this.threadPool = Executors.newCachedThreadPool();
        this.listeningPort = (port <= 0) ? 5060 : port;
        this.currentState = new AtomicReference<>();
        this.currentState.set(new Free());
        this.serverSocket = new ServerSocket(port);
        registrateAllInSignals();
        System.out.println("Can accept call: ip: " + Inet4Address.getLocalHost() + " port: " + listeningPort);
    }

    private void registrateAllInSignals() {
        //signalList.put("INVITE",new phoneapp.Invoker.InvokeInvite());
        signalList.put("TRO", new Invoker.InvokeTRO());
        signalList.put("200", new Invoker.InvokeOK());
        signalList.put("ACK", new Invoker.InvokeAck());
        signalList.put("BUSY", new Invoker.InvokeBusy());
        signalList.put("INVALID", new Invoker.InvokeInvalid());
        signalList.put("BYE", new Invoker.InvokeBye());
    }

    public void startServer() {
        threadPool.execute(this);
    }

    public void shutdownServer() {
        running.set(false);
        try {
            serverSocket.close();
            endCall();
            threadPool.shutdown();
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
        while (connected || running.get()) {

            String msg = input.readLine();
            ClientSipState oldState = this.currentState.get();
            SignalInvoker signalInvoker = evaluateCommand(msg);
            ClientSipState newState = signalInvoker.invoke(socket,oldState, msg);

            boolean b = this.currentState.compareAndSet(oldState, newState);
            if (!b) {
                return;
            }

            connected = currentState.get().isConnceted();
        }
    }


    public SignalInvoker evaluateCommand(String msg) {
        System.out.println("Msg to eval: " + msg);
        if (msg == null) {
            return new Invoker.InvokeInvalid();
        }
        StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITERS);
        String cmd = null;
        String body = "";

        if (tokenizer.hasMoreTokens()) {
            cmd = tokenizer.nextToken();
            System.out.println("EvaluateCommand: Command = " + cmd);
        }
        while (tokenizer.hasMoreTokens()) {
            body += tokenizer.nextToken() + " ";
        }
        System.out.println("EvaluateCommand: Body = " + body);
        return invokeSignal(cmd);
    }

    private void handleConnection(Socket socket) {
        BufferedReader input = null;
        try {
            System.out.println("handeling Incoming connection");
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = input.readLine();
            System.out.println("handleIncomingConncetion: incoming msg: " + msg);

            // this always should be from a new connection
            ClientSipState oldState = this.currentState.get();
            ClientSipState newState = oldState.recieveInvite(socket, msg);

            if (oldState == newState) {
                System.out.println("handleConnection;Busy");
                return;
            }
            boolean b = this.currentState.compareAndSet(oldState, newState);
            if (!b) {
                System.out.println("CurrentState.CompareAndSet failed!");
                return;
            }
            handelOpenConnection(socket);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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



    public void handleIncomingConnection(final Socket socket){
        Runnable invit = new Runnable() {
            @Override
            public void run() {
                handleConnection(socket);
            }
        };
        threadPool.execute(invit);
    }

    public void sendInvite(String ip) {
        System.out.println("Starting call");
        Runnable invit = new Runnable() {
            @Override
            public void run() {
                try (Socket socket = new Socket(ip, listeningPort)) {
                    System.out.println("sending invite");
                    ClientSipState oldState = currentState.get();
                    ClientSipState newState = oldState.sendInvite(socket, "INVITE");
                    if(oldState == newState) {
                        System.out.println("sendInvite:busy");
                        return;
                    }
                    boolean b = currentState.compareAndSet(oldState, newState);
                    if (!b) {
                        System.out.println("sendInvite:failed");
                        return;
                    }
                    handelOpenConnection(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        threadPool.execute(invit);
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

                    handleIncomingConnection(incomingConnection);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
