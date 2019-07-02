package it.polimi.ingsw.client.user_interface.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.user_interface.UserInterface;
import it.polimi.ingsw.client.PlayerView;
import it.polimi.ingsw.server.model.enums.Border;
import it.polimi.ingsw.server.model.enums.PlayerState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This Class starts GUI main, creating a window in which to choose network technology
 */

public class Gui extends Application implements UserInterface {
    /**
     * Logger used to properly print exception message
     */
    private static final Logger LOGGER = Logger.getLogger(Gui.class.getName());
    /**
     * String used as title
     */
    private static final String TITLE = "Welcome to ADRENALINE";
    /**
     * String used when showing connection
     */
    private static final String CONNECTION_SOCKET = "Socket";
    /**
     * String used when showing connection
     */
    private static final String CONNECTION_RMI = "RMI";
    /**
     * Chosen font of sceneTitle
     */
    private static final String FONT = "Tahoma";
    /**
     * String used in error alert
     */
    private static final String ERROR_ALERT = "Error";
    /**
     * String used when showing connection
     */
    private static final String CONNECTION_BUTTON = "Connection";
    /**
     * String used when on button to connect
     */
    private static final String CONNECT_BUTTON = "Connect";
    /**
     * String used when closing window
     */
    private static final String WINDOW_EVENT = "Closing";
    /**
     * Ratio to properly rescale font
     */
    private static final double RATIO_FONT_SIZE = 76.8;
    /**
     * Ratio to properly rescale combo box width
     */
    private static final double SCALE_RATIO_WIDTH_COMBO_BOX = 5;
    /**
     * Ratio to properly rescale combo box height
     */
    private static final double SCALE_RATIO_HEIGHT_COMBO_BOX = 25;
    /**
     * Ratio to properly rescale gap grid
     */
    private static final double SCALE_RATIO_GAP_GRID = 153.6;
    /**
     * Ratio to properly rescale the stage
     */
    private static final double SCALE_RATIO_STAGE = 2;
    /**
     * Row of grid in which put elements
     */
    private static final int GRID_ZERO_ROW = 0;
    /**
     * Row of grid in which put elements
     */
    private static final int GRID_FIRST_ROW = 1;
    /**
     * Column of grid in which put elements
     */
    private static final int GRID_SIXTH_COLUMN = 6;
    /**
     * Column of grid in which put elements
     */
    private static final int GRID_FOURTH_COLUMN = 4;
    /**
     * Column of grid in which put elements
     */
    private static final int GRID_FIRST_COLUMN = 1;
    /**
     * Column of grid in which put elements
     */
    private static final int GRID_ZERO_COLUMN = 0;
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

    private static Stage stage;

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
     * @param primaryStage the first and only the of the GUI
     */
    @Override
    public synchronized void start(Stage primaryStage) {
        initialize(primaryStage);
    }

