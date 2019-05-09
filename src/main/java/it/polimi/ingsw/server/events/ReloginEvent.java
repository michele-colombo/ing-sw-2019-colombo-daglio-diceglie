package it.polimi.ingsw.server.events;

import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.VisitorServer;

public class ReloginEvent extends EventVisitable {
    private String name;

    public ReloginEvent(String name){
        this.name = name;
    }

    @Override
    public void accept(VisitorServer visitorServer, ServerView serverView){
        visitorServer.visit(this, serverView);
    }

    public String getName(){
        return name;
    }
}
