package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;


/**
 * Represents interface for event from clients
 */
public interface EventVisitable{
    /**
     * Method used to properly recognize dynamic type of Event
     * @param eventVisitor eventVisitor who "visits" this event
     */
    void accept(EventVisitor eventVisitor);

}
