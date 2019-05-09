package it.polimi.ingsw.server.events;

import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.VisitorServer;

public class PlayerSelectedEvent extends EventVisitable {
    private int selection;
    private String nickname;

    public PlayerSelectedEvent(int selection, String nickname) {
        this.selection = selection;
        this.nickname = nickname;
    }

    public int getSelection() {
        return selection;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void accept(VisitorServer visitorServer, ServerView serverView) {
        visitorServer.visit(this, serverView);
    }
}
