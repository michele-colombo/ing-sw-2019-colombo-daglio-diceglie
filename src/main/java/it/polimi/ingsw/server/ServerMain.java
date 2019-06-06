package it.polimi.ingsw.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.communication.CommonProperties;
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
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    //SERVE PER FAR PARTIRE IL SERVER
    private String acceptedIp;
    private int port;
    private final GameModel gameModel;
    private final Controller controller;

    public ServerMain(String acceptedIp, int port, int loginTimerDuration, int inputTimerDuration){
        this.acceptedIp= acceptedIp;
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


            System.setProperty("java.rmi.server.hostname", acceptedIp);



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

                socket.setSoTimeout((int) CommonProperties.PING_PONG_DELAY*2);

                System.out.println("A new client has connected");
                SocketServer socketServer = new SocketServer(socket, controller);
                //executor.submit(socketServer);
                //todo: viene chiamato un metodo createServerView(NetworkInterfaceServer socketServer)
                //CREARE IL THREAD
            } catch (IOException e){
                break;
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args){
        String acceptedIp;
        if(args.length == 0){
            acceptedIp= "127.0.0.1";
        }
        else{
            acceptedIp= args[0];
        }


        InputStream url= ServerMain.class.getClassLoader().getResourceAsStream("serverConfig.json");
        Scanner sc= new Scanner(url);

        //getting port number
        try {
            JsonObject o = (JsonObject) new JsonParser().parse(sc.nextLine());
            JsonElement data = o.get("port");
            int portNumber = data.getAsInt();
            int loginTimerDuration;
            int inputTimerDuration;
            try {
                data = o.get("loginTimer");
                loginTimerDuration = data.getAsInt();
            } catch (Exception e) {
                loginTimerDuration = -1;
            }
            try {
                data = o.get("inputTimer");
                inputTimerDuration = data.getAsInt();
            } catch (Exception e) {
                inputTimerDuration = -1;
            }


            new ServerMain(acceptedIp, portNumber, loginTimerDuration, inputTimerDuration);
        }
        catch (NullPointerException e){
            System.out.println("Configuration file not found");
        }
    }

    public static <T> String listToString(List<T> list){
        StringBuilder result = new StringBuilder();
        for (T t : list){
            if (t == null){
                result.append("    null;");
            } else {
                result.append("    "+t.toString()+";");
            }
        }
        return result.toString();
    }
}
