package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.NextMicroActionException;

public interface MicroAction {
    public void act(Match match, Player p) throws NextMicroActionException;
}

