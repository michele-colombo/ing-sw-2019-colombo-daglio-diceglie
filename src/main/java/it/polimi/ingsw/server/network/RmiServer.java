package it.polimi.ingsw.server.network;

import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.communication.message.MessageVisitable;

import java.io.IOException;
import java.rmi.RemoteException;

public class RmiServer implements NetworkInterfaceServer{
    ClientInterface client;

    public RmiServer(ClientInterface client){
        this.client = client;
    }

    @Override
    public void forwardMessage(MessageVisitable messaggio) throws IOException{
        try {
            messaggio.accept(client);
        }
        catch (RemoteException e){
            throw new IOException();
        }
    }

    @Override
    public void closeNetwork() throws IOException {

    }
}
