package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;

/**
 * Represents a generic message
 */
public class GenericMessage implements MessageVisitable, Serializable {
    /**
     * Contents of the message
     */
    private String string;

    /**
     * Creates GenericMessage with given parameters
     * @param string content of the message
     */
    public GenericMessage(String string){
        this.string = string;
    }

    /**
     * Gets string
     * @return string
     */
    public String getString(){
        return string;
    }

    /**
     * Method used to properly recognize dynamic type of Message
     * @param messageVisitor messageVisitor who "visits" this message
     */
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
