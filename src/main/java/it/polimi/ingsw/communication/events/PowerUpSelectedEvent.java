package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;

import java.io.Serializable;

/**
 * Event from clients, represents the selection of a PowerUp
 */
public class PowerUpSelectedEvent implements EventVisitable, Serializable {
    /**
     * Index of selected power up
     */
    private int selection;

    /**
     * Creates a PowerUpSelectedEvent with given index
     * @param selection index of selected power up
     */
    public PowerUpSelectedEvent(int selection) {
        this.selection = selection;
    }

    /**
     *
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
