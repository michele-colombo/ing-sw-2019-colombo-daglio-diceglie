package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

public abstract class MessageVisitable{
    protected String string;

    public abstract void accept(MessageVisitor messageVisitor);

    @Override
    public String toString(){
        return string;
    }
}
