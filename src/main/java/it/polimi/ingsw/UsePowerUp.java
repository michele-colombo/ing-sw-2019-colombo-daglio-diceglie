package it.polimi.ingsw;

public class UsePowerUp implements MicroAction {
    private PowerUpType type;

    public UsePowerUp(PowerUpType type) {
        this.type = type;
    }

    public PowerUpType getType() {
        return type;
    }

    @Override
    public void act(Match match, Player p) {

    }
}
