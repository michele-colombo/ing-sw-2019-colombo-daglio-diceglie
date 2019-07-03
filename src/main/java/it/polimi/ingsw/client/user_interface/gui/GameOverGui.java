package it.polimi.ingsw.client.user_interface.gui;

import it.polimi.ingsw.server.model.GameModel;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Map;

public class GameOverGui {
    public static final Font FONT= new Font(Gui.getScreenBounds().getWidth() / 76.8);
    public static final double GAP_SIZE = Gui.getScreenBounds().getWidth() / 153.6;
    public static final int POS_COL_INDEX = 0;
    public static final int PLAYER_COL_INDEX = 1;
    public static final int POINTS_COL_INDEX = 2;
    public static final String POSITION_TEXT = "Position";
    public static final String PLAYER_TEXT = "Player";
    public static final String POINTS_TEXT = "Points";

    private StackPane view;
/*
    public GameOverGui(Map<String, Integer> rank, Map<String, Integer> points){
        view= new BorderPane();

        HBox horizontal= new HBox();
        VBox positionCol= new VBox();
        VBox playerCol= new VBox();
        VBox pointsCol= new VBox();


        positionCol.getChildren().add(new Text("Position"));
        playerCol.getChildren().add(new Text("Player"));
        pointsCol.getChildren().add(new Text("Points"));

        int i=1;
        for(String s: rank.keySet()){
            Text position= new Text(String.valueOf( rank.get(s) ));
            position.setFont(FONT);
            Text player= new Text(s);
            player.setFont(FONT);
            Text score= new Text(String.valueOf( points.get(s) ));
            score.setFont(FONT);

            positionCol.getChildren().add(position);
            playerCol.getChildren().add(player);
            pointsCol.getChildren().add(score);

            i++;
        }

        horizontal.getChildren().addAll(positionCol, playerCol, pointsCol);
        BorderPane.setAlignment(horizontal, Pos.CENTER);

        horizontal.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null, null)));
        horizontal.setMinWidth(Gui.getScreenBounds().getWidth() / 2);

        view.setCenter(horizontal);
    }
*/
    public GameOverGui(Map<String, Integer> rank, Map<String, Integer> points){
        view = new StackPane();

        GridPane gridRank = new GridPane();
        view.getChildren().add(gridRank);
        StackPane.setAlignment(gridRank, Pos.CENTER);

        view.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane.setHalignment(gridRank, HPos.CENTER);
        gridRank.setAlignment(Pos.CENTER);
        gridRank.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        gridRank.setHgap(GAP_SIZE);
        gridRank.setVgap(GAP_SIZE);


        Text positionTitle= new Text(POSITION_TEXT); positionTitle.setFont(FONT);
        Text playerTitle= new Text(PLAYER_TEXT); playerTitle.setFont(FONT);
        Text pointsTitle= new Text(POINTS_TEXT); pointsTitle.setFont(FONT);

        gridRank.add(positionTitle, POS_COL_INDEX, 0);
        gridRank.add(playerTitle, PLAYER_COL_INDEX, 0);
        gridRank.add(pointsTitle, POINTS_COL_INDEX, 0);


        int i=1;
        for(String s: rank.keySet()){
            Text position= new Text(String.valueOf( rank.get(s) ));
            position.setFont(FONT);
            Text player= new Text(s);
            player.setFont(FONT);
            Text score= new Text(String.valueOf( points.get(s) ));
            score.setFont(FONT);

            gridRank.add(position, POS_COL_INDEX, i);
            gridRank.add(player, PLAYER_COL_INDEX, i);
            gridRank.add(score, POINTS_COL_INDEX, i);

            i++;
        }

        HBox buttonBox= new HBox();
        ExitButton quit= new ExitButton();

        buttonBox.getChildren().add(quit);

        buttonBox.setAlignment(Pos.TOP_RIGHT);



        //gridRank.addRow(i, buttonBox);
        view.getChildren().add(buttonBox);
        StackPane.setAlignment(buttonBox, Pos.TOP_RIGHT);



    }

    public Parent getParent(){
        return view;
    }
}