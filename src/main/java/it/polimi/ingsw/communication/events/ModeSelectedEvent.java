package it.polimi.ingsw.communication.events;


import it.polimi.ingsw.communication.EventVisitor;

import java.rmi.RemoteException;

public class ModeSelectedEvent extends EventVisitable {
    private int selection;

    public ModeSelectedEvent(int selection) {
        this.selection = selection;
    }

    public int getSelection() {
        return selection;
    }

    @Override
    public void accept(EventVisitor eventVisitor) throws RemoteException {
        eventVisitor.visit(this);
    }
}
