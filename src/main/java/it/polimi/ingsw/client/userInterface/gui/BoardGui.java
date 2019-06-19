package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerState;
import it.polimi.ingsw.server.model.enums.PlayerState;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.*;

public class BoardGui {
    private final static double X_KILLSHOT_TRACK = 0.0909;
    private final static double Y_KILLSHOT_TRACK = 0.0921;
    private final static double GAP_KILLSHOT_TRACK = 0.0421;
    private final static double SCALE_RATIO_SELECTABLE_RECTANGLE_X = 7.68;
    private final static double SCALE_RATIO_SELECTABLE_RECTANGLE_Y = 5.25;
    private final static double SKULL_CIRCLE = 76.08;

    private final GridPane view;
    private HBox weaponBox;
    private HBox powerUpBox;
    private GridPane connectionState;
    private GridPane damageTracks;
    private AnchorPane board;
    private List<Label> connectionLabels;
    private Label currentPlayer;
    private ComboBox selectables;
    private List<PixelPosition> pixelPositions;
    private List<AmmoButton> ammoButtonsList;
    private List<WeaponButton> weaponButtonList;
    private List<PixelWeapon> yellowWeapons;
    private List<PixelWeapon> blueWeapons;
    private List<PixelWeapon> redWeapons;
    private static List<SelectableRectangle> selectableRectangles;
    private Map<PlayerRectangle, HBox> playerPositions;
    private Map<SquareView, HBox> playerPositionHBox;
    private Map<PlayerView, DamageTrack> playerDamageTracks;
    private static double boardWidth;
    private static double boardHeight;

    private ModeChoiceDialog modeChoiceDialog;

    public BoardGui(MatchView match){
        view = new GridPane();
        weaponBox = new HBox();
        powerUpBox = new HBox();
        selectables = new ComboBox();
        ammoButtonsList = new LinkedList<>();
        weaponButtonList = new LinkedList<>();
        Parser parser = new Parser(match.getLayout().getLayoutConfiguration());
        pixelPositions = parser.loadAmmoResource();
        yellowWeapons = parser.loadWeaponResource("yellow");
        blueWeapons = parser.loadWeaponResource("blue");
        redWeapons = parser.loadWeaponResource("red");
        playerPositions = new HashMap<>();
        playerPositionHBox = new HashMap<>();
        selectableRectangles = new LinkedList<>();
        playerDamageTracks = new HashMap<>();
        //createPlayerPositionHBox(match);
        createPlayerRectangle(match);
        //selectables.setDisable(true);

        view.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //view.setAlignment(Pos.CENTER);
        view.setHgap(5);
        view.setVgap(15);

        board = new AnchorPane();
        board.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //board.setAlignment(Pos.TOP_LEFT);
        board.setPadding(new Insets(0,0,0,0));

        InputStream boardUrl = getClass().getClassLoader().getResourceAsStream("layoutPNG/layout" + match.getLayout().getLayoutConfiguration() + ".png");
        Image image = new Image(boardUrl);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight()/1.5);
        imageView.setFitWidth(Gui.getScreenBounds().getWidth()/1.5);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        boardWidth = imageView.boundsInParentProperty().get().getWidth();
        boardHeight = imageView.boundsInParentProperty().get().getHeight();

        board.getChildren().addAll(imageView);
        GridPane.setHalignment(imageView, HPos.LEFT);
        view.add(board, 0,0);
        view.add(weaponBox, 0,1);
        view.add(powerUpBox, 1, 1);
        addDamageTrack(match); //todo utilizzare il messaggio di updateStartMatch
        addConnectionState(match.getAllPlayers());
        addSelectables();
        updateConnection(match.readConnections());

