package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

public class PingMessage extends MessageVisitable {
    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
