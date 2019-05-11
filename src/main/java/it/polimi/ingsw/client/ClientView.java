package it.polimi.ingsw.client;

import it.polimi.ingsw.server.events.LoginEvent;
import it.polimi.ingsw.server.message.LoginMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public interface ClientView{

    void printLoginMessage(String text);
    void printDisconnectionMessage(String text);

    void askLogin();
}
