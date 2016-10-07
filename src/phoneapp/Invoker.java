package phoneapp;


import phoneapp.states.ClientSipState;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by o_0 on 2016-10-05.
 */
public class Invoker {
    public static class InvokeInvite implements SignalInvoker{
        @Override
        public ClientSipState invoke(Socket fromSocket, ClientSipState state, String body) throws IOException {
            return state.recieveInvite(fromSocket,body);
        }
    }
    public static class InvokeTRO implements SignalInvoker {
        @Override
        public ClientSipState invoke(Socket fromSocket, ClientSipState state, String body) throws IOException {
            return state.recieveTRO();
        }
    }
    public static class InvokeBye implements SignalInvoker {
        @Override
        public ClientSipState invoke(Socket fromSocket, ClientSipState state, String body) throws IOException {
            return state.recieveBye();
        }
    }
    public static class InvokeOK implements SignalInvoker {
        @Override
        public ClientSipState invoke(Socket fromSocket, ClientSipState state, String body) throws IOException {
            return state.recieveOk();
        }
    }
    public static class InvokeAck implements SignalInvoker {
        @Override
        public ClientSipState invoke(Socket fromSocket, ClientSipState state, String body) throws IOException {
            return state.recieveAck();
        }
    }

    public static class InvokeBusy implements SignalInvoker {
        @Override
        public ClientSipState invoke(Socket fromSocket, ClientSipState state, String body) throws IOException {
            return state.recieveBusy();
        }
    }

    public static class InvokeInvalid implements SignalInvoker {
        @Override
        public ClientSipState invoke(Socket fromSocket, ClientSipState state, String body) throws IOException {
            return state.recieveInvalid(body);
        }
    }

}
