package it.polimi.ingsw.client.network;

import it.polimi.ingsw.communication.message.MessageVisitable;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiClientRemoteInterface extends Remote {
    void receive(MessageVisitable message) throws RemoteException;
}
