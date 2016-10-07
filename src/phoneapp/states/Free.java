package phoneapp.states;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by cj on 2016-10-04.
 */
public class Free extends ClientSipState {
    private Object lock = new Object();
    private Free nextState = this;

    public Free() {
        System.out.println("Entering free");
    }

    @Override
    public ClientSipState sendInvite(Socket socket, String body) {
        System.out.println("Free:sendInvite");
        PrintWriter output = null;
        try {
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            output.println("INVITE");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TROCaller(socket);
    }

    @Override
    public ClientSipState recieveInvite(Socket socket,String body) {
        System.out.println("Free:recieveInvite");
        PrintWriter output = null;
        try {
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            output.println("TRO");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TROReceiver(socket);
    }

    @Override
    public boolean isConnceted() {
        return false;
    }

    @Override
    public String getStatename() {
        return "FREE";
    }
}
