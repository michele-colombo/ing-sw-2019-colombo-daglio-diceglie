package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.util.List;
import java.util.Map;

public class KillshotTrackUpdateMessage extends MessageVisitable {

    private int skulls;
    private List<Map<String, Integer>> track;
    private boolean frenzyOn;
    private int yourPoints;

    public KillshotTrackUpdateMessage(int skulls, List<Map<String, Integer>> track, boolean frenzyOn, int yourPoints) {
        this.skulls = skulls;
        this.track = track;
        this.frenzyOn = frenzyOn;
        this.yourPoints = yourPoints;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }

}
