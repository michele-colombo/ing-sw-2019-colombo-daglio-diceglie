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

public class ServerView implements Observer, EventVisitor {

    private NetworkInterfaceServer network;
    private Controller controller;


    public ServerView(NetworkInterfaceServer network, Controller controller){
        this.network = network;
        this.controller = controller;
        controller.addServerView(this);
    }
/*
    public void receiveEvent(EventVisitable event){
        //todo: remove next line
        System.out.println("sto per fare la accept di un evento");
        event.accept(this);
        System.out.println("ho appena finito la accept di un evento");
    }
*/

    public void update(MessageVisitable messageVisitable){
        try{
            network.forwardMessage(messageVisitable);
        } catch(IOException e){
            System.out.println("Error while updating view!");
            controller.setToDisconnect(this);
            System.out.println("Server view "+this+" set to disconnect");
        }
    }



    public void disconnectPlayer(){
        //todo: perde il lock su controller. Fare un metodo setToDisconnectAndClean(this) ?
        controller.setToDisconnect(this);
        controller.finalCleaning();
        System.out.println("[OK] I've completely disconnected a player");
    }


    @Override
    public void visit(LoginEvent loginEvent){
        controller.login(loginEvent.getName(), this);
    }

    @Override
    public void visit(SquareSelectedEvent squareSelectedEvent) {
        controller.squareSelected(squareSelectedEvent.getSelection(), this);

    }

    @Override
    public void visit(ActionSelectedEvent actionSelectedEvent) {
        controller.actionSelected(actionSelectedEvent.getSelection(), this);

    }

    @Override
    public void visit(PlayerSelectedEvent playerSelectedEvent) {
        controller.playerSelected(playerSelectedEvent.getSelection(), this);

    }

    @Override
    public void visit(WeaponSelectedEvent weaponSelectedEvent) {
        controller.weaponSelected(weaponSelectedEvent.getSelection(), this);

    }

    @Override
    public void visit(ModeSelectedEvent modeSelectedEvent) {
        controller.modeSelected(modeSelectedEvent.getSelection(), this);

    }

    @Override
    public void visit(CommandSelectedEvent commandSelectedEvent) {
        controller.commandSelected(commandSelectedEvent.getSelection(), this);

    }

    @Override
    public void visit(ColorSelectedEvent colorSelectedEvent) {
        controller.colorSelected(colorSelectedEvent.getSelection(), this);

    }

    @Override
    public void visit(PowerUpSelectedEvent powerUpSelectedEvent) {
        controller.powerUpSelected(powerUpSelectedEvent.getSelection(), this);

    }


    public void shutDown(){
        network.closeNetwork();
    }




}
