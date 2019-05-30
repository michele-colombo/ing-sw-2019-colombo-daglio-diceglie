package it.polimi.ingsw.server.network;

import it.polimi.ingsw.client.network.RmiClientRemoteInterface;
import it.polimi.ingsw.communication.events.EventVisitable;
import it.polimi.ingsw.communication.message.MessageVisitable;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.controller.Controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RmiServer extends UnicastRemoteObject implements NetworkInterfaceServer, RmiServerRemoteInterface{
    private RmiClientRemoteInterface client;
    private ServerView serverView;
    private boolean active;
    private Thread eventEater;

    private  List<EventVisitable> eventQueue;

    public RmiServer(RmiClientRemoteInterface client, Controller controller) throws RemoteException{
        this.client = client;
        this.serverView= new ServerView(this, controller);
        eventQueue= Collections.synchronizedList(new ArrayList<>());
        active= true;

        eventEater= new Thread( new Runnable() {
            @Override
            public void run() {
                while(active){
                    if(!eventQueue.isEmpty()){
                        eventQueue.remove(0).accept(serverView);
                    }
                }
            }
        } );

        eventEater.start();


    }


    @Override
    public void forwardMessage(MessageVisitable messaggio){
        try {
            client.receive(messaggio);
        }
        catch (RemoteException e){
            //todo cosa fare in questo caso
            e.printStackTrace();
        }
    }

    @Override
    public void closeNetwork() {
        active= false;
    }

    @Override
    public void receive(EventVisitable event) throws RemoteException {
        eventQueue.add(event);
    }

}
