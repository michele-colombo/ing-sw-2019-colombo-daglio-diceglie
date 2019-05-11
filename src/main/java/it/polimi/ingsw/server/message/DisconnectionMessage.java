package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.VisitorClient;

public class DisconnectionMessage extends MessageVisitable {

    public void accept(VisitorClient visitorClient){
        visitorClient.visit(this);
    }

    public DisconnectionMessage(String string){
        this.string = string;
        this.closeSocket = false;
    }
}
