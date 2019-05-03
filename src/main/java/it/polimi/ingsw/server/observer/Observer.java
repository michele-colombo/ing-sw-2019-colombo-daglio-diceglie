package it.polimi.ingsw.server.observer;

import it.polimi.ingsw.server.message.MessageVisitable;

public interface Observer {
    void update(MessageVisitable messageVisitable);
}
