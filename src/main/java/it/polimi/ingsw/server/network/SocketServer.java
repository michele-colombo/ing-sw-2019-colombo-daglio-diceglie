package it.polimi.ingsw.server.network;

import com.google.gson.Gson;
import it.polimi.ingsw.communication.CommonProperties;
import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.communication.events.*;
import it.polimi.ingsw.communication.message.*;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.controller.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import static it.polimi.ingsw.communication.events.EventVisitable.*;
import static it.polimi.ingsw.communication.message.MessageVisitable.*;

public class SocketServer implements NetworkInterfaceServer, MessageVisitor {
    private static final Logger logger = Logger.getLogger(SocketServer.class.getName());
    private static final String CONNECTION_IMPOSSIBLE = "impossible to initiate connection";
    private static final String SOCKET_ALREADY_CLOSED = "Socket already closed";

    /**
     * socket to send and receive through tcp
     */
    private Socket socket;
    /**
     * the serverView owner of this connection class
     */
    private ServerView serverView;
    /**
     * output stream printer
     */
    private PrintWriter out;

    /**
     * the prefix of the current outgoing message
     */
    private String messagePrefix;

    /**
     * thread that always read the input stream
     */
    private Asker asker;

    /**
     * the ping generation source
     */
    private PingSource pinging;

    /**
     * build the socketServer
     * @param socket linked socket
     * @param controller game controller
     */

    public SocketServer(Socket socket, Controller controller){
        try {
            this.socket = socket;

            out = new PrintWriter(socket.getOutputStream());
            out.flush();

            //todo: non crea la serverView
            serverView = new ServerView(this, controller);

            pinging = new PingSource();
            pinging.start();

            asker = new Asker();
            asker.start();
        }
        catch (IOException e){
            logger.info(CONNECTION_IMPOSSIBLE);
        }

    }


    /**
     * send a message to the client
     * @param messageVisitable message to send
     * @throws IOException if something gows wrong in the net
     */
    public void forwardMessage(MessageVisitable messageVisitable) throws IOException{
        //PrintWriter out = new PrintWriter(socket.getOutputStream());
        Gson gson = new Gson();

        messageVisitable.accept(this);
        out.println(messagePrefix + gson.toJson(messageVisitable));
        //System.out.println(messagePrefix + gson.toJson(messageVisitable));
        out.flush();
    }

    /**
     * close all threads
     */
   public void closeNetwork(){
        asker.close();

       try {
           this.socket.close();
       } catch (IOException e) {
           logger.info(SOCKET_ALREADY_CLOSED);
       }
   }

    /**
     * set the prefix for login messages
     * @param loginMessage
     */
    @Override
    public void visit(LoginMessage loginMessage) {
        messagePrefix= LOGIN_MESSAGE_PREFIX;
    }

    /**
     * set the prefix for generic messages
     * @param genericMessage
     */
    @Override
    public void visit(GenericMessage genericMessage) {
        messagePrefix= GENERIC_MESSAGE_PREFIX;

    }

    /**
     * set the prefix for  layout update messages
     * @param layoutUpdateMessage
     */
    @Override
    public void visit(LayoutUpdateMessage layoutUpdateMessage) {
        messagePrefix= LAYOUTUPDATE_MESSAGE_PREFIX;

    }

    /**
     * set the prefix for  killshot track update messages
     * @param killshotTrackUpdate
     */
    @Override
    public void visit(KillshotTrackUpdateMessage killshotTrackUpdate) {
        messagePrefix= KILLSHOTUPDATE_MESSAGE_PREFIX;

    }

    /**
     * set the prefix for current player update messages
     * @param currentPlayerUpdate
     */
    @Override
    public void visit(CurrentPlayerUpdateMessage currentPlayerUpdate) {
        messagePrefix= CURRENTPLAYERUPDATE_MESSAGE_PREFIX;

    }

    /**
     * set the prefix for start match update messages
     * @param startMatchUpdateMessage
     */
    @Override
    public void visit(StartMatchUpdateMessage startMatchUpdateMessage) {
        messagePrefix= STARTMATCHUPDATE_MESSAGE_PREFIX;

    }

    /**
     * set the prefix for player update messages
     * @param playerUpdateMessage
     */
    @Override
    public void visit(PlayerUpdateMessage playerUpdateMessage) {
        messagePrefix= PLAYERUPDATEMESSAGE_MESSAGE_PREFIX;

    }

    /**
     * set the prefix for  payment update messages
     * @param paymentUpdateMessage
     */
    @Override
    public void visit(PaymentUpdateMessage paymentUpdateMessage) {
        messagePrefix= PAYMENTUPDATE_MESSAGE_PREFIX;

    }

    /**
     * set the prefix for  weapons update message
     * @param weaponsUpdateMessage
     */
    @Override
    public void visit(WeaponsUpdateMessage weaponsUpdateMessage) {
        messagePrefix= WEAPONSUPDATE_MESSAGE_PREFIX;

    }

