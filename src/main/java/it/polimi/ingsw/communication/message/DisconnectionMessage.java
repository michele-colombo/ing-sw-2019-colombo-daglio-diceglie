package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

public class DisconnectionMessage extends MessageVisitable {

    public void accept(MessageVisitor messageVisitor){
        messageVisitor.visit(this);
    }

    public DisconnectionMessage(String string){
        this.string = string;
        this.closeSocket = false;
    }
}
