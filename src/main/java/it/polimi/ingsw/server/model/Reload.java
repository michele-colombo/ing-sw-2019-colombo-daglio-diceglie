package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerState;

import java.util.ArrayList;
import java.util.List;

public class Reload implements MicroAction {
    public Reload() {

    }

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

    @Override
    public String toString(){
        return "R";
    }
}
