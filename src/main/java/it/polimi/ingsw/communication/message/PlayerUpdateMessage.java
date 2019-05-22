package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.PlayerState;

public class PlayerUpdateMessage extends MessageVisitable {
    private String name;
    private PlayerState state;
    private String position;
    private Cash wallet;

    public PlayerUpdateMessage(String name, PlayerState state, String position, Cash wallet) {
        this.name = name;
        this.state = state;
        this.position = position;
        this.wallet = wallet;
    }

    public String getName() {
        return name;
    }

    public PlayerState getState() {
        return state;
    }

    public String getPosition() {
        return position;
    }

    public Cash getWallet() {
        return wallet;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
