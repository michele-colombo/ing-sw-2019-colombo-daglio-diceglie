package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

public interface MessageVisitable{
    void accept(MessageVisitor messageVisitor);
}
