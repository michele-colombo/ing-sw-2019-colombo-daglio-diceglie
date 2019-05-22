package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

public class ConnectionUpdateMessage extends MessageVisitable{
    private String name;
    private boolean connected;

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
