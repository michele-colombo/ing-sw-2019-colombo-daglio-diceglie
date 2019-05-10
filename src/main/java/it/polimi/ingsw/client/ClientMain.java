package it.polimi.ingsw.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientMain {


    public static void main(String[] args) throws IOException {
        boolean done= false;

        while(!done) {
            System.out.println("Please, insert ip, port, connection technology (socket) and user interface(cli or gui):");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Scanner intReader = new Scanner(System.in);

            String ip = reader.readLine();
            int port = intReader.nextInt();
            String connection = reader.readLine();
            String userInterface = reader.readLine();

            connection = connection.toLowerCase();
            userInterface = userInterface.toLowerCase();

            if (isValidIp(ip) && isValidPort(port) && isValidConnection(connection) && isValidInterface(userInterface)) {
                try {
                    Client client = new Client(ip, port, connection, userInterface);
                    client.startClient();
                    done = true;
                }
                catch (IOException e){
                    System.out.println("Error! Server non raggiungibile");
                    return;
                }
            }

        }
    }


    private static boolean isValidInterface(String userInterface) {
        if(userInterface.equals( "gui" ) || userInterface.equals("cli") ){
            return true;
        }
        return false;
    }


    private static boolean isValidConnection(String connection) {
        if(connection.equals("socket") || connection.equals("rmi")) {
            return true;
        }
        return false;
    }

    private static boolean isValidPort(int port) {
        if(port>1023 && port<65536) {
            return true;
        }
        return false;
    }

    public static void printHelpScreen(){
        System.out.println("Qui ci metto l'aiuto");
    }

    public static boolean isValidIp(String ip){
        String[] splitted= ip.split("\\.");
        if(splitted.length != 4) return false;

        for(int i=0; i<splitted.length; i++){
            if(! splitted[i].matches("[0-9]*")){
                return false;
            }
        }

        return true;
    }

}
