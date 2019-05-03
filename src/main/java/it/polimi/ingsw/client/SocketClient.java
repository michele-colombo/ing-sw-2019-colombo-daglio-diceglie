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
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean loginOk;

    public SocketClient(Socket socket){
        this.socket = socket;
        this.loginOk = false;
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
            while(!loginOk){
                clientView.startLogin();
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
