package it.polimi.ingsw.server.network;

import com.google.gson.Gson;
import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.communication.events.*;
import it.polimi.ingsw.communication.message.*;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.controller.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketServer extends Thread implements NetworkInterfaceServer, MessageVisitor {
    private Socket socket;
    private ServerView serverView;
    private PrintWriter out;
    private String messagePrefix;

    public SocketServer(Socket socket, Controller controller){
        this.socket = socket;
        //todo: non crea la serverView
        try {
            serverView = new ServerView(this, controller);
        }
        catch (RemoteException e){
            System.out.println("Remote exception cannot start");
        }
    }

    @Override
    public void run(){
        try{
            out = new PrintWriter(socket.getOutputStream());
            out.flush();
            final Scanner in = new Scanner(socket.getInputStream());
            while(true){
                EventVisitable received= unwrap( in.nextLine() );
                if(received!=null) {
                    received.accept(serverView);  //if everything went right
                }
            }
        } catch(NoSuchElementException e){
            System.out.println("Client disconnesso");
            serverView.disconnectPlayer(serverView);
            //todo passare al prossimo player ed eventualmente annullare l'azione(se match è iniziato)
        }
        catch (IOException e){
            System.out.println("Eccezione sull'input");
        }
    }

    public void forwardMessage(MessageVisitable messageVisitable) throws IOException{
        //PrintWriter out = new PrintWriter(socket.getOutputStream());
        Gson gson = new Gson();

        messageVisitable.accept(this);
        out.println(messagePrefix + gson.toJson(messageVisitable));
        out.flush();
    }

   public Socket getSocket(){
        return socket;
   }

   public void closeNetwork() throws  IOException{
            this.socket.close();
   }

    @Override
    public void visit(LoginMessage loginMessage) {
        messagePrefix= "#LOGIN#";
    }

    @Override
    public void visit(DisconnectionMessage disconnectionMessage) {
        messagePrefix= "#DISCONNECTION#";

    }

    @Override
    public void visit(GenericMessage genericMessage) {
        messagePrefix= "#GENERIC#";

    }

    @Override
    public void visit(LayoutUpdateMessage layoutUpdateMessage) {
        messagePrefix= "#LAYOUTUPDATE#";

    }

    @Override
    public void visit(KillshotTrackUpdateMessage killshotTrackUpdate) {
        messagePrefix= "#KILLSHOTUPDATE#";

    }

    @Override
    public void visit(CurrentPlayerUpdateMessage currentPlayerUpdate) {
        messagePrefix= "#CURRENTPLAYERUPDATE#";

    }

    @Override
    public void visit(StartMatchUpdateMessage startMatchUpdateMessage) {
        messagePrefix= "#STARTMATCHUPDATE#";

    }

    @Override
    public void visit(PlayerUpdateMessage playerUpdateMessage) {
        messagePrefix= "#PLAYERUPDATEMESSAGE#";

    }

    @Override
    public void visit(PaymentUpdateMessage paymentUpdateMessage) {
        messagePrefix= "#PAYMENTUPDATE#";

    }

    @Override
    public void visit(WeaponsUpdateMessage weaponsUpdateMessage) {
        messagePrefix= "#WEAPONSUPDATE#";

    }

    @Override
    public void visit(PowerUpUpdateMessage powerUpUpdateMessage) {
        messagePrefix= "#POWERUPUPDATE#";

    }

    @Override
    public void visit(SelectablesUpdateMessage selectablesUpdateMessage) {
        messagePrefix= "#SELECTABLESUPDATE#";

    }

    @Override
    public void visit(DamageUpdateMessage damageUpdateMessage) {
        messagePrefix= "#DAMAGEUPDATE#";

    }

    @Override
    public void visit(ConnectionUpdateMessage connectionUpdateMessage) {
        messagePrefix= "#CONNECTIONUPDATE#";

    }

    private EventVisitable unwrap(String eventText){
        //todo: remove next line
        System.out.println(eventText);
        Gson gson= new Gson();
        String prefix= eventText.substring(eventText.indexOf('#'), eventText.lastIndexOf('#') + 1);
        eventText= eventText.substring(eventText.lastIndexOf('#') + 1);

        EventVisitable result= null;
        switch (prefix){
            case "#LOGIN#": result= gson.fromJson(eventText, LoginEvent.class); break;
            case "#SQUARESELECTED#": result= gson.fromJson(eventText, SquareSelectedEvent.class); break;
            case "#ACTIONSELECTED#": result= gson.fromJson(eventText, ActionSelectedEvent.class); break;
            case "#PLAYERSELECTED#": result= gson.fromJson(eventText, PlayerSelectedEvent.class); break;
            case "#WEAPONSELECTED#": result= gson.fromJson(eventText, WeaponSelectedEvent.class); break;
            case "#MODESELECTED#": result= gson.fromJson(eventText, ModeSelectedEvent.class); break;
            case "#COMMANDSELECTED#": result= gson.fromJson(eventText, CommandSelectedEvent.class); break;
            case "#COLORSELECTED#": result= gson.fromJson(eventText, ColorSelectedEvent.class); break;
            case "#POWERUPSELECTED#": result= gson.fromJson(eventText, PowerUpSelectedEvent.class); break;



        }
        return result;
    }
}
