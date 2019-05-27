package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;

import java.io.Serializable;
import java.rmi.RemoteException;

public abstract class EventVisitable implements Serializable {
    public abstract void accept(EventVisitor eventVisitor) throws RemoteException;

}
