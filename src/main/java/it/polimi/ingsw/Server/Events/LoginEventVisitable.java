package it.polimi.ingsw.Server.Events;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.Server.Controller;
import it.polimi.ingsw.Server.Message.Message;

public class LoginEventVisitable extends EventVisitable {
    private String name;
    private PlayerColor color;

    public LoginEventVisitable(String name, PlayerColor color){
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }

    public Message accept(Controller visitor){
        return visitor.visit(this);
    }
}
