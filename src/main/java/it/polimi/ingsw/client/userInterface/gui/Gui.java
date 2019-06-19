package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.LayoutView;
import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.userInterface.UserInterface;
import it.polimi.ingsw.client.PlayerView;
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

public class Gui extends Application implements UserInterface {
    private static Client client;
    private Text actionTarget;
    public static Text disconnectionText;
    private Button btn;
    private GridPane grid;
    private Stage stage;
    private Scene scene;
    private ComboBox comboBox;
    private LoginGui loginGui;
    private BoardGui boardGui;
    private static Rectangle2D screenBounds;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void showConnectionSelection() {
        changeScene(grid);
    }

    @Override
    public void showLogin() {
        Platform.runLater( () -> { loginGui = new LoginGui();
            changeScene(loginGui.getView()); } );
    }

    @Override
    public void printLoginMessage(String text, boolean loginSuccessful){
        loginGui.printLoginMessage(text, loginSuccessful);
    }

    @Override
    public synchronized void start(Stage primaryStage) {
        initialize(primaryStage);
    }

    public void initialize(Stage primaryStage){
        Gui.screenBounds = Screen.getPrimary().getBounds();
        loginGui = null;
        boardGui = null;
        stage = primaryStage;
        stage.setTitle("Welcome to Adrenaline!");
        stage.setFullScreen(true);
        stage.setMinWidth(screenBounds.getWidth()/2);
        stage.setMinHeight(screenBounds.getHeight()/2);

        //Group group = new Group();
        grid = new GridPane();
        //group.getChildren().add(grid);
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

        //stage.minWidthProperty().bind(scene.heightProperty().multiply(1.5));
        //stage.minHeightProperty().bind(scene.widthProperty().divide(1.5));

        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                try {
                    client.shutDown();
                }
                catch (NullPointerException e){
                    //nothing to do, it's right
                }

                stage.close();
            }
        });
    }

    @Override
    public synchronized void updateStartMatch(MatchView match){
        boardGui = new BoardGui(match);
        boardGui.createSelectableRectangle(match);
        boardGui.createPlayerPositionHBox(match);

        Platform.runLater(() -> {
            changeScene(boardGui.getView());
        });

        notify();
    }


    public static Client getClient(){
        return client;
    }

    public static Rectangle2D getScreenBounds(){
        return screenBounds;
    }

    @Override
    public void showAndAskSelection() {
        boardGui.updateSelectables();
    }

    @Override
    public void updateConnection() {
        if(boardGui == null){
            loginGui.updateConnection(client.getConnections());
        } else {
            //sleepGui();
            boardGui.updateConnection(client.getConnections());
        }
    }

    @Override
    public void updateLayout() {
        boardGui.updateLayout(client.getMatch().getLayout());
    }

    @Override
    public void updateKillshotTrack() {
        boardGui.updateKillshotTrack(client.getMatch().getSkulls());
    }

    @Override
    public void updateCurrentPlayer() {
        boardGui.updateCurrentPlayer();
    }

    @Override
    public void updatePlayer(PlayerView updated) {
        boardGui.updatePlayer(updated);
    }

    @Override
    public void updatePayment() {
        return;
    }

    @Override
    public void updateWeapons(PlayerView player) {
        boardGui.updateWeapons(player);
    }

    @Override
    public synchronized void updatePowerUp(PlayerView player) {
        while(boardGui == null){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        boardGui.updatePowerUp(player);
    }

    @Override
    public void updateDamage(PlayerView player) {
        boardGui.updateDamageTrack(player);
    }

    @Override
    public void updateSelectables() {
        boardGui.updateSelectables();
    }

    @Override
    public void printError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();

        alert.setOnCloseRequest( (we) -> alert.close() );
    }

    @Override
    public void showGameOver(Map<PlayerView, Integer> rank, Map<PlayerView, Integer> points) {

    }

    private void changeScene(Parent newView){
        scene.setRoot(newView);
    }

}
