package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.controller.VisitorServer;

public class SquareSelectedEvent extends EventVisitable{
    private int selection;
    private String nickname;

    public SquareSelectedEvent(int selection, String nickname) {
        this.selection = selection;
        this.nickname = nickname;
    }

    public int getSelection() {
        return selection;
    }

    public String getNickname() {
        return nickname;
    }

    public void accept(VisitorServer visitorServer, ServerView serverView){
        visitorServer.visit(this, serverView);
    }
}
