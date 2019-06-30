package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.NextMicroActionException;

public interface MicroAction {
    /**
     * Prepares selectable items for the micro action (grabbing, moving, shooting, etc) to be executed
     * @param match the current match
     * @param p the player which is taking the action
     * @throws NextMicroActionException if the next micro action has to be started immediately, without any further interaction by the player
     */
    public void act(Match match, Player p) throws NextMicroActionException;
}

