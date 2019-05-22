package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;

public abstract class MessageVisitable implements Serializable {
    protected String string;
    protected boolean closeSocket;

    public abstract void accept(MessageVisitor messageVisitor);

    @Override
    public String toString(){
        return string;
    }

    public boolean getCloseSocket(){
        return closeSocket;
    }
}
