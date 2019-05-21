package it.polimi.ingsw.server.observer;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.communication.message.MessageVisitable;

public interface Observable {
    void attach(Player player, Observer observer);
    void detach(Observer observer);
    void notify(MessageVisitable messageVisitable, Observer observer);
}
