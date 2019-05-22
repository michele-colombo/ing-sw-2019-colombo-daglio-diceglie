package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.PlayerState;

public class PlayerUpdateMessage extends MessageVisitable {
    private String name;
    private PlayerState state;
    private String position;
    private Cash wallet;

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
