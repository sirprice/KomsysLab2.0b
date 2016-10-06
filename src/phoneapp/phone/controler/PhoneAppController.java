package phoneapp.phone.controler;//package phoneapp.phone.controler;

import phoneapp.CommunicationHub;
import phoneapp.phone.view.PhoneView;

/**
 * Created by o_0 on 2016-10-01.
 */
public class PhoneAppController {
    private PhoneView userView;
    private CommunicationHub hub;

    /**
     * Construct the controller for the adminTool in a MVC application
     *
     * @param userView the user view
     */
    public PhoneAppController(PhoneView userView, CommunicationHub comuncation) {
        this.userView = userView;
        this.hub = comuncation;
    }

    public void startCall(String ip) {
        hub.sendInvite(ip);
    }

    public void endCall() {
        hub.endCall();
    }

    /**
     * Starts the application
     */
    public void startApp() {
        hub.startServer();
        userView.setControllerDelegate(this);
        userView.init();
        userView.showScene();
    }
}