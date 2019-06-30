package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.PlayerView;
import it.polimi.ingsw.client.ClientExceptions.WrongSelectionException;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * It represent a player on the board as a rectangle
 */
public class PlayerRectangle extends Rectangle{
    /**
     * Contains the information of the player associated to this playerRectangle
     */
    private PlayerView playerView;


    /**
     * Creates a new PlayerRectangle. Pressing on it, it will send this playerView's name to the server
     * @param width The width of this PlayerRectangle
     * @param height The height of this PlayerRectangle
     * @param color The color of this PlayerRectangle(it depends on playerView)
     * @param playerView The playerView associated to this PlayerRectangle
     */
    public PlayerRectangle(double width, double height, Color color, PlayerView playerView){
        super(width, height, color);
        setStrokeType(StrokeType.CENTERED);
        setStroke(Color.BLACK);
        this.playerView = playerView;

        this.setOnMouseClicked((MouseEvent t) -> {
            try{
                Gui.getClient().selected(playerView.getName());
            } catch(WrongSelectionException e){
                System.out.println("Wrong selection!");
            }
        });
    }

    /**
     *
     * @return playerView
     */
    public PlayerView getPlayerView(){
        return playerView;
    }
}
