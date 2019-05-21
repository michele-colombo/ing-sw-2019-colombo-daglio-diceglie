package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.controller.VisitorServer;

import java.io.Serializable;

public abstract class EventVisitable implements Serializable {
    public abstract void accept(VisitorServer visitorServer, ServerView serverView); //Class Controller is the VisitorServer
}
