package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerState;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the task of reloading a weapon
 */
public class Reload implements MicroAction {
    /**
     * Builds the micro action
     */
    public Reload() {

    }

    /**
     * It prepares the selectable items of the player with the reloadable weapons.
     * It also activates OK, to skip the reloading.
     * @param match the current match
     * @param p the player which is taking the action
     */
    @Override
    public void act(Match match, Player p) {
        p.setState(PlayerState.RELOAD);
        p.resetSelectables();
        List<Weapon> tempWeapons = new ArrayList<>();
        for (Weapon w : p.getUnloadedWeapons()){
            if (p.canAfford(w.getCost())){
                tempWeapons.add(w);
            }
        }
        p.setSelectableWeapons(tempWeapons);
        p.setSelectableCommands(Command.OK);
    }

    /**
     * Gets a string that uniquely describes this microaction
     * @return a unique string
     */
    @Override
    public String toString(){
        return "R";
    }
}
