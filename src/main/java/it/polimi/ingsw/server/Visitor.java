package it.polimi.ingsw.server;

import it.polimi.ingsw.server.events.LoginEvent;

public interface Visitor {
    void visit(LoginEvent loginEvent, ServerView serverView);
}
