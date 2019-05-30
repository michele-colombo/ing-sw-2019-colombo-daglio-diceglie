package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.rmi.RemoteException;
import java.util.Map;

public class ConnectionUpdateMessage extends MessageVisitable{
    private Map<String, Boolean> connectionStates;

    public ConnectionUpdateMessage(Map<String, Boolean> connectionStates) {
        this.connectionStates = connectionStates;
    }

    public Map<String, Boolean> getConnectionStates() {
        return connectionStates;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
