package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.PlayerView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlayerRectangle extends Rectangle{
    private PlayerView playerView;

    public PlayerRectangle(double v, double v1, Color color, PlayerView playerView){
        super(v, v1, color);
        this.playerView = playerView;
    }

    public PlayerView getPlayerView(){
        return playerView;
    }
}
