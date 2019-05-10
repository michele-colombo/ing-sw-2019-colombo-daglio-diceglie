package it.polimi.ingsw.client;

import it.polimi.ingsw.server.events.EventVisitable;
import it.polimi.ingsw.server.message.LoginMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient extends Thread{
    private final Socket socket;
    private ClientView clientView;
    private ObjectOutputStream out;

    public SocketClient(Socket socket){
        this.socket = socket;
        this.clientView = new ClientView(this);
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch(IOException e){
            System.out.println("Error while creating stream!");
            e.printStackTrace();
        }
        start();
    }

    @Override
    public void run(){
        try{
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            boolean loginOk = false;
            while(!loginOk){
                clientView.login();
                LoginMessage loginMessage = (LoginMessage) in.readObject();
                loginMessage.accept(clientView);
                if(loginMessage.getLoginSuccessful()){
                    loginOk = true;
                }
                if(loginMessage.getCloseSocket()){
                    in.close();
                    out.close();
                    socket.close();
                    loginOk = true;
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
