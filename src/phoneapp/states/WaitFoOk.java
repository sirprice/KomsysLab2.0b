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
        System.out.println("Entering WaitFoOk");
        this.currentSocket = currentSocket;
    }

    @Override
    public ClientSipState recieveOk() {
        System.out.println("WaitFoOk:recieveOk");
        return new Free();
    }
    @Override
    public String getStatename() {
        return "WAIT FOR OK";
    }
}
