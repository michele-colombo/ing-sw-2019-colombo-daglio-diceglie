package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

public class GenericMessage extends MessageVisitable {
    public GenericMessage(String string){
        this.string = string;
        this.closeSocket = false;
    }

    public void accept(MessageVisitor messageVisitor){
        messageVisitor.visit(this);
    }
}
