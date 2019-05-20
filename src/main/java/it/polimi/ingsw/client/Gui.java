package it.polimi.ingsw.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Gui implements ClientView {
    Client client;

    public Gui(Client client){
        this.client= client;
    }

    public void printLoginMessage(String text){
        System.out.println("[GUI]" + text);
    }
    public void printDisconnectionMessage(String text){
        System.out.println("[GUI]" + text);
    }

    @Override
    public void askLogin() {

        System.out.println("[GUI]Vuoi utilizzare connessione socket o rmi?");
        Boolean ok= false;

        String choice= "";
        String name= "";

        while(!ok){
            choice= new Scanner(System.in).nextLine();

            if(choice.equalsIgnoreCase("socket") || choice.equalsIgnoreCase("rmi")){
                ok= true;
            }
            else{
                System.out.println("[GUI]Illegal answer. Please insert only socket or rmi");
            }
        }


        System.out.println("[GUI]Inserisci il tuo nome:");
        try{
            name= readStringFromUser();
        }
        catch (IOException e){
            System.out.println("[GUI]Something went wrong IOException");
        }

        client.login(choice, name);

    }


    public String readStringFromUser() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine().toUpperCase();
    }
}
