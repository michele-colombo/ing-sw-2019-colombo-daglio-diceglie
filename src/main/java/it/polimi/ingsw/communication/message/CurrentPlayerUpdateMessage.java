package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;

/**
 * Represents current player's update
 */
public class CurrentPlayerUpdateMessage implements MessageVisitable, Serializable {
    /**
     * Current player's name
     */
    private String currentPlayer;

    /**
     * Creates CurrentPlayerUpdateMessage with given String
     * @param currentPlayer current player's name
     */
    public CurrentPlayerUpdateMessage(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     *
     * @return currentPlayer
     */
    public String getCurrentPlayer() {
        return currentPlayer;
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
