package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents the update of players' connections
 */
public class ConnectionUpdateMessage implements MessageVisitable, Serializable {
    /**
     * Map each players' name with their connection state: true if online, false if offline
     */
    private Map<String, Boolean> connectionStates;

    /**
     * Creates ConnectionUpdateMessage with given Map
     * @param connectionStates Map of players' connection
     */
    public ConnectionUpdateMessage(Map<String, Boolean> connectionStates) {
        this.connectionStates = connectionStates;
    }

    /**
     *
     * @return connectionStates
     */
    public Map<String, Boolean> getConnectionStates() {
        return connectionStates;
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
