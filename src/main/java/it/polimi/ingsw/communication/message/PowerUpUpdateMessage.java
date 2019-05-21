package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.client.VisitorClient;

import java.util.List;

public class PowerUpUpdateMessage extends MessageVisitable {
    private String name;
    private List<String> powerUps;
    private int numPowerUps;

    @Override
    public void accept(VisitorClient visitorClient) {
        visitorClient.visit(this);
    }
}
