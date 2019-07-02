package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;

public class GenericMessage implements MessageVisitable, Serializable {
    private String string;

    public GenericMessage(String string){
        this.string = string;
    }

    public String getString(){
        return string;
    }

    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
