package it.polimi.ingsw.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    private final String ip;
    private final int port;

    public ClientMain(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void startClient() throws IOException{
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        new SocketClient(socket);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner intReader = new Scanner(System.in);

        String ip = reader.readLine();
        int port = intReader.nextInt();

        ClientMain client = new ClientMain(ip, port);
        client.startClient();
    }
}
