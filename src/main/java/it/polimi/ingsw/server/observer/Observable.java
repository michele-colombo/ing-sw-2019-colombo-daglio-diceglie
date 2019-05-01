package it.polimi.ingsw.server.observer;

import it.polimi.ingsw.server.message.Message;

public interface Observable {
    void attach(Observer observer);
    void detach(Observer observer);
    void notify(Message message, Observer observer);
}
