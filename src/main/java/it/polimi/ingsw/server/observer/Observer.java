package it.polimi.ingsw.server.observer;

import it.polimi.ingsw.communication.message.MessageVisitable;

/**
 * Observer interface for observer pattern
 */
public interface Observer {
    /**
     * Used by Observable to update Observers
     * @param messageVisitable update to be sent
     */
    void update(MessageVisitable messageVisitable);
}
