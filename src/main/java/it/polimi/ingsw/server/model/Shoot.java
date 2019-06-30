package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerState;

public class Shoot implements MicroAction {
    /**
     * Builds the micro action
     */
    public Shoot() {

    }

    /**
     * Puts the loaded weapons as selectable. If none is present, it activates BACK command
     * @param match the current match
     * @param p the player which is taking the action
     */
    @Override
    public void act(Match match, Player p) {
        p.setState(PlayerState.SHOOT_WEAPON);
        p.resetSelectables();
        if (p.getLoadedWeapons().isEmpty()){
            p.setSelectableCommands(Command.BACK);
        } else {
            p.setSelectableWeapons(p.getLoadedWeapons());
        }
    }


    /**
     * Gets a string that uniquely describes this microaction
     * @return a unique string
     */
    @Override
    public String toString(){
        return "S";
    }

}
