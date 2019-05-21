package it.polimi.ingsw.server.network;

import it.polimi.ingsw.communication.events.EventVisitable;
import it.polimi.ingsw.communication.message.MessageVisitable;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.controller.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketServer extends Thread implements NetworkInterfaceServer {
    private Socket socket;
    private ServerView serverView;
    private ObjectOutputStream out;

    public SocketServer(Socket socket, Controller controller){
        this.socket = socket;
        serverView = new ServerView(this, controller);
    }

    @Override
    public void run(){
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            final ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            while(true){
                serverView.receiveEvent((EventVisitable) in.readObject());
            }
        } catch(final IOException e){
            serverView.disconnectPlayer(serverView);
            //todo passare al prossimo player ed eventualmente annullare l'azione(se match è iniziato)
        } catch(final ClassNotFoundException e){
            System.err.println(e.getMessage());
        }
    }

    public void forwardMessage(MessageVisitable messageVisitable) throws IOException{
        //ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(messageVisitable);
        out.flush();
    }

   public Socket getSocket(){
        return socket;
   }

   public void closeNetwork() throws  IOException{
            this.socket.close();
   }

}
