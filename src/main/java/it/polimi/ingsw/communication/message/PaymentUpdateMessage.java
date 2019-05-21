package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.client.VisitorClient;
import it.polimi.ingsw.server.model.Cash;

public class PaymentUpdateMessage extends MessageVisitable {

    private Cash pending;
    private Cash credit;

    @Override
    public void accept(VisitorClient visitorClient) {
        visitorClient.visit(this);
    }
}
