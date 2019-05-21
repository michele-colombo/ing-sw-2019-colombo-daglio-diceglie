package it.polimi.ingsw.client.userInterface;

public interface ClientView{

    void printLoginMessage(String text, boolean loginSuccessful);
    void printDisconnectionMessage(String text);

    //void askLogin();
}
