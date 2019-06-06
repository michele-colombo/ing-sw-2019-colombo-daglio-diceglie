package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.message.*;

public interface MessageVisitor {
    void visit(LoginMessage loginMessage);

    void visit(GenericMessage genericMessage);

    void visit(LayoutUpdateMessage layoutUpdateMessage);

    void visit(KillshotTrackUpdateMessage killshotTrackUpdate);

    void visit(CurrentPlayerUpdateMessage currentPlayerUpdate);

    void visit(StartMatchUpdateMessage startMatchUpdateMessage);

    void visit(PlayerUpdateMessage playerUpdateMessage);

    void visit(PaymentUpdateMessage paymentUpdateMessage);

    void visit(WeaponsUpdateMessage weaponsUpdateMessage);

    void visit(PowerUpUpdateMessage powerUpUpdateMessage);

    void visit(SelectablesUpdateMessage selectablesUpdateMessage);

    void visit(DamageUpdateMessage damageUpdateMessage);

    void visit(ConnectionUpdateMessage connectionUpdateMessage);
}
