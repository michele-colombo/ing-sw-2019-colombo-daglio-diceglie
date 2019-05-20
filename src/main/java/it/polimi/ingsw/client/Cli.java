package it.polimi.ingsw.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Cli implements ClientView {
    private Client client;

    public Cli(){
        this.client = null;
        askLogin();
    }

    public void printLoginMessage(String text, boolean loginSuccessful){
        System.out.println(text);
        if(!loginSuccessful){
            System.out.println("Insert a new name: ");
            try{
                client.chooseName(readStringFromUser());
            } catch(IOException e){
                System.out.println("Error while reading input from user!");
            }
        }
    }

    public void printDisconnectionMessage(String text){
        System.out.println(text);
    }


    public void askLogin() {

        System.out.println("Choose: socket or rmi?");
        boolean ok = false;

        String choice = "";
        String name = "";

        while(!ok){
            choice  = new Scanner(System.in).nextLine();

            if(choice.equalsIgnoreCase("socket") || choice.equalsIgnoreCase("rmi")){
                ok = true;
            }
            else{
                System.out.println("Illegal answer. Please insert only socket or rmi");
            }
        }



        System.out.println("Insert your name");
        try{
            name = readStringFromUser();
        }
        catch (IOException e){
            System.out.println("Something went wrong IOException");
        }

        this.client = new Client(this);
        client.login(choice, name);
    }


    private String readStringFromUser() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
}
