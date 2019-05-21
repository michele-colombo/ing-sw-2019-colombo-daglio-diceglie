package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.controller.VisitorServer;

public class LoginEvent extends EventVisitable {
    private String name;

    public LoginEvent(String name){
        this.name = name;
    }

    public void accept(VisitorServer visitorServer, ServerView serverView){
        visitorServer.visit(this, serverView);
    }

    public String getName() {
        return name;
    }

}
