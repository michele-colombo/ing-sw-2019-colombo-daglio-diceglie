package it.polimi.ingsw.Server.Events;

import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Message.Message;

import java.io.Serializable;

public abstract class EventVisitable implements Serializable {
    public abstract Message accept(Controller visitor); //Class Controller is the Visitor
}
