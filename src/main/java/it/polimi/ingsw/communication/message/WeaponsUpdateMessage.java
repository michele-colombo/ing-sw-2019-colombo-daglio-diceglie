package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.client.VisitorClient;

import java.util.List;

public class WeaponsUpdateMessage extends MessageVisitable {
    private String name;
    private List<String> loadedWeapons;
    private List<String> unloadedWeapons;
    private int numeWeapons;

    @Override
    public void accept(VisitorClient visitorClient) {
        visitorClient.visit(this);
    }
 }
