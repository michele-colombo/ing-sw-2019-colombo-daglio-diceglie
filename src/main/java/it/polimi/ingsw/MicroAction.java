package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.NextMicroActionException;

public interface MicroAction {
    public void act(Match match, Player p) throws NextMicroActionException;
}

