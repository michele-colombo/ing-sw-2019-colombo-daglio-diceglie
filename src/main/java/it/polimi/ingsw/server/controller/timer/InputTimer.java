package it.polimi.ingsw.server.controller.timer;

import it.polimi.ingsw.server.ServerView;
import java.util.TimerTask;

public class InputTimer extends TimerTask {
    private ServerView serverView;

    public InputTimer(ServerView serverView){
        this.serverView = serverView;
    }

    public void run(){
        serverView.disconnectPlayer();
    }
}
