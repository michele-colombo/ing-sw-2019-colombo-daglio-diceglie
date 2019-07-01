package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.PowerUpType;

public class PowerUpView {
    private AmmoColor color;
    private PowerUpType type;
    private String name;
    private String description;
    private int powerUpID;

    public AmmoColor getColor() {
        return color;
    }

    public PowerUpType getType() {
        return type;
    }

    public void setPowerUpID(int powerUpID) {
        this.powerUpID = powerUpID;
    }

    @Override
    public String toString(){
        return powerUpID+"-"+name+"-"+color;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
