package phoneapp.states;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by cj on 2016-10-04.
 */
public class WaitFoOk extends ClientSipState {
    private Socket currentSocket;

    public WaitFoOk(Socket currentSocket) {
        System.out.println("Entering TROReceiver");
        PrintWriter output = null;
        try {
            output = new PrintWriter(new OutputStreamWriter(currentSocket.getOutputStream()));
            output.println("ACK");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.currentSocket = currentSocket;
    }
}
