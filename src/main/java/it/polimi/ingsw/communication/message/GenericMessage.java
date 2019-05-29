package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.rmi.RemoteException;

public class GenericMessage extends MessageVisitable {
    public GenericMessage(String string){
        this.string = string;
    }

    public void accept(MessageVisitor messageVisitor) throws RemoteException {
        messageVisitor.visit(this);
    }
}
