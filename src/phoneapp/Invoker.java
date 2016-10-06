package phoneapp;


import phoneapp.states.ClientSipState;

/**
 * Created by o_0 on 2016-10-05.
 */
public class Invoker {
//    public static class InvokeInvite implements phoneapp.SignalInvoker {
//        @Override
//        public ClientSipState invoke(ClientSipState state, String body) {
//            return state.recieveInvite(body);
//        }
//    }
    public static class InvokeTRO implements SignalInvoker {
        @Override
        public ClientSipState invoke(ClientSipState state, String body) {
            return state.recieveTRO();
        }
    }
    public static class InvokeBye implements SignalInvoker {
        @Override
        public ClientSipState invoke(ClientSipState state, String body) {
            return state.recieveBye();
        }
    }
    public static class InvokeOK implements SignalInvoker {
        @Override
        public ClientSipState invoke(ClientSipState state, String body) {
            return state.recieveOk();
        }
    }
    public static class InvokeAck implements SignalInvoker {
        @Override
        public ClientSipState invoke(ClientSipState state, String body) {
            return state.recieveAck();
        }
    }

    public static class InvokeBusy implements SignalInvoker {
        @Override
        public ClientSipState invoke(ClientSipState state, String body) {
            return state.recieveBusy();
        }
    }

    public static class InvokeInvalid implements SignalInvoker {
        @Override
        public ClientSipState invoke(ClientSipState state, String body) {
            return state.recieveInvalid(body);
        }
    }

}
