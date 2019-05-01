package it.polimi.ingsw.server.observer;

import it.polimi.ingsw.server.message.Message;

public interface Observer {
    void update(Message message);
}
