package it.polimi.ingsw.client;



import com.google.gson.Gson;
import com.google.gson.JsonParser;


import it.polimi.ingsw.client.network.NetworkInterfaceClient;
import it.polimi.ingsw.client.user_interface.cli.Cli;
import it.polimi.ingsw.client.user_interface.gui.Gui;
import it.polimi.ingsw.communication.CommonProperties;
import it.polimi.ingsw.ArgumentNavigator;
import javafx.application.Application;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;
import java.util.Scanner;

/**
 * Start client, reading arguments from command line or using default
 */
public class ClientMain{
    private static final String CLIENTCONFIG_PATH= "resources/clientConfig.json";
    private static final String HELP_MESSAGE_CLIENT = "-ui [gui|cli]\n" +
            "-ip [x.x.x.x]\n" +
            "-port [int from 1024 to 65534";
    private static final String GUI = "gui";
    private static final String CLI = "cli";
    private static final String ASK_IP = "Please insert ip (X.X.X.X): ";
    private static final String ASK_PORT = "Please insert port (from 1024 to 65535): ";
    private static final String ASK_UI = "Please insert user interface (cli or gui): ";

    /**
     * the configuration for the client
     */
    private static ClientConfig config;

    /**
     * Gets config
     * @return config
     */
    public static ClientConfig getConfig(){
        return config;
    }

    /**
     * Client main
     * @param args args
     * @throws IOException if reading goes wrong
     */
    public static void main(String[] args) throws IOException {
        config = new ClientConfig();

        loadFromConfigurationFile();

        try {

            RMISocketFactory.setSocketFactory(new RMISocketFactory() {
                public Socket createSocket(String host, int port )
                        throws IOException
                {
                    Socket socket = new Socket();
                    socket.setSoTimeout((int) CommonProperties.PING_PONG_DELAY*2);
                    socket.setSoLinger( false, 0 );
                    socket.connect( new InetSocketAddress( host, port ), NetworkInterfaceClient.REACHING_TIME);
                    return socket;
                }

                public ServerSocket createServerSocket(int port )
                        throws IOException
                {
                    return new ServerSocket( port );
                }
            } );
        } catch (IOException e){
            //Nothing to do
        }

        if(args.length > 0) {
            if (args[0].equals("-h") || args[0].equals("--help")) {
                printHelpScreen();
                return;
            } else {
                getFromCmdArguments(args);
            }
        }

        askUserInput();

        if(config.getUserInterface().equals("cli")){
            new Cli();
        } else {
            Application.launch(Gui.class);
        }
    }

    /**
     * ask input from user
     */
    private static void askUserInput(){
        while(! isValidIp(config.getIp())){
            System.out.println(ASK_IP);
            config.setIp(new Scanner(System.in).nextLine());
        }
        while(! isValidPort( config.getPort())){
            System.out.println(ASK_PORT);
            config.setPort(new Scanner(System.in).nextInt());
        }
        while(! isValidInterface(config.getUserInterface() )){
            System.out.println(ASK_UI);
            config.setUserInterface(new Scanner(System.in).nextLine());
        }
    }

    /**
     * load configuration parameters from configuration file (defaults)
     */

    private static void loadFromConfigurationFile(){
        Gson gson= new Gson();
        JsonParser jp= new JsonParser();

        InputStream url= new ClientMain().getClass().getClassLoader().getResourceAsStream(CLIENTCONFIG_PATH);

        if(url==null){
            return;
        }
        String json;
        Scanner sc = new Scanner(url);

        json = sc.nextLine();
        sc.close();
        config= gson.fromJson(json, ClientConfig.class);


    }

    /**
     * load configuration parameters from cmd arguments
     * @param args command line arguments
     */
    private static void getFromCmdArguments(String[] args){
        ArgumentNavigator argNav= new ArgumentNavigator(args, "-");
        config.setIp(argNav.getFieldAsStringorDefault("ip", config.getIp()));
        try {
            config.setPort(argNav.getFieldAsIntOrDefault("port", config.port));
        }
        catch (NumberFormatException e){
            printHelpScreen();
        }
        config.setUserInterface(argNav.getFieldAsStringorDefault("ui", config.userInterface));
    }


    /**
     *
     * @param userInterface user interface choice
     * @return true if it's valid
     */
    private static boolean isValidInterface(String userInterface) {
        if(userInterface.equals(GUI) || userInterface.equals(CLI) ){
            return true;
        }
        return false;
    }


    /**
     *
     * @param port port choice
     * @return true if it's valid
     */
    private static boolean isValidPort(int port) {
        if(port>1023 && port<65535) {
            return true;
        }
        return false;
    }

    /**
     * print the help dialog in the command line
     */
    public static void printHelpScreen(){
        System.out.println(HELP_MESSAGE_CLIENT);
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


    /**
     * Represents client configuration
     */
    public static class ClientConfig{
        /**
         * Ip to connect to
         */
        private String ip;
        /**
         * port to connect to
         */
        private int port;
        /**
         * Chosen interface; cli or gui
         */
        private String userInterface;

        /**
         * Creates ClientConfig with useless value
         */
        public ClientConfig() {
            ip= "non-initialized";
            port= -1;
            userInterface= "non-initialized";
        }

        /**
         * Sets ip
         * @param ip chosen ip
         */
        public void setIp(String ip) {
            this.ip = ip;
        }

        /**
         * Sets port
         * @param port chosen port
         */
        public void setPort(int port) {
            this.port = port;
        }


        /**
         * Sets userInterface
         * @param userInterface chosen user interface
         */
        public void setUserInterface(String userInterface) {
            this.userInterface = userInterface;
        }

        /**
         * Gets ip
         * @return ip
         */
        public String getIp() {
            return ip;
        }

        /**
         * Gets port
         * @return port
         */
        public int getPort() {
            return port;
        }


        /**
         * Gets userInterface
         * @return userInterface
         */
        public String getUserInterface() {
            return userInterface;
        }

    }

}

