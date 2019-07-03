package it.polimi.ingsw.server.network;

import it.polimi.ingsw.communication.message.MessageVisitable;

import java.io.IOException;

public interface NetworkInterfaceServer {
    /**
     * send a message throw the networn
     * @param messageVisitable message to send
     * @throws IOException if error occurs
     */
    void forwardMessage(MessageVisitable messageVisitable) throws IOException;

    /**
     * close every thread
     */
    void closeNetwork();
}
