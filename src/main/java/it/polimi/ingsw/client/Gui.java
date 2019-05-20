package it.polimi.ingsw.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Gui extends Application implements ClientView{
    private Client client;
    private Text actiontarget;
    private Text disconnectionText;
    private Button btn;

    public static void main(String[] args){
        launch(args);
    }

    public void printLoginMessage(String text, boolean loginSuccessful){
        actiontarget.setFill(Color.FIREBRICK);
        actiontarget.setText(text);
        if(loginSuccessful){
            btn.setDisable(true);
        }
    }

    public void printDisconnectionMessage(String text){
        actiontarget.setFill(Color.BLUE);
        actiontarget.setText(text);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome to Adrenaline!");

        GridPane grid = new GridPane();
        Text sceneTitle = new Text("Welcome");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        disconnectionText = new Text();
        grid.add(disconnectionText, 0,7);


        btn.setOnAction(event -> {
            client = new Client(this);
            client.login("socket", userTextField.getText());
        });

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
