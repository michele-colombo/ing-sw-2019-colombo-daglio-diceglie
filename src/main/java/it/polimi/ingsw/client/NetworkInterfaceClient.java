package it.polimi.ingsw.client;

import it.polimi.ingsw.server.events.EventVisitable;

import java.io.IOException;

public interface NetworkInterfaceClient{
    void forward(EventVisitable eventVisitable) throws IOException;
    void startNetwork();
}
