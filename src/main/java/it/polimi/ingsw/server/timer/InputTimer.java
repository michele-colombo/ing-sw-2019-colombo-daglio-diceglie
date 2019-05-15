package it.polimi.ingsw.server.timer;

import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.observer.Observer;

import java.util.TimerTask;

public class InputTimer extends TimerTask {
    private Controller controller;
    private Observer observer;

    public InputTimer(Controller controller, Observer observer){
        this.controller = controller;
        this.observer = observer;
    }

    public void run(){
        ;
    }
}
