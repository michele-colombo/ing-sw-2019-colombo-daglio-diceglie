package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;

/**
 * Represents login response to a LoginEvent
 */
public class LoginMessage implements MessageVisitable, Serializable {
    /**
     * True if player logged properly, else false
     */
    private boolean loginSuccessful;
    private String response;

    /**
     * Creates LoginMessage with given parameters
     * @param response response of LoginEvent
     * @param loginSuccessful true if player logged properly, else false
     */
    public LoginMessage(String response, boolean loginSuccessful){
        this.response = response;
        this.loginSuccessful = loginSuccessful;
    }

    /**
     *
     * @return loginSuccessful
     */
    public boolean getLoginSuccessful(){
        return loginSuccessful;
    }

    /**
     *
     * @return response
     */
    public String getResponse(){
        return response;
    }

    /**
     * Method used to properly recognize dynamic type of Message
     * @param messageVisitor messageVisitor who "visits" this message
     */
    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
