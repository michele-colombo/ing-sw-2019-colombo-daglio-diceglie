package it.polimi.ingsw.server.network;

import it.polimi.ingsw.client.network.RmiClientRemoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerAcceptorInterface extends Remote{
    RmiServerRemoteInterface addMe (RmiClientRemoteInterface rmiClientRemoteInterface) throws RemoteException;
}



