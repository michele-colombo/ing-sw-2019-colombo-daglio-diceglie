package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.NextMicroActionException;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerState;
import it.polimi.ingsw.server.model.enums.PowerUpType;

public class ReceiveTagback implements MicroAction {

    public ReceiveTagback() {

    }

    @Override
    public void act(Match match, Player p) throws NextMicroActionException {
        match.clearWaitingFor();
        for (Player player : match.getPlayers()){
            if (match.getCurrentAction().getDamaged().contains(player) && player.howManyPowerUps(PowerUpType.TAGBACK_GRENADE)>0
                    && match.getLayout().getVisibleSquares(player.getSquarePosition()).contains(p.getSquarePosition())){
                match.addWaitingFor(player);
                //check: if inactive is put in the toFakeList, too
                player.setState(PlayerState.USE_POWERUP);
                player.resetSelectables();
                player.setSelectablePowerUps(player.getPowerUpsOfType(PowerUpType.TAGBACK_GRENADE));
                player.setSelectableCommands(Command.OK);

                match.notifyPlayerUpdate(player);
            }
        }
        p.setState(PlayerState.USE_POWERUP);
        p.resetSelectables();
        if (match.getWaitingFor().isEmpty()) {    //if there isn't any player which can use tagback, go on with the next microAction
            match.addWaitingFor(match.getCurrentPlayer());
            throw new NextMicroActionException();
        }
    }

    @Override
    public String toString() {
        return "";
    }
}
