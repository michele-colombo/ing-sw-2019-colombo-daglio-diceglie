package it.polimi.ingsw.server;

import it.polimi.ingsw.server.events.*;

public interface VisitorServer {
    void visit(LoginEvent loginEvent, ServerView serverView);
    void visit(ReloginEvent reloginEvent, ServerView serverView);
}
