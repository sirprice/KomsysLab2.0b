package phoneapp.phone;/**
 * Created by o_0 on 2016-10-01.
 */

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import phoneapp.CommunicationHub;
import phoneapp.phone.controler.PhoneAppController;
import phoneapp.phone.view.PhoneView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class PhoneApp extends Application {

    public static final int width = 800;
    public static final int height = 600;


    public static void main(String[] args) {
        launch(args);
    }
    private PhoneAppController controler;

    /**
     * This starts the javafx app, and creates all views and models and connect them together
     * with a controller, MVC
     * @param primaryStage javafx stage
     */
    @Override
    public void start(Stage primaryStage) {
        PhoneView view = new PhoneView(primaryStage);
        final CommunicationHub hub;
        try {
            hub = new CommunicationHub(55000);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.controler = new PhoneAppController(view,hub);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                hub.shutdownServer();
                view.cleanUp();
            }
        });
        controler.startApp();
    }
}
