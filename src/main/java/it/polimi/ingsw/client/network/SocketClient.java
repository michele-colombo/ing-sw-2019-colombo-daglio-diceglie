package it.polimi.ingsw.client.network;

import com.google.gson.Gson;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientExceptions.ConnectionInitializationException;
import it.polimi.ingsw.client.ClientExceptions.ForwardingException;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.communication.CommonProperties;
import it.polimi.ingsw.communication.events.*;
import it.polimi.ingsw.communication.message.*;
import it.polimi.ingsw.communication.EventVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.communication.events.EventVisitable.*;
import static it.polimi.ingsw.communication.message.MessageVisitable.*;

public class SocketClient extends NetworkInterfaceClient implements EventVisitor {
    /**
     * the socket for send and receive things
     */
    private final Socket socket;


    /**
     * the current prefix for the sending event
     */
    private String eventPrefix;

    /**
     * the thread that always listen to arriving messages
     */
    private Asker asker;


    /**
     * the output stream printer
     */
    private PrintWriter out;


    /**
     * build a SocketClient
     * @param client the owner of this connection
     * @throws ConnectionInitializationException if something goes wrong during connection initialization
     */
    public SocketClient(Client client) throws ConnectionInitializationException{
        super(client);

        String ip= ClientMain.getConfig().getIp();
        int port= ClientMain.getConfig().getPort();

        try {

            SocketAddress address = new InetSocketAddress(ip, port);
            Socket sock = new Socket();
            this.socket = sock;
            this.socket.connect(address, REACHING_TIME);
            this.socket.setSoTimeout((int) CommonProperties.PING_PONG_DELAY*2);
            this.client = client;

            out = new PrintWriter(sock.getOutputStream());
            out.flush();

            asker= new Asker();
            asker.start();

            ponging.start();

        }
        catch (IOException e){
            throw new ConnectionInitializationException();
        }

    }


    /**
     * put in the network the desired event
     * @param event sending event
     * @throws ForwardingException if something goes wrong during sending phase
     */
    public void forward(EventVisitable event) throws ForwardingException {
        Gson gson= new Gson();
        event.accept(this);
        out.println(eventPrefix + gson.toJson(event));
        out.flush();

    }

    /**
     * understand what type of message is arriving and parse it to the correct Message Type
     * @param messageText incoming text
     * @return the messages that the text contains
     */
    private MessageVisitable unwrap(String messageText){
        Gson gson= new Gson();

        String prefix= messageText.substring(messageText.indexOf('#'), messageText.lastIndexOf('#') + 1);
        messageText= messageText.substring(messageText.lastIndexOf('#') + 1);
        MessageVisitable result= null;

        switch (prefix){
            case LOGIN_MESSAGE_PREFIX: result= gson.fromJson(messageText, LoginMessage.class); break;
            case GENERIC_MESSAGE_PREFIX: result= gson.fromJson(messageText, GenericMessage.class); break;
            case LAYOUTUPDATE_MESSAGE_PREFIX: result= gson.fromJson(messageText, LayoutUpdateMessage.class); break;
            case KILLSHOTUPDATE_MESSAGE_PREFIX: result= gson.fromJson(messageText, KillshotTrackUpdateMessage.class); break;
            case CURRENTPLAYERUPDATE_MESSAGE_PREFIX: result= gson.fromJson(messageText, CurrentPlayerUpdateMessage.class); break;
            case STARTMATCHUPDATE_MESSAGE_PREFIX: result= gson.fromJson(messageText, StartMatchUpdateMessage.class); break;
            case PLAYERUPDATEMESSAGE_MESSAGE_PREFIX: result= gson.fromJson(messageText, PlayerUpdateMessage.class); break;
            case PAYMENTUPDATE_MESSAGE_PREFIX: result= gson.fromJson(messageText, PaymentUpdateMessage.class); break;
            case WEAPONSUPDATE_MESSAGE_PREFIX: result= gson.fromJson(messageText, WeaponsUpdateMessage.class); break;
            case POWERUPUPDATE_MESSAGE_PREFIX: result= gson.fromJson(messageText, PowerUpUpdateMessage.class); break;
            case SELECTABLESUPDATE_MESSAGE_PREFIX: result= gson.fromJson(messageText, SelectablesUpdateMessage.class); break;
            case DAMAGEUPDATE_MESSAGE_PREFIX: result= gson.fromJson(messageText, DamageUpdateMessage.class); break;
            case CONNECTIONUPDATE_MESSAGE_PREFIX: result= gson.fromJson(messageText, ConnectionUpdateMessage.class); break;
            case GAMEOVER_MESSAGE_PREFIX: result = gson.fromJson(messageText, GameOverMessage.class); break;
            default: break;
        }

        return result;
    }

