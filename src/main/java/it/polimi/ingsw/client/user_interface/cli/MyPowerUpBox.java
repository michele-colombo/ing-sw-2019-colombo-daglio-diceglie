package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PowerUpView;
import it.polimi.ingsw.server.model.Cash;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.user_interface.cli.CliUtils.*;

/**
 * Contains the powerUps of my player
 */
public class MyPowerUpBox extends MiniBox {
    /**
     * Builds the box specifying all its parameters (except its content)
     * @param x the x position int the window (from the left side)
     * @param y the y position in the window (from the top)
     * @param height the height of this box
     * @param width the width of this box
     */
    public MyPowerUpBox(int x, int y, int height, int width) {
        super(x, y, height, width);
    }

    /**
     * Updates the powerups of my player
     */
    @Override
    public void update(MatchView match) {
        insertSubBox(prepareBorder(height, width), 0, 0);
        List<PowerUpView> myPowerups = new ArrayList<>(match.getMyPlayer().getPowerUps());
        if (myPowerups.isEmpty()){
            String noPowerups = "You have no powerUps";
            insertText(noPowerups, (width-noPowerups.length())/2, height/2);
        } else {
            int i = 0;
            for (PowerUpView p : myPowerups){
                insertLine(printCash(new Cash(p.getColor(), 1)), 2, 1+i);
                insertText(p.getName().toUpperCase(), 4, 1+i);
                i++;
            }
        }
    }
}
