package phone.view;

import phone.controler.PhoneAppController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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



    private TextField ipField = new TextField();
    private HBox horizontalBox1 = new HBox();
    private HBox horizontalBox2 = new HBox();
    private VBox mainBox = new VBox();

    private Button call;
    private Button endCall;
    private Button answer;
    private Label messageLabel;


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


        horizontalBox1.getChildren().addAll(ipField, call, endCall);
        horizontalBox1.setSpacing(3);

        horizontalBox2.getChildren().add(messageLabel);

        mainBox.setSpacing(6);
        mainBox.setPadding(new Insets(10, 0, 0, 10));
        mainBox.getChildren().addAll(pageTitle, horizontalBox1,horizontalBox2);

        Group root = new Group();
        scene = new Scene(root,400,200);

        ((Group) scene.getRoot()).getChildren().addAll(mainBox);
    }

    private void initLabel() {
        messageLabel = new Label();
        messageLabel.setText("FREE");
    }

    /**
     * Sets the userView in the primaryStage
     */
    public void showScene() {
        primaryStage.setTitle("Phone World! ");
        primaryStage.setScene(scene);
        primaryStage.show();
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
            }
        });

        endCall = createButton("End Call", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Ending Call.....");

            }
        });
    }
    public void changeMessage(String newMessage){


    }
}
