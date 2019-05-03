package it.polimi.ingsw.client;

import it.polimi.ingsw.server.message.LoginMessage;

public interface VisitorClient {
    void visit(LoginMessage loginMessage);
}
