package it.polimi.ingsw.server.network;

import it.polimi.ingsw.client.network.RmiClientRemoteInterface;
import it.polimi.ingsw.communication.events.EventVisitable;
import it.polimi.ingsw.communication.message.MessageVisitable;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.controller.Controller;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RmiServer extends UnicastRemoteObject implements NetworkInterfaceServer, RmiServerRemoteInterface{
    private RmiClientRemoteInterface client;
    private ServerView serverView;

    private EventTrafficManager eventEater;

    private  List<EventVisitable> eventQueue;

    public RmiServer(RmiClientRemoteInterface client, Controller controller) throws RemoteException{
        this.client = client;
        this.serverView= new ServerView(this, controller);

        eventEater= new EventTrafficManager();
        eventEater.start();


    }


    @Override
    public void forwardMessage(MessageVisitable messaggio){
        try {
            client.receive(messaggio);
        }
        catch (RemoteException e){
            serverView.disconnectPlayer();
            e.printStackTrace();
        }
    }

    @Override
    public void closeNetwork() {
        eventEater.close();
        try {
            UnicastRemoteObject.unexportObject(this, true);
        }
        catch (NoSuchObjectException e){  }
    }

    @Override
    public void receive(EventVisitable event) throws RemoteException {
        eventEater.put(event);
    }



    private class EventTrafficManager extends Thread{
        private List<EventVisitable> queue;
        private AtomicBoolean active;

        public EventTrafficManager(){
            queue= Collections.synchronizedList(new ArrayList<>());
            active= new AtomicBoolean(true);
        }

        public synchronized void eat(){
            if(queue.isEmpty()){
                try {
                    wait();
                } catch (InterruptedException e) {
                    //nothing
                }
            }
            else {
                notifyAll();
                queue.remove(0).accept(serverView);
            }
        }

        public synchronized void put(EventVisitable event){
            queue.add(event);
            notifyAll();
        }


        public synchronized void run(){
            while(active.get()) {
                eat();
            }
            System.out.println("run finished");

        }

        public synchronized void close(){
            active.set(false);
            notifyAll();
        }
    }

}
