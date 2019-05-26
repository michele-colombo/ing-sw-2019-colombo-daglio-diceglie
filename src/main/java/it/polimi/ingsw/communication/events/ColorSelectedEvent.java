package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;

public class ColorSelectedEvent extends EventVisitable {
    private int selection;

    public ColorSelectedEvent(int selection) {
        this.selection = selection;
    }

    public int getSelection() {
        return selection;
    }

    @Override
    public void accept(EventVisitor eventVisitor) {
        eventVisitor.visit(this);
    }
}
