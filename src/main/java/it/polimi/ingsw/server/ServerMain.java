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
import java.util.logging.Logger;

public class ServerMain {
    public static final String SAVING_BACKUP = "Saving backup to file";
    public static final String CANNOT_WRITE_BACKUP_FILE = "Cannot write backup file";
    public static final String BACKUP_FILE_WRITTEN = "backup file written";
    public static final String UNSUPPORTED_ENCODING_PARSING_BACKUP = "Unsupported encoding exception while parsing currBackup";
    public static final String IO_EXCEPTION_FILE = "File not found or error while closing stream";
    public static final String BACKUP_SYNTAX_ERROR = "Backup file is not correctly written";
    public static final String PROBLEM_CLOSING_INPUT_STREAM = "problem while closing inputStream";
    public static final String NO_FILE_DETECTED_FOR_STACKS = "no file detected for stacks";

    public static final String SERVER_READY = "Server ready";
    public static final String ERROR_INIT_SERVER_SOCKET = "Error while initializing the server";
    public static final String ERROR_INIT_SERVER_RMI = "impossible to create RmiServerAcceptor";
    public static final String ALREADY_BOUND_ITEM = "Already Bound item!";
    public static final String SOCKET_CLIENT_HAS_CONNECTED = "A new socket client has connected";
    public static final String SERVER_CONFIG_NOT_FOUND = "Configuration file not found";

    public static final String ACCEPTED_IP = "127.0.0.1";
    //SERVE PER FAR PARTIRE IL SERVER
    private String acceptedIp;
    private int port;
    private final GameModel gameModel;
    private final Controller controller;
    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());

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
                logger.severe(ERROR_INIT_SERVER_SOCKET);
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
            logger.severe(ERROR_INIT_SERVER_RMI);
            re.printStackTrace();
        }
        catch (AlreadyBoundException E){
            logger.warning(ALREADY_BOUND_ITEM);
        }

        logger.info(SERVER_READY);

        System.out.println("Server ready!");
    }

    public static void main(String[] args){
        String acceptedIp;
        if(args.length == 0){
            acceptedIp= ACCEPTED_IP;
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
            logger.warning(SERVER_CONFIG_NOT_FOUND);
        }
    }
}
