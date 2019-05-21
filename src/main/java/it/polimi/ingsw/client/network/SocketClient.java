package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.communication.events.EventVisitable;
import it.polimi.ingsw.communication.message.MessageVisitable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketClient extends Thread implements NetworkInterfaceClient {
    private final Socket socket;
    private Client client;

    private ObjectOutputStream out;

    public SocketClient(Socket socket, Client client){
        this.socket = socket;
        this.client= client;
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch(IOException e){
            System.out.println("Error while creating stream!");
            e.printStackTrace();
        }
    }

    public SocketClient(String ip, int port, Client client) throws IOException{
        try{
            SocketAddress address= new InetSocketAddress(ip, port);
            Socket sock= new Socket();
            this.socket= sock;
            this.socket.connect(address, 10000);
            this.client= client;

            out= new ObjectOutputStream(sock.getOutputStream());
            out.flush();
        }
        catch (IOException e){
            throw new IOException();
        }
    }

    public void startNetwork(){
        this.start();
    }

    public void run() {
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                while(true) {
                    MessageVisitable message = (MessageVisitable) in.readObject();
                    message.accept(client);
                }
            } catch (IOException e) {
                System.out.println("Error while receiving messages!");
                // qui mi sa che e' meglio spegnere tutto
            } catch (ClassNotFoundException e) {
                System.out.println("Class //LoginMessage// not found!");
            }
    }




    public void forward(EventVisitable event) throws IOException{
        out.writeObject(event);
        out.flush();
    }
}
