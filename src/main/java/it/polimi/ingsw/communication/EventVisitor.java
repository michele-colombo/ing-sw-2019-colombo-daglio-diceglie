package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.events.*;

public interface EventVisitor{

    void visit(LoginEvent loginEvent);
    void visit(SquareSelectedEvent squareSelectedEvent);
    void visit(ActionSelectedEvent actionSelectedEvent);
    void visit(PlayerSelectedEvent playerSelectedEvent);
    void visit(WeaponSelectedEvent weaponSelectedEvent);
    void visit(ModeSelectedEvent modeSelectedEvent);
    void visit(CommandSelectedEvent commandSelectedEvent);
    void visit(ColorSelectedEvent colorSelectedEvent);
    void visit(PowerUpSelectedEvent powerUpSelectedEvent);
    void visit(PongEvent pongEvent);
}


