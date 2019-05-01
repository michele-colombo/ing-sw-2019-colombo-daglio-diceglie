package it.polimi.ingsw.client;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.server.events.LoginEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientView {
    private final SocketClient socket;

    public ClientView(SocketClient socket){
        this.socket = socket;
    }

    public void login(){
        System.out.println("Please, insert your name and color");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try{
            String name = reader.readLine();
            PlayerColor color = PlayerColor.valueOf(reader.readLine().toUpperCase());
            try{
                LoginEvent loginEvent = new LoginEvent(name, color, false);
                socket.forward(loginEvent);
            } catch(IOException e){
                System.out.println("Error while forwarding the login event!");
                e.printStackTrace();
            }
        } catch(IOException e){
            System.out.println("Error while reading from user!");
        }
    }
}