    /**
     * set the prefix for powerup update messages
     * @param powerUpUpdateMessage
     */
    @Override
    public void visit(PowerUpUpdateMessage powerUpUpdateMessage) {
        messagePrefix= POWERUPUPDATE_MESSAGE_PREFIX;

    }

    /**
     * set the prefix for selectables update messages
     * @param selectablesUpdateMessage
     */
    @Override
    public void visit(SelectablesUpdateMessage selectablesUpdateMessage) {
        messagePrefix= SELECTABLESUPDATE_MESSAGE_PREFIX;

    }

    /**
     * set the prefix for damage update messages
     * @param damageUpdateMessage
     */
    @Override
    public void visit(DamageUpdateMessage damageUpdateMessage) {
        messagePrefix= DAMAGEUPDATE_MESSAGE_PREFIX;

    }

    /**
     * set the prefix for  connection update messages
     * @param connectionUpdateMessage
     */
    @Override
    public void visit(ConnectionUpdateMessage connectionUpdateMessage) {
        messagePrefix= CONNECTIONUPDATE_MESSAGE_PREFIX;

    }

    /**
     * set the prefix for game over message
     * @param gameOverMessage
     */
    @Override
    public void visit(GameOverMessage gameOverMessage) {
        messagePrefix = GAMEOVER_MESSAGE_PREFIX;
    }

    /**
     * recognise what type of event has arrived
     * @param eventText the text of the event
     * @return the correct type of event
     */
    private EventVisitable unwrap(String eventText){
        //todo: remove next line
        //System.out.println(eventText);
        Gson gson= new Gson();
        String prefix= eventText.substring(eventText.indexOf('#'), eventText.lastIndexOf('#') + 1);
        eventText= eventText.substring(eventText.lastIndexOf('#') + 1);

        EventVisitable result= null;
        switch (prefix){
            case LOGIN_PREFIX: result= gson.fromJson(eventText, LoginEvent.class); break;
            case SQUARESELECTED_PREFIX: result= gson.fromJson(eventText, SquareSelectedEvent.class); break;
            case ACTIONSELECTED_PREFIX: result= gson.fromJson(eventText, ActionSelectedEvent.class); break;
            case PLAYERSELECTED_PREFIX: result= gson.fromJson(eventText, PlayerSelectedEvent.class); break;
            case WEAPONSELECTED_PREFIX: result= gson.fromJson(eventText, WeaponSelectedEvent.class); break;
            case MODESELECTED_PREFIX: result= gson.fromJson(eventText, ModeSelectedEvent.class); break;
            case COMMANDSELECTED_PREFIX: result= gson.fromJson(eventText, CommandSelectedEvent.class); break;
            case COLORSELECTED_PREFIX: result= gson.fromJson(eventText, ColorSelectedEvent.class); break;
            case POWERUPSELECTED_PREFIX: result= gson.fromJson(eventText, PowerUpSelectedEvent.class); break;
        }
        return result;
    }


    /**
     * send a ping to the client
     */
    private void ping(){
        out.println(CommonProperties.PING_NAME);
        out.flush();
    }



    private class Asker extends Thread{
        /**
         * flag true if thread is active
         */
        private AtomicBoolean active;

        /**
         * build the asker thread
         */
        private Asker(){
            active= new AtomicBoolean(true);
        }

        /**
         * repetively control if there's something in the input stream and make the serverView visit the incoming message
         */
        @Override
        public synchronized void run(){
            try{
                final Scanner in = new Scanner(socket.getInputStream());
                while(true){
                    String strcv= in.nextLine();
                    if(!strcv.equalsIgnoreCase(CommonProperties.PONG_NAME)) {
                        EventVisitable received = unwrap(strcv);
                        if (received != null) {
                            received.accept(serverView);  //if everything went right
                        }
                    }
                }
            } catch(NoSuchElementException e){
                logger.info("Client disconnected");
                serverView.disconnectPlayer();
                //todo passare al prossimo player ed eventualmente annullare l'azione(se match è iniziato)
            }
            catch (IOException e){
                logger.info("Eccezione sull'input");
            } catch (Exception e){
                logger.info("there's another exception");
            }
        }

        /**
         * close the thread
         */
        public void close(){
            active.set(false);
        }
    }



    protected class PingSource extends Thread{
        /**
         * flag true if thread active
         */
        private AtomicBoolean active;

        /**
         * build the ping source
         */
        protected PingSource(){
            active= new AtomicBoolean(true);
        }

        /**
         * repetively generate ping, send to the client and wait
         */
        public synchronized void run() {
            try {
                while (active.get()) {
                    ping();
                    wait(CommonProperties.PING_PONG_DELAY);
                }
            }
            catch (InterruptedException e){
                interrupt();
            }


        }

        /**
         * close the thread
         */
        protected void close(){
            active.set(false);
        }
    }
}
