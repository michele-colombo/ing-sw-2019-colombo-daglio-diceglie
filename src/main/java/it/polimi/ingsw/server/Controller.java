package it.polimi.ingsw.server;

import it.polimi.ingsw.GameModel;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.server.events.*;
import it.polimi.ingsw.server.message.LoginMessage;
import it.polimi.ingsw.server.observer.Observer;

public class Controller implements VisitorServer {

    private final GameModel gameModel;


    public Controller(GameModel gameModel){
        this.gameModel = gameModel;
    }

    public synchronized void visit(LoginEvent loginEvent, ServerView serverView){
        LoginMessage message = new LoginMessage("Login successful!", true, false);
        try{
            Player newPlayer = new Player(loginEvent.getName(), loginEvent.getColor());
            gameModel.addPlayer(newPlayer);
            gameModel.attach(newPlayer, serverView);
        } catch(NameAlreadyTakenException e){
            message = new LoginMessage("Name already taken!", false, false);
        } catch(ColorAlreadyTakenException e){
            message = new LoginMessage("Color already taken!", false, false);
        } catch(GameFullException e){
            message = new LoginMessage("Game full!", false, true);
        }
        finally {
            gameModel.notify(message, serverView);
            System.out.println("Login ok");
        }
    }

    public synchronized void visit(ReloginEvent reloginEvent, ServerView serverView){
        LoginMessage message = new LoginMessage("Login successful!", true, false);
        try{
            gameModel.relogin(reloginEvent.getName());
            gameModel.attach(gameModel.getPlayerByName(reloginEvent.getName()), serverView);
        } catch(NameNotFoundException e){
            message = new LoginMessage("Name not found!", false, false);
        } catch(AlreadyLoggedException e){
            message = new LoginMessage("Player already logged", false, true);
        }
        finally{
            gameModel.notify(message, serverView);
            System.out.println("Relogin ok");
        }
    }

    public void removeGameModelObserver(Observer observer){
        gameModel.detach(observer);
    }
}
