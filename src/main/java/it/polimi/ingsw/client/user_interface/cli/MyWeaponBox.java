package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.WeaponView;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.client.user_interface.cli.CliUtils.*;

/**
 * Contains the weapons of my player
 */
public class MyWeaponBox extends MiniBox {
    /**
     * Builds the box specifying all its parameters (except its content)
     * @param x the x position int the window (from the left side)
     * @param y the y position in the window (from the top)
     * @param height the height of this box
     * @param width the width of this box
     */
    public MyWeaponBox(int x, int y, int height, int width) {
        super(x, y, height, width);
    }

    /**
     * Updates weapons of my player
     * @param match the match from which retrieve information
     */
    @Override
    public void update(MatchView match) {
        Map<WeaponView, Boolean> myWeapons = new HashMap<>(match.getMyPlayer().getWeapons());
        //List<WeaponView> myUnloadedWeapons = new ArrayList<>(match.getMyPlayer().getUnloadedWeapons());
        insertSubBox(prepareBorder(height, width), 0, 0);
        if (myWeapons.isEmpty()){
            String noWeapons = "You have no weapons";
            insertText(noWeapons, (width-noWeapons.length())/2, height/2);
        } else {
            int i = 0;
            for (Map.Entry<WeaponView, Boolean> w : myWeapons.entrySet()){
                String weaponString = w.getKey().getName().toUpperCase();
                if (w.getValue() == true){
                    insertText(weaponString, 2, 1+i, OK_COLOR);
                } else {
                    weaponString = weaponString + " -> ";
                    insertText(weaponString, 2, 1+i, WRONG_COLOR);
                    insertLine(printCash(w.getKey().getCost()), weaponString.length()+2, 1+i);
                }
                i++;
            }
        }
    }
}
