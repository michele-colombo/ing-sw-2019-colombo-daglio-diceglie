package it.polimi.ingsw.server.controller.timer;

import it.polimi.ingsw.server.controller.Controller;

import java.util.TimerTask;

/**
 * TimerTask that check is match can starts, performed when a LoginTimer is over
 */
public class LoginTimer extends TimerTask {
    /**
     * Reference to controller
     */
    private Controller controller;

    /**
     * Creates an LoginTimer with given parameters
     * @param controller controller to call startMatch on
     */
    public LoginTimer(Controller controller){
        this.controller = controller;
    }

    /**
     * If there are almost 3 players, make match to start, else set loginTimerStarted of controller to false
     */
    public void run(){
        if(!controller.getGameModel().tooFewPlayers()){
            controller.startMatch();
        } else{
            controller.setLoginTimerStarted();
        }
    }
}
