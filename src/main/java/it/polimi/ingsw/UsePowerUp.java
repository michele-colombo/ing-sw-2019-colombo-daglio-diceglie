package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.NextMicroActionException;

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
        match.getCurrentAction().setWaitingFor(0);
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
                for (Player player : match.getPlayers()){
                    if (match.getCurrentAction().getDamaged().contains(player) && player.howManyPowerUps(type)>0){
                        match.getCurrentAction().incrWaitingFor(1);
                        player.setState(PlayerState.USE_POWERUP);
                        player.resetSelectables();
                        player.setSelectablePowerUps(player.getPowerUpsOfType(type));
                        player.setSelectableCommands(Command.OK);
                    }
                }
                p.resetSelectables();
                if (match.getCurrentAction().getWaitingFor() <= 0) {
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
        return "P";
    }
}
