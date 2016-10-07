package phoneapp.states;

import phoneapp.AudioStreamUDP;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by cj on 2016-10-04.
 */
public class TROCaller extends ClientSipState {
    private Socket currentSocket;
    private AudioStreamUDP audioStreamUDP;

    public TROCaller(Socket currentSocket, AudioStreamUDP audioStreamUDP) {
        System.out.println("TROCaller: Enter TROCaller state");
        this.currentSocket = currentSocket;
        this.audioStreamUDP = audioStreamUDP;
    }

    @Override
    public ClientSipState recieveTRO() {
        System.out.println("TROCaller: Receiving TRO call");
        PrintWriter output = null;
        try {
            output = new PrintWriter(new OutputStreamWriter(currentSocket.getOutputStream()));
            output.println("ACK");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new InSession(currentSocket,audioStreamUDP);
    }

    @Override
    public String getStatename() {
        return "TRO CALLER";
    }
}
