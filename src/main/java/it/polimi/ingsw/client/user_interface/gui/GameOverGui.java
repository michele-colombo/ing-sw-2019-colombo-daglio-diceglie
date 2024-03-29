package it.polimi.ingsw.client.user_interface.gui;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Map;

/**
 * Window showed when a GameOverMessage is received, showing each player whit his final position and points
 */
public class GameOverGui {
    /**
     * font for the text
     */
    private static final Font FONT= new Font(Gui.getScreenBounds().getWidth() / 76.8);
    /**
     * gap between cells
     */
    private static final double GAP_SIZE = Gui.getScreenBounds().getWidth() / 153.6;
    /**
     * index of position column
     */
    private static final int POS_COL_INDEX = 0;
    /**
     * index of player column
     */
    private static final int PLAYER_COL_INDEX = 1;
    /**
     * index of points column
     */
    private static final int POINTS_COL_INDEX = 2;

    /**
     * title of position field
     */
    private static final String POSITION_TEXT = "Position";
    /**
     * title of player field
     */
    private static final String PLAYER_TEXT = "Player";
    /**
     * title of points field
     */
    private static final String POINTS_TEXT = "Points";

    /**
     * main pane of screen
     */
    private StackPane view;

    /**
     * builds a game over screen
     * @param rank final rank of the game
     * @param points final point for each player
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

        Platform.runLater(() -> {
            buttonBox.getChildren().add(Gui.getExitButton());
        });
        buttonBox.setAlignment(Pos.TOP_RIGHT);



        //gridRank.addRow(i, buttonBox);
        view.getChildren().add(buttonBox);
        StackPane.setAlignment(buttonBox, Pos.TOP_RIGHT);



    }

    /**
     * Gets view
     * @return the main pane of the screen
     */
    public Parent getParent(){
        return view;
    }
}
