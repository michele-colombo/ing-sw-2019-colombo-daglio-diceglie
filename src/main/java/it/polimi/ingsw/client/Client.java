package it.polimi.ingsw.client;

import it.polimi.ingsw.server.events.EventVisitable;
import it.polimi.ingsw.server.events.LoginEvent;
import it.polimi.ingsw.server.message.DisconnectionMessage;
import it.polimi.ingsw.server.message.GenericMessage;
import it.polimi.ingsw.server.message.LoginMessage;
import it.polimi.ingsw.server.message.UpdateMessage;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements VisitorClient{

    private NetworkInterfaceClient network;
    private ClientView clientView;

    private String name;



    public Client(String ip, int port, String connection, String userInterface) throws IOException {

        try {

            switch (connection) {
                case "socket":
                    network = new SocketClient(ip, port, this);
                    break;
                case "rmi":
                    break;
                default: //non deve succedere
                    break;
            }

            network.startNetwork();
        }
        catch (IOException e){
            throw new IOException();
        }

        switch (userInterface){
            case "gui": clientView= new Gui(this); break;
            case "cli": clientView= new Cli(this); break;
            default: //non dovrebbe succedere
                break;
        }

    }

    public void visit(LoginMessage message){
        clientView.printLoginMessage(message.toString());
        if(! message.getLoginSuccessful()){
            clientView.askLogin();
        }

    }

    public void visit(DisconnectionMessage message){
        clientView.printDisconnectionMessage(message.toString());
    }
    public void visit(GenericMessage message){
        clientView.printDisconnectionMessage(message.toString());
    }

    public void startClient(){
        clientView.askLogin();
    }


    public void setName(String name) {

        this.name = name;

        try{
            //PlayerColor color = PlayerColor.valueOf(selectedColor); //SOSTITUIRE CON UN METODO FORWARD
            EventVisitable loginEvent = new LoginEvent(name);
            network.forward(loginEvent);

        } catch(IOException e){
            System.out.println("Error while forwarding the login event!");
            e.printStackTrace();
        }
    }

    public void visit(UpdateMessage message){

    }

}
