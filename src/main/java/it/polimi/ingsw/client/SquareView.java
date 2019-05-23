package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.AmmoTile;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Border;

public class SquareView {
    private int x;
    private int y;
    private Border north;
    private Border east;
    private Border south;
    private Border west;
    private AmmoColor color;
    private boolean isAmmo;
    private AmmoTile ammo;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Border getNorth() {
        return north;
    }

    public Border getEast() {
        return east;
    }

    public Border getSouth() {
        return south;
    }

    public Border getWest() {
        return west;
    }

    public AmmoColor getColor() {
        return color;
    }

    public boolean isAmmo() {
        return isAmmo;
    }

    public AmmoTile getAmmo() {
        return ammo;
    }

    public void setAmmo(AmmoTile ammo) {
        this.ammo = ammo;
    }

    @Override
    public String toString() {
        return "("+x+"-"+y+")";
    }
}
