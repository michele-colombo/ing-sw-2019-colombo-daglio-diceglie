package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.NextMicroActionException;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerState;
import it.polimi.ingsw.server.model.enums.PowerUpType;

public class UseTargeting implements MicroAction {
    public UseTargeting() {

    }

    @Override
    public void act(Match match, Player p) throws NextMicroActionException {
        if (p.howManyPowerUps(PowerUpType.TARGETING_SCOPE)>0 && !match.getCurrentAction().getDamaged().isEmpty()) {
            p.setState(PlayerState.USE_POWERUP);
            p.resetSelectables();
            p.setSelectablePowerUps(p.getPowerUpsOfType(PowerUpType.TARGETING_SCOPE));
            p.setSelectableCommands(Command.OK);
        } else {
            throw new NextMicroActionException();
        }
    }

    @Override
    public String toString() {
        return "";
    }
}
