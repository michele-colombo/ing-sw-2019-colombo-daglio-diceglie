package it.polimi.ingsw.server;

import it.polimi.ingsw.Command;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.server.events.*;

public interface VisitorServer {
    void visit(LoginEvent loginEvent, ServerView serverView);
    void visit(ReloginEvent reloginEvent, ServerView serverView);
    void visit(SquareSelectedEvent squareSelectedEvent, ServerView serverView);
    void visit(ActionSelectedEvent actionSelectedEvent, ServerView serverView);
    void visit(PlayerSelectedEvent playerSelectedEvent, ServerView serverView);
    void visit(WeaponSelectedEvent weaponSelectedEvent, ServerView serverView);
    void visit(ModeSelectedEvent modeSelectedEvent, ServerView serverView);
    void visit(CommandSelectedEvent commandSelectedEvent, ServerView serverView);
    void visit(ColorSelectedEvent colorSelectedEvent, ServerView serverView);
    void visit(PowerUpSelectedEvent powerUpSelectedEvent, ServerView serverView);
}