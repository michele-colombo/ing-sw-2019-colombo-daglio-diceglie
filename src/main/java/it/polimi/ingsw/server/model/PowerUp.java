package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.PowerUpType;

import java.util.ArrayList;
import java.util.List;

/**
 * Is the powerUp card, which contains a list of effects.
 * Each powerup has its unique ID (an integer)
 */
public class PowerUp{
    /**
     * the color of the powerUp (on of the three spawn point colors)
     */
    private AmmoColor color;

    /**
     * the type of the powerUp. It indicates when it can be used.
     */
    private PowerUpType type;

    /**
     * the name of the powerup
     */
    private String name;

    /**
     * a description of the powerup
     */
    private String description;

    /**
     * the list of effects of the powerup. Each effect corresponds to an interaction with the user.
     */
    private List<Effect> effects;

    /**
     * the unique ID of the powerup
     */
    private int powerUpID;

    /**
     *
     * Builds the powerup specifing color, type and name. Used in tests only.
     * @param color color of the powerup
     * @param type type of the powerup
     * @param name name of the powerup
     */
    public PowerUp (AmmoColor color, PowerUpType type, String name){
        this.color = color;
        this.type = type;
        this.name = name;

        this.effects= new ArrayList<>(); //added by Giuseppe: list must not be NULL
    }

    /**
     * Gets the color of the powreup
     * @return one of the three spawnpoint colors
     */
    public AmmoColor getColor() {
        return color;
    }

    /**
     * Gets the type of the powerup. There are three possible types,
     * depending on the moment it can be used: ACTION, TAGBACK, TARGETING
     * @return an element of the enum PowerUpType.
     */
    public PowerUpType getType() {
        return type;
    }

    /**
     * Gets the unique identifier of the powerup
     * @param powerUpID an integer which is the uniwue identifier
     */
    public void setPowerUpID(int powerUpID) {
        this.powerUpID = powerUpID;
    }

    /**
     * Gets a description string of the content of the powerup (which is unique, too)
     * @return string with ID, name and color
     */
    @Override
    public String toString(){
        return powerUpID+"-"+name+"-"+color;
    }

    /**
     * Gets the name of the powerup
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the powerup (what it does and how to use it)
     * @return a string with the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the list of the effects
     * @return a clone of the list (so any modify does not affect the original list).
     */
    public List<Effect> getEffects() {
        return new ArrayList<>(effects);
    }
}
