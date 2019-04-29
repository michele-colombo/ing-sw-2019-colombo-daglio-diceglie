package it.polimi.ingsw.Server;

import it.polimi.ingsw.GameModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    //SERVE PER FAR PARTIRE IL SERVER
    private final int port;
    private final GameModel gameModel;

    public ServerMain(int port){
        this.port = port;
        gameModel = new GameModel();
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
                executor.submit(new SocketServer(socket, gameModel));
                //CREARE IL THREAD
            } catch (IOException e){
                break;
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args){
        ServerMain server = new ServerMain(12000);
    }
}
