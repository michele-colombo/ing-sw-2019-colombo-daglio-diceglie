package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;

import java.rmi.RemoteException;

public class LoginEvent extends EventVisitable {
    private String name;

    public LoginEvent(String name){
        this.name = name;
    }

    public void accept(EventVisitor eventVisitor) throws RemoteException {
        eventVisitor.visit(this);
    }

    public String getName() {
        return name;
    }

}
