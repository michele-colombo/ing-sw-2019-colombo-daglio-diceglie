package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.PlayerState;

import java.io.Serializable;

/**
 * Represents a player's update
 */
public class PlayerUpdateMessage implements MessageVisitable, Serializable {
    /**
     * Updated player's name
     */
    private String name;
    /**
     * Player's state
     */
    private PlayerState state;
    /**
     * Name of the square of the player
     */
    private String position;
    /**
     * Player's wallet
     */
    private Cash wallet;

    /**
     * Creates PlayerUpdateMessage with given parameters
     * @param name updated player's name
     * @param state player's state
     * @param position name of the square of the player
     * @param wallet player's wallet
     */
    public PlayerUpdateMessage(String name, PlayerState state, String position, Cash wallet) {
        this.name = name;
        this.state = state;
        this.position = position;
        this.wallet = wallet;
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
     * @return state
     */
    public PlayerState getState() {
        return state;
    }

    /**
     *
     * @return position
     */
    public String getPosition() {
        return position;
    }

    /**
     *
     * @return wallet
     */
    public Cash getWallet() {
        return wallet;
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
