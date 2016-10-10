package phoneapp.states;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by cj on 2016-10-04.
 */
public abstract class ClientSipState {
    public boolean setSessionSocket(Socket socket) {return false;};
    public boolean hasTimedOut() {return true;};
    public ClientSipState sendInvite(Socket socket, String body) {return this;}
    public ClientSipState sendBye(String body) {return this;}
    public ClientSipState recieveInvite(Socket socket, String body){
        System.out.println("ClientSipState:recieveInvite: default ");
        PrintWriter output = null;
        try {
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            output.println("BUSY");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
    public ClientSipState recieveTRO(String body){return this;}
    public ClientSipState recieveBye() throws IOException {return this;}
    public ClientSipState recieveOk() throws IOException {return this;}
    public ClientSipState recieveAck() throws IOException {return this;}
    public ClientSipState recieveBusy(){
        System.out.println("ClientSipState:recieveBusy: default ");
        return new Free();}
    public ClientSipState recieveInvalid(String body){return this;}
    public boolean isConnceted() {return true;}
    public String getStatename(){return "unknown state";};
}
