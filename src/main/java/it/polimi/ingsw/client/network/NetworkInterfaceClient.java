package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientExceptions.ForwardingException;
import it.polimi.ingsw.communication.CommonProperties;
import it.polimi.ingsw.communication.events.EventVisitable;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class NetworkInterfaceClient{

    protected static final int REACHING_TIME = 2000;
    protected Client client;

    protected PongSource ponging;


    public NetworkInterfaceClient(Client client){
        this.client= client;
        ponging= new PongSource();
    }



    abstract public void forward(EventVisitable eventVisitable) throws ForwardingException;
    abstract public void closeConnection();
    abstract void pong();


    protected class PongSource extends Thread{
        private AtomicBoolean active;

        protected PongSource(){
            active= new AtomicBoolean(true);
        }

        public synchronized void run() {
            try {
                while (active.get()) {
                    pong();
                    wait(CommonProperties.PING_PONG_DELAY);
                }
            }
            catch (InterruptedException e){
                interrupt();
            }


        }

        protected void close(){
            active.set(false);
        }
    }
}
