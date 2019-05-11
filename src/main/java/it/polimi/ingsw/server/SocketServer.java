package it.polimi.ingsw.server;

import it.polimi.ingsw.server.events.EventVisitable;
import it.polimi.ingsw.server.message.MessageVisitable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketServer extends Thread implements NetworkInterfaceServer{
    private Socket socket;
    private ServerView serverView;
    private ObjectOutputStream out;

    public SocketServer(Socket socket, Controller controller){
        this.socket = socket;
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch(IOException e){
            System.out.println("Error while creating output stream!");
            e.printStackTrace();
        }
        serverView = new ServerView(this, controller);
    }

    @Override
    public void run(){
        try{
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            while(true){
                serverView.receiveEvent((EventVisitable) in.readObject());
            }
        } catch(IOException e){
            serverView.playerDisconnetted(serverView);
            //todo passare al prossimo player ed eventualmente annullare l'azione(se match è iniziato)
        } catch(ClassNotFoundException e){
            System.out.println("Class not found!");
            e.printStackTrace();
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
