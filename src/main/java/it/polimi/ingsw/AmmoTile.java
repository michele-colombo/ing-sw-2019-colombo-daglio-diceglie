package it.polimi.ingsw;

public class AmmoTile{
    private Cash ammos;
    private boolean hasPowerUp;

    public AmmoTile(Cash ammos, boolean hasPowerUp) {
        this.ammos = ammos;
        this.hasPowerUp = hasPowerUp;
    }

    @Override
    public String toString(){
        StringBuilder result= new StringBuilder();
        result.append(ammos.toString());
        if (hasPowerUp()) result.append("| with a powerUp");
        return result.toString();
    }

    public Cash getAmmos() {
        return ammos;
    }

    public boolean hasPowerUp() {
        return hasPowerUp;
    }
}
