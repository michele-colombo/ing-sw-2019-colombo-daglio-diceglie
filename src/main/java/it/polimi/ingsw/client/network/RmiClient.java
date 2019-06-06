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
    private RmiServerRemoteInterface server;
    private Asker asker;

   // private MessageTrafficManager messageEater;

    public RmiClient(Client cl) throws ConnectionInitializationException{
        super(cl);

        String ip= ClientMain.config.getIp();
        int port= ClientMain.config.getPort();
        int rmiPort= port + 1;

        try {

            RMISocketFactory.setSocketFactory(new RMISocketFactory() {
                public Socket createSocket( String host, int port )
                        throws IOException
                {
                    Socket socket = new Socket();
                    socket.setSoTimeout((int) CommonProperties.PING_PONG_DELAY*2);
                    socket.setSoLinger( false, 0 );
                    socket.connect( new InetSocketAddress( host, port ), REACHING_TIME );
                    return socket;
                }

                public ServerSocket createServerSocket(int port )
                        throws IOException
                {
                    return new ServerSocket( port );
                }
            } );


            //System.out.println("Getting registry");
            Registry registry = LocateRegistry.getRegistry(ip, rmiPort);


            System.out.println("Connecting to server");
            RmiServerAcceptorInterface acceptor = (RmiServerAcceptorInterface) registry.lookup("Acceptor");


            System.out.println("Attaching server");
            RmiServerRemoteInterface server = acceptor.addMe();
            this.server = server;

            ponging.start();

            asker= new Asker();
            asker.start();

/*
            messageEater= new MessageTrafficManager();
            messageEater.start();
            */


        }
        catch (IOException e){
            throw new ConnectionInitializationException();
        }
        catch (NotBoundException nbe){
            //it cannot happen
            nbe.printStackTrace();
        }


    }

    @Override
    public void forward(EventVisitable eventVisitable) throws ForwardingException {
        //todo: remove
        try {
            server.receive(eventVisitable);
        }
        catch (RemoteException e){
            System.out.println("Merdoccia");
            throw new ForwardingException();
        }
    }


    @Override
    public synchronized void closeConnection() {
        ponging.interrupt();
        ponging.close();
        asker.close();
    }

    @Override
    void pong() {
        try {
            server.pong();
        }
        catch (RemoteException e){

        }
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
                    close();
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

    private class Asker extends Thread{
        private AtomicBoolean active;

        private Asker(){
            active= new AtomicBoolean(true);
        }

        @Override
        public synchronized void run(){
            try{
                while(active.get()){
                    MessageVisitable message= server.ask();
                    if(message!= null) {
                        Thread resend = new Thread(() -> message.accept(client));
                        resend.start();
                    }
                }
            }
            catch (RemoteException e){
                System.out.println("Connection down");
                client.restart();
            }
        }

        public void close(){
            active.set(false);
        }
    }

}
