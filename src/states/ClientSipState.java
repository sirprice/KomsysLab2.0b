package states;

import java.net.Socket;

/**
 * Created by cj on 2016-10-04.
 */
public abstract class ClientSipState {
    public boolean setSessionSocket(Socket socket) {return false;};
    public ClientSipState recieveInvite(String body){return this;}
    public ClientSipState recieveTRO(){return this;}
    public ClientSipState recieveBye(){return this;}
    public ClientSipState recieveOk(){return this;}
    public ClientSipState recieveAck(){return this;}
    public ClientSipState recieveBusy(){return this;}
    public ClientSipState recieveInvalid(String body){return this;}
    public boolean isConnected() {return false;};
}
