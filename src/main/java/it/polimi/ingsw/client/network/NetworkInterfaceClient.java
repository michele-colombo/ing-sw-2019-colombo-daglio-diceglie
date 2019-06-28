package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientExceptions.ForwardingException;
import it.polimi.ingsw.communication.CommonProperties;
import it.polimi.ingsw.communication.events.EventVisitable;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class NetworkInterfaceClient{

    /**
     * maximum time for the connection to initiate
     */
    public static final int REACHING_TIME = 2000;

    /**
     * reference of the owner of the connection
     */
    protected Client client;

    /**
     * the source of sent pongs
     */
    protected PongSource ponging;


    /**
     * build the network layer
     * @param client
     */
    public NetworkInterfaceClient(Client client){
        this.client= client;
        ponging= new PongSource();
    }



    abstract public void forward(EventVisitable eventVisitable) throws ForwardingException;
    abstract public void closeConnection();
    abstract void pong();


    protected class PongSource extends Thread{
        /**
         * flag true if thread is active
         */
        private AtomicBoolean active;

        /**
         * build the pongSource and set it active
         */
        protected PongSource(){
            active= new AtomicBoolean(true);
        }

        /**
         * repetively pong and wait
         */
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

        /**
         * stop the ponging generation
         */
        protected void close(){
            active.set(false);
        }
    }
}
