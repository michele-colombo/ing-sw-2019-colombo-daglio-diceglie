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

public class BoardGui {
    private final GridPane view;
    private GridPane damageTracks;

    public BoardGui(){
        view = new GridPane();
        view.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //view.setAlignment(Pos.CENTER);
        //view.setHgap(10);
        //view.setVgap(10);
        GridPane board = new GridPane();
        board.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        board.setAlignment(Pos.TOP_LEFT);
        board.setPadding(new Insets(0,0,0,0));
        Image image = new Image("layoutPNG\\layout0.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight());
        imageView.setFitWidth(Gui.getScreenBounds().getHeight());
        imageView.setPreserveRatio(true);
        board.getChildren().add(imageView);
        GridPane.setHalignment(imageView, HPos.LEFT);
        view.add(board, 0,0);
        addDamageTrack(5);
    }

    public Parent getView(){
        return view;
    }

    public void addDamageTrack(int players){
        damageTracks = new GridPane();
        damageTracks.setVgap(5);
        damageTracks.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        damageTracks.setAlignment(Pos.TOP_RIGHT);
        Image image = new Image("damageTracks\\dmg1.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight());
        imageView.setFitWidth(Gui.getScreenBounds().getHeight()/1.27);
        imageView.setPreserveRatio(true);
        damageTracks.add(imageView, 0, 0);

        image = new Image("damageTracks\\dmg2.png");
        imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight());
        imageView.setFitWidth(Gui.getScreenBounds().getHeight()/1.27);
        imageView.setPreserveRatio(true);
        damageTracks.add(imageView, 0, 1);
        view.add(damageTracks, 1,0);
    }
}
