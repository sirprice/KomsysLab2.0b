package phoneapp.states;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

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
        String sip_me = null;
        String sip_to = null;
        int port = (new Random()).nextInt(10000) + 44000;
        try {
            sip_me = Inet4Address.getLocalHost().getHostAddress();
            sip_to = socket.getInetAddress().toString();
            System.out.println("sip_to: " + sip_to);
            System.out.println("sip_from: " + sip_me);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            output.println("INVITE " + sip_me + " " + sip_to + " " + port);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TROCaller(socket);
    }

    @Override
    public ClientSipState recieveInvite(Socket socket,String body) {
        System.out.println("Free:recieveInvite: body:" + body);
        PrintWriter output = null;
        try {
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            output.println("TRO");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TROReceiver(socket,"","",1);
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
