package phone;/**
 * Created by o_0 on 2016-10-01.
 */

import phone.controler.PhoneAppController;
import phone.view.PhoneView;
import javafx.application.Application;
import javafx.stage.Stage;

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
        this.controler = new PhoneAppController(view);
        controler.startApp();
    }
}
