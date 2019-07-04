package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;

import java.io.Serializable;


/**
 * Event from clients, represents the selection of a Square
 */
public class SquareSelectedEvent implements EventVisitable, Serializable {
    /**
     * Index of selected square
     */
    private int selection;

    /**
     * Creates a SquareSelectedEvent with given index
     * @param selection index of selected square
     */
    public SquareSelectedEvent(int selection) {
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
    public void accept(EventVisitor eventVisitor) {
        eventVisitor.visit(this);
    }
}
