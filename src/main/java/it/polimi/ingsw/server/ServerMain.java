package it.polimi.ingsw.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.network.RmiServerAcceptor;
import it.polimi.ingsw.server.network.SocketServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    //SERVE PER FAR PARTIRE IL SERVER
    private int port;
    private final GameModel gameModel;
    private final Controller controller;

    public ServerMain(int port, long loginTimerDuration, long inputTimerDuration){
        this.port = port;
        gameModel = new GameModel();
        controller = new Controller(gameModel);
        controller.setLoginTimerDuration(loginTimerDuration);
        controller.setInputTimerDuration(inputTimerDuration);
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

        try {

            int rmiPort= port + 1;

            RmiServerAcceptor acceptor = new RmiServerAcceptor(controller);
            Registry registry = LocateRegistry.createRegistry(rmiPort);

            registry.bind("Acceptor", acceptor);
        }
        catch (RemoteException re){
            //impossible to create RmiServerAcceptor
            System.out.println("impossible to create RmiServerAcceptor");
            re.printStackTrace();
        }
        catch (AlreadyBoundException E){
            System.out.println("Already Bound item!");
        }



        System.out.println("Server ready!");
        while(true){  //sara' finche'  i giocatori sono meno di cinque o piu' di tre ed e' scattato il timer
            try{
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                SocketServer socketServer = new SocketServer(socket, controller);
                executor.submit(socketServer);
                //todo: viene chiamato un metodo createServerView(NetworkInterfcaeServer socketServer)
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
        long loginTimerDuration;
        long inputTimerDuration;
        try {
            data = o.get("loginTimer");
            loginTimerDuration = data.getAsLong();
        } catch (Exception e){
            loginTimerDuration = -1;
        }
        try {
            data = o.get("inputTimer");
            inputTimerDuration = data.getAsLong();
        } catch (Exception e){
            inputTimerDuration = -1;
        }

        new ServerMain(portNumber, loginTimerDuration, inputTimerDuration);
    }
}
