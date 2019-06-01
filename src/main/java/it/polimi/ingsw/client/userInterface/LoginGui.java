package it.polimi.ingsw.client.userInterface;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Map;

public class LoginGui {
    private final BorderPane view;
    private Text actionTarget;
    private Button loginButton;
    private GridPane connectionPane;


    public LoginGui(){
        view = new BorderPane();

        GridPane loginGrid = new GridPane();
        view.setCenter(loginGrid);

        view.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane.setHalignment(loginGrid, HPos.CENTER);
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        //loginGrid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Login");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        loginGrid.add(sceneTitle, 1, 0, 2, 1);
        GridPane.setHalignment(sceneTitle, HPos.CENTER);

        Label userName = new Label("User Name:");
        userName.setMinWidth(80); //todo da riscalare
        loginGrid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        loginGrid.add(userTextField, 1, 1);

        loginButton = new Button("Login");
        GridPane.setHalignment(loginButton, HPos.CENTER);
        loginButton.prefWidthProperty().bind(loginGrid.widthProperty());
        loginButton.prefHeightProperty().bind(loginGrid.heightProperty());
        loginButton.setMaxHeight(Gui.getScreenBounds().getHeight()/25);
        loginButton.setMinHeight(Gui.getScreenBounds().getHeight()/25);
        loginButton.setMaxWidth(Gui.getScreenBounds().getWidth()/5);
        loginButton.setMinWidth(Gui.getScreenBounds().getWidth()/5);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(loginButton);
        HBox.setHgrow(loginButton, Priority.NEVER);
        loginGrid.add(hbBtn, 1, 4);
        loginGrid.add(loginButton,1 ,4);

        actionTarget = new Text();
        loginGrid.add(actionTarget, 1, 6);
        GridPane.setHalignment(actionTarget, HPos.CENTER);
        loginGrid.setGridLinesVisible(false);

        Gui.disconnectionText = actionTarget;
        connectionPane = new GridPane();
        connectionPane.setHgap(10);
        connectionPane.setVgap(25);
        view.setRight(connectionPane);
        GridPane.setHalignment(connectionPane, HPos.RIGHT);

        loginButton.setOnAction(event -> {
            Gui.getClient().chooseName(userTextField.getText());
        });
    }

    public void printLoginMessage(String text, boolean loginSuccessful){
            if(loginSuccessful){
                loginButton.setDisable(true);
                actionTarget.setFill(Color.GREEN);
            } else {
                actionTarget.setFill(Color.RED);
            }
            actionTarget.setText(text);
    }

    public void updateConnection(Map<String, Boolean> connections){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    connectionPane.getChildren().clear();
                    Text text = new Text("OTHER PLAYERS");
                    Text sceneTitle = new Text("Login");
                    sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
                    connectionPane.add(text, 0, 0);
                    int i = 1;
                    for(String string : connections.keySet()){
                        Label playerLabel = new Label(string);
                        playerLabel.setWrapText(true);
                        playerLabel.setMaxWidth(Gui.getScreenBounds().getWidth()/20);
                        //playerLabel.textProperty().bind()
                        connectionPane.add(playerLabel, 0, i);
                        GridPane.setHalignment(playerLabel, HPos.CENTER);
                        i++;
                    }
                }
            });
    }

    public Parent getView(){
        return view;
    }
}
