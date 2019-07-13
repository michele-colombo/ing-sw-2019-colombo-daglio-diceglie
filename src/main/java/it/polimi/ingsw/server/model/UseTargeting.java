package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.NextMicroActionException;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerState;
import it.polimi.ingsw.server.model.enums.PowerUpType;

/**
 * Represents the task of using the targeeting scope powerup
 */
public class UseTargeting implements MicroAction {
    /**
     * Builds the micro action for using the targeting scope.
     * It can be included as part of a shooting action.
     */
    public UseTargeting() {}

    /**
     * Checks if the targeting scope can be used. If so, it puts the powerups as selectable and OK to skip.
     * @param match the current match
     * @param p the player which is taking the action
     * @throws NextMicroActionException if the next micro action has to be started immediately, without any further interaction by the player
     */
    @Override
    public void act(Match match, Player p) throws NextMicroActionException {
        if (p.howManyPowerUps(PowerUpType.TARGETING_SCOPE)>0 && !match.getCurrentAction().getDamaged().isEmpty() && (p.getPowerUps().size()>1 || p.getWallet().getTotal()>0)) {
            p.setState(PlayerState.USE_POWERUP);
            p.resetSelectables();
            p.setSelectablePowerUps(p.getPowerUpsOfType(PowerUpType.TARGETING_SCOPE));
            p.setSelectableCommands(Command.OK);
        } else {
            throw new NextMicroActionException();
        }
    }

    /**
     * Gets an empty string,
     * since the presence of this microaction does not modify the action from the player's point of view.
     */
    @Override
    public String toString() {
        return "";
    }
}
