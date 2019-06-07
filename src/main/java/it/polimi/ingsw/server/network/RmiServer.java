package it.polimi.ingsw.server.network;

import it.polimi.ingsw.communication.CommonProperties;
import it.polimi.ingsw.communication.events.EventVisitable;
import it.polimi.ingsw.communication.message.ConnectionUpdateMessage;
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
/*
public class RmiServer extends UnicastRemoteObject implements NetworkInterfaceServer, RmiServerRemoteInterface{
    private ServerView serverView;
    private ResponseQueue responses;

    private Timer pingPongTimer;


    public RmiServer(Controller controller) throws RemoteException{
        this.serverView= new ServerView(this, controller);

        responses= new ResponseQueue();

        Timer pinger= new Timer();
        pinger.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    forwardMessage(new GenericMessage("cacchina"));
                }
                catch (IOException e){
                    cancel();
                }
            }
        }, CommonProperties.PING_PONG_DELAY, CommonProperties.PING_PONG_DELAY);

        pingPongTimer= new Timer();
        pingPongTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                closeNetwork();
            }
        }, CommonProperties.PING_PONG_DELAY*2);
    }


    @Override
    public void forwardMessage(MessageVisitable messaggio) throws IOException{
        responses.metti(messaggio);
    }

    @Override
    public void closeNetwork() {
        try {
            UnicastRemoteObject.unexportObject(this, true);
        }
        catch (NoSuchObjectException e){  }
    }

    @Override
    public void receive(EventVisitable event) throws RemoteException {
        pingPongTimer.cancel();
        pingPongTimer= new Timer();
        pingPongTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                closeNetwork();
            }
        }, CommonProperties.PING_PONG_DELAY*2);


        Thread resend= new Thread( () -> event.accept(serverView));
        resend.start();
    }

    @Override
    public MessageVisitable ask(){
        return responses.prendi();
    }

    @Override
    public boolean pong(){
        return responses.isEmpty();
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


    private class ResponseQueue extends Thread{
        private List<MessageVisitable> coda;

        public ResponseQueue() {
            coda= Collections.synchronizedList(new ArrayList<>());
        }

        private boolean isEmpty(){
            return coda.isEmpty();
        }

        public synchronized void metti(MessageVisitable daMettere){
            coda.add(daMettere);
            notifyAll();
        }

        public synchronized MessageVisitable prendi() {

            try {
                while(coda.isEmpty()) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.interrupt();
            }

            return coda.remove(0);
        }



    }

}


*/

public class RmiServer extends UnicastRemoteObject implements NetworkInterfaceServer, RmiServerRemoteInterface{
    private ServerView serverView;
    private ResponseQueue responses;
    private EventTrafficManager eventEater;

    private Timer disconnectionTimer;


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


    @Override
    public void forwardMessage(MessageVisitable messaggio) throws IOException{
        responses.metti(messaggio);
    }

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

    @Override
    public void receive(EventVisitable event) throws RemoteException {
        eventEater.put(event);
    }

    @Override
    public MessageVisitable ask(){
        return responses.prendi();
    }

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


    private class ResponseQueue extends Thread{
        private List<MessageVisitable> coda;

        public ResponseQueue() {
            coda= new ArrayList<>();
        }

        public synchronized void metti(MessageVisitable daMettere){
            coda.add(daMettere);
            notifyAll();
        }

        public synchronized MessageVisitable prendi(){
            try {
                while(coda.isEmpty()) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.interrupt();
            }

            return coda.remove(0);
        }
    }

}