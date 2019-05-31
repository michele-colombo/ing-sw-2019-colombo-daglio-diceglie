package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.communication.events.EventVisitable;
import it.polimi.ingsw.communication.message.MessageVisitable;
import it.polimi.ingsw.server.network.RmiServerAcceptorInterface;
import it.polimi.ingsw.server.network.RmiServerRemoteInterface;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RmiClient extends UnicastRemoteObject implements  NetworkInterfaceClient, RmiClientRemoteInterface{
    private RmiServerRemoteInterface server;
    private Client client;

    private MessageTrafficManager messageEater;

    public RmiClient(Client cl) throws RemoteException{

        String ip= ClientMain.config.getIp();
        int port= ClientMain.config.getPort();

        int rmiPort= port + 1;

        this.client= cl;

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

        messageEater= new MessageTrafficManager();
        messageEater.start();

        System.out.println("finito tutto");



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
        messageEater.close();
        try {
            UnicastRemoteObject.unexportObject(this, true);
        }
        catch (NoSuchObjectException e){  }
    }

    @Override
    public void receive(MessageVisitable message)throws RemoteException {
        messageEater.put(message);
    }



    private class MessageTrafficManager extends Thread{
        private List<MessageVisitable> queue;
        private AtomicBoolean active;

        public MessageTrafficManager(){
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
                queue.remove(0).accept(client);
            }
        }

        public synchronized void put(MessageVisitable message){
            queue.add(message);
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
