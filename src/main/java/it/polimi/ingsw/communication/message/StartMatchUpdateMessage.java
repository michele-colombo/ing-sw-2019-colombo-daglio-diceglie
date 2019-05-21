package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.client.VisitorClient;
import it.polimi.ingsw.server.model.enums.PlayerColor;

import java.util.List;

public class StartMatchUpdateMessage extends MessageVisitable{
    private int layoutConfiguration;
    private List<String> names;
    private List<PlayerColor> colors;

    @Override
    public void accept(VisitorClient visitorClient) {
        visitorClient.visit(this);
    }
}
