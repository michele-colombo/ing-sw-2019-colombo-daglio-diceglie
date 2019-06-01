package it.polimi.ingsw.client.userInterface;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardGui {
    private final GridPane view;
    private GridPane connectionState;
    private GridPane damageTracks;
    private List<Label> connectionLabels;

    public BoardGui(MatchView match){
        view = new GridPane();
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
        imageView.setFitHeight(Gui.getScreenBounds().getHeight()/1.2);
        imageView.setFitWidth(Gui.getScreenBounds().getHeight()/1.2);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        //board.getChildren().addAll(imageView, createKillButton());
        GridPane.setHalignment(imageView, HPos.LEFT);
        view.add(board, 0,0);
        view.add(ivTarget,0,0);
        addDamageTrack(match); //todo utilizzare il messaggio di UpdateStartMatch
        addConnectionState(match.getAllPlayers());
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
        int i = 1;
        for(PlayerView pv : players){
            Label playerLabel = new Label(pv.getName());
            playerLabel.setTextFill(Color.WHITE);
            Label onlineLabel = new Label("online");
            onlineLabel.setTextFill(Color.GREEN);
            playerLabel.setWrapText(true);
            playerLabel.setMaxWidth(Gui.getScreenBounds().getWidth()/24);
            //playerLabel.textProperty().bind()
            onlineLabel.setWrapText(true);
            onlineLabel.setMinWidth(Gui.getScreenBounds().getWidth()/17);
            connectionState.add(playerLabel, 0, i);
            connectionState.add(onlineLabel, 1, i);
            connectionLabels.add(playerLabel);
            connectionLabels.add(onlineLabel);
            i++;
        }
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
            }
        });
    }

    /*private Button createKillButton() {

        final Button killButton = new Button("Kill the evil witch");

        killButton.setStyle("-fx-base: firebrick;");

        killButton.setTranslateX(500);

        killButton.setTranslateY(-250);

        killButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override public void handle(ActionEvent t) {

                killButton.setStyle("-fx-base: forestgreen;");

                killButton.setText("Ding-Dong! The Witch is Dead");

            }

        });

        return killButton;

    }*/

}
