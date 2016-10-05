package states;

/**
 * Created by cj on 2016-10-04.
 */
public abstract class ClientSipState {

    public ClientSipState recieveInvite(String body){return this;}
    public ClientSipState recieveTRO(){return this;}
    public ClientSipState recieveBye(){return this;}
    public ClientSipState recieveOk(){return this;}
    public ClientSipState recieveAck(){return this;}
    public ClientSipState recieveInvalid(String body){return this;}
}
