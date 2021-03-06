package phoneapp.states;

import phoneapp.AudioStreamUDP;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by cj on 2016-10-04.
 */
public class InSession extends ClientSipState {
    private Socket currentSocket;
    private AudioStreamUDP audioStreamUDP;

    public InSession(Socket currentSocket, AudioStreamUDP audioStreamUDP) {
        System.out.println("Entering InSession");
        this.currentSocket = currentSocket;
        System.out.println("Current socket set in insession: "+ currentSocket);
        this.audioStreamUDP = audioStreamUDP;
        audioStreamUDP.startStreaming();
    }

    @Override
    public ClientSipState sendBye(String body) {
        System.out.println("InSession;sendBye");
        PrintWriter output = null;
        audioStreamUDP.close();
        audioStreamUDP.stopStreaming();
        try {
            output = new PrintWriter(new OutputStreamWriter(currentSocket.getOutputStream()));
            output.println("BYE");
            output.flush();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return new WaitFoOk(currentSocket);
    }

    @Override
    public ClientSipState recieveBye() throws IOException {
        System.out.println("InSession;recieveBye");
        PrintWriter output = null;
        audioStreamUDP.close();
        audioStreamUDP.stopStreaming();
        try {
            System.out.println("Current socket = "+currentSocket);
            output = new PrintWriter(new OutputStreamWriter(currentSocket.getOutputStream()));
            output.println("200 OK");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentSocket.close();
        return new Free();
    }
    @Override
    public String getStatename() {
        return "IN SESSION";
    }

    @Override
    public ClientSipState recieveConnectionDroped() throws IOException {
        audioStreamUDP.close();
        audioStreamUDP.stopStreaming();
        return super.recieveConnectionDroped();
    }

    @Override
    public boolean hasTimedOut() {
        return false;
    }
}
