package phoneapp.states;

import java.net.Socket;

/**
 * Created by cj on 2016-10-04.
 */
public class Free extends ClientSipState {
    private Object lock = new Object();
    private Free nextState = this;
    @Override
    public ClientSipState sendInvite(Socket socket, String body) {
        return new TROCaller(socket);
    }

    @Override
    public ClientSipState recieveInvite(Socket socket,String body) {

        return new TROReceiver(socket);
    }

    @Override
    public boolean isConnceted() {
        return false;
    }
}
