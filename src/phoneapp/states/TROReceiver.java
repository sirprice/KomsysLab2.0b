package phoneapp.states;

import java.net.Socket;

/**
 * Created by cj on 2016-10-04.
 */
public class TROReceiver extends ClientSipState {
    private Socket currentSocket;

    public TROReceiver(Socket currentSocket, String sip_ip_to, String sip_ip_from, int port) {
        System.out.println("Entering TROReceiver");
        this.currentSocket = currentSocket;
    }

    @Override
    public ClientSipState recieveAck() {
        return new InSession(currentSocket);
    }

    @Override
    public String getStatename() {
        return "TRO RECEIVER";
    }
}
