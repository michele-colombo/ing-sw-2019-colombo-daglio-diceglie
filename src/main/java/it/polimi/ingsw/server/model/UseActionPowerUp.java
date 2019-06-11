package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.NextMicroActionException;
import it.polimi.ingsw.server.model.enums.PlayerState;
import it.polimi.ingsw.server.model.enums.PowerUpType;

public class UseActionPowerUp implements MicroAction {
    public UseActionPowerUp() {
    }

    @Override
    public void act(Match match, Player p) throws NextMicroActionException {
        p.setState(PlayerState.USE_POWERUP);
        p.resetSelectables();
        p.setSelectablePowerUps(p.getPowerUpsOfType(PowerUpType.ACTION_POWERUP));
    }

    @Override
    public String toString() {
        return "P";
    }
}
