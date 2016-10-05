package states;

/**
 * Created by cj on 2016-10-04.
 */
public abstract class State {

    public State recieveInvite(String body){return this;}
    public State recieveTRO(){return this;}
    public State recieveBuy(){return this;}
    public State recieveOk(){return this;}
    public State recieveAck(){return this;}



}
