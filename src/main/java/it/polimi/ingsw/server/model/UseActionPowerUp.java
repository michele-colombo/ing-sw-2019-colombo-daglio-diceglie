package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.NextMicroActionException;
import it.polimi.ingsw.server.model.enums.PlayerState;
import it.polimi.ingsw.server.model.enums.PowerUpType;

public class UseActionPowerUp implements MicroAction {
    /**
     * Builds the micro action for using the action powerups (teleporter and newton)
     */
    public UseActionPowerUp() {
    }

    /**
     * Prepares the powerups as selectebles
     * @param match the current match
     * @param p the player which is taking the action
     */
    @Override
    public void act(Match match, Player p) {
        p.setState(PlayerState.USE_POWERUP);
        p.resetSelectables();
        p.setSelectablePowerUps(p.getPowerUpsOfType(PowerUpType.ACTION_POWERUP));
    }

    /**
     * Gets a string that uniquely describes this microaction
     * @return a unique string
     */
    @Override
    public String toString() {
        return "P";
    }
}
