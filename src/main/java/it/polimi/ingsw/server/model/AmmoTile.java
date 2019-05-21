package it.polimi.ingsw.server.model;

public class AmmoTile{
    private Cash ammos;
    private boolean hasPowerUp;
    private int ammoTileID;

    public AmmoTile(Cash ammos, boolean hasPowerUp) {
        this.ammos = new Cash(ammos);
        this.hasPowerUp = hasPowerUp;
    }

    public void setAmmoTileID(int ammoTileID) {
        this.ammoTileID = ammoTileID;
    }

    @Override
    public String toString(){
        StringBuilder result= new StringBuilder();
        result.append(ammoTileID+"-");
        result.append(ammos.toString());
        if (hasPowerUp()) result.append("| with powerUp");
        return result.toString();
    }

    //ammoTile is immutable
    public Cash getAmmos() {
        return new Cash(ammos);
    }

    public boolean hasPowerUp() {
        return hasPowerUp;
    }
}
