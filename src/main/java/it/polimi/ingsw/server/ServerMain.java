package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.ArgumentNavigator;
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
import java.util.Scanner;
import java.util.logging.Logger;

public class ServerMain {

    private static final String SERVER_READY = "Server ready";
    private static final String ERROR_INIT_SERVER_SOCKET = "Error while initializing the server";
    private static final String ERROR_INIT_SERVER_RMI = "impossible to create RmiServerAcceptor";
    private static final String ALREADY_BOUND_ITEM = "Already Bound item!";
    private static final String SOCKET_CLIENT_HAS_CONNECTED = "A new socket client has connected";
    private static final String SERVER_CONFIG_NOT_FOUND = "Configuration file not found";

    private static final String FILE_NOT_FOUND = "default configuration file not found";
    private static final String PORT = "port";
    private static final String LAYOUT = "layout";
    private static final String SKULLS = "skulls";
    private static final String LOGIN = "login";
    private static final String INPUT = "input";
    private static final String IP = "ip";
    private static final String HELPMESSAGE = "usage:\n" +
            "-" + IP + " XXX.XXX.XXX.XXX \n" +
            "-" + PORT + " from 1024 to 65534 \n" +
            "-" + SKULLS + " from 5 to 8 \n" +
            "-" + LAYOUT + " from -1 to 3\n" +
            "-" + INPUT + " input timer duration in milliseconds\n" +
            "-" + LOGIN + " login timer duaration in milliseconds\n";
    private static final String CONFIG_LOCATION = "resources/serverConfig.json";
    private static final String NOT_FOUND_STRING = "notFound";
    private static final String ACCEPTOR = "Acceptor";
    private static final String JAVA_RMI_SERVER_HOSTNAME = "java.rmi.server.hostname";
    //SERVE PER FAR PARTIRE IL SERVER

    private static Controller controller;
    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());

    private static int loginTimerDuration;
    private static int inputTimerDuration;
    private static int port;
    private static String acceptedIp;
    private static int skullsNumber;
    private static int layoutConfig;

    private static RmiServerAcceptor rmiServerAcceptor;

    /**
     * build the serverMain class with its parameter
     * @param acceptedIp  the ip tha RMIServer can show outside
     * @param port the socket port of the server (RMI port = socketPort + 1)
     * @param loginTimerDuration the maxmum time to login before match starts
     * @param inputTimerDuration maximum time a player can wait to chose what to do
     */
    public ServerMain(String acceptedIp, int port, int loginTimerDuration, int inputTimerDuration){
        this.loginTimerDuration= loginTimerDuration;
        this.inputTimerDuration= inputTimerDuration;

        this.acceptedIp= acceptedIp;
        this.port = port;
        GameModel gameModel = new GameModel();
        controller = new Controller(gameModel);
        controller.setLoginTimerDuration(loginTimerDuration);
        controller.setInputTimerDuration(inputTimerDuration);
        start();
    }

    public ServerMain() {

        GameModel gameModel = new GameModel(layoutConfig, skullsNumber);
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

                    logger.info(SOCKET_CLIENT_HAS_CONNECTED);
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
            System.setProperty(JAVA_RMI_SERVER_HOSTNAME, acceptedIp);
            rmiServerAcceptor = new RmiServerAcceptor(controller);
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            registry.bind(ACCEPTOR, rmiServerAcceptor);
        }
        catch (RemoteException re){
            //impossible to create RmiServerAcceptor
            logger.severe(ERROR_INIT_SERVER_RMI);
        }
        catch (AlreadyBoundException E){
            logger.warning(ALREADY_BOUND_ITEM);
        }

        logger.info(SERVER_READY);
    }

    public static void restart(){
        GameModel gameModel = new GameModel(layoutConfig, skullsNumber);
        controller = new Controller(gameModel);
        controller.setLoginTimerDuration(loginTimerDuration);
        controller.setInputTimerDuration(inputTimerDuration);
        rmiServerAcceptor.setController(controller);
    }

    public static void main(String[] args){

        for(String s: args){
            if(s.equals("-h")){
                printHelp(-1);
            }
        }

        try(Scanner sc= new Scanner(ServerMain.class.getClassLoader().getResourceAsStream(CONFIG_LOCATION))){
            Gson gson= new Gson();
            ServerConfig config= gson.fromJson(sc.nextLine(), ServerConfig.class);
            port= config.port;
            skullsNumber= config.skullNumber;
            inputTimerDuration= config.inputTimer;
            loginTimerDuration= config.loginTimer;
            layoutConfig= config.layoutConfig;
            acceptedIp= config.ip;
        }

        ArgumentNavigator argNavigator= new ArgumentNavigator(args, "-");

        try {
            int temp = argNavigator.getFieldAsIntOrDefault(PORT, -1);
            if (temp >= 1024 && temp <= 65534) {
                port = temp;
            } else if (temp != -1) {
                printHelp(0);
            }

            temp = argNavigator.getFieldAsIntOrDefault(LAYOUT, -2);
            if (temp >= -1 && temp <= 3) {
                layoutConfig = temp;
            } else if (temp != -2) {
                printHelp(0);
            }

            temp = argNavigator.getFieldAsIntOrDefault(SKULLS, -1);
            if (temp >= 5 && temp <= 8) {
                skullsNumber = temp;
            } else if (temp != -1) {
                printHelp(0);
            }

            temp = argNavigator.getFieldAsIntOrDefault(INPUT, -1);
            if (temp > 0) {
                inputTimerDuration = temp;
            } else if (temp != -1) {
                printHelp(0);
            }

            temp = argNavigator.getFieldAsIntOrDefault(LOGIN, -1);
            if (temp > 0) {
                loginTimerDuration = temp;
            } else if (temp != -1) {
                printHelp(0);
            }

            String tempString = argNavigator.getFieldAsStringorDefault(IP, "null");
            if (isValidIp(tempString)) {
                acceptedIp = tempString;
            }
        }
        catch (NumberFormatException e){
            printHelp(1);
        }

        new ServerMain();
    }

    private static void  printHelp(int type){
        String prefix;
        switch (type){
            case 0: prefix= "Value out of bound\n"; break;
            case 1: prefix= "Wrong value format\n"; break;
            default: prefix= "";
        }
        System.out.println(prefix + HELPMESSAGE);
        System.exit(0);
    }

    /**
     *
     * @param ip ip choice
     * @return true if it's valid
     */
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


    private static class ServerConfig{
        private String ip;
        private int port;
        private int loginTimer;
        private int inputTimer;
        private int skullNumber;
        private int layoutConfig;
    }
}
