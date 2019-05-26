package it.polimi.ingsw.client.userInterface.cli;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PowerUpView;
import it.polimi.ingsw.server.model.Cash;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.userInterface.cli.CliUtils.*;

public class MyPowerUpBox extends MiniBox {
    public MyPowerUpBox(int x, int y, int height, int width) {
        super(x, y, height, width);
    }

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
