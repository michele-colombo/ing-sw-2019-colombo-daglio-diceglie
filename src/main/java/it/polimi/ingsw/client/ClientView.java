package it.polimi.ingsw.client;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.server.events.LoginEvent;
import it.polimi.ingsw.server.message.LoginMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientView implements VisitorClient{
    private final SocketClient socket;

    public ClientView(SocketClient socket){
        this.socket = socket;
    }

    public void startLogin(){
        boolean stopLogin = false;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try{
            while(!stopLogin){
                System.out.println("Do you want to log or re-log?");
                String input = reader.readLine().toUpperCase();
                if(input.equals("LOG")){
                    stopLogin = true;
                    login();
                } else if(input.equals("RE-LOG")){
                    stopLogin = true;
                    reLogin();
                }
            }
        } catch(IOException e){
            System.out.println("Error while asking whether to log or re-log!");
        }
    }

    public void login(){
        System.out.println("Please, insert your name and color");
        try{
            String name = readStringFromUser();
            String selectedColor = readStringFromUser();
            try{
                PlayerColor color = PlayerColor.valueOf(selectedColor); //SOSTITUIRE CON UN METODO FORWARD
                LoginEvent loginEvent = new LoginEvent(name, color);
                socket.forward(loginEvent);
            } catch(IOException e){
                System.out.println("Error while forwarding the login event!");
                e.printStackTrace();
            } catch(IllegalArgumentException e){
                System.out.println(selectedColor + " isn't a valid color!");
                login();
            }
        } catch(IOException e){
            System.out.println("Error while reading from user!");
        }
    }

    public void reLogin(){
        ;
    }

    public void visit(LoginMessage loginMessage){
        System.out.println(loginMessage.toString());
    }

    public String readStringFromUser() throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine().toUpperCase();
    }
}
