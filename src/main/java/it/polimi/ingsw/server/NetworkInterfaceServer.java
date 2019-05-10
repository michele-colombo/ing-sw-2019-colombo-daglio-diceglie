package it.polimi.ingsw.server;

import it.polimi.ingsw.server.message.MessageVisitable;

import java.io.IOException;

public interface NetworkInterfaceServer {
    void forwardMessage(MessageVisitable messageVisitable) throws IOException;
    void closeNetwork() throws IOException;
}
