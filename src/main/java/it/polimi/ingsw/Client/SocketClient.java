package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.Events.EventVisitable;
import it.polimi.ingsw.Server.Message.LoginMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient extends Thread{
    private final Socket socket;
    private ClientView clientView;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public SocketClient(Socket socket){
        this.socket = socket;
        this.clientView = new ClientView(this);
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        } catch(IOException e){
            System.out.println("Error while creating streams!");
            e.printStackTrace();
        }
        run();
    }

    @Override
    public void run(){
        try{
            while(true){
                clientView.login();
                LoginMessage message = (LoginMessage) in.readObject();
                System.out.println(message.getString());
                if(message.getLoginSuccessful()){
                    break;
                }
                if(message.getCloseSocket()){
                    in.close();
                    out.close();
                    socket.close();
                    break;
                }
            }
        } catch(IOException e){
            System.out.println("Error while receiving the login message!");
        } catch(ClassNotFoundException e){
            System.out.println("Class LoginMessage not found!");
        }
    }

    public void forward(EventVisitable event) throws IOException{
        out.writeObject(event);
        out.flush();
    }
}
