package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.message.DisconnectionMessage;
import it.polimi.ingsw.communication.message.GenericMessage;
import it.polimi.ingsw.communication.message.LoginMessage;
import it.polimi.ingsw.communication.message.UpdateMessage;

public interface VisitorClient {
    void visit(LoginMessage loginMessage);
    void visit(DisconnectionMessage disconnectionMessage);
    void visit(GenericMessage genericMessage);
    void visit(UpdateMessage updateMessage);
}
