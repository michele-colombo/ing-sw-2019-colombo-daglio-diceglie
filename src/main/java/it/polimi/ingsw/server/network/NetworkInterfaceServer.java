package it.polimi.ingsw.server.network;

import it.polimi.ingsw.communication.message.MessageVisitable;

import java.io.IOException;

public interface NetworkInterfaceServer {
    void forwardMessage(MessageVisitable messageVisitable) throws IOException;
    void closeNetwork() throws IOException;
}
