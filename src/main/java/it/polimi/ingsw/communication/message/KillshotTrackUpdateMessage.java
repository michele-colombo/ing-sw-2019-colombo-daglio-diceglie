package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents the update of the killshot track
 */
public class KillshotTrackUpdateMessage implements MessageVisitable, Serializable {
    /**
     * Number of skulls on the killshot track
     */
    private int skulls;
    /**
     * List of Map, representing kills on killshot track. They maps player's name with 1 or 2, depending on
     * if a kill was performed without or with overkill
     */
    private List<Map<String, Integer>> track;
    /**
     * True if match is in frenzy mode, else false
     */
    private boolean frenzyOn;
    /**
     * Points of this client
     */
    private int yourPoints;

    /**
     * Creates KillshotTrackUpdateMessage with given parameters
     * @param skulls skull on killshot track
     * @param track list of kills
     * @param frenzyOn frenzy situation of the match
     * @param yourPoints your points
     */
    public KillshotTrackUpdateMessage(int skulls, List<Map<String, Integer>> track, boolean frenzyOn, int yourPoints) {
        this.skulls = skulls;
        this.track = track;
        this.frenzyOn = frenzyOn;
        this.yourPoints = yourPoints;
    }

    /**
     *
     * @return skulls
     */
    public int getSkulls() {
        return skulls;
    }

    /**
     *
     * @return track
     */
    public List<Map<String, Integer>> getTrack() {
        return track;
    }

    /**
     *
     * @return frenzyOn
     */
    public boolean isFrenzyOn() {
        return frenzyOn;
    }

    /**
     *
     * @return yourPoints
     */
    public int getYourPoints() {
        return yourPoints;
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
