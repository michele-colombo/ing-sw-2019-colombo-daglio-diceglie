package it.polimi.ingsw.server.timer;

import it.polimi.ingsw.server.ServerView;
import java.io.IOException;
import java.util.TimerTask;

public class InputTimer extends TimerTask {
    private ServerView serverView;

    public InputTimer(ServerView serverView){
        this.serverView = serverView;
    }

    public void run(){
        try{
            serverView.closeNetwork();
        } catch(IOException e){
            System.out.println("Timer: player already disconnected!");
        }
    }
}
