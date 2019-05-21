package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.NextMicroActionException;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerState;
import it.polimi.ingsw.server.model.enums.PowerUpType;

public class UsePowerUp implements MicroAction {
    private PowerUpType type;

    public UsePowerUp(PowerUpType type) {
        this.type = type;
    }

    public PowerUpType getType() {
        return type;
    }

    @Override
    public void act(Match match, Player p) throws NextMicroActionException {
        switch (type){
            case TARGETING_SCOPE:
                if (p.howManyPowerUps(type)>0 && !match.getCurrentAction().getDamaged().isEmpty()) {
                    p.setState(PlayerState.USE_POWERUP);
                    p.resetSelectables();
                    p.setSelectablePowerUps(p.getPowerUpsOfType(type));
                    p.setSelectableCommands(Command.OK);
                } else {
                    throw new NextMicroActionException();
                }
                break;
            case TAGBACK_GRENADE:
                match.clearWaitingFor();
                for (Player player : match.getPlayers()){
                    if (match.getCurrentAction().getDamaged().contains(player) && player.howManyPowerUps(type)>0
                            && match.getLayout().getVisibleSquares(player.getSquarePosition()).contains(p.getSquarePosition())){
                        match.addWaitingFor(player);
                        //check: if inactive is put in the toFakeList, too
                        player.setState(PlayerState.USE_POWERUP);
                        player.resetSelectables();
                        player.setSelectablePowerUps(player.getPowerUpsOfType(type));
                        player.setSelectableCommands(Command.OK);
                    }
                }
                //each player in the toFakeList has the action faked
                p.setState(PlayerState.USE_POWERUP);
                p.resetSelectables();
                if (match.getWaitingFor().isEmpty()) {    //if there isn't any player which can use tagback, go on with the next microAction
                    match.addWaitingFor(match.getCurrentPlayer());
                    throw new NextMicroActionException();
                }
                break;
            case ACTION_POWERUP:
                p.setState(PlayerState.USE_POWERUP);
                p.resetSelectables();
                p.setSelectablePowerUps(p.getPowerUpsOfType(type));
                break;
        }
    }

    @Override
    public String toString(){
        switch (type){
            case TAGBACK_GRENADE:
                return "";
            case TARGETING_SCOPE:
                return "";
            case ACTION_POWERUP:
                return "P";
            default:
                return "";
        }
    }
}