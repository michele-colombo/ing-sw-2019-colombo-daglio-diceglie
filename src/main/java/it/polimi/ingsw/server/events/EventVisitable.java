package it.polimi.ingsw.server.events;

import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.VisitorServer;

import java.io.Serializable;

public abstract class EventVisitable implements Serializable {
    public abstract void accept(VisitorServer visitorServer, ServerView serverView); //Class Controller is the VisitorServer
}
