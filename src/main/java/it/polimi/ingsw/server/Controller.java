package it.polimi.ingsw.server;

import it.polimi.ingsw.GameModel;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.exceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.exceptions.GameFullException;
import it.polimi.ingsw.exceptions.NameAlreadyTakenException;
import it.polimi.ingsw.server.events.*;
import it.polimi.ingsw.server.message.LoginMessage;
import it.polimi.ingsw.server.message.Message;

public class Controller implements Visitor{

    private static GameModel gameModel;


    public Controller(GameModel gameModel){
        this.gameModel = gameModel;
    }

    public synchronized void visit(LoginEvent loginEvent, ServerView serverView){
        LoginMessage message = new LoginMessage("Login successful!", true, false);
        try{
            gameModel.addPlayer(new Player(loginEvent.getName(), loginEvent.getColor()));
        } catch(NameAlreadyTakenException e){
            message = new LoginMessage("Name already taken!", false, false);
        } catch(ColorAlreadyTakenException e){
            message = new LoginMessage("Color already taken!", false, false);
        } catch(GameFullException e){
            message = new LoginMessage("Game full!", false, true);
        }
        finally {
            gameModel.notify(message, serverView);
        }
    }
}
