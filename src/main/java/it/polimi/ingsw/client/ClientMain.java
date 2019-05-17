package it.polimi.ingsw.client;



import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.Scanner;

import static javafx.application.Application.launch;

public class ClientMain{

    static ClientConfig config;




    public static void main(String[] args) throws IOException {
        config= new ClientConfig();

        if(args.length==0) {
            loadFromConfigurationFile();
        }
        else {
            if (args[0] == "-h" || args[0] == "--help") {
                printHelpScreen();
                return;
            }
            else {
                getFromCmdArguments(args);
            }
        }

        testGUI.main(null);

        askUserInput();



        try {
            Client client = new Client(config.getIp(), config.getPort(), config.getLink(), config.getUserInterface());
            client.startClient();
        }
        catch (IOException e){
            System.out.println("Error! Server non raggiungibile");
            return;
        }
    }

    private static void askUserInput(){
        while(! isValidIp(config.getIp())){
            System.out.println("Please insert ip (X.X.X.X): ");
            config.setIp(new Scanner(System.in).nextLine());
        }
        while(! isValidPort( config.getPort())){
            System.out.println("Please insert port (from 1024 to 65535): ");
            config.setPort(new Scanner(System.in).nextInt());
        }
        while(! isValidConnection(config.getLink())){
            System.out.println("Please insert link type (socket or rmi): ");
            config.setLink(new Scanner(System.in).nextLine());
        }
        while(! isValidInterface(config.getUserInterface() )){
            System.out.println("Please insert user interface (cli or gui): ");
            config.setUserInterface(new Scanner(System.in).nextLine());
        }
    }

    private static void loadFromConfigurationFile(){
        Gson gson= new Gson();
        JsonParser jp= new JsonParser();

        InputStream url= new ClientMain().getClass().getClassLoader().getResourceAsStream("clientConfig.json");

        if(url==null){
            System.out.println("No configuration file!");
            return;
        }
        String json;
        Scanner sc = new Scanner(url);

        json = sc.nextLine();
        sc.close();
        config= gson.fromJson(json, ClientConfig.class);


    }

    private static void getFromCmdArguments(String[] args){
        for(int i=0; i<args.length; i++){
            String argument= args[i];
            String nextArgument;
            if(i< args.length - 1){
                nextArgument= args[i+1];
            }
            else{
                nextArgument= "";
            }

            switch(argument){
                case "-ip":

                    config.setIp(nextArgument);

                    break;
                case "-port":

                    config.setPort(Integer.parseInt(nextArgument));

                    break;
                case "-link":

                    config.setLink(nextArgument);

                    break;
                case "-ui":

                    config.setUserInterface(nextArgument);

                    break;
                case "-h":
                    printHelpScreen();
                    return;

                default:
                    break;

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


    private static class ClientConfig{
        private String ip;
        private int port;
        private String link;
        private String userInterface;

        public ClientConfig() {
            ip= "non-initialized";
            port= -1;
            link="non-initialized";
            userInterface= "non-initialized";
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setUserInterface(String userInterface) {
            this.userInterface = userInterface;
        }

        public String getIp() {
            return ip;
        }

        public int getPort() {
            return port;
        }

        public String getLink() {
            return link;
        }

        public String getUserInterface() {
            return userInterface;
        }

    }

}

