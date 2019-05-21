package it.polimi.ingsw.client.network;

import it.polimi.ingsw.communication.events.EventVisitable;

import java.io.IOException;

public interface NetworkInterfaceClient{
    void forward(EventVisitable eventVisitable) throws IOException;
    void startNetwork();
}
