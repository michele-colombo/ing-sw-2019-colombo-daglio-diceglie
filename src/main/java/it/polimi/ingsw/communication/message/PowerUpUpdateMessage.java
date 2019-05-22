package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.util.List;

public class PowerUpUpdateMessage extends MessageVisitable {
    private String name;
    private List<String> powerUps;
    private int numPowerUps;

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
