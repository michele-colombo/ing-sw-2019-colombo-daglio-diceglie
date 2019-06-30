package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.CommonProperties;
import it.polimi.ingsw.server.controller.ParserManager;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    //SERVE PER FAR PARTIRE IL SERVER
    private String acceptedIp;
    private int port;
    private final GameModel gameModel;
    private final Controller controller;

    /**
     * build the serverMain class with its parameter
     * @param acceptedIp  the ip tha RMIServer can show outside
     * @param port the socket port of the server (RMI port = socketPort + 1)
     * @param loginTimerDuration the maxmum time to login before match starts
     * @param inputTimerDuration maximum time a player can wait to chose what to do
     */
    public ServerMain(String acceptedIp, int port, int loginTimerDuration, int inputTimerDuration){
        this.acceptedIp= acceptedIp;
        this.port = port;
        gameModel = new GameModel();
        controller = new Controller(gameModel);
        controller.setLoginTimerDuration(loginTimerDuration);
        controller.setInputTimerDuration(inputTimerDuration);
        start();
    }

    /**
     * starts the server
     */
    public void start(){
        Thread socketListener= new Thread(()-> {
            try(ServerSocket serverSocket= new ServerSocket(port)){
                while(true){
                    Socket socket = serverSocket.accept();
                    socket.setSoTimeout((int) CommonProperties.PING_PONG_DELAY*2);

                    System.out.println("A new client has connected");
                    new SocketServer(socket, controller);
                }
            }
            catch (IOException e){
                System.out.println("Error while initializing the server or accepting sockets");
            }
        });
        socketListener.start();

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
    }

    public static void main(String[] args){
        String acceptedIp;
        if(args.length == 0){
            acceptedIp= "127.0.0.1";
        }
        else{
            acceptedIp= args[0];
        }

        //getting port number
        try {
            ParserManager pm= new ParserManager();
            int port= pm.getPortConfig();
            int loginTimer= pm.getLoginTimerConfig();
            int inputTimer= pm.getInputTimerConfig();


            new ServerMain(acceptedIp, port, loginTimer, inputTimer);
        }
        catch (NullPointerException e){
            System.out.println("Configuration file not found");
        }
    }
}
