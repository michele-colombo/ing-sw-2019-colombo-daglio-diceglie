package it.polimi.ingsw.Server;

import it.polimi.ingsw.GameModel;
import it.polimi.ingsw.Server.Events.EventVisitable;
import it.polimi.ingsw.Server.Message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketServer implements Runnable{
    private Socket socket;
    private ServerView serverView;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public SocketServer(Socket socket, GameModel gameModel){
        this.socket = socket;
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        } catch(IOException e){
            System.out.println("Error while creating streams!");
            e.printStackTrace();
        }
        Controller controller = new Controller(gameModel);
        serverView = new ServerView(this, controller);
        controller.addServerView(serverView);
    }

    @Override
    public void run(){
        try{
            while(true){
                Message message = serverView.receiveEvent((EventVisitable) in.readObject());
                out.writeObject(message);
                if(message.getCloseSocket()){
                    in.close();
                    out.close();
                    socket.close();
                    break;
                }
            }
        } catch(IOException e){
            System.out.println("Error while receiving/forwarding messages");
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            System.out.println("Class not found!");
            e.printStackTrace();
        }
    }
}
