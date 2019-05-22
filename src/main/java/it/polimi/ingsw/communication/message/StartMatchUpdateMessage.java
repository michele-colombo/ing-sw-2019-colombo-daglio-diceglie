package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.server.model.enums.PlayerColor;

import java.util.List;

public class StartMatchUpdateMessage extends MessageVisitable{
    private int layoutConfiguration;
    private List<String> names;
    private List<PlayerColor> colors;

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
