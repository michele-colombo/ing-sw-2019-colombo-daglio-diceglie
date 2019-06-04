package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;
import it.polimi.ingsw.client.PowerUpView;
import it.polimi.ingsw.client.WrongSelectionException;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardGui {
    private final GridPane view;
    private HBox weaponBox;
    private HBox powerUpBox;
    private GridPane connectionState;
    private GridPane damageTracks;
    private List<Label> connectionLabels;
    private ComboBox selectables;

    public BoardGui(MatchView match){
        view = new GridPane();
        weaponBox = new HBox();
        powerUpBox = new HBox();
        selectables = new ComboBox();
        //selectables.setDisable(true);

        view.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //view.setAlignment(Pos.CENTER);
        view.setHgap(15);
        view.setVgap(15);

        GridPane board = new GridPane();
        board.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        board.setAlignment(Pos.TOP_LEFT);
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
                        powerUpBox.getChildren().add(new PowerUpButton(powerUpView));
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
                ObservableList<String> comboItems = FXCollections.observableList(Gui.getClient().getMatch().getMyPlayer().getSelectableActions());
                selectables.getItems().clear();
                selectables.getItems().addAll(comboItems);
            }
        });
    }


}
