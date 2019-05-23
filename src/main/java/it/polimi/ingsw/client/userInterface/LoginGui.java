package it.polimi.ingsw.client.userInterface;

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
    private final GridPane view;
    private Text actionTarget;
    private Button loginButton;


    public LoginGui(){

        view = new GridPane();
        view.setAlignment(Pos.CENTER);
        view.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        view.setHgap(10);
        view.setVgap(10);
        view.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Login");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        view.add(sceneTitle, 1, 0, 2, 1);
        GridPane.setHalignment(sceneTitle, HPos.CENTER);

        Label userName = new Label("User Name:");
        view.add(userName, 0, 1);

        TextField userTextField = new TextField();
        view.add(userTextField, 1, 1);

        loginButton = new Button("Login");
        GridPane.setHalignment(loginButton, HPos.CENTER);
        loginButton.prefWidthProperty().bind(view.widthProperty());
        loginButton.prefHeightProperty().bind(view.heightProperty());
        loginButton.setMaxHeight(Gui.getScreenBounds().getHeight()/25);
        loginButton.setMaxWidth(Gui.getScreenBounds().getWidth()/5);
        //loginButton.setMinHeight(Gui.getScreenHeight()/20);
        /*HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(loginButton);
        HBox.setHgrow(loginButton, Priority.ALWAYS);
        view.add(hbBtn, 1, 4);*/
        view.add(loginButton,1 ,4);

        actionTarget = new Text();
        view.add(actionTarget, 1, 6);
        GridPane.setHalignment(actionTarget, HPos.CENTER);
        view.setGridLinesVisible(true);


        loginButton.setOnAction(event -> {
            Gui.getClient().chooseName(userTextField.getText());
        });
    }

    public void printLoginMessage(String text, boolean loginSuccessful){
        actionTarget.setFill(Color.GREEN);
        actionTarget.setText(text);
        if(loginSuccessful){
            loginButton.setDisable(true);
            /*Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Parent newView = new BoardGui().getView();
                    grid.getScene().setRoot(newView);
                }
            });*/
        }
    }

    public Parent getView(){
        return view;
    }
}
