package it.polimi.ingsw.server;

import it.polimi.ingsw.server.events.LoginEvent;

public interface VisitorServer {
    void visit(LoginEvent loginEvent, ServerView serverView);
}
