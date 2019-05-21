package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.client.VisitorClient;

public class CurrentPlayerUpdateMessage extends MessageVisitable{
    private String currentPlayer;

    @Override
    public void accept(VisitorClient visitorClient) {
        visitorClient.visit(this);
    }
}
