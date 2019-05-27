package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.rmi.RemoteException;

public class LoginMessage extends MessageVisitable {
    private boolean loginSuccessful;

    public LoginMessage(String string, boolean loginSuccessful, boolean closeSocket){
        this.string = string;
        this.loginSuccessful = loginSuccessful;
        this.closeSocket = closeSocket;
    }

    public void accept(MessageVisitor messageVisitor) throws RemoteException {
        messageVisitor.visit(this);
    }

    public boolean getLoginSuccessful(){
        return loginSuccessful;
    }
}
