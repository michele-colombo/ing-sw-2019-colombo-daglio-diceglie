package it.polimi.ingsw.server;

import it.polimi.ingsw.server.events.EventVisitable;
import it.polimi.ingsw.server.message.MessageVisitable;
import it.polimi.ingsw.server.observer.Observer;

import java.io.IOException;

public class ServerView implements Observer {
    private NetworkInterfaceServer network;
    private Controller controller;

    public ServerView(NetworkInterfaceServer network, Controller controller){
        this.network = network;
        this.controller = controller;
    }

    public void receiveEvent(EventVisitable event){
         event.accept(controller, this);
    }

    public void update(MessageVisitable messageVisitable){
        try{
            network.forwardMessage(messageVisitable);
            System.out.println("Messaggio mandato!");
            try{
                if(messageVisitable.getCloseSocket()){ //close socket connection
                    closeNetwork();
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

    public void disconnectPlayer(Observer observer){
        controller.disconnectPlayer(observer);
    }

    public void closeNetwork() throws IOException{
        network.closeNetwork();
    }
}
