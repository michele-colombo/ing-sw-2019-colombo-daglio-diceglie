package it.polimi.ingsw.client.network;

import com.google.gson.Gson;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientExceptions.ConnectionInitializationException;
import it.polimi.ingsw.client.ClientExceptions.ForwardingException;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.communication.CommonProperties;
import it.polimi.ingsw.communication.events.*;
import it.polimi.ingsw.communication.message.*;
import it.polimi.ingsw.communication.EventVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.rmi.RemoteException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketClient extends NetworkInterfaceClient implements EventVisitor {
    private final Socket socket;
    private Client client;
    private String eventPrefix;

    private Asker asker;


    private PrintWriter out;


    public SocketClient(Client client) throws ConnectionInitializationException{
        super(client);

        String ip= ClientMain.config.getIp();
        int port= ClientMain.config.getPort();

        try {

            SocketAddress address = new InetSocketAddress(ip, port);
            Socket sock = new Socket();
            this.socket = sock;
            this.socket.connect(address, REACHING_TIME);
            this.socket.setSoTimeout((int) CommonProperties.PING_PONG_DELAY*2);
            this.client = client;

            out = new PrintWriter(sock.getOutputStream());
            out.flush();

            asker= new Asker();
            asker.start();

            ponging.start();

        }
        catch (IOException e){
            throw new ConnectionInitializationException();
        }

    }




    public void forward(EventVisitable event) throws ForwardingException {
        Gson gson= new Gson();
        event.accept(this);
        out.println(eventPrefix + gson.toJson(event));
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
            case "#GAMEOVER#": result = gson.fromJson(messageText, GameOverMessage.class); break;
            default: break;
        }

        return result;
    }

    public void closeConnection(){
        try{
            ponging.interrupt();
            ponging.close();
            asker.close();
            socket.close();
        }
        catch (IOException e){}
    }

    @Override
    void pong() {
        out.println(CommonProperties.PONG_NAME);
        out.flush();
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


    private class Asker extends Thread{
        private AtomicBoolean active;

        private Asker(){
            active= new AtomicBoolean(true);
        }

        @Override
        public void run(){
            try {
                Scanner in = new Scanner(socket.getInputStream());
                while(active.get()) {
                    String strcv= in.nextLine();
                    if(!strcv.equalsIgnoreCase(CommonProperties.PING_NAME)) {
                        MessageVisitable received = unwrap(strcv);
                        if (received != null){
                            received.accept(client);
                        } else {
                            System.out.println("NULL MESSAGE RECEIVED");
                        }
                    }
                }
            }
            catch (NoSuchElementException nsee){
                //it is very important because i get to this exception in every case i want to disconnect
                //client.restart();

                System.out.println("Server has stopped. Relogin");
                client.restart();

            }
            catch (IOException e) {
                System.out.println("Error while receiving messages!");
                // qui mi sa che e' meglio spegnere tutto
            }
            catch (Exception e){
                System.out.println("eccezione sconosciuta");
                e.printStackTrace();
            }
        }

        public void close(){
            active.set(false);
        }
    }
}
