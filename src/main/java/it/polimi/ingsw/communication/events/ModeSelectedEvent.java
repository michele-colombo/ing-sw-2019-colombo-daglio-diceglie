package it.polimi.ingsw.communication.events;


import it.polimi.ingsw.communication.EventVisitor;

import java.io.Serializable;

/**
 * Event from clients, represents the selection of a Mode
 */
public class ModeSelectedEvent implements EventVisitable, Serializable {
    /**
     * Index of selected command
     */
    private int selection;

    /**
     * Creates a ModeSelectedEvent with given index
     * @param selection index of selected mode
     */
    public ModeSelectedEvent(int selection) {
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
