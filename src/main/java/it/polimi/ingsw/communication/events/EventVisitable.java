package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;


public abstract class EventVisitable{
    public abstract void accept(EventVisitor eventVisitor);

}
