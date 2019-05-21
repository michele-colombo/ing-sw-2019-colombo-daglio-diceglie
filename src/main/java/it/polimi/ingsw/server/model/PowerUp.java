package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.PowerUpType;

import java.util.ArrayList;
import java.util.List;

public class PowerUp{
    private AmmoColor color;
    private PowerUpType type;
    private String name;
    private String description;
    private List<Effect> effects;
    private int powerUpID;

    public PowerUp (AmmoColor color, PowerUpType type, String name){
        this.color = color;
        this.type = type;
        this.name = name;

        this.effects= new ArrayList<>(); //added by Giuseppe: list must not be NULL
    }

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

    public List<Effect> getEffects() {
        return effects;
    }
}
