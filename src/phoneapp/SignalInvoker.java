package phoneapp;

import phoneapp.states.ClientSipState;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by o_0 on 2016-10-05.
 */
public interface SignalInvoker {
    ClientSipState invoke(Socket fromSocket, ClientSipState state, String body) throws IOException;
}
