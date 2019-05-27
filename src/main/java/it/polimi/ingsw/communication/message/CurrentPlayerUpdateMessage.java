package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.rmi.RemoteException;

public class CurrentPlayerUpdateMessage extends MessageVisitable{
    private String currentPlayer;

    public CurrentPlayerUpdateMessage(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) throws RemoteException {
        messageVisitor.visit(this);
    }
}