        modeChoiceDialog= null;
    }

    public Parent getView(){
        return view;
    }

    public void addDamageTrack(MatchView match){
        List<Color> colors = getAllColors(match);
        damageTracks = new GridPane();
        damageTracks.setVgap(5);
        damageTracks.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        damageTracks.setAlignment(Pos.TOP_CENTER);

        //InputStream myDmgUrl = getClass().getClassLoader().getResourceAsStream("damageTracks/dmg" + match.getMyPlayer().getColor().toString().toLowerCase() + ".png");
        //Image image = new Image(myDmgUrl);
        //ImageView imageView = new ImageView(image);
        DamageTrack damageTrack = new DamageTrack(colors, match.getMyPlayer());
        damageTrack.updateInfo();
        playerDamageTracks.put(match.getMyPlayer(), damageTrack);
        damageTracks.add(damageTrack, 0, 0);

        int i = 1;
        for(PlayerView pv : match.getAllPlayers()){
            if(pv.getColor() != match.getMyPlayer().getColor()){
                //InputStream dmgUrl = getClass().getClassLoader().getResourceAsStream("damageTracks/dmg" + pv.getColor().toString().toLowerCase() + ".png");
                //image = new Image(dmgUrl);
                //imageView = new ImageView(image);
                DamageTrack otherDamageTrack = new DamageTrack(colors, pv);
                otherDamageTrack.updateInfo();
                playerDamageTracks.put(pv, otherDamageTrack);
                damageTracks.add(otherDamageTrack, 0, i);
            }
            i++;
        }
        view.add(damageTracks, 1,0);
    }

    public void addConnectionState(List<PlayerView> players ){
        connectionLabels = new ArrayList<>();
        connectionState = new GridPane();
        currentPlayer = new Label("Current: " + players.get(0).getName());
        currentPlayer.setTextFill(Color.WHITE);
        currentPlayer.setWrapText(true);
        Label connectionLabel = new Label("PLAYERS");
        connectionLabel.setTextFill(Color.GHOSTWHITE);
        connectionState.add(connectionLabel, 0, 0);
        connectionState.setVgap(10);
        connectionState.setHgap(10);
        connectionState.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        connectionState.add(currentPlayer, 0, 1);
        view.add(connectionState, 4,0);
    }

    public void addSelectables(){
        selectables = new ComboBox();
        view.add(selectables,4,0);

        selectables.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(selectables.getValue() != null && !selectables.getValue().toString().isEmpty()){
                    try{
                        Gui.getClient().selected(selectables.getValue().toString());
                    } catch(WrongSelectionException e){
                        System.out.println("Action ComboBox error!");
                    }
                }
            }
        });
    }

    public void updateConnection(Map<String, Boolean> connections){
        Platform.runLater(() -> {
            connectionState.getChildren().removeAll(connectionLabels);
            connectionLabels.clear();
            int i = 2;
            for(PlayerView pv : Gui.getClient().getMatch().getAllPlayers()){
                Label playerLabel = new Label(pv.getName());
                playerLabel.setWrapText(true);
                playerLabel.setTextFill(Color.valueOf(pv.getColor().toString()));
                playerLabel.setMaxWidth(Gui.getScreenBounds().getWidth()/20);
                Label onlineLabel;
                if(connections.get(pv.getName())){
                    onlineLabel = new Label("online");
                    onlineLabel.setTextFill(Color.GREEN);
                    onlineLabel.setWrapText(true);
                } else {
                    onlineLabel = new Label("offline");
                    onlineLabel.setTextFill(Color.RED);
                    onlineLabel.setWrapText(true);
                }
                onlineLabel.setMaxWidth(Gui.getScreenBounds().getWidth()/20);
                connectionState.add(playerLabel, 0, i);
                connectionState.add(onlineLabel, 1, i);
                connectionLabels.add(playerLabel);
                connectionLabels.add(onlineLabel);
                i++;
            }
            if(Gui.getClient().getMatch().getCurrentPlayer() != null){
                currentPlayer.setText("Current: " + Gui.getClient().getMatch().getCurrentPlayer());
            }
        });
    }

    public void updateCurrentPlayer(){
        Platform.runLater(() -> {
            currentPlayer.setText("Current: " + Gui.getClient().getMatch().getCurrentPlayer());
        });
    }

    public void updatePowerUp(PlayerView player){
        Platform.runLater(() -> {
            MyPlayer me = Gui.getClient().getMatch().getMyPlayer();
                if(player.getName().equals(me.getName())){
                powerUpBox.getChildren().clear();
                for(PowerUpView powerUpView : me.getPowerUps()){
                    PowerUpButton powerUpButton = new PowerUpButton(powerUpView);
                    powerUpBox.getChildren().add(powerUpButton);
                    powerUpBox.setHgrow(powerUpButton, Priority.ALWAYS);
                }
            }
                playerDamageTracks.get(player).updateInfo();
        });
    }

    public void updateSelectables(){
        Platform.runLater(() -> {
            MyPlayer me = Gui.getClient().getMatch().getMyPlayer();
            ObservableList<String> comboItemsActions = FXCollections.observableList(me.getSelectableActions());
            ObservableList<Command> comboItemsCommand = FXCollections.observableList(me.getSelectableCommands());
            selectables.getItems().clear();
            selectables.getItems().addAll(comboItemsActions);
            selectables.getItems().addAll(comboItemsCommand);

            for(SquareView sv : me.getSelectableSquares()){
                for(SelectableRectangle sr : selectableRectangles){
                    if(sr.equals(sv)){
                        sr.setVisible(true);
                    }
                }
            }


            //added by Giuseppe
            if(me.getState() == PlayerState.CHOOSE_MODE){
                System.out.println("Selecting modes, si deve aprire");

            //if(!me.getSelectableModes().isEmpty()){
                if(modeChoiceDialog == null){
                    System.out.println("Creating and updating modeSelection");
                    modeChoiceDialog= new ModeChoiceDialog();
                }
                else{
                    System.out.println("updating modeSelection");
                    modeChoiceDialog.update();
                }
            }
            //finished Giuseppe's part


        });
    }

    public void updateWeapons(PlayerView player){
        Platform.runLater(() -> {
            if(player.getName().equals(Gui.getClient().getMatch().getMyPlayer().getName())){
                weaponBox.getChildren().clear();
                //todo usare un metodo getAllWeapons o qualcosa di simile oppure cambiare dinamicamente immagine
                for(WeaponView weaponView : Gui.getClient().getMatch().getMyPlayer().getWeapons().keySet()){
                    WeaponButton newWeapon = new WeaponButton(weaponView, true);
                    newWeapon.reScale();
                    weaponBox.getChildren().add(newWeapon);
                }
            }
            playerDamageTracks.get(player).updateInfo(); //todo forse è inutile
        });
    }

    public void updateKillshotTrack(int skulls){
        Platform.runLater(() -> {
            for(double i = 0; i < skulls; i++){
                Circle skull = new Circle(boardWidth / SKULL_CIRCLE);
                skull.setFill(Color.RED);
                skull.setTranslateX(boardWidth * (BoardGui.X_KILLSHOT_TRACK + i * BoardGui.GAP_KILLSHOT_TRACK));
                skull.setTranslateY(boardHeight * BoardGui.Y_KILLSHOT_TRACK);
                board.getChildren().add(skull);
            }
        });
    }

    public void updatePlayer(PlayerView player){
        Platform.runLater(() -> {
            updatePosition(player);
            updateWallet(player);
            //updateDamageTrack(player);
        });
    }

    private void updatePosition(PlayerView player){
        if(player.getSquarePosition() != null){
            PlayerRectangle playerRectangle = getPlayerRectangle(player);
            HBox newPosition = playerPositionHBox.get(player.getSquarePosition());

            if(playerPositions.get(player) == null){

                if(newPosition != playerPositions.get(playerRectangle)){
                    playerPositions.put(playerRectangle, newPosition);
                    newPosition.getChildren().add(playerRectangle);
                }
            } else {
                HBox oldPosition = playerPositions.get(playerRectangle);
                oldPosition.getChildren().remove(playerRectangle);
                newPosition.getChildren().add(playerRectangle);
                playerPositions.put(playerRectangle, newPosition);
                //elimino dall'hbox precedente e lo sposto in quello nuovo
            }
        }
    }

    public void updateDamageTrack(PlayerView player){
        Platform.runLater(() -> {
            DamageTrack toUpdate = playerDamageTracks.get(player);
            for(PlayerView pv : player.getDamageList()){
                playerDamageTracks.get(player).addDamage(Color.valueOf(pv.getColor().toString()));
            }
            toUpdate.addMark(player.getMarkMap());
            //toUpdate.updateInfo();
        });

    }

    private void updateWallet(PlayerView player){
        DamageTrack damageTrackToUpdate = playerDamageTracks.get(player);
        damageTrackToUpdate.addAmmo(Color.BLUE, player.getWallet().getBlue());
        damageTrackToUpdate.addAmmo(Color.RED,player.getWallet().getRed());
        damageTrackToUpdate.addAmmo(Color.YELLOW, player.getWallet().getYellow());
    }

    public void updateLayout(LayoutView layoutView){
        Platform.runLater(() -> {
            board.getChildren().removeAll(ammoButtonsList);
            updateLayoutAmmo(layoutView.getSquares());

            board.getChildren().removeAll(weaponButtonList);
            updateLayoutWeapon(yellowWeapons, layoutView.getYellowWeapons());
            updateLayoutWeapon(blueWeapons, layoutView.getBlueWeapons());
            updateLayoutWeapon(redWeapons, layoutView.getRedWeapons());
        });
    }

    private void updateLayoutWeapon(List<PixelWeapon> weapons, List<WeaponView> weaponViews){
        int i = 0;
        for(WeaponView wp : weaponViews){
            WeaponButton weaponButton = new WeaponButton(wp, false);
            weaponButton.reScale();
            weaponButtonList.add(weaponButton);
            board.getChildren().add(weaponButton);
            weaponButton.setTranslateX(boardWidth * weapons.get(i).getX());
            weaponButton.setTranslateY(boardHeight * weapons.get(i).getY());
            weaponButton.setRotate(weapons.get(i).getRotate());
            i++;
        }
    }

    private void updateLayoutAmmo(List<SquareView> squareViews){
        for(SquareView sv : squareViews){
            for(PixelPosition pixelPosition : pixelPositions){
                if(sv.getAmmo() != null && pixelPosition.equalsSquare(sv)){
                    AmmoButton ammoButton = new AmmoButton(sv);
                    ammoButtonsList.add(ammoButton);
                    board.getChildren().add(ammoButton);
                    ammoButton.setTranslateX(boardWidth * pixelPosition.getxAmmo());
                    ammoButton.setTranslateY(boardHeight * pixelPosition.getyAmmo());
                }
            }
        }
    }

    private Rectangle createPlayerRectangle(Color color){
        final Rectangle rectangle = new Rectangle(5, 20);
        rectangle.setFill(color);

        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                rectangle.setFill(Color.GAINSBORO);
            }
        });

        return rectangle;
    }

    private PixelPosition searchPixelPosition(SquareView squareView){
        for(PixelPosition pp : pixelPositions){
            if(squareView.getX() == pp.getxSquare() && squareView.getY() == pp.getySquare()){
                return pp;
            }
        }
        return null; //non dovrebbe verificarsi mai
    }

    public void createPlayerPositionHBox(MatchView matchView){
        //List<HBox> toAdd = new LinkedList<>();
        for(PixelPosition pp : pixelPositions){
            HBox newPlayerPositionHBox = new HBox();
            newPlayerPositionHBox.setTranslateX(boardWidth * pp.getxPlayer());
            newPlayerPositionHBox.setTranslateY(boardHeight * pp.getyPlayer());
            //newPlayerPositionHBox.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            playerPositionHBox.put(matchView.getLayout().getSquare(pp.getxSquare(), pp.getySquare()), newPlayerPositionHBox);
            board.getChildren().add(newPlayerPositionHBox);
        }
    }

    private void createPlayerRectangle(MatchView matchView){
        for(PlayerView pv : matchView.getAllPlayers()){
            playerPositions.put(new PlayerRectangle(5, 20, Color.valueOf(pv.getColor().toString()), pv), null);
        }
    }

    public void createSelectableRectangle(MatchView matchView){
        for(PixelPosition pp : pixelPositions){
            SelectableRectangle newSelectableRectangle = new SelectableRectangle(boardWidth / SCALE_RATIO_SELECTABLE_RECTANGLE_X, boardHeight / SCALE_RATIO_SELECTABLE_RECTANGLE_Y, matchView.getLayout().getSquare(pp.getxSquare(), pp.getySquare()));
            newSelectableRectangle.setTranslateX(boardWidth * pp.getxSelectable());
            newSelectableRectangle.setTranslateY(boardHeight * pp.getySelectable());
            selectableRectangles.add(newSelectableRectangle);
            board.getChildren().add(newSelectableRectangle);
        }
    }

    private PlayerRectangle getPlayerRectangle(PlayerView playerView){
        for(PlayerRectangle pr : playerPositions.keySet()){
            if(pr.getPlayerView() == playerView){
                return pr;
            }
        }
        return null;
    }

    public synchronized static void setUnvisibleSelectableRectangle(){
        for(SelectableRectangle sr : selectableRectangles){
            sr.setVisible(false);
        }
    }

    public static double getWidth(){
        return boardWidth;
    }

    private List<Color> getAllColors(MatchView match){
        List<Color> colors = new LinkedList<>();
        for(PlayerView pv : match.getAllPlayers()){
            colors.add(Color.valueOf(pv.getColor().toString()));
        }
        return colors;
    }
}
