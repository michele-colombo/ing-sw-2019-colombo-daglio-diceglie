package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.events.*;
import it.polimi.ingsw.server.ServerView;

public interface VisitorServer {
    void visit(LoginEvent loginEvent, ServerView serverView);
    void visit(SquareSelectedEvent squareSelectedEvent, ServerView serverView);
    void visit(ActionSelectedEvent actionSelectedEvent, ServerView serverView);
    void visit(PlayerSelectedEvent playerSelectedEvent, ServerView serverView);
    void visit(WeaponSelectedEvent weaponSelectedEvent, ServerView serverView);
    void visit(ModeSelectedEvent modeSelectedEvent, ServerView serverView);
    void visit(CommandSelectedEvent commandSelectedEvent, ServerView serverView);
    void visit(ColorSelectedEvent colorSelectedEvent, ServerView serverView);
    void visit(PowerUpSelectedEvent powerUpSelectedEvent, ServerView serverView);
}