    /**
     * stop all threads
     */
    public void closeConnection(){
        try{
            ponging.interrupt();
            ponging.close();
            asker.close();
            socket.close();
        }
        catch (IOException e){}
    }

    /**
     * send a pong
     */

    @Override
    void pong() {
        out.println(CommonProperties.PONG_NAME);
        out.flush();
    }


    /**
     * setting eventPrefix characterize the outcomoing loginEvent
     * @param loginEvent
     */
    @Override
    public void visit(LoginEvent loginEvent) {
        eventPrefix= LOGIN_PREFIX;

    }

    /**
     * setting eventPrefix characterize the outcoming squareSelectedEvent
     * @param squareSelectedEvent
     */
    @Override
    public void visit(SquareSelectedEvent squareSelectedEvent) {
        eventPrefix= SQUARESELECTED_PREFIX;

    }

    /**
     * setting eventPrefix characterize the outcoming actionSelectedEvent
     * @param actionSelectedEvent
     */
    @Override
    public void visit(ActionSelectedEvent actionSelectedEvent) {
        eventPrefix= ACTIONSELECTED_PREFIX;

    }

    /**
     * setting eventPrefix characterize the outcoming playerSelectedEvent
     * @param playerSelectedEvent
     */

    @Override
    public void visit(PlayerSelectedEvent playerSelectedEvent) {
        eventPrefix= PLAYERSELECTED_PREFIX;

    }

    /**
     * setting eventPrefix characterize the outcoming weaponSelectedEvent
     * @param weaponSelectedEvent
     */
    @Override
    public void visit(WeaponSelectedEvent weaponSelectedEvent) {
        eventPrefix= WEAPONSELECTED_PREFIX;

    }

    /**
     * setting eventPrefix characterize the outcoming modeSelectedEvent
     * @param modeSelectedEvent
     */
    @Override
    public void visit(ModeSelectedEvent modeSelectedEvent) {
        eventPrefix= MODESELECTED_PREFIX;

    }

    /**
     * setting eventPrefix characterize the outcoming commendSelectedEvent
     * @param commandSelectedEvent
     */
    @Override
    public void visit(CommandSelectedEvent commandSelectedEvent) {
        eventPrefix= COMMANDSELECTED_PREFIX;

    }

    /**
     * setting eventPrefix characterize the outcoming colorSelectedEvent
     * @param colorSelectedEvent
     */
    @Override
    public void visit(ColorSelectedEvent colorSelectedEvent) {
        eventPrefix= COLORSELECTED_PREFIX;

    }

    /**
     * setting eventPrefix characterize the outcoming powerUpSelectedEvent
     * @param powerUpSelectedEvent
     */
    @Override
    public void visit(PowerUpSelectedEvent powerUpSelectedEvent) {
        eventPrefix= POWERUPSELECTED_PREFIX;
    }


    private class Asker extends Thread{
        /**
         * this flag is true when this thread is active
         */
        private AtomicBoolean active;

        /**
         * asker builder, set active to true
         */
        private Asker(){
            active= new AtomicBoolean(true);
        }

        /**
         * always listen to inputStream. If nothing arrives, restart all
         */
        @Override
        public void run(){
            try {
                Scanner in = new Scanner(socket.getInputStream());
                while(active.get()) {
                    String strcv= in.nextLine();
                    if(!strcv.equalsIgnoreCase(CommonProperties.PING_NAME)) {
                        MessageVisitable received = unwrap(strcv);
                        if (received != null){
                            received.accept(client);
                        }
                    }
                }
            }
            catch (NoSuchElementException nsee){
                if(client.isConnected()) {
                    client.restart();
                }
            }
            catch (IOException e) {}
        }

        public void close(){
            active.set(false);
        }
    }
}