    /**
     * Creates the first scene, in which displays network technology choice
     * @param primaryStage the first and only the of the GUI
     */
    public void initialize(Stage primaryStage){
        Gui.setScreenBounds(Screen.getPrimary().getBounds());
        Text actionTarget;
        ComboBox comboBox;
        Button btn;
        loginGui = null;
        boardGui = null;

        stage = primaryStage;
        stage.setTitle(TITLE);
        stage.setFullScreen(true);
        stage.setMinWidth(screenBounds.getWidth());
        stage.setMinHeight(screenBounds.getHeight());

        grid = new GridPane();
        grid.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        Text sceneTitle = new Text(TITLE);
        sceneTitle.setFont(Font.font(FONT, FontWeight.NORMAL, Gui.getScreenBounds().getWidth() / RATIO_FONT_SIZE));
        grid.add(sceneTitle, GRID_FIRST_ROW, GRID_ZERO_COLUMN);
        GridPane.setHalignment(sceneTitle, HPos.CENTER);
        grid.setGridLinesVisible(false);


        Label connection = new Label(CONNECTION_BUTTON);
        grid.add(connection, GRID_ZERO_ROW, GRID_FIRST_COLUMN);

        ObservableList<String> comboItems = FXCollections.observableArrayList(
                CONNECTION_RMI,
                CONNECTION_SOCKET
        );
        comboBox = new ComboBox(comboItems);
        comboBox.setMaxHeight(screenBounds.getHeight() / SCALE_RATIO_HEIGHT_COMBO_BOX);
        comboBox.setMaxWidth(screenBounds.getWidth() / SCALE_RATIO_WIDTH_COMBO_BOX);

        grid.add(comboBox, GRID_FIRST_ROW, GRID_FIRST_COLUMN);
        GridPane.setHalignment(comboBox, HPos.CENTER);

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_GAP_GRID);
        grid.setVgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_GAP_GRID);


        btn = new Button(CONNECT_BUTTON);
        btn.prefWidthProperty().bind(grid.widthProperty());
        btn.prefHeightProperty().bind(grid.heightProperty());
        btn.setMaxWidth(comboBox.getMaxWidth());
        btn.setMaxHeight(comboBox.getMaxHeight());
        HBox hbBtn = new HBox();
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        HBox.setHgrow(btn, Priority.ALWAYS);
        grid.add(hbBtn, GRID_FIRST_ROW, GRID_FOURTH_COLUMN);
        grid.add(btn,GRID_FIRST_ROW ,GRID_FOURTH_COLUMN);

        actionTarget = new Text();
        grid.add(actionTarget, GRID_FIRST_ROW, GRID_SIXTH_COLUMN);
        GridPane.setHalignment(actionTarget, HPos.CENTER);

        btn.setOnAction(event -> {
            if(comboBox.getValue() != null && !comboBox.getValue().toString().isEmpty()){

                Gui.setClient(new Client(this));
                client.createConnection(comboBox.getValue().toString().toLowerCase());
            }
        });




        scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest((WindowEvent we) -> {
            shutDown();
        });
    }

    /**
     * Creates the board when a startMatch update arrives
     * @param match Contains match information
     */
    @Override
    public void updateStartMatch(MatchView match){
        boardGui = new BoardGui(match);
        boardGui.createSquareRectangle(match);
        boardGui.createPlayerPositionHBox(match);

        Platform.runLater(() ->
            changeScene(boardGui.getView())
        );
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
        Platform.runLater(() ->
                boardGui.updatePayment());
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

            if(modeChoiceScreen == null){
                modeChoiceScreen= new ModeChoiceScreen();
                Platform.runLater( () ->  changeScene(modeChoiceScreen.getParent()));

            }
            else{
                modeChoiceScreen.update();
            }
        }
        else{
            if(modeChoiceScreen != null){
                modeChoiceScreen= null;
            }
            boardGui.updateSelectables();
            Platform.runLater(() -> changeScene(boardGui.getView()));
        }
    }

    /**
     * Creates and show an error window
     * @param message The error text
     */
    @Override
    public void printError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ERROR_ALERT);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();

        alert.setOnCloseRequest( we -> alert.close() );
    }

    @Override
    public void showGameOver(Map<PlayerView, Integer> rank, Map<PlayerView, Integer> points) {

        Map<String, Integer> rankString= new HashMap<>();
        Map<String, Integer> pointsString= new HashMap<>();

        for(Map.Entry<PlayerView, Integer> entry: rank.entrySet()){
            rankString.put(entry.getKey().getName(), entry.getValue());
        }

        for(Map.Entry<PlayerView, Integer> entry: rank.entrySet()){
            pointsString.put(entry.getKey().getName(), entry.getValue());
        }

        GameOverGui gameOverScreen= new GameOverGui(rankString, pointsString);

        Platform.runLater( () -> changeScene(gameOverScreen.getParent()));

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

    /**
     * Sets GUI screenBounds
     * @param screenBounds screenBounds referred to the current screen
     */
    public static void setScreenBounds(Rectangle2D screenBounds){
        Gui.screenBounds = screenBounds;
    }

    /**
     * Sets GUI client
     * @param client new client created to connect to server
     */
    public static void setClient(Client client){
        Gui.client = client;
    }

    public static void shutDown(){
        try {
            client.shutDown();
        }
        catch (NullPointerException e){
            //nothing to do id client is null
        }
        Platform.runLater( () -> stage.close());
    }

    public static void restart() {
        client.restart();
    }
}