package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.server.model.Cash;

public class PaymentUpdateMessage extends MessageVisitable {

    private Cash pending;
    private Cash credit;

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
