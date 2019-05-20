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
    private String ip;
    private int port;



    public Client(ClientView clientView){
        this.clientView = clientView;
        this.ip = ClientMain.config.getIp();
        this.port = ClientMain.config.getPort();
    }

    public void visit(LoginMessage message){
        clientView.printLoginMessage(message.toString(), message.getLoginSuccessful());
    }

    public void visit(DisconnectionMessage message){
        clientView.printDisconnectionMessage(message.toString());
    }

    public void visit(GenericMessage message){
        clientView.printDisconnectionMessage(message.toString());
    }

    public void login(String choice, String name){
        createConnection(choice);
        chooseName(name);
    }

    private void createConnection(String connection){
        try {
            switch (connection) {
                case "socket":
                    network = new SocketClient(this.ip, this.port, this);
                    break;
                case "rmi":
                    break;
                default: //non deve succedere
                    break;
            }

            network.startNetwork();
        }
        catch (IOException e){
            System.out.println("Error while creating the network. Try again logging in");
            //clientView.askLogin();
        }
    }

    public void chooseName(String name){
        this.name = name;
        try{

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
