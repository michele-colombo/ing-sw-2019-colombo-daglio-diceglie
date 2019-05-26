package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;

public class SquareSelectedEvent extends EventVisitable{
    private int selection;

    public SquareSelectedEvent(int selection) {
        this.selection = selection;
    }

    public int getSelection() {
        return selection;
    }

    public void accept(EventVisitor eventVisitor){
        eventVisitor.visit(this);
    }
}
