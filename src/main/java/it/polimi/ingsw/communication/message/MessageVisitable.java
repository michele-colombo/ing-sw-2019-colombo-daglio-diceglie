package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;
import java.rmi.RemoteException;

public abstract class MessageVisitable implements Serializable {
    protected String string;
    protected boolean closeSocket;

    public abstract void accept(MessageVisitor messageVisitor) throws RemoteException;

    @Override
    public String toString(){
        return string;
    }

    public boolean getCloseSocket(){
        return closeSocket;
    }
}
