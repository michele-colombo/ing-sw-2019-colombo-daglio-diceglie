package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.client.VisitorClient;

public class ConnectionUpdateMessage extends MessageVisitable{
    private String name;
    private boolean connected;

    @Override
    public void accept(VisitorClient visitorClient) {
        visitorClient.visit(this);
    }
}
