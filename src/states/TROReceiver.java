package states;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by cj on 2016-10-04.
 */
public class TROReceiver extends ClientSipState {
    private Socket currentSocket;

    public TROReceiver(Socket currentSocket) {
        this.currentSocket = currentSocket;
    }

    @Override
    public ClientSipState recieveAck() {
        return new InSession(currentSocket);
    }
}
