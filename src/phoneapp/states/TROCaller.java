package phoneapp.states;

import phoneapp.AudioStreamUDP;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Created by cj on 2016-10-04.
 */
public class TROCaller extends ClientSipState {
    private Socket currentSocket;
    private AudioStreamUDP audioStreamUDP;
    private String sip_to;
    private int remotePort;
    private static String DELIMITERS = " \n";

    public TROCaller(Socket currentSocket, AudioStreamUDP audioStreamUDP,String sip_to) {
        System.out.println("TROCaller: Enter TROCaller state");
        this.currentSocket = currentSocket;
        this.audioStreamUDP = audioStreamUDP;
        this.sip_to = sip_to;

    }

    @Override
    public ClientSipState recieveTRO(String body) {
        System.out.println("TROCaller: Receiving TRO call");
        if (!parseBody(body)){
            return new Free();
        }

        PrintWriter output = null;
        try {
            audioStreamUDP.connectTo(InetAddress.getByName(sip_to),remotePort);


            output = new PrintWriter(new OutputStreamWriter(currentSocket.getOutputStream()));
            output.println("ACK");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new InSession(currentSocket,audioStreamUDP);
    }

    private boolean parseBody(String body){
        StringTokenizer tokenizer = new StringTokenizer(body, DELIMITERS);
        String[] args = new String[4];
        for (int i = 0; i < 2; i++) {
            if (tokenizer.hasMoreTokens() == false) {
                System.out.println("Invalid string:" + body);
                return false;
            }
            args[i] = tokenizer.nextToken();
        }
        if (!args[0].equals("TRO")) {
            return false;
        }
        try{
            remotePort = Integer.parseInt(args[1]);

        }   catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Override
    public ClientSipState recieveConnectionDroped() throws IOException {
        audioStreamUDP.close();
        return super.recieveConnectionDroped();
    }

    @Override
    public String getStatename() {
        return "TRO CALLER";
    }
}
