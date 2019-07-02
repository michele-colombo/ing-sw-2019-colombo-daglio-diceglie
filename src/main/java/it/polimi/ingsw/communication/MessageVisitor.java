package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.message.*;

/**
 * Represent interface for MessageVisitor, who matches, dinamically, Messages with their proper method on client
 */
public interface MessageVisitor {
    /**
     * Method performed when a LoginMessage is received
     * @param loginMessage message received
     */
    void visit(LoginMessage loginMessage);

    /**
     * Method performed when a GenericMessage is received
     * @param genericMessage message received
     */
    void visit(GenericMessage genericMessage);

    /**
     * Method performed when a LayoutUpdateMessage is received
     * @param layoutUpdateMessage message received
     */
    void visit(LayoutUpdateMessage layoutUpdateMessage);

    /**
     * Method performed when a KillshotTrackUpdateMessage is received
     * @param killshotTrackUpdate message received
     */
    void visit(KillshotTrackUpdateMessage killshotTrackUpdate);

    /**
     * Method performed when a CurrentPlayerUpdate is received
     * @param currentPlayerUpdate message received
     */
    void visit(CurrentPlayerUpdateMessage currentPlayerUpdate);

    /**
     * Method performed when a StartMatchUpdateMessaged is received
     * @param startMatchUpdateMessage message received
     */
    void visit(StartMatchUpdateMessage startMatchUpdateMessage);

    /**
     * Method performed when a PlayerUpdateMessage is received
     * @param playerUpdateMessage message received
     */
    void visit(PlayerUpdateMessage playerUpdateMessage);

    /**
     * Method performed when a PaymentUpdateMessage is received
     * @param paymentUpdateMessage message received
     */
    void visit(PaymentUpdateMessage paymentUpdateMessage);

    /**
     * Method performed when a WeaponsUpdateMessage is received
     * @param weaponsUpdateMessage message received
     */
    void visit(WeaponsUpdateMessage weaponsUpdateMessage);

    /**
     * Method performed when a PowerUpUpdateMessage is received
     * @param powerUpUpdateMessage message received
     */
    void visit(PowerUpUpdateMessage powerUpUpdateMessage);

    /**
     * Method performed when a SelectablesUpdateMessage is received
     * @param selectablesUpdateMessage message received
     */
    void visit(SelectablesUpdateMessage selectablesUpdateMessage);

    /**
     * Method performed when a DamageUpdateMessage is received
     * @param damageUpdateMessage message received
     */
    void visit(DamageUpdateMessage damageUpdateMessage);

    /**
     * Method performed when a ConnectionUpdateMessage is received
     * @param connectionUpdateMessage message received
     */
    void visit(ConnectionUpdateMessage connectionUpdateMessage);

    /**
     * Method performed when a GameOverMessage is received
     * @param gameOverMessage message received
     */
    void visit(GameOverMessage gameOverMessage);

}
