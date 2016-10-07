package phoneapp.states;

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
        System.out.println("TROCaller: Enter TROCaller state");
        this.currentSocket = currentSocket;
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
        return new InSession(currentSocket);
    }

    @Override
    public String getStatename() {
        return "TRO CALLER";
    }
}
