package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents the update of the layout
 */
public class LayoutUpdateMessage implements MessageVisitable, Serializable {
    /**
     * List of name of weapon on blue spaces of layout
     */
    private List<String> blueWeapons;
    /**
     * List of name of weapon on red spaces of layout
     */
    private List<String> redWeapons;
    /**
     * List of name of weapon on yellow spaces of layout
     */
    private List<String> yellowWeapons;
    /**
     * Maps name of AmmoTile with name of Ammo
     */
    private Map<String, String> ammoTilesInSquares;

    /**
     * Creates LayoutUpdateMessage with given parameters
     * @param blueWeapons name of weapon on blue spaces of layout
     * @param redWeapons name of weapon on red spaces of layout
     * @param yellowWeapons name of weapon on yellow spaces of layout
     * @param ammoTilesInSquares maps name of AmmoTile with name of Ammo
     */
    public LayoutUpdateMessage(List<String> blueWeapons, List<String> redWeapons, List<String> yellowWeapons, Map<String, String> ammoTilesInSquares) {
        this.blueWeapons = blueWeapons;
        this.redWeapons = redWeapons;
        this.yellowWeapons = yellowWeapons;
        this.ammoTilesInSquares = ammoTilesInSquares;
    }

    /**
     * Gets blueWeapons
     * @return blueWeapons
     */
    public List<String> getBlueWeapons() {
        return blueWeapons;
    }

    /**
     * Gets redWeapons
     * @return redWeapons
     */
    public List<String> getRedWeapons() {
        return redWeapons;
    }

    /**
     * Gets yellowWeapons
     * @return yellowWeapons
     */
    public List<String> getYellowWeapons() {
        return yellowWeapons;
    }

    /**
     * Gets ammoTilesInSquares
     * @return ammoTilesInSquares
     */
    public Map<String, String> getAmmoTilesInSquares() {
        return ammoTilesInSquares;
    }

    /**
     * Method used to properly recognize dynamic type of Message
     * @param messageVisitor messageVisitor who "visits" this message
     */
    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
