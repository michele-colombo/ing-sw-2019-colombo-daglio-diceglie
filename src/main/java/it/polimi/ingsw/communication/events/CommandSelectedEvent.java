package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;

import java.io.Serializable;

/**
 * Event from clients, represents the selection of a Command
 */
public class CommandSelectedEvent implements EventVisitable, Serializable {
    /**
     * Index of selected command
     */
    private int selection;

    /**
     * Creates a CommandSelectedEvent with given index
     * @param selection index of selected command
     */
    public CommandSelectedEvent(int selection) {
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
