package it.polimi.ingsw.server;

import it.polimi.ingsw.GameModel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        while(true){
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
        System.out.println("Insert port number");
        Scanner in = new Scanner(System.in);
        new ServerMain(in.nextInt());
    }
}
