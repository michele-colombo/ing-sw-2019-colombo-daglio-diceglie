package it.polimi.ingsw.server.message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    protected String string;
    protected boolean closeSocket;

    //public abstract void accept(ServerView visitor);

    public String getString(){
        return string;
    }

    public boolean getCloseSocket(){
        return closeSocket;
    }
}
