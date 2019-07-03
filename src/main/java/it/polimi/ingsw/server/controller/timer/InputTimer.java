package it.polimi.ingsw.server.controller.timer;

import it.polimi.ingsw.server.ServerView;
import java.util.TimerTask;

/**
 * TimerTask that forces player disconnection, performed when an InputTimer is over
 */
public class InputTimer extends TimerTask {
    /**
     * ServerView from which server in waiting for
     */
    private ServerView serverView;

    /**
     * Creates a InputTimer with given serverView
     * @param serverView ServerView server is waiting for
     */
    public InputTimer(ServerView serverView){
        this.serverView = serverView;
    }

    /**
     * Forces serverView disconnection
     */
    public void run(){
        serverView.disconnectPlayer();
    }
}
