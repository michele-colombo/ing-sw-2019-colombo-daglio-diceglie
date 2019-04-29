package it.polimi.ingsw.Server;

import it.polimi.ingsw.GameModel;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Server.Events.*;
import it.polimi.ingsw.Server.Message.*;

public class Controller{

    private static GameModel gameModel;
    private ServerView serverView;


    public Controller(GameModel gameModel){
        this.gameModel = gameModel;
        this.serverView = null;
    }

    public void addServerView(ServerView serverView){
        if(this.serverView == null){
            this.serverView = serverView;
        }
    }

    public Message visit(LoginEventVisitable login){
        if(gameModel.getNumberOfPlayers() < 5) {
            if (gameModel.addPlayer(new Player(login.getName(), login.getColor()))) {
                return new LoginMessage("Login successful!", true, false);
            }
            return new LoginMessage("Login unsuccessful!", false, false);
        }
        return new LoginMessage("Game full!", false, true);
    }
}
