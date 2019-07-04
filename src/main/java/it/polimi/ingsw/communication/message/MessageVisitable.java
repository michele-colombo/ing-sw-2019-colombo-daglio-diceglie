package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

/**
 * Represents interface for messages from server
 */
public interface MessageVisitable{
    /**
     * Used to properly unwrap json
     */
    String LOGIN_MESSAGE_PREFIX = "#LOGIN#";
    /**
     * Used to properly unwrap json
     */
    String GENERIC_MESSAGE_PREFIX = "#GENERIC#";
    /**
     * Used to properly unwrap json
     */
    String LAYOUTUPDATE_MESSAGE_PREFIX = "#LAYOUTUPDATE#";
    /**
     * Used to properly unwrap json
     */
    String KILLSHOTUPDATE_MESSAGE_PREFIX = "#KILLSHOTUPDATE#";
    /**
     * Used to properly unwrap json
     */
    String CURRENTPLAYERUPDATE_MESSAGE_PREFIX = "#CURRENTPLAYERUPDATE#";
    /**
     * Used to properly unwrap json
     */
    String STARTMATCHUPDATE_MESSAGE_PREFIX = "#STARTMATCHUPDATE#";
    /**
     * Used to properly unwrap json
     */
    String PLAYERUPDATEMESSAGE_MESSAGE_PREFIX = "#PLAYERUPDATEMESSAGE#";
    /**
     * Used to properly unwrap json
     */
    String PAYMENTUPDATE_MESSAGE_PREFIX = "#PAYMENTUPDATE#";
    /**
     * Used to properly unwrap json
     */
    String WEAPONSUPDATE_MESSAGE_PREFIX = "#WEAPONSUPDATE#";
    /**
     * Used to properly unwrap json
     */
    String POWERUPUPDATE_MESSAGE_PREFIX = "#POWERUPUPDATE#";
    /**
     * Used to properly unwrap json
     */
    String SELECTABLESUPDATE_MESSAGE_PREFIX = "#SELECTABLESUPDATE#";
    /**
     * Used to properly unwrap json
     */
    String DAMAGEUPDATE_MESSAGE_PREFIX = "#DAMAGEUPDATE#";
    /**
     * Used to properly unwrap json
     */
    String CONNECTIONUPDATE_MESSAGE_PREFIX = "#CONNECTIONUPDATE#";
    /**
     * Used to properly unwrap json
     */
    String GAMEOVER_MESSAGE_PREFIX = "#GAMEOVER#";

    /**
     * Method used to properly recognize dynamic type of Message
     * @param messageVisitor messageVisitor who "visits" this message
     */
    void accept(MessageVisitor messageVisitor);
}
