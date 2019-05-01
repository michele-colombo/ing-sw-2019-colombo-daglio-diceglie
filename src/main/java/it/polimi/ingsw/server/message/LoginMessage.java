package it.polimi.ingsw.server.message;

public class LoginMessage extends Message {
    private boolean loginSuccessful;

    public LoginMessage(String string, boolean loginSuccessful, boolean closeSocket){
        this.string = string;
        this.loginSuccessful = loginSuccessful;
        this.closeSocket = closeSocket;
    }

    //public void accept(ServerView visitor){
        //visitor.visit(this);
    //}

    public boolean getLoginSuccessful(){
        return loginSuccessful;
    }
}
