package it.polimi.ingsw.server.timer;

import it.polimi.ingsw.server.Controller;

import java.util.TimerTask;

public class LoginTimer extends TimerTask {
    private Controller controller;

    public LoginTimer(Controller controller){
        this.controller = controller;
    }

    public void run(){
        if(!controller.getGameModel().tooFewPlayers()){
            controller.startMatch();
        } else{
            controller.setLoginTimerStarted();
        }
    }
}
