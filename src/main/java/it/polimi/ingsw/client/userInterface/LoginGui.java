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

public class LoginGui {
    private final BorderPane view;
    private Text actionTarget;
    private Button loginButton;


    public LoginGui(){
        view = new BorderPane();

        GridPane loginGrid = new GridPane();
        view.setCenter(loginGrid);
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        loginGrid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Login");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        loginGrid.add(sceneTitle, 1, 0, 2, 1);
        GridPane.setHalignment(sceneTitle, HPos.CENTER);

        Label userName = new Label("User Name:");
        loginGrid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        loginGrid.add(userTextField, 1, 1);

        loginButton = new Button("Login");
        GridPane.setHalignment(loginButton, HPos.CENTER);
        loginButton.prefWidthProperty().bind(loginGrid.widthProperty());
        loginButton.prefHeightProperty().bind(loginGrid.heightProperty());
        loginButton.setMaxHeight(Gui.getScreenBounds().getHeight()/25);
        loginButton.setMaxWidth(Gui.getScreenBounds().getWidth()/5);
        //loginButton.setMinHeight(Gui.getScreenHeight()/20);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(loginButton);
        HBox.setHgrow(loginButton, Priority.ALWAYS);
        loginGrid.add(hbBtn, 1, 4);
        loginGrid.add(loginButton,1 ,4);

        actionTarget = new Text();
        loginGrid.add(actionTarget, 1, 6);
        GridPane.setHalignment(actionTarget, HPos.CENTER);
        loginGrid.setGridLinesVisible(true);

        Gui.disconnectionText = actionTarget;


        loginButton.setOnAction(event -> {
            Gui.getClient().chooseName(userTextField.getText());
        });
    }

    public void printLoginMessage(String text, boolean loginSuccessful){
        actionTarget.setFill(Color.GREEN);
        actionTarget.setText(text);
        if(loginSuccessful){
            loginButton.setDisable(true);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Parent newView = new BoardGui().getView();
                    view.getScene().setRoot(newView);
                }
            });
        }
    }

    public Parent getView(){
        return view;
    }
}
