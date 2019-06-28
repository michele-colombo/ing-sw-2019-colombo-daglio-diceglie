package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.LayoutView;
import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.userInterface.UserInterface;
import it.polimi.ingsw.client.PlayerView;
import it.polimi.ingsw.server.model.enums.PlayerState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.Map;

/**
 * This Class starts GUI main, creating a window in which to choose network technology
 */

public class Gui extends Application implements UserInterface {
    /**
     * Reference to the object who receives messages from server
     */
    private static Client client;
    /**
     * Text that show the contents of login message from server
     */
    private static Text disconnectionText;
    /**
     * GridPane who contains the element of the current scene
     */
    private GridPane grid;
    /**
     * The current scene displayed on the stage
     */
    private Scene scene;
    /**
     * Reference to the login layer
     */
    private LoginGui loginGui;
    /**
     * Reference to the board layer
     */
    private BoardGui boardGui;
    /**
     * Reference to the actual screen
     */
    private static Rectangle2D screenBounds;
    /**
     * Reference to the screen in which choose how to use a weapon
     */
    private ModeChoiceScreen modeChoiceScreen;

    public static void main(String[] args){
        launch(args);
    }


    /**
     * Makes GUI to return to the connection selection window
     */
    @Override
    public void showConnectionSelection() {
        changeScene(grid);
    }

    /**
     * Create a login layer and changes the current scene into the login scene
     */
    @Override
    public void showLogin() {
        loginGui = new LoginGui();

        Platform.runLater( () ->   changeScene(loginGui.getView())  );
    }

    /**
     * Makes loginGui to show the content of a LoginMessage
     * @param text The text that loginGui shows
     * @param loginSuccessful The situation of the login
     */
    @Override
    public void printLoginMessage(String text, boolean loginSuccessful){
        loginGui.printLoginMessage(text, loginSuccessful);
    }

    /**
     * Creates the windows into show the GUI
     * @param primaryStage
     */
    @Override
    public synchronized void start(Stage primaryStage) {
        initialize(primaryStage);
    }

    /**
     * Creates the first scene, in which displays network technology choice
     * @param primaryStage
     */
    public void initialize(Stage primaryStage){
        Gui.screenBounds = Screen.getPrimary().getBounds();
        Text actionTarget;
        ComboBox comboBox;
        Button btn;
        loginGui = null;
        boardGui = null;
        Stage stage;
        stage = primaryStage;
        stage.setTitle("Welcome to Adrenaline!");
        stage.setFullScreen(true);
        stage.setMinWidth(screenBounds.getWidth()/2);
        stage.setMinHeight(screenBounds.getHeight()/2);

        grid = new GridPane();
        grid.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        Text sceneTitle = new Text("Welcome to ADRENALINE");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 1, 0, 2, 1);
        GridPane.setHalignment(sceneTitle, HPos.CENTER);
        grid.setGridLinesVisible(false);


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

                Gui.client = new Client(this);
                client.createConnection(comboBox.getValue().toString().toLowerCase());
            }
        });

        scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest((WindowEvent) -> {
            System.out.println("Stage is closing");
                try {
                client.shutDown();
            }
                catch (NullPointerException e){
                //nothing to do, it's right
            }

                stage.close();
        });
    }

    /**
     * Creates the board when a startMatch update arrives
     * @param match Contains match information
     */
    @Override
    public void updateStartMatch(MatchView match){
        boardGui = new BoardGui(match);
        boardGui.createSelectableRectangle(match);
        boardGui.createPlayerPositionHBox(match);

        Platform.runLater(() -> {
            changeScene(boardGui.getView());
        });
    }


    /**
     *
     * @return client reference
     */
    public static Client getClient(){
        return client;
    }

    /**
     *
     * @return screenBounds reference
     */
    public static Rectangle2D getScreenBounds(){
        return screenBounds;
    }

    @Override
    public void showAndAskSelection() {
        boardGui.updateSelectables();
    }

    /**
     * Updates the connection state of the other player
     */
    @Override
    public void updateConnection() {
        if(boardGui == null){
            loginGui.updateConnection(client.getConnections());
        } else {
            boardGui.updateConnection(client.getConnections());
        }
    }

    /**
     * Makes board to update layout
     */
    @Override
    public void updateLayout() {
        boardGui.updateLayout(client.getMatch().getLayout());
    }

    /**
     * Makes board to update killshotTrack
     */
    @Override
    public void updateKillshotTrack() {
        boardGui.updateKillshotTrack();
    }

    /**
     * Makes board to update current player
     */
    @Override
    public void updateCurrentPlayer() {
        boardGui.updateCurrentPlayer();
    }

    /**
     * Makes board to update a player's information
     * @param updated Player to be updated
     */
    @Override
    public void updatePlayer(PlayerView updated) {
        boardGui.updatePlayer(updated);
    }

    @Override
    public void updatePayment() {
        return;
    }

    /**
     * Makes board to update a player's weapon
     * @param player Player to be updated
     */
    @Override
    public void updateWeapons(PlayerView player) {
        boardGui.updateWeapons(player);
    }

    /**
     * Makes board to update a player's powerUp
     * @param player Player to be updated
     */
    @Override
    public void updatePowerUp(PlayerView player) {
        boardGui.updatePowerUp(player);
    }

    /**
     * Makes board to update a player's damageTrack
     * @param player Player to be updated
     */
    @Override
    public void updateDamage(PlayerView player) {
        boardGui.updateDamageTrack(player);
    }

    /**
     * Makes board to update your selectables
     */
    @Override
    public void updateSelectables() {
        PlayerView me= client.getMatch().getMyPlayer();

        if(me.getState() == PlayerState.CHOOSE_MODE){
            System.out.println("Selecting modes, si deve aprire");

            if(modeChoiceScreen == null){
                System.out.println("Creating and updating modeSelection");
                modeChoiceScreen= new ModeChoiceScreen();
                Platform.runLater( () ->  changeScene(modeChoiceScreen.getParent()));

            }
            else{
                System.out.println("updating modeSelection");
                modeChoiceScreen.update();
            }
        }
        else{
            if(modeChoiceScreen != null){
                modeChoiceScreen= null;
            }
            boardGui.updateSelectables();
            changeScene(boardGui.getView());
        }
    }

    /**
     * Creates and show an error window
     * @param message The error text
     */
    @Override
    public void printError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();

        alert.setOnCloseRequest( we -> alert.close() );
    }

    @Override
    public void showGameOver(Map<PlayerView, Integer> rank, Map<PlayerView, Integer> points) {

    }

    /**
     * Changes the current scene to a new one
     * @param newView The new scene
     */
    private void changeScene(Parent newView){
        scene.setRoot(newView);
    }

    /**
     * Changes the disconnection text
     * @param text The text to be shown
     */
    public static void setDisconnectionText(Text text){
        disconnectionText = text;
    }

}
