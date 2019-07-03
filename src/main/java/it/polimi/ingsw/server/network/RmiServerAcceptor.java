package it.polimi.ingsw.server.network;

import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.controller.Controller;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RmiServerAcceptor extends UnicastRemoteObject implements RmiServerAcceptorInterface {
    /**
     * the controller of the game
     */
    private Controller controller;


    /**
     * build the rmi server acceptor
     * @param controller its controller
     * @throws RemoteException if something goes wrong
     */
    public RmiServerAcceptor(Controller controller) throws RemoteException {
        this.controller= controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * add a new player connection
     * @return the player's serverView remote object
     * @throws RemoteException if something goes wrong
     */
    @Override
    public RmiServerRemoteInterface addMe () throws RemoteException {
        return new RmiServer(controller);
    }

}
