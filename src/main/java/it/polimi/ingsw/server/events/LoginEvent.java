package it.polimi.ingsw.server.events;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.Visitor;

public class LoginEvent extends EventVisitable {
    private String name;
    private PlayerColor color;

    public LoginEvent(String name, PlayerColor color, boolean relogin){
        this.name = name;
        this.color = color;
    }

    public void accept(Visitor visitor, ServerView serverView){
        visitor.visit(this, serverView);
    }

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }
}
