package it.polimi.ingsw.server.network;

import it.polimi.ingsw.communication.events.EventVisitable;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerRemoteInterface extends Remote {
    void receive(EventVisitable event) throws RemoteException;
}
