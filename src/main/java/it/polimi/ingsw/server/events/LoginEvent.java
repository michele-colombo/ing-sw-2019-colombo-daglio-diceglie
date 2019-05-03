package it.polimi.ingsw.server.events;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.VisitorServer;

public class LoginEvent extends EventVisitable {
    private String name;
    private PlayerColor color;

    public LoginEvent(String name, PlayerColor color){
        this.name = name;
        this.color = color;
    }

    public void accept(VisitorServer visitorServer, ServerView serverView){
        visitorServer.visit(this, serverView);
    }

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }
}
