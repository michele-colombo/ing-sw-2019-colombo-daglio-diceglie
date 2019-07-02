package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a player's power ups update
 */
public class PowerUpUpdateMessage implements MessageVisitable, Serializable {
    /**
     * Updated player's name
     */
    private String name;
    /**
     * Names of power ups owned
     */
    private List<String> powerUps;
    /**
     * Number of power ups owned
     */
    private int numPowerUps;

    /**
     * Creates PowerUpUpdateMessage with given parameters
     * @param name updated player's name
     * @param powerUps names of power ups owned
     * @param numPowerUps number of power ups owned
     */
    public PowerUpUpdateMessage(String name, List<String> powerUps, int numPowerUps) {
        this.name = name;
        this.powerUps = powerUps;
        this.numPowerUps = numPowerUps;
    }

    /**
     * Creates PowerUpUpdateMessage with given parameters, initialing powerUps
     * to a new empty list
     * @param name updated player's name
     * @param numPowerUps number of power ups owned
     */
    public PowerUpUpdateMessage(String name, int numPowerUps) {
        this.name = name;
        this.powerUps = new ArrayList<>();
        this.numPowerUps = numPowerUps;
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
     * @return powerUps
     */
    public List<String> getPowerUps() {
        return powerUps;
    }

    /**
     *
     * @return numPowerUps
     */
    public int getNumPowerUps() {
        return numPowerUps;
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
