package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientExceptions.ConnectionInitializationException;
import it.polimi.ingsw.client.ClientExceptions.ForwardingException;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.communication.CommonProperties;
import it.polimi.ingsw.communication.events.EventVisitable;
import it.polimi.ingsw.communication.message.MessageVisitable;
import it.polimi.ingsw.server.network.RmiServerAcceptorInterface;
import it.polimi.ingsw.server.network.RmiServerRemoteInterface;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RmiClient extends NetworkInterfaceClient{
    /**
     * the remote interface of the server
     */
    private RmiServerRemoteInterface server;

    /**
     * a thread that alway asks to the server if he wants to send a message
     */
    private Asker asker;

   // private MessageTrafficManager messageEater;

    /**
     * builds an RmiClient and starts it
     * @param cl the owner of this connection
     * @throws ConnectionInitializationException if something goes wriong during initialization
     */
    public RmiClient(Client cl) throws ConnectionInitializationException{
        super(cl);

        String ip= ClientMain.getConfig().getIp();
        int port= ClientMain.getConfig().getPort();
        int rmiPort= port + 1;

        try {
            Registry registry = LocateRegistry.getRegistry(ip, rmiPort);

            RmiServerAcceptorInterface acceptor = (RmiServerAcceptorInterface) registry.lookup("Acceptor");

            RmiServerRemoteInterface server = acceptor.addMe();
            this.server = server;
            ponging.start();
            asker = new Asker();
            asker.start();

        }
        catch (RemoteException e){
            throw new ConnectionInitializationException();
        }
        catch (NotBoundException nbe){}


    }

    /**
     * forward an event to the server
     * @param eventVisitable
     * @throws ForwardingException
     */
    @Override
    public void forward(EventVisitable eventVisitable) throws ForwardingException {
        //todo: remove
        try {
            server.receive(eventVisitable);
        }
        catch (RemoteException e){
            throw new ForwardingException();
        }
    }


    /**
     * stop all active threads
     */
    @Override
    public synchronized void closeConnection() {
        ponging.interrupt();
        ponging.close();
        asker.close();
    }

    /**
     * pong the server to keep him awake
     */
    @Override
    void pong() {
        try {
            server.pong();
        }
        catch (RemoteException e){

        }
    }



    private class Asker extends Thread{
        /**
         * flag true when the thread active
         */
        private AtomicBoolean active;

        /**
         * build the asker
         */
        private Asker(){
            active= new AtomicBoolean(true);
        }

        /**
         * repetively asks the server if he wants to send something. Close everything if connection fall
         */
        @Override
        public synchronized void run(){
            try{
                while(active.get()){
                    MessageVisitable message= server.ask();
                    if(message!= null) {
                        message.accept(client);
                    }
                }
            }
            catch (RemoteException e){
                if( client.isConnected()) {
                    client.restart();
                }
            }
        }

        public void close(){
            active.set(false);
        }


    }

}
