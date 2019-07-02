package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.server.model.Cash;

import java.io.Serializable;

/**
 * Represents payment update
 */
public class PaymentUpdateMessage implements MessageVisitable, Serializable {
    /**
     * Cash to be paid
     */
    private Cash pending;
    /**
     * Cash already paid
     */
    private Cash credit;

    /**
     * Creates a PaymentUpdateMessage with given parameters
     * @param pending cash to be paid
     * @param credit cash already paid
     */
    public PaymentUpdateMessage(Cash pending, Cash credit) {
        this.pending = pending;
        this.credit = credit;
    }

    /**
     *
     * @return pending
     */
    public Cash getPending() {
        return pending;
    }

    /**
     *
     * @return credit
     */
    public Cash getCredit() {
        return credit;
    }

    /**
     * Method used to properly recognize dynamic type of Message
     * @param messageVisitor messageVisitor who "visits" this message
     */
    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
