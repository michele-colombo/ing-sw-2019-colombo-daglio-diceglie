package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.PlayerView;
import it.polimi.ingsw.client.WrongSelectionException;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlayerRectangle extends Rectangle{
    private PlayerView playerView;

    public PlayerRectangle(double width, double height, Color color, PlayerView playerView){
        super(width, height, color);
        this.playerView = playerView;

        this.setOnMouseClicked((MouseEvent t) -> {
            try{
                Gui.getClient().selected(playerView.getName());
            } catch(WrongSelectionException e){
                System.out.println("Wrong selection!");
            }
        });
    }

    public PlayerView getPlayerView(){
        return playerView;
    }
}
