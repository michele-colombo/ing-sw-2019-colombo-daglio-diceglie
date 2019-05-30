package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.communication.events.EventVisitable;
import it.polimi.ingsw.communication.message.MessageVisitable;
import it.polimi.ingsw.server.network.RmiServerAcceptorInterface;
import it.polimi.ingsw.server.network.RmiServerRemoteInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RmiClient extends UnicastRemoteObject implements  NetworkInterfaceClient, RmiClientRemoteInterface{
    private RmiServerRemoteInterface server;
    private Client client;

    private Thread messageEater;

    private boolean active;
    private List<MessageVisitable> messageQueue;

    public RmiClient(Client cl) throws RemoteException{

        String ip= ClientMain.config.getIp();
        int port= ClientMain.config.getPort();

        int rmiPort= port + 1;

        this.client= cl;
        active= true;
        messageQueue= Collections.synchronizedList(new ArrayList<>());

        try {
            //System.out.println("Getting registry");
            Registry registry = LocateRegistry.getRegistry(ip, rmiPort);


            System.out.println("Connecting to server");
            RmiServerAcceptorInterface acceptor = (RmiServerAcceptorInterface) registry.lookup("Acceptor");



            System.out.println("Attaching server");
            RmiClientRemoteInterface myFace= this;
            RmiServerRemoteInterface server = acceptor.addMe(myFace);
            this.server = server;
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
        catch (NotBoundException nbe){
            nbe.printStackTrace();
        }

        messageEater= new Thread( new Runnable() {
            @Override
            public void run() {
                while(active){
                    if(!messageQueue.isEmpty()){
                        messageQueue.remove(0).accept(client);
                    }
                }
            }
        });
        messageEater.start();

    }

    @Override
    public void forward(EventVisitable eventVisitable) throws IOException {
        //todo: remove
        System.out.println("Sto per mandare un messaggio");
        server.receive(eventVisitable);
        System.out.println("ho mandato un messaggio");
    }

    @Override
    public void startNetwork() {
        //this method is useless
    }

    @Override
    public void closeConnection() {
        active= false;
    }

    @Override
    public void receive(MessageVisitable message)throws RemoteException {
        messageQueue.add(message);
    }
}
