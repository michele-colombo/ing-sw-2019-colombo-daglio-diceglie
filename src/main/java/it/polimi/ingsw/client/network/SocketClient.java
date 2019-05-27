package it.polimi.ingsw.client.network;

import com.google.gson.Gson;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.communication.events.*;
import it.polimi.ingsw.communication.message.*;
import it.polimi.ingsw.communication.EventVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketClient extends Thread implements NetworkInterfaceClient, EventVisitor {
    private final Socket socket;
    private Client client;
    private String eventPrefix;

    private PrintWriter out;

    public SocketClient(Socket socket, Client client){
        this.socket = socket;
        this.client= client;
        try{
            out = new PrintWriter(socket.getOutputStream());
            out.flush();
        } catch(IOException e){
            System.out.println("Error while creating stream!");
            e.printStackTrace();
        }
    }

    public SocketClient(String ip, int port, Client client) throws IOException{
        try{
            SocketAddress address= new InetSocketAddress(ip, port);
            Socket sock= new Socket();
            this.socket= sock;
            this.socket.connect(address, 10000);
            this.client= client;

            out= new PrintWriter(sock.getOutputStream());
            out.flush();

            start();
        }
        catch (IOException e){
            throw new IOException();
        }
    }

    public void startNetwork(){
        this.start();
    }

    public void run() {
            try {
                Scanner in = new Scanner(socket.getInputStream());
                while(true) {
                    MessageVisitable received = unwrap( in.nextLine() );
                    received.accept(client);
                    //todo: remove line below
                    System.out.println("ho fatto la accept di un messaggio");
                }
            }
            catch (NoSuchElementException nsee){
                //when server is unreachable this is what to do
                //client.restart();
                System.out.println("Server has stopped. Relogin");

                try {
                    socket.close();
                    client.restart();
                }
                catch (IOException e){
                    System.out.println("Impossible to close socket");
                }

            }
            catch (IOException e) {
                System.out.println("Error while receiving messages!");
                // qui mi sa che e' meglio spegnere tutto
            }
    }




    public void forward(EventVisitable event) throws IOException{
        Gson gson= new Gson();
        event.accept(this);
        out.println(eventPrefix+gson.toJson(event));
        out.flush();
    }

    private MessageVisitable unwrap(String messageText){
        //todo: remove line below
        System.out.println(messageText);
        Gson gson= new Gson();

        String prefix= messageText.substring(messageText.indexOf('#'), messageText.lastIndexOf('#') + 1);
        messageText= messageText.substring(messageText.lastIndexOf('#') + 1);
        MessageVisitable result= null;

        switch (prefix){
            case "#LOGIN#": result= gson.fromJson(messageText, LoginMessage.class); break;
            case "#DISCONNECTION#": result= gson.fromJson(messageText, DisconnectionMessage.class); break;
            case "#GENERIC#": result= gson.fromJson(messageText, GenericMessage.class); break;
            case "#LAYOUTUPDATE#": result= gson.fromJson(messageText, LayoutUpdateMessage.class); break;
            case "#KILLSHOTUPDATE#": result= gson.fromJson(messageText, KillshotTrackUpdateMessage.class); break;
            case "#CURRENTPLAYERUPDATE#": result= gson.fromJson(messageText, CurrentPlayerUpdateMessage.class); break;
            case "#STARTMATCHUPDATE#": result= gson.fromJson(messageText, StartMatchUpdateMessage.class); break;
            case "#PLAYERUPDATEMESSAGE#": result= gson.fromJson(messageText, PlayerUpdateMessage.class); break;
            case "#PAYMENTUPDATE#": result= gson.fromJson(messageText, PaymentUpdateMessage.class); break;
            case "#WEAPONSUPDATE#": result= gson.fromJson(messageText, WeaponsUpdateMessage.class); break;
            case "#POWERUPUPDATE#": result= gson.fromJson(messageText, PowerUpUpdateMessage.class); break;
            case "#SELECTABLESUPDATE#": result= gson.fromJson(messageText, SelectablesUpdateMessage.class); break;
            case "#DAMAGEUPDATE#": result= gson.fromJson(messageText, DamageUpdateMessage.class); break;
            case "#CONNECTIONUPDATE#": result= gson.fromJson(messageText, ConnectionUpdateMessage.class); break;
            default: break;
        }

        return result;
    }



    @Override
    public void visit(LoginEvent loginEvent) {
        eventPrefix= "#LOGIN#";

    }

    @Override
    public void visit(SquareSelectedEvent squareSelectedEvent) {
        eventPrefix= "#SQUARESELECTED#";

    }

    @Override
    public void visit(ActionSelectedEvent actionSelectedEvent) {
        eventPrefix= "#ACTIONSELECTED#";

    }

    @Override
    public void visit(PlayerSelectedEvent playerSelectedEvent) {
        eventPrefix= "#PLAYERSELECTED#";

    }

    @Override
    public void visit(WeaponSelectedEvent weaponSelectedEvent) {
        eventPrefix= "#WEAPONSELECTED#";

    }

    @Override
    public void visit(ModeSelectedEvent modeSelectedEvent) {
        eventPrefix= "#MODESELECTED#";

    }

    @Override
    public void visit(CommandSelectedEvent commandSelectedEvent) {
        eventPrefix= "#COMMANDSELECTED#";

    }

    @Override
    public void visit(ColorSelectedEvent colorSelectedEvent) {
        eventPrefix= "#COLORSELECTED#";

    }

    @Override
    public void visit(PowerUpSelectedEvent powerUpSelectedEvent) {
        eventPrefix= "#POWERUPSELECTED#";
    }
}
