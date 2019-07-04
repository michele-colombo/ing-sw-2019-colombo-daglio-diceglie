package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;


/**
 * Represents interface for event from clients
 */
public interface EventVisitable{
    /**
     * Used to properly unwrap json
     */
    String LOGIN_PREFIX = "#LOGIN#";
    /**
     * Used to properly wrap/unwrap json
     */
    String SQUARESELECTED_PREFIX = "#SQUARESELECTED#";
    /**
     * Used to properly wrap/unwrap json
     */
    String ACTIONSELECTED_PREFIX = "#ACTIONSELECTED#";
    /**
     * Used to properly wrap/unwrap json
     */
    String PLAYERSELECTED_PREFIX = "#PLAYERSELECTED#";
    /**
     * Used to properly wrap/unwrap json
     */
    String WEAPONSELECTED_PREFIX = "#WEAPONSELECTED#";
    /**
     * Used to properly wrap/unwrap json
     */
    String MODESELECTED_PREFIX = "#MODESELECTED#";
    /**
     * Used to properly wrap/unwrap json
     */
    String COMMANDSELECTED_PREFIX = "#COMMANDSELECTED#";
    /**
     * Used to properly wrap/unwrap json
     */
    String COLORSELECTED_PREFIX = "#COLORSELECTED#";
    /**
     * Used to properly wrap/unwrap json
     */
    String POWERUPSELECTED_PREFIX = "#POWERUPSELECTED_PREFIX#";
    /**
     * Method used to properly recognize dynamic type of Event
     * @param eventVisitor eventVisitor who "visits" this event
     */
    void accept(EventVisitor eventVisitor);

}
