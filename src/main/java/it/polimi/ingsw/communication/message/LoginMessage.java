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

    /**
     * a text message sent by the server
     */
    private String response;
    /**
     * the validated name of the player
     */
    private String name;

    /**
     * Creates LoginMessage with given parameters
     * @param response response of LoginEvent
     * @param loginSuccessful true if player logged properly, else false
     */
    public LoginMessage(String response, boolean loginSuccessful, String name){
        this.response = response;
        this.loginSuccessful = loginSuccessful;
        this.name = name;
    }

    /**
     * Gets loginSuccessful
     * @return loginSuccessful
     */
    public boolean getLoginSuccessful(){
        return loginSuccessful;
    }

    /**
     * Gets response
     * @return response
     */
    public String getResponse(){
        return response;
    }

    /**
     * Gets the name of the player. If login fails it is an empty string.
     * @return string of the name. Empty if login failed.
     */
    public String getName() {
        return name;
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
