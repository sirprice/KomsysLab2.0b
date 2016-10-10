package phoneapp.states;

import phoneapp.AudioStreamUDP;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by cj on 2016-10-04.
 */
public class TROReceiver extends ClientSipState {
    private Socket currentSocket;
    private AudioStreamUDP audioStreamUDP;
    private int remotePort, localPort;
    private InetAddress remoteAddr;


    public TROReceiver(Socket currentSocket, int localPort, int remotePort, AudioStreamUDP audioStreamUDP) {
        this.currentSocket = currentSocket;
        this.audioStreamUDP = audioStreamUDP;
        this.remotePort = remotePort;
        this.localPort = localPort;
        this.currentSocket = currentSocket;
    }

    @Override
    public ClientSipState recieveAck() throws IOException {
        return new InSession(currentSocket,audioStreamUDP);
    }

    @Override
    public ClientSipState recieveConnectionDroped() throws IOException {
        audioStreamUDP.close();
        return super.recieveConnectionDroped();
    }
    @Override
    public String getStatename() {
        return "TRO RECEIVER";
    }
}
