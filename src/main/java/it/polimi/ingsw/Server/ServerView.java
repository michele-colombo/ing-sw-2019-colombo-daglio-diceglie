package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.Events.EventVisitable;
import it.polimi.ingsw.Server.Message.Message;

public class ServerView {
    private SocketServer socketServer;
    private Controller controller;

    public ServerView(SocketServer socketServer, Controller controller){
        this.socketServer = socketServer;
        this.controller = controller;
    }

    public Message receiveEvent(EventVisitable event){
        return event.accept(controller);
    }
}
