package it.polimi.ingsw.client.userInterface;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Map;

public class BoardGui {
    private final GridPane view;
    private GridPane connectionState;
    private GridPane damageTracks;

    public BoardGui(int layout){
        view = new GridPane();
        view.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //view.setAlignment(Pos.CENTER);
        view.setHgap(25);
        view.setVgap(25);
        GridPane board = new GridPane();
        board.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        board.setAlignment(Pos.TOP_LEFT);
        board.setPadding(new Insets(0,0,0,0));

        Image image = new Image("layoutPNG\\layout" + layout + ".png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight());
        imageView.setFitWidth(Gui.getScreenBounds().getHeight());
        imageView.setPreserveRatio(true);
        board.getChildren().add(imageView);
        GridPane.setHalignment(imageView, HPos.LEFT);
        view.add(board, 0,0);

        addDamageTrack(3); //todo utilizzare il messaggio di startMatchUpdate
        addConnectionState(2);
    }

    public Parent getView(){
        return view;
    }

    //todo la prima damagetrack è quella del player corrente
    public void addDamageTrack(int players){
        damageTracks = new GridPane();
        damageTracks.setVgap(5);
        damageTracks.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        damageTracks.setAlignment(Pos.TOP_CENTER);
        /*Image image = new Image("damageTracks\\dmg1.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight());
        imageView.setFitWidth(Gui.getScreenBounds().getWidth()/4);
        imageView.setPreserveRatio(true);
        damageTracks.add(imageView, 0, 0);

        image = new Image("damageTracks\\dmg2.png");
        imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight());
        imageView.setFitWidth(Gui.getScreenBounds().getWidth()/4);
        imageView.setPreserveRatio(true);
        damageTracks.add(imageView, 0, 1);


        image = new Image("damageTracks\\dmg3.png");
        imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight());
        imageView.setFitWidth(Gui.getScreenBounds().getWidth()/4);
        imageView.setPreserveRatio(true);
        damageTracks.add(imageView, 0, 2);

        image = new Image("damageTracks\\dmg4.png");
        imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight());
        imageView.setFitWidth(Gui.getScreenBounds().getWidth()/4);
        imageView.setPreserveRatio(true);
        damageTracks.add(imageView, 0, 3);

        image = new Image("damageTracks\\dmg5.png");
        imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight());
        imageView.setFitWidth(Gui.getScreenBounds().getWidth()/4);
        imageView.setPreserveRatio(true);
        damageTracks.add(imageView, 0, 4);*/

        for(int i = 1; i <= players; i++){
            Image image = new Image("damageTracks\\dmg" + i + ".png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(Gui.getScreenBounds().getHeight());
            imageView.setFitWidth(Gui.getScreenBounds().getWidth()/4);
            imageView.setPreserveRatio(true);
            damageTracks.add(imageView, 0, i - 1);
        }
        view.add(damageTracks, 1,0);
    }

    public void addConnectionState(int players){
        connectionState = new GridPane();
        Text connectionText = new Text("PLAYERS");
        connectionState.add(connectionText, 0, 0);
        connectionState.setVgap(50);
        connectionState.setHgap(50);
        connectionState.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        view.add(connectionState, 4,0);
        for(int i = 1; i <= players; i++){

            Text playerText = new Text("ciao");
            Text onlineText = new Text("online");
            connectionState.add(playerText, 0, i);
            connectionState.add(onlineText, 1, i);

        }
    }

}
