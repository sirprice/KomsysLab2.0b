package phone.controler;

import phone.view.PhoneView;

/**
 * Created by o_0 on 2016-10-01.
 */
public class PhoneAppController {
    private PhoneView userView;

    /**
     * Construct the controller for the adminTool in a MVC application
     *
     * @param userView the user view
     */
    public PhoneAppController(PhoneView userView) {
        this.userView = userView;
    }

    /**
     * Starts the application
     */
    public void startApp() {
        userView.setControllerDelegate(this);
        userView.init();
        userView.showScene();
    }
}