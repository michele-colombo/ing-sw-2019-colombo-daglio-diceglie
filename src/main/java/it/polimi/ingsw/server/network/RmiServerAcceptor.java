package it.polimi.ingsw.server.network;

import it.polimi.ingsw.client.network.RmiClientRemoteInterface;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.controller.Controller;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RmiServerAcceptor extends UnicastRemoteObject implements RmiServerAcceptorInterface {

    private List<ServerView> serverViewList;
    private Controller controller;


    public RmiServerAcceptor(Controller controller) throws RemoteException {
        serverViewList= new ArrayList<>();
        this.controller= controller;
    }

    public List<ServerView> getServerViewList() {
        return serverViewList;
    }

    @Override
    public RmiServerRemoteInterface addMe (RmiClientRemoteInterface rmiClientRemoteInterface) throws RemoteException {
        return new RmiServer(rmiClientRemoteInterface, controller);
    }

}
