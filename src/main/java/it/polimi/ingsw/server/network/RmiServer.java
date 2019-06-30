package it.polimi.ingsw.server.network;

import it.polimi.ingsw.communication.CommonProperties;
import it.polimi.ingsw.communication.events.EventVisitable;
import it.polimi.ingsw.communication.message.GenericMessage;
import it.polimi.ingsw.communication.message.MessageVisitable;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.controller.Controller;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RmiServer extends UnicastRemoteObject implements NetworkInterfaceServer, RmiServerRemoteInterface{
    /**
     * the owner of this connection
     */
    private ServerView serverView;

    /**
     * queue with messages to send to client
     */
    private ResponseQueue responses;

    /**
     * manager of incoming events
     */
    private EventTrafficManager eventEater;

    /**
     * timer to understand if a client is already connected or not
     */
    private Timer disconnectionTimer;


    /**
     * builds the rmi server
     * @param controller controller of the game
     * @throws RemoteException if an error occurs during initialization
     */
    public RmiServer(Controller controller) throws RemoteException{
        this.serverView= new ServerView(this, controller);
        responses= new ResponseQueue();

        Timer pinging= new Timer();
        pinging.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    forwardMessage(new GenericMessage("ping"));
                }
                catch (IOException e){
                    cancel();
                }
            }
        }, CommonProperties.PING_PONG_DELAY, CommonProperties.PING_PONG_DELAY);


        disconnectionTimer= new Timer();
        disconnectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                serverView.disconnectPlayer();
            }
        }, CommonProperties.PING_PONG_DELAY*2);
        eventEater= new EventTrafficManager();
        eventEater.start();



    }


    /**
     * forward a message to the client
     * @param messaggio
     * @throws IOException
     */
    @Override
    public void forwardMessage(MessageVisitable messaggio) throws IOException{
        responses.put(messaggio);
    }

    /**
     * stop all the active threads
     */
    @Override
    public void closeNetwork() {
        eventEater.close();
        disconnectionTimer.cancel();
        disconnectionTimer.purge();

        try {
            UnicastRemoteObject.unexportObject(this, true);
            System.out.println("object unexported");
        }
        catch (NoSuchObjectException e){
            System.out.println("Unable to unexport object. NoSuchObjectException");
        }
    }

    /**
     * receive an event
     * @param event incoming event
     */
    @Override
    public void receive(EventVisitable event){
        eventEater.put(event);
    }


    /**
     * ask if there are messages to send
     * @return the first message to send
     */
    @Override
    public MessageVisitable ask(){
        return responses.getIt();
    }

    /**
     * reset the disconnection timer. It's an impulse to know that the client is alive
     * @return always true
     * @throws RemoteException if connection is down
     */

    @Override
    public boolean pong() throws RemoteException {
        disconnectionTimer.cancel();
        disconnectionTimer.purge();

        disconnectionTimer= new Timer();
        disconnectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("finito");
                serverView.disconnectPlayer();
            }
        }, CommonProperties.PING_PONG_DELAY*2);
        return true;
    }


    private class EventTrafficManager extends Thread{
        /**
         * incoming events
         */
        private List<EventVisitable> queue;
        /**
         * flag true if thread is active
         */
        private AtomicBoolean active;

        /**
         * build the eventTraffic Manager
         */
        public EventTrafficManager(){
            queue= Collections.synchronizedList(new ArrayList<>());
            active= new AtomicBoolean(true);
        }

        /**
         * make the serverView visit the incoming events
         */
        public synchronized void eat(){
            if(queue.isEmpty()){
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            else {
                notifyAll();
                queue.remove(0).accept(serverView);
            }
        }

        /**
         * add an event to the queue
         * @param event the event to add
         */
        public synchronized void put(EventVisitable event){
            queue.add(event);
            notifyAll();
        }


        /**
         * repetively try to eat the events in the queue
         */
        public synchronized void run(){
            while(active.get()) {
                eat();
            }
            System.out.println("run finished");

        }

        /**
         * stop the thread
         */
        public synchronized void close(){
            active.set(false);
            notifyAll();
        }
    }


    private class ResponseQueue{
        /**
         * queue with the outc=going responses
         */
        private List<MessageVisitable> coda;

        /**
         * build the queue
         */
        public ResponseQueue() {
            coda= new ArrayList<>();
        }

        /**
         * put a message in the responseQueue
         * @param toPut
         */
        public synchronized void put(MessageVisitable toPut){
            coda.add(toPut);
            notifyAll();
        }

        /**
         * get the firs message to send to the client from the queue
         * @return the message
         */
        public synchronized MessageVisitable getIt(){
            try {
                while(coda.isEmpty()) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            return coda.remove(0);
        }
    }

}