package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.VisitorClient;

public class LoginMessage extends MessageVisitable {
    private boolean loginSuccessful;

    public LoginMessage(String string, boolean loginSuccessful, boolean closeSocket){
        this.string = string;
        this.loginSuccessful = loginSuccessful;
        this.closeSocket = closeSocket;
    }

    public void accept(VisitorClient visitorClient){
        visitorClient.visit(this);
    }

    public boolean getLoginSuccessful(){
        return loginSuccessful;
    }
}
