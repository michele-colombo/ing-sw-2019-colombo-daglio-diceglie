package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.communication.events.EventVisitable;
import it.polimi.ingsw.server.ServerViewInterface;
import it.polimi.ingsw.server.network.RmiServerAcceptorInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient implements  NetworkInterfaceClient{
    private ServerViewInterface serverViewInterface;
    private Client client;

    public RmiClient(String ip, int port, Client client){

        int rmiPort= port + 1;

        this.client= client;

        try {
            //System.out.println("Getting registry");
            Registry registry = LocateRegistry.getRegistry(ip, rmiPort);


            System.out.println("Connecting to server");
            RmiServerAcceptorInterface acceptor = (RmiServerAcceptorInterface) registry.lookup("Acceptor");


            ClientInterface clientInterface = client;

            System.out.println("Attaching server");
            ServerViewInterface server = acceptor.addMe(clientInterface);
            serverViewInterface = server;
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
        catch (NotBoundException nbe){
            nbe.printStackTrace();
        }

    }

    @Override
    public void forward(EventVisitable eventVisitable) throws IOException {
        //todo: remove
        System.out.println("Sto per mandare un messaggio");
        eventVisitable.accept(serverViewInterface);
        System.out.println("ho mandato un messaggio");
    }

    @Override
    public void startNetwork() {
        //this method is useless
    }
}
