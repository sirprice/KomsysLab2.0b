package phoneapp.states;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by cj on 2016-10-04.
 */
public class InSession extends ClientSipState {
    private Socket currentSocket;

    public InSession(Socket currentSocket) {
        this.currentSocket = currentSocket;
    }

    @Override
    public ClientSipState sendBye(String body) {
        PrintWriter output = null;
        try {
            output = new PrintWriter(new OutputStreamWriter(currentSocket.getOutputStream()));
            output.println("200 OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new WaitFoOk(currentSocket);
    }

    @Override
    public ClientSipState recieveBye() {
        PrintWriter output = null;
        try {
            output = new PrintWriter(new OutputStreamWriter(currentSocket.getOutputStream()));
            output.println("200 OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Free();
    }
}
