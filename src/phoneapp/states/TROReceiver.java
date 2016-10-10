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
    private int remotePort;
    private InetAddress remoteAddr;

    public TROReceiver(Socket currentSocket, String sip_ip_from,String sip_ip_to, int port) throws IOException {
        System.out.println("Entering TROReceiver");
        this.currentSocket = currentSocket;
        audioStreamUDP = new AudioStreamUDP();
        this.remotePort = port;
        this.remoteAddr = InetAddress.getByName(sip_ip_from);
        System.out.println("Remote Addr: "+remoteAddr);

    }

    @Override
    public ClientSipState recieveAck() throws IOException {
        audioStreamUDP.connectTo(remoteAddr,remotePort);
        audioStreamUDP.startStreaming();
        return new InSession(currentSocket,audioStreamUDP);
    }

    @Override
    public String getStatename() {
        return "TRO RECEIVER";
    }
}
