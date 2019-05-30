package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.server.model.Cash;

import java.rmi.RemoteException;

public class PaymentUpdateMessage extends MessageVisitable {

    private Cash pending;
    private Cash credit;

    public PaymentUpdateMessage(Cash pending, Cash credit) {
        this.pending = pending;
        this.credit = credit;
    }

    public Cash getPending() {
        return pending;
    }

    public Cash getCredit() {
        return credit;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
