package phoneapp.phone.view;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import phoneapp.phone.controler.PhoneAppController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Created by o_0 on 2016-10-01.
 */
public class PhoneView {
    private Stage primaryStage;
    private PhoneAppController controllerDelegate = null;
    private Scene scene;

    private UpdateTimer updateTimer;

    private TextField ipField = new TextField();
    private HBox horizontalBox1 = new HBox();
    private HBox horizontalBox2 = new HBox();
    private HBox horizontalBox3 = new HBox();
    private VBox mainBox = new VBox();

    private Button call;
    private Button endCall;
    private Button answer;
    private Label label1 = new Label("State: ");
    private Label label2 = new Label("Messages: ");
    private Label messageLabel;
    private Label stateLabel;


    /**
     * These steps initializes the admin user view. Creating buttons, Vboxes,hboxes... etc.
     * No rocket science here really.
     * @param stage
     */
    public PhoneView(Stage stage) {
        this.primaryStage = stage;
    }
    public void setControllerDelegate(PhoneAppController delegate) {
        this.controllerDelegate = delegate;
    }
    public void init() {

        Label pageTitle = new Label("PhoneApp");
        pageTitle.setFont(new Font("Arial", 20));


        initLabel();
        initButtons();
        initTextFields();

        updateTimer = new UpdateTimer();

        horizontalBox1.getChildren().addAll(ipField, call, endCall);
        horizontalBox1.setSpacing(3);

        horizontalBox2.getChildren().addAll(label2,messageLabel);
        horizontalBox3.getChildren().addAll(label1,stateLabel);
        mainBox.setSpacing(6);
        mainBox.setPadding(new Insets(10, 0, 0, 10));
        mainBox.getChildren().addAll(pageTitle, horizontalBox1,horizontalBox2,horizontalBox3);

        Group root = new Group();
        scene = new Scene(root,400,200);

        ((Group) scene.getRoot()).getChildren().addAll(mainBox);
    }

    private void initLabel() {
        messageLabel = new Label();
        messageLabel.setText("Message");
        stateLabel = new Label();
        stateLabel.setText("Default");
    }

    /**
     * Sets the userView in the primaryStage
     */
    public void showScene() {
        primaryStage.setTitle("Phone World! ");
        primaryStage.setScene(scene);
        primaryStage.show();
        updateTimer.start();

    }


    private void initTextFields() {
        ipField.setPromptText("Ip");
        ipField.setMaxWidth(350);
    }


    /**
     * Creator for buttons
     *
     * @param lable
     * @param eventEventhandler
     * @return Button
     */
    private Button createButton(String lable, EventHandler<ActionEvent> eventEventhandler) {
        Button button = new Button();
        button.setText(lable);
        button.setOnAction(eventEventhandler);
        return button;
    }

    /**
     * Initiate all the buttons!!
     */
    private void initButtons() {
        call = createButton("Call", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Calling.....");
                controllerDelegate.startCall(ipField.getText());
            }
        });

        endCall = createButton("End Call", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Ending Call.....");
                controllerDelegate.endCall();

            }
        });
    }
    public void cleanUp(){
        updateTimer.stop();
    }

    private class UpdateTimer extends AnimationTimer{
        @Override
        public void handle(long now) {
            stateLabel.setText(controllerDelegate.getStateName());
        }
    }
}
