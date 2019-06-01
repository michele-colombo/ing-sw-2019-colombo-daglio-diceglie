package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;

public class PongEvent extends EventVisitable{

    @Override
    public void accept(EventVisitor eventVisitor) {
        eventVisitor.visit(this);
    }
}
