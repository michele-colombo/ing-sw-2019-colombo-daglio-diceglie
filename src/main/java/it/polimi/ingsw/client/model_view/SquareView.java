package it.polimi.ingsw.client.model_view;

import it.polimi.ingsw.server.model.AmmoTile;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Border;

/**
 * Represents a Square on client
 */
public class SquareView {
    /**
     * Coordinates x of SquareView
     */
    private int x;
    /**
     * Coordinates y of SquareView
     */
    private int y;
    /**
     * Norther Border of the SquareView
     */
    private Border north;
    /**
     * Eastern Border of the SquareView
     */
    private Border east;
    /**
     * Southern Border of the SquareView
     */
    private Border south;
    /**
     * Western Border of the SquareView
     */
    private Border west;
    /**
     * Color of this SquareView
     */
    private AmmoColor color;
    /**
     * True if this SquareView has a WeaponView, else false
     */
    private boolean isAmmo;
    /**
     * AmmoTile associated to this SquareView
     */
    private AmmoTile ammo;

    /**
     * Gets x
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Gets y
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Gets north
     * @return north
     */
    public Border getNorth() {
        return north;
    }

    /**
     * Gets east
     * @return east
     */
    public Border getEast() {
        return east;
    }

    /**
     * Gets south
     * @return south
     */
    public Border getSouth() {
        return south;
    }

    /**
     * Gets west
     * @return west
     */
    public Border getWest() {
        return west;
    }

    /**
     * Gets color
     * @return color
     */
    public AmmoColor getColor() {
        return color;
    }

    /**
     * Retunr isAmmo
     * @return isAmmo
     */
    public boolean isAmmo() {
        return isAmmo;
    }

    /**
     * Gets ammo
     * @return ammo
     */
    public AmmoTile getAmmo() {
        return ammo;
    }

    /**
     * Sets Ammo
     * @param ammo ammo to be set
     */
    public void setAmmo(AmmoTile ammo) {
        this.ammo = ammo;
    }

    /**
     * Gets String representation of SquareView
     * @return String representation of SquareView
     */
    @Override
    public String toString() {
        return "("+x+"-"+y+")";
    }
}
