package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;

import java.io.Serializable;

/**
 * Event from clients, represents the chosen name for the match
 */
public class LoginEvent implements EventVisitable, Serializable {
    /**
     * Name chosen
     */
    private String name;

    /**
     * Creates a LoginEvent with chosen name
     * @param name name chosen for the match
     */
    public LoginEvent(String name){
        this.name = name;
    }

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
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
