package states;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by cj on 2016-10-04.
 */
public class TROCaller extends ClientSipState {
    private Socket currentSocket;

    public TROCaller(Socket currentSocket) {
        this.currentSocket = currentSocket;
    }

    @Override
    public ClientSipState recieveTRO() {

        PrintWriter output = null;
        try {
            output = new PrintWriter(new OutputStreamWriter(currentSocket.getOutputStream()));
            output.println("ACK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new InSession(currentSocket);
    }
}
