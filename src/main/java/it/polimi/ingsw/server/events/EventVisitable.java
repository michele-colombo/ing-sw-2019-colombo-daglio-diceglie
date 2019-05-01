package it.polimi.ingsw.server.events;

import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.Visitor;

import java.io.Serializable;

public abstract class EventVisitable implements Serializable {
    public abstract void accept(Visitor visitor, ServerView serverView); //Class Controller is the Visitor
}
