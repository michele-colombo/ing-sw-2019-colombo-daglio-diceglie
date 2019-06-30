package it.polimi.ingsw.server.model;

public class AmmoTile{
    /**
     * The ammos contained (2 or 3 in total)
     */
    private Cash ammos;

    /**
     * Boolean for the presence of a powerUP to draw
     */
    private boolean hasPowerUp;

    /**
     * Integer that identifies the ammoTile among others
     */
    private int ammoTileID;

    /**
     * Constructs the ammoTile setting ammos and the presence of powerUP
     * @param ammos the contained ammos
     * @param hasPowerUp true if it contains a powerUp
     */
    public AmmoTile(Cash ammos, boolean hasPowerUp) {
        this.ammos = new Cash(ammos);
        this.hasPowerUp = hasPowerUp;
    }

    /**
     * Build a string with the content of the ammoTile. It is the unique identifier of the ammoTile.
     * @return string specific of this ammoTile
     */
    @Override
    public String toString(){
        StringBuilder result= new StringBuilder();
        result.append(ammoTileID+"-");
        result.append(ammos.toString());
        if (hasPowerUp()) result.append("| with powerUp");
        return result.toString();
    }

    /**
     * Gets the ammos contained
     * @return a copy of the ammos
     */
    public Cash getAmmos() {
        return new Cash(ammos);
    }

    /**
     * Tells if the ammoTile contains a powerUp
     * @return true if it has a powerUP
     */
    public boolean hasPowerUp() {
        return hasPowerUp;
    }

    /**
     * Gets the integer ID o fthe ammoTile
     */
    public int getAmmoTileID(){
        return ammoTileID;
    }
}
