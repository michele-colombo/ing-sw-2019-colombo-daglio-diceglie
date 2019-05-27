package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.rmi.RemoteException;

public class DisconnectionMessage extends MessageVisitable {

    public void accept(MessageVisitor messageVisitor) throws RemoteException {
        messageVisitor.visit(this);
    }

    public DisconnectionMessage(String string){
        this.string = string;
        this.closeSocket = false;
    }
}
