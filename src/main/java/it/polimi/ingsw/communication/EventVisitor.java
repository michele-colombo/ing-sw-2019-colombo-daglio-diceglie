package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.events.*;

/**
 * Represents interface for the EventVisitor, who matches, dinamically, Events with their proper method on server
 */
public interface EventVisitor{
    /**
     * Method performed when a LoginEvent is received
     * @param loginEvent event received
     */
    void visit(LoginEvent loginEvent);

    /**
     * Method performed when a SquareSelectedEvent is received
     * @param squareSelectedEvent event received
     */
    void visit(SquareSelectedEvent squareSelectedEvent);

    /**
     * Method performed when a ActionSelectedEvent is received
     * @param actionSelectedEvent event received
     */
    void visit(ActionSelectedEvent actionSelectedEvent);

    /**
     * Method performed when a PlayerSelectedEvent is received
     * @param playerSelectedEvent event received
     */
    void visit(PlayerSelectedEvent playerSelectedEvent);

    /**
     * Method performed when a WeaponSelectedEvent
     * @param weaponSelectedEvent event received
     */
    void visit(WeaponSelectedEvent weaponSelectedEvent);

    /**
     * Method performed when a ModeSelectedEvent
     * @param modeSelectedEvent event received
     */
    void visit(ModeSelectedEvent modeSelectedEvent);

    /**
     * Method performed when a CommandSelectedEvent
     * @param commandSelectedEvent event received
     */
    void visit(CommandSelectedEvent commandSelectedEvent);

    /**
     * Method performed when ColorSelectedEvent
     * @param colorSelectedEvent event received
     */
    void visit(ColorSelectedEvent colorSelectedEvent);

    /**
     * Method performed when PowerUpSelectedEvent
     * @param powerUpSelectedEvent event received
     */
    void visit(PowerUpSelectedEvent powerUpSelectedEvent);
}


