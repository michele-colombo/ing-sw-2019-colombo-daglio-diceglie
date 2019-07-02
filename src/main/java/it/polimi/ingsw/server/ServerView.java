package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.events.*;
import it.polimi.ingsw.communication.message.MessageVisitable;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.communication.EventVisitor;
import it.polimi.ingsw.server.network.NetworkInterfaceServer;
import it.polimi.ingsw.server.observer.Observer;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class ServerView implements Observer, EventVisitor {

    public static final String ERROR_UPDATING_VIEW = "error while updating view!";
    public static final String SERVER_VIEW_SET_TO_DISCONNECT = "server view set to disconnect:";
    public static final String PLAYER_COMPLETELY_DISCONNECTED = "player completely disconnected";
    /**
     * the network class that send/receive messages and events
     */
    private NetworkInterfaceServer network;

    /**
     * the controller of the game
     */
    private Controller controller;

    private static final Logger logger = Logger.getLogger(ServerView.class.getName());


    /**
     * build a serverView
     * @param network serverView's network
     * @param controller his controller
     */
    public ServerView(NetworkInterfaceServer network, Controller controller){
        this.network = network;
        this.controller = controller;
        controller.addServerView(this);
    }


    /**
     * send the update message to client through network
     * @param messageVisitable the update message
     */
    public void update(MessageVisitable messageVisitable){
        try{
            network.forwardMessage(messageVisitable);
        } catch(IOException e){
            logger.warning(ERROR_UPDATING_VIEW);
            controller.setToDisconnect(this);
            logger.info(SERVER_VIEW_SET_TO_DISCONNECT + this);
        }
    }


    /**
     * disconnect the player linked to this serverView
     */
    public void disconnectPlayer(){
        //todo: perde il lock su controller. Fare un metodo setToDisconnectAndClean(this) ?
        controller.setToDisconnect(this);
        controller.finalCleaning();
        logger.info(PLAYER_COMPLETELY_DISCONNECTED);
    }


    /**
     * login with parameters contained in the loginEvent
     * @param loginEvent
     */
    @Override
    public void visit(LoginEvent loginEvent){
        controller.login(loginEvent.getName(), this);
    }

    /**
     * select the square chosen by client
     * @param squareSelectedEvent
     */
    @Override
    public void visit(SquareSelectedEvent squareSelectedEvent) {
        controller.squareSelected(squareSelectedEvent.getSelection(), this);

    }

    /**
     * select the action chosen by client
     * @param actionSelectedEvent
     */
    @Override
    public void visit(ActionSelectedEvent actionSelectedEvent) {
        controller.actionSelected(actionSelectedEvent.getSelection(), this);

    }

    /**
     * select the player chosen by client
     * @param playerSelectedEvent
     */
    @Override
    public void visit(PlayerSelectedEvent playerSelectedEvent) {
        controller.playerSelected(playerSelectedEvent.getSelection(), this);

    }

    /**
     * select the weapon chosen by client
     * @param weaponSelectedEvent
     */
    @Override
    public void visit(WeaponSelectedEvent weaponSelectedEvent) {
        controller.weaponSelected(weaponSelectedEvent.getSelection(), this);

    }

    /**
     * select the mode chosen by client
     * @param modeSelectedEvent
     */
    @Override
    public void visit(ModeSelectedEvent modeSelectedEvent) {
        controller.modeSelected(modeSelectedEvent.getSelection(), this);

    }

    /**
     * select the command chosen by client
     * @param commandSelectedEvent
     */
    @Override
    public void visit(CommandSelectedEvent commandSelectedEvent) {
        controller.commandSelected(commandSelectedEvent.getSelection(), this);

    }

    /**
     * select the color chosen by client
     * @param colorSelectedEvent
     */
    @Override
    public void visit(ColorSelectedEvent colorSelectedEvent) {
        controller.colorSelected(colorSelectedEvent.getSelection(), this);

    }

    /**
     * select the powerUp chosen by client
     * @param powerUpSelectedEvent
     */
    @Override
    public void visit(PowerUpSelectedEvent powerUpSelectedEvent) {
        controller.powerUpSelected(powerUpSelectedEvent.getSelection(), this);

    }


    /**
     * close the network (it's full of threads)
     */
    public void shutDown(){
        network.closeNetwork();
    }




}
