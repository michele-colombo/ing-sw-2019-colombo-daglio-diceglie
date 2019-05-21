package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.client.VisitorClient;

import java.util.List;

public class KillshotTrackUpdateMessage extends MessageVisitable {

    private int skulls;
    private List<String> track;
    private boolean frenzyOn;
    private int yourPoints;

    @Override
    public void accept(VisitorClient visitorClient) {
        visitorClient.visit(this);
    }

}
