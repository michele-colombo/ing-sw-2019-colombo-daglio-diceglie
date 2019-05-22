package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.NetworkInterfaceClient;
import it.polimi.ingsw.client.network.SocketClient;
import it.polimi.ingsw.client.userInterface.ClientView;
import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.communication.events.EventVisitable;
import it.polimi.ingsw.communication.events.LoginEvent;
import it.polimi.ingsw.communication.message.*;

import java.io.IOException;

public class Client implements MessageVisitor {

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

    @Override
    public void visit(LayoutUpdateMessage layoutUpdateMessage) {

    }

    @Override
    public void visit(KillshotTrackUpdateMessage killshotTrackUpdate) {

    }

    @Override
    public void visit(CurrentPlayerUpdateMessage currentPlayerUpdate) {

    }

    @Override
    public void visit(StartMatchUpdateMessage startMatchUpdateMessage) {

    }

    @Override
    public void visit(PlayerUpdateMessage playerUpdateMessage) {

    }

    @Override
    public void visit(PaymentUpdateMessage paymentUpdateMessage) {

    }

    @Override
    public void visit(WeaponsUpdateMessage weaponsUpdateMessage) {

    }

    @Override
    public void visit(PowerUpUpdateMessage powerUpUpdateMessage) {

    }

    @Override
    public void visit(SelectablesUpdateMessage selectablesUpdateMessage) {

    }

    @Override
    public void visit(DamageUpdateMessage damageUpdateMessage) {

    }

    @Override
    public void visit(ConnectionUpdateMessage connectionUpdateMessage) {

    }

}
