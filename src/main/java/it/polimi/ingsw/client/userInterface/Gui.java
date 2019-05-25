package it.polimi.ingsw.client.userInterface;

import it.polimi.ingsw.client.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Gui extends Application implements ClientView {
    private static Client client;
    private Text actionTarget;
    public static Text disconnectionText;
    private Button btn;
    private GridPane grid;
    private Stage stage;
    private Scene scene;
    private ComboBox comboBox;
    private LoginGui loginGui;
    private static Rectangle2D screenBounds;

    public static void main(String[] args){
        launch(args);
    }

    public void printLoginMessage(String text, boolean loginSuccessful){
        loginGui.printLoginMessage(text, loginSuccessful);
    }

    public void printDisconnectionMessage(String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                disconnectionText.setFill(Color.GREEN);
                disconnectionText.setText(text);
            }
        });
        //System.out.println(text);
    }

    //public void startMatchUpdate()

    @Override
    public void start(Stage primaryStage) {

        screenBounds = Screen.getPrimary().getBounds();
        loginGui = new LoginGui();
        stage = primaryStage;
        stage.setTitle("Welcome to Adrenaline!");
        stage.setFullScreen(true);
        //stage.setMinWidth(screenWidth/2);
        //stage.setMinHeight(screenHeight/2);


        grid = new GridPane();
        grid.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        Text sceneTitle = new Text("Welcome to ADRENALINE");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 1, 0, 2, 1);
        GridPane.setHalignment(sceneTitle, HPos.CENTER);
        grid.setGridLinesVisible(true);


        Label connection = new Label("Connection:");
        grid.add(connection, 0, 1);

        ObservableList<String> comboItems = FXCollections.observableArrayList(
                "RMI",
                "Socket"
        );
        comboBox = new ComboBox(comboItems);
        comboBox.setMaxHeight(screenBounds.getHeight()/25);
        comboBox.setMaxWidth(screenBounds.getWidth()/5);

        grid.add(comboBox, 1, 1);
        GridPane.setHalignment(comboBox, HPos.CENTER);

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0,0,0,0));


        btn = new Button("Connect");
        btn.prefWidthProperty().bind(grid.widthProperty());
        btn.prefHeightProperty().bind(grid.heightProperty());
        btn.setMaxWidth(comboBox.getMaxWidth());
        btn.setMaxHeight(comboBox.getMaxHeight());
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        HBox.setHgrow(btn, Priority.ALWAYS);
        grid.add(hbBtn, 1, 4);
        grid.add(btn,1 ,4);

        actionTarget = new Text();
        grid.add(actionTarget, 1, 6);
        GridPane.setHalignment(actionTarget, HPos.CENTER);

        btn.setOnAction(event -> {
            if(comboBox.getValue() != null && !comboBox.getValue().toString().isEmpty()){
                client = new Client(this);
                client.createConnection(comboBox.getValue().toString().toLowerCase());
                changeToLogin();
            }
        });

        scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }

    public void changeToLogin(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent newView = loginGui.getView();
                grid.getScene().setRoot(newView);
            }
        });
    }

    public static Client getClient(){
        return client;
    }

    public static Rectangle2D getScreenBounds(){
        return screenBounds;
    }
}
