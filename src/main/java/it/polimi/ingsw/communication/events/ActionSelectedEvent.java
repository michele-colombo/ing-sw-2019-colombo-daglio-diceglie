package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;

import java.io.Serializable;

/**
 * Event from clients, represents the selection of an Action
 */
public class ActionSelectedEvent implements EventVisitable, Serializable {
    /**
     * Index of selected action
     */
    private int selection;

    /**
     * Creates an ActionSelectedEvent with given index
     * @param selection index of selected action
     */
    public ActionSelectedEvent(int selection) {
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
