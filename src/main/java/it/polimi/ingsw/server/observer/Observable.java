package it.polimi.ingsw.server.observer;

import it.polimi.ingsw.server.model.Player;

/**
 * Observable interface for observer pattern
 */
public interface Observable {
    /**
     * Attaches an Observer to an observable
     * @param player player referred to observer
     * @param observer Observer to be attached
     */
    void attach(Player player, Observer observer);

    /**
     * Removes observer from Observable
     * @param observer Observer to be removed
     */
    void detach(Observer observer);
}
