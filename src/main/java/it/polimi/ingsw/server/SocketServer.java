package it.polimi.ingsw.server;

import it.polimi.ingsw.server.events.EventVisitable;
import it.polimi.ingsw.server.message.MessageVisitable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketServer extends Thread{
    private Socket socket;
    private ServerView serverView;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public SocketServer(Socket socket, Controller controller){
        this.socket = socket;
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        } catch(IOException e){
            System.out.println("Error while creating streams!");
            e.printStackTrace();
        }
        serverView = new ServerView(this, controller);
    }

    @Override
    public void run(){
        try{
            while(true){
                serverView.receiveEvent((EventVisitable) in.readObject());
            }
        } catch(IOException e){
            System.out.println("Error while receiving/forwarding messages!");
            System.out.println("Observer removed!");
            serverView.removeGameModelObserver(serverView);
        } catch(ClassNotFoundException e){
            System.out.println("Class not found!");
            e.printStackTrace();
        }
    }

    public void forwardMessage(MessageVisitable messageVisitable) throws IOException{
        out.writeObject(messageVisitable);
        out.flush();
    }

    public void stopSocket() throws IOException{
        out.close();
        in.close();
        socket.close();
    }
}
