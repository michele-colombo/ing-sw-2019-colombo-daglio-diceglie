package it.polimi.ingsw.client;

import it.polimi.ingsw.server.message.DisconnectionMessage;
import it.polimi.ingsw.server.message.GenericMessage;
import it.polimi.ingsw.server.message.LoginMessage;

public interface VisitorClient {
    void visit(LoginMessage loginMessage);
    void visit(DisconnectionMessage disconnectionMessage);
    void visit(GenericMessage genericMessage);
}
