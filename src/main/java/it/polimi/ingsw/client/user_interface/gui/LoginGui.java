package it.polimi.ingsw.client.user_interface.gui;

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

/**
 * It's the login window of the GUI, in which choose own nickname
 */
public class LoginGui {
    /**
     * String used as title
     */
    private static final String TITLE = "Login";
    /**
     * Chosen font
     */
    private static final String FONT = "Tahoma";
    /**
     * String used when showing user text
     */
    private static final String USERNAME_LABEL = "User Name:";
    /**
     * String used when showing other connected players
     */
    private static final String CONNECTION_TEXT = "OTHER PLAYERS";
    /**
     * Ratio to properly rescale login button height
     */
    private static final int SCALE_RATIO_HEIGHT_LOGIN = 25;
    /**
     * Ratio to properly rescale login button width
     */
    private static final int SCALE_RATIO_WIDTH_LOGIN = 5;
    /**
     * Ratio to properly rescale label of connection pane width
     */
    private static final int SCALE_RATIO_WIDTH_PLAYERS = 20;
    /**
     * Column of connection pane in which put elements
     */
    private static final int CONNECTION_ZERO_ROW = 0;
    /**
     * Column of connection pane in which put elements
     */
    private static final int CONNECTION_ZERO_COLUMN = 0;
    /**
     * Row of login grid in which put elements
     */
    private static final int GRID_ZERO_ROW = 0;
    /**
     * Row of login grid in which put elements
     */
    private static final int GRID_FIRST_ROW = 1;
    /**
     * Column of login grid in which put elements
     */
    private static final int GRID_SIXTH_COLUMN = 6;
    /**
     * Column of login grid in which put elements
     */
    private static final int GRID_FOURTH_COLUMN = 4;
    /**
     * Column of login grid in which put elements
     */
    private static final int GRID_FIRST_COLUMN = 1;
    /**
     * Ratio to properly rescale gap of login grid
     */
    private static final double SCALE_RATIO_GAP_GRID = 153.6;
    /**
     * Chose font of sceneTitle
     */
    private static final double SCALE_RATIO_FONT_SIZE = 76.8;
    /**
     * Ratio to properly rescale username label
     */
    private static final double USERNAME_SCALE_RATIO = 19.2;
    /**
     * The main part of the stage
     */
    private final BorderPane view;
    /**
     * Text in which write the chosen name
     */
    private Text actionTarget;
    /**
     * Button to confirm the written name and send it to the server
     */
    private Button loginButton;
    /**
     * The grid added to view, where actionTarget and loginButton are put
     */
    private GridPane connectionPane;


    /**
     * Create the loginGui with textField in which write chosen name and a button to send name to the server
     */
    public LoginGui(){
        view = new BorderPane();

        GridPane loginGrid = new GridPane();
        view.setCenter(loginGrid);

        view.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane.setHalignment(loginGrid, HPos.CENTER);
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        loginGrid.setHgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_GAP_GRID);
        loginGrid.setVgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_GAP_GRID);

        Text sceneTitle = new Text(TITLE);
        sceneTitle.setFont(Font.font(FONT, FontWeight.NORMAL, Gui.getScreenBounds().getWidth() / SCALE_RATIO_FONT_SIZE));
        loginGrid.add(sceneTitle, GRID_FIRST_ROW, GRID_ZERO_ROW);
        GridPane.setHalignment(sceneTitle, HPos.CENTER);

        Label userName = new Label(USERNAME_LABEL);
        userName.setMinWidth(Gui.getScreenBounds().getWidth() / USERNAME_SCALE_RATIO);
        loginGrid.add(userName, GRID_ZERO_ROW, GRID_FIRST_COLUMN);

        TextField userTextField = new TextField();
        loginGrid.add(userTextField, GRID_FIRST_ROW, GRID_FIRST_COLUMN);

        loginButton = new Button(TITLE);
        GridPane.setHalignment(loginButton, HPos.CENTER);
        loginButton.prefWidthProperty().bind(loginGrid.widthProperty());
        loginButton.prefHeightProperty().bind(loginGrid.heightProperty());
        loginButton.setMaxHeight(Gui.getScreenBounds().getHeight() / SCALE_RATIO_HEIGHT_LOGIN);
        loginButton.setMinHeight(Gui.getScreenBounds().getHeight() / SCALE_RATIO_HEIGHT_LOGIN);
        loginButton.setMaxWidth(Gui.getScreenBounds().getWidth() / SCALE_RATIO_WIDTH_LOGIN);
        loginButton.setMinWidth(Gui.getScreenBounds().getWidth() / SCALE_RATIO_WIDTH_LOGIN);
        HBox hbBtn = new HBox();
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(loginButton);
        HBox.setHgrow(loginButton, Priority.NEVER);
        loginGrid.add(hbBtn, GRID_FIRST_ROW, GRID_FOURTH_COLUMN);
        loginGrid.add(loginButton,GRID_FIRST_ROW ,GRID_FOURTH_COLUMN);

        actionTarget = new Text();
        loginGrid.add(actionTarget, GRID_FIRST_ROW, GRID_SIXTH_COLUMN);
        GridPane.setHalignment(actionTarget, HPos.CENTER);
        loginGrid.setGridLinesVisible(false);

        Gui.setDisconnectionText(actionTarget);
        connectionPane = new GridPane();
        connectionPane.setHgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_GAP_GRID);
        connectionPane.setVgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_GAP_GRID);
        view.setRight(connectionPane);
        GridPane.setHalignment(connectionPane, HPos.RIGHT);

        loginButton.setOnAction(event ->
            Gui.getClient().chooseName(userTextField.getText())
        );
    }

    /**
     * Shows the login message;
     * @param text The text to be written; it's green if loginSuccessful is true, otherwise is red
     * @param loginSuccessful The situation of the login
     */
    public void printLoginMessage(String text, boolean loginSuccessful){
            if(loginSuccessful){
                loginButton.setDisable(true);
                actionTarget.setFill(Color.GREEN);
            } else {
                actionTarget.setFill(Color.RED);
            }
            actionTarget.setText(text);
    }

    /**
     * Create a text for each connected player, waiting for a new match to start
     * @param connections Map with players' name and their connection status
     */
    public void updateConnection(Map<String, Boolean> connections){
            Platform.runLater(() -> {
                connectionPane.getChildren().clear();
                Text text = new Text(CONNECTION_TEXT);
                Text sceneTitle = new Text(TITLE);
                    sceneTitle.setFont(Font.font(FONT, FontWeight.NORMAL, Gui.getScreenBounds().getWidth() / SCALE_RATIO_FONT_SIZE));
                    connectionPane.add(text, CONNECTION_ZERO_ROW, CONNECTION_ZERO_COLUMN);
                int i = 1;
                    for(String string : connections.keySet()){
                    Label playerLabel = new Label(string);
                    playerLabel.setWrapText(true);
                    playerLabel.setMaxWidth(Gui.getScreenBounds().getWidth() / SCALE_RATIO_WIDTH_PLAYERS);
                    connectionPane.add(playerLabel, CONNECTION_ZERO_ROW, i);
                    GridPane.setHalignment(playerLabel, HPos.CENTER);
                    i++;
                }
            });
    }

    /**
     *
     * @return return view
     */
    public Parent getView(){
        return view;
    }
}
