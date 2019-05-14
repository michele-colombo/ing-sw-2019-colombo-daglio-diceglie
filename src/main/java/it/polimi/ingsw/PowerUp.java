package it.polimi.ingsw;

import java.util.List;

public class PowerUp{
    private Color color;
    private PowerUpType type;
    private String name;
    private String description;
    private List<Effect> effects;
    private int powerUpID;

    public PowerUp (Color color, PowerUpType type, String name){
        this.color = color;
        this.type = type;
        this.name = name;
    }

    public Color getColor() {
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
