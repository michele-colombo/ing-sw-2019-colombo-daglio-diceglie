package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.PowerUpType;

/**
 * Represents PowerUp on client
 */
public class PowerUpView {
    /**
     * Color of the PowerUpView
     */
    private AmmoColor color;
    /**
     * Name of the PowerUpView
     */
    private String name;
    /**
     * Description of the PowerUpView
     */
    private String description;
    /**
     * ID of the PowerUp
     */
    private int powerUpID;

    /**
     *
     * @return color
     */
    public AmmoColor getColor() {
        return color;
    }

    /**
     *
     * @return String representation of this PowerUpView
     */
    @Override
    public String toString(){
        return powerUpID+"-"+name+"-"+color;
    }

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description
     * @param description description to be set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
