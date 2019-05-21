package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PlayerState;

public class Reload implements MicroAction {
    public Reload() {

    }

    @Override
    public void act(Match match, Player p) {
        p.setState(PlayerState.RELOAD);
        p.resetSelectables();
        p.setSelectableWeapons(p.getUnloadedWeapons());
    }

    @Override
    public String toString(){
        return "R";
    }
}
