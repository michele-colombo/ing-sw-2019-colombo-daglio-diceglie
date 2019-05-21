package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PlayerState;

public class Shoot implements MicroAction {
    public Shoot() {

    }

    @Override
    public void act(Match match, Player p) {
        p.setState(PlayerState.SHOOT_WEAPON);
        p.resetSelectables();
        p.setSelectableWeapons(p.getLoadedWeapons());
    }

    @Override
    public String toString(){
        return "S";
    }

}
