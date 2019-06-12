package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.InputStream;
import java.util.*;

public class BoardGui {
    private final static double xKillshotTrack = 0.0909;
    private final static double yKillshotTrack = 0.0921;
    private final static double gapKillshotTrack = 0.0421;

    private final GridPane view;
    private HBox weaponBox;
    private HBox powerUpBox;
    private GridPane connectionState;
    private GridPane damageTracks;
    private AnchorPane board;
    private List<Label> connectionLabels;
    private ComboBox selectables;
    private List<PixelPosition> pixelPositions;
    private List<AmmoButton> ammoButtonsList;
    private List<WeaponButton> weaponButtonList;
    private List<PixelWeapon> yellowWeapons;
    private List<PixelWeapon> blueWeapons;
    private List<PixelWeapon> redWeapons;
    private Map<PlayerView, HBox> playerPositions;
    private double boardWidth;
    private double boardHeight;

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
        System.out.println("we");
        //selectables.setDisable(true);

        view.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //view.setAlignment(Pos.CENTER);
        view.setHgap(15);
        view.setVgap(15);

        board = new AnchorPane();
        board.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //board.setAlignment(Pos.TOP_LEFT);
        board.setPadding(new Insets(0,0,0,0));

        final ImageView ivTarget = new ImageView();
        //board.add(ivTarget, 0,0);
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
        //view.add(ivTarget,0,0);
        view.add(weaponBox, 0,1);
        view.add(powerUpBox, 1, 1);
        addDamageTrack(match); //todo utilizzare il messaggio di UpdateStartMatch
        addConnectionState(match.getAllPlayers());
        addSelectables();
        updateConnection(match.readConnections());
    }

    public Parent getView(){
        return view;
    }

    public void addDamageTrack(MatchView match){
        damageTracks = new GridPane();
        damageTracks.setVgap(5);
        damageTracks.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        damageTracks.setAlignment(Pos.TOP_CENTER);

        InputStream myDmgUrl = getClass().getClassLoader().getResourceAsStream("damageTracks/dmg" + match.getMyPlayer().getColor().toString().toLowerCase() + ".png");
        Image image = new Image(myDmgUrl);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight());
        imageView.setFitWidth(Gui.getScreenBounds().getWidth()/4);
        imageView.setPreserveRatio(true);
        damageTracks.add(imageView, 0, 0);

        int i = 1;
        for(PlayerView pv : match.getAllPlayers()){
            if(pv.getColor() != match.getMyPlayer().getColor()){
                InputStream dmgUrl = getClass().getClassLoader().getResourceAsStream("damageTracks/dmg" + pv.getColor().toString().toLowerCase() + ".png");
                image = new Image(dmgUrl);
                imageView = new ImageView(image);
                imageView.setFitHeight(Gui.getScreenBounds().getHeight());
                imageView.setFitWidth(Gui.getScreenBounds().getWidth()/4);
                imageView.setPreserveRatio(true);
                damageTracks.add(imageView, 0, i);
            }
            i++;
        }
        view.add(damageTracks, 1,0);
    }

    public void addConnectionState(List<PlayerView> players ){
        connectionLabels = new ArrayList<>();
        connectionState = new GridPane();
        Label connectionLabel = new Label("PLAYERS");
        connectionLabel.setTextFill(Color.GHOSTWHITE);
        connectionState.add(connectionLabel, 0, 0);
        connectionState.setVgap(10);
        connectionState.setHgap(10);
        connectionState.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                connectionState.getChildren().removeAll(connectionLabels);
                connectionLabels.clear();
                int i = 1;
                for(String string : connections.keySet()){
                    Label playerLabel = new Label(string);
                    playerLabel.setWrapText(true);
                    playerLabel.setTextFill(Color.WHITE);
                    playerLabel.setMaxWidth(Gui.getScreenBounds().getWidth()/20);
                    Label onlineLabel;
                    if(connections.get(string)){
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
                //sleepGui(750);
            }
        });
    }

    public void updatePowerUp(PlayerView player){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(player.getName().equals(Gui.getClient().getMatch().getMyPlayer().getName())){
                    powerUpBox.getChildren().clear();
                    for(PowerUpView powerUpView : Gui.getClient().getMatch().getMyPlayer().getPowerUps()){
                        PowerUpButton powerUpButton = new PowerUpButton(powerUpView);
                        powerUpBox.getChildren().add(powerUpButton);
                        powerUpBox.setHgrow(powerUpButton, Priority.ALWAYS);
                    }
                }
                //sleepGui(750);
            }
        });
    }

    public void updateSelectables(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //ObservableList<String> comboItems = FXCollections.observableList(Gui.getClient().getMatch().getMyPlayer().getSelectableActions());
                MyPlayer me= Gui.getClient().getMatch().getMyPlayer();
                ObservableList<String> comboItems = FXCollections.observableList(me.getSelectableActions());
                selectables.getItems().clear();
                selectables.getItems().addAll(comboItems);

                /*for(SquareView squareView : Gui.getClient().getMatch().getMyPlayer().getSelectableSquares()){
                    Rectangle square = new Rectangle(25,25);
                    square.setFill(Color.RED);
                    square.setStroke(Color.RED);
                    square.setStrokeType(StrokeType.INSIDE);
                    square.setTranslateX(-square.getxAmmo() * 60);
                    square.setTranslateY(-square.getyAmmo() * 15);
                    board.getChildren().add(square);
                }*/
            }
        });
    }

    public void updateWeapons(PlayerView player){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(player.getName().equals(Gui.getClient().getMatch().getMyPlayer().getName())){
                    weaponBox.getChildren().clear();
                    for(WeaponView weaponView : Gui.getClient().getMatch().getMyPlayer().getSelectableWeapons()){
                        weaponBox.getChildren().add(new WeaponButton(weaponView));
                    }
                }
            }
        });
    }

    public void updateKillshotTrack(int skulls){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for(double i = 0; i < skulls; i++){
                    Circle skull = new Circle(10);
                    skull.setFill(Color.RED);
                    skull.setTranslateX(boardWidth * (BoardGui.xKillshotTrack + i * BoardGui.gapKillshotTrack));
                    skull.setTranslateY(boardHeight * BoardGui.yKillshotTrack);
                    board.getChildren().add(skull);
                }
            }
        });
    }

    public void updatePlayer(PlayerView player){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                return;
            }
        });
    }

    public void updateLayout(LayoutView layoutView){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                board.getChildren().removeAll(ammoButtonsList);
                for(SquareView sv : layoutView.getSquares()){
                    for(PixelPosition pixelPosition : pixelPositions){
                        if(sv.isAmmo() && pixelPosition.equalsSquare(sv)){
                            AmmoButton ammoButton = new AmmoButton(sv);
                            ammoButtonsList.add(ammoButton);
                            board.getChildren().add(ammoButton);
                            ammoButton.setTranslateX(boardWidth * pixelPosition.getxAmmo());
                            ammoButton.setTranslateY(boardHeight * pixelPosition.getyAmmo());
                        }
                    }
                }
                board.getChildren().removeAll(weaponButtonList);
                updateLayoutWeapon(yellowWeapons, layoutView.getYellowWeapons());
                updateLayoutWeapon(blueWeapons, layoutView.getBlueWeapons());
                updateLayoutWeapon(redWeapons, layoutView.getRedWeapons());
            }
        });
    }

    private void updateLayoutWeapon(List<PixelWeapon> weapons, List<WeaponView> weaponViews){
        int i = 0;
        for(WeaponView wp : weaponViews){
            WeaponButton weaponButton = new WeaponButton(wp);
            weaponButtonList.add(weaponButton);
            board.getChildren().add(weaponButton);
            weaponButton.setTranslateX(boardWidth * weapons.get(i).getX());
            weaponButton.setTranslateY(boardHeight * weapons.get(i).getY());
            weaponButton.setRotate(weapons.get(i).getRotate());
            i++;
        }
    }
}
