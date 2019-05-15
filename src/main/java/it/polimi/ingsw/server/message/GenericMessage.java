package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.VisitorClient;

public class GenericMessage extends MessageVisitable {
    public GenericMessage(String string){
        this.string = string;
        this.closeSocket = false;
    }

    public void accept(VisitorClient visitorClient){
        visitorClient.visit(this);
    }
}
