package it.polimi.ingsw.server.network;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerAcceptorInterface extends Remote{
    RmiServerRemoteInterface addMe () throws RemoteException;
}



