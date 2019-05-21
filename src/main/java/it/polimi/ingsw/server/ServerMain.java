package it.polimi.ingsw.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.network.SocketServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    //SERVE PER FAR PARTIRE IL SERVER
    private int port;
    private final GameModel gameModel;
    private final Controller controller;

    public ServerMain(int port){
        this.port = port;
        gameModel = new GameModel();
        controller = new Controller(gameModel);
        start();
    }

    public void start(){
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
        } catch(IOException e){
            System.out.println("Error while initializing the server");
            e.printStackTrace();
        }
        System.out.println("Server ready!");
        while(true){  //sara' finche'  i giocatori sono meno di cinque o piu' di tre ed e' scattato il timer
            try{
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                executor.submit(new SocketServer(socket, controller));
                //CREARE IL THREAD
            } catch (IOException e){
                break;
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args){

        InputStream url= ServerMain.class.getClassLoader().getResourceAsStream("serverConfig.json");
        Scanner sc= new Scanner(url);

        //getting port number
        JsonObject o= (JsonObject) new JsonParser().parse(sc.nextLine());
        JsonElement data=  o.get("port");
        int portNumber= data.getAsInt();

        new ServerMain(portNumber);
    }
}
