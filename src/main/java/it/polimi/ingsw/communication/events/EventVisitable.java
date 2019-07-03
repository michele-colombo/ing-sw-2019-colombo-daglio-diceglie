package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;


/**
 * Represents interface for event from clients
 */
public interface EventVisitable{
    String LOGIN_PREFIX = "#LOGIN#";
    String SQUARESELECTED_PREFIX = "#SQUARESELECTED#";
    String ACTIONSELECTED_PREFIX = "#ACTIONSELECTED#";
    String PLAYERSELECTED_PREFIX = "#PLAYERSELECTED#";
    String WEAPONSELECTED_PREFIX = "#WEAPONSELECTED#";
    String MODESELECTED_PREFIX = "#MODESELECTED#";
    String COMMANDSELECTED_PREFIX = "#COMMANDSELECTED#";
    String COLORSELECTED_PREFIX = "#COLORSELECTED#";
    String POWERUPSELECTED_PREFIX = "#POWERUPSELECTED_PREFIX#";
    /**
     * Method used to properly recognize dynamic type of Event
     * @param eventVisitor eventVisitor who "visits" this event
     */
    void accept(EventVisitor eventVisitor);

}
