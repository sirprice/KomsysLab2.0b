package phoneapp;

import phoneapp.states.ClientSipState;

/**
 * Created by o_0 on 2016-10-05.
 */
public interface SignalInvoker {
    ClientSipState invoke(ClientSipState state, String body);
}
