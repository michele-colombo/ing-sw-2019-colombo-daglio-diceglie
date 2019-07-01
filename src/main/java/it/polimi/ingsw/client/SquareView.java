package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.AmmoTile;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Border;

/**
 * Represent a Square on client
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
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @return north
     */
    public Border getNorth() {
        return north;
    }

    /**
     *
     * @return east
     */
    public Border getEast() {
        return east;
    }

    /**
     *
     * @return south
     */
    public Border getSouth() {
        return south;
    }

    /**
     *
     * @return west
     */
    public Border getWest() {
        return west;
    }

    /**
     *
     * @return color
     */
    public AmmoColor getColor() {
        return color;
    }

    /**
     *
     * @return isAmmo
     */
    public boolean isAmmo() {
        return isAmmo;
    }

    /**
     *
     * @return ammo
     */
    public AmmoTile getAmmo() {
        return ammo;
    }

    /**
     * Set Ammo
     * @param ammo ammo to be set
     */
    public void setAmmo(AmmoTile ammo) {
        this.ammo = ammo;
    }

    /**
     *
     * @return String representation of SquareView
     */
    @Override
    public String toString() {
        return "("+x+"-"+y+")";
    }
}
