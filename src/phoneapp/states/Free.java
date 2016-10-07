package phoneapp.states;

import phoneapp.AudioStreamUDP;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.StringTokenizer;

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
        AudioStreamUDP audioStreamUDP = null;
        int port = 0;

        try {
            audioStreamUDP = new AudioStreamUDP();
            port = audioStreamUDP.getLocalPort();
            sip_me = Inet4Address.getLocalHost().getHostAddress();
            sip_to = socket.getInetAddress().toString().substring(1);
            System.out.println("sip_to: " + sip_to);
            System.out.println("sip_from: " + sip_me);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return this;
        } catch (IOException e) {
            e.printStackTrace();
            return this;
        }
        try {
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            output.println("INVITE " + sip_me + " " + sip_to + " " + port);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return this;
        }
        return new TROCaller(socket, audioStreamUDP);
    }

    private static String DELIMITERS = " \n";

    private ClientSipState parseBody(Socket socket, String body) {

        StringTokenizer tokenizer = new StringTokenizer(body, DELIMITERS);

        String cmd = null;

        String[] args = new String[4];
        for (int i = 0; i < 4; i++) {
            if (tokenizer.hasMoreTokens() == false) {
                System.out.println("Invalid string:" + body);
                return this;
            }
            args[i] = tokenizer.nextToken();
        }
        if (!args[0].equals("INVITE")) {
            return this;
        }

        String sip_from = args[1];
        String sip_to = args[2];
        // TODO: 2016-10-07 get corret port from audio udp class
        int port = 0;
        try {
            port = Integer.parseInt(args[3]);

        }catch (NumberFormatException ex) {
            System.out.println("Invalid string:" + body);
            return this;
        }

        return new TROReceiver(socket, sip_from, sip_to, port);
    }

    @Override
    public ClientSipState recieveInvite(Socket socket, String body) {
        System.out.println("Free:recieveInvite: body:" + body);
        PrintWriter output = null;
        try {
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            output.println("TRO");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseBody(socket,body);
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
