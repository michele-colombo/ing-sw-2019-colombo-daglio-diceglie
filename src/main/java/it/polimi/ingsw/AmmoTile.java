package it.polimi.ingsw;

public class AmmoTile{
    private Cash ammos;
    private boolean hasPowerUp;

    public AmmoTile(Cash ammos, boolean hasPowerUp) {
        this.ammos = ammos;
        this.hasPowerUp = hasPowerUp;
    }

    public Cash getAmmos() {
        return ammos;
    }

    public boolean HasPowerUp() {
        return hasPowerUp;
    }
}
