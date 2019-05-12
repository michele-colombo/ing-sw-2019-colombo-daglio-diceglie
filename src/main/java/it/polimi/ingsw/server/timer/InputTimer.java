package it.polimi.ingsw.server.timer;

import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.ServerView;

import java.io.IOException;
import java.util.TimerTask;

public class InputTimer extends TimerTask {
    private Controller controller;
    private ServerView serverView;

    public InputTimer(Controller controller, ServerView serverView){
        this.controller = controller;
        this.serverView = serverView;
    }

    public void run(){
        controller.getGameModel().endTurn();
        try{
            serverView.closeNetwork();
        } catch(IOException e){
            System.out.println("Error");
        }
    }
}
