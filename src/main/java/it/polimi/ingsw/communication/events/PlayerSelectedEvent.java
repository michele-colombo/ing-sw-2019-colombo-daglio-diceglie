package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;

import java.io.Serializable;

/**
 * Event from clients, represents the selection of a Player
 */
public class PlayerSelectedEvent implements EventVisitable, Serializable {
    /**
     * Index of selected player
     */
    private int selection;

    /**
     * Creates a PlayerSelectedEvent with given index
     * @param selection index of selected player
     */
    public PlayerSelectedEvent(int selection) {
        this.selection = selection;
    }

    /**
     * Gets selection
     * @return selection
     */
    public int getSelection() {
        return selection;
    }

    /**
     * Method used to properly recognize dynamic type of this Event
     * @param eventVisitor eventVisitor who "visits" this event
     */
    @Override
    public void accept(EventVisitor eventVisitor) {
        eventVisitor.visit(this);
    }
}
