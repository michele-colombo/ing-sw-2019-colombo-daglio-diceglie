package it.polimi.ingsw.client.userInterface;

import it.polimi.ingsw.client.MatchView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoginGui {
    private final BorderPane view;
    private Text actionTarget;
    private Button loginButton;
    private BoardGui boardGui;


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

        /*final Rectangle rectBasicTimeline = new Rectangle(100, 50, 100, 50);
        rectBasicTimeline.setFill(Color.RED);
        Gui.timeline = new Timeline();
        Gui.timeline.setCycleCount(Timeline.INDEFINITE);
        Gui.timeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(rectBasicTimeline.xProperty(), 300);
        final KeyFrame kf = new KeyFrame(Duration.millis(5000), kv);
        view.setLeft(rectBasicTimeline);
        Gui.timeline.getKeyFrames().add(kf);*/


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

    public void startMatchUpdate(MatchView match){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Parent newView = new BoardGui(match.getLayout().getLayoutConfiguration()).getView();
                boardGui = new BoardGui(0);
                Parent newView = boardGui.getView();
                view.getScene().setRoot(newView);
            }
        });
    }

    //todo qualcosa che faccia partire la board

    public Parent getView(){
        return view;
    }
}
