package it.polimi.ingsw.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cli implements ClientView {
    Client client;

    public Cli(Client client){
        this.client= client;
    }

    public void printLoginMessage(String text){
        System.out.println(text);
    }
    public void printDisconnectionMessage(String text){
        System.out.println(text);
    }

    @Override
    public void askLogin() {
        System.out.println("Inserisci il tuo nome:");
        try{
            String name= readStringFromUser();
            client.setName(name);
        }
        catch (IOException e){
            System.out.println("Something went wrong IOException");
        }

    }


    public String readStringFromUser() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine().toUpperCase();
    }
}
