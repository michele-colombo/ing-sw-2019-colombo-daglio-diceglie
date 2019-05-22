package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

public class ConnectionUpdateMessage extends MessageVisitable{
    private String name;
    private boolean connected;

    public ConnectionUpdateMessage(String name, boolean connected) {
        this.name = name;
        this.connected = connected;
    }

    public String getName() {
        return name;
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
