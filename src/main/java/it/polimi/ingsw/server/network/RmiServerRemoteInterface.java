package it.polimi.ingsw.server.network;

import it.polimi.ingsw.communication.events.EventVisitable;
import it.polimi.ingsw.communication.message.MessageVisitable;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerRemoteInterface extends Remote {
    void receive(EventVisitable event) throws RemoteException;

    MessageVisitable ask() throws RemoteException;

    boolean pong() throws RemoteException;
}
