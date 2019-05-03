package it.polimi.ingsw.server;

import it.polimi.ingsw.server.events.EventVisitable;
import it.polimi.ingsw.server.message.Message;
import it.polimi.ingsw.server.observer.Observer;

import java.io.IOException;

public class ServerView implements Observer {
    private SocketServer socketServer;
    private Controller controller;

    public ServerView(SocketServer socketServer, Controller controller){
        this.socketServer = socketServer;
        this.controller = controller;
    }

    public void receiveEvent(EventVisitable event){
         event.accept(controller, this);
    }

    public void update(Message message){
        try{
            socketServer.forwardMessage(message);
            try{
                if(message.getCloseSocket()){
                    socketServer.stopSocket();
                }
            } catch(IOException e){
                System.out.println("Error while closing the socket!");
                e.printStackTrace();
            }
        } catch(IOException e){
            System.out.println("Error while updating view!");
            e.printStackTrace();
        }
    }
}