package it.polimi.ingsw.server.network;

import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.server.ServerViewInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerAcceptorInterface extends Remote{
    ServerViewInterface addMe(ClientInterface clientInterface) throws RemoteException;

}



