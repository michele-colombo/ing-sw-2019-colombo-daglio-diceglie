package it.polimi.ingsw.server;

import it.polimi.ingsw.GameModel;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.server.events.*;
import it.polimi.ingsw.server.message.LoginMessage;
import it.polimi.ingsw.server.message.Message;

public class Controller implements Visitor{

    private static GameModel gameModel;


    public Controller(GameModel gameModel){
        this.gameModel = gameModel;
    }

    public synchronized void visit(LoginEvent loginEvent, ServerView serverView){
        LoginMessage message;
        if(gameModel.getNumberOfPlayers() < 5) {
            message = gameModel.addPlayer(new Player(loginEvent.getName(), loginEvent.getColor()));

            if(message.getLoginSuccessful()){
                gameModel.attach(serverView);
            }
        } else{
            message = new LoginMessage("Game full", false, false);
        }
        gameModel.notify(message, serverView);
    }
}
