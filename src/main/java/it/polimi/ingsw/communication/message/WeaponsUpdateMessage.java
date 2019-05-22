package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.util.ArrayList;
import java.util.List;

public class WeaponsUpdateMessage extends MessageVisitable {
    private String name;
    private List<String> loadedWeapons;
    private List<String> unloadedWeapons;
    private int numeWeapons;

    public WeaponsUpdateMessage(String name, List<String> loadedWeapons, List<String> unloadedWeapons, int numeWeapons) {
        this.name = name;
        this.loadedWeapons = loadedWeapons;
        this.unloadedWeapons = unloadedWeapons;
        this.numeWeapons = numeWeapons;
    }

    public WeaponsUpdateMessage(String name, List<String> unloadedWeapons, int numeWeapons) {
        this.name = name;
        this.unloadedWeapons = unloadedWeapons;
        this.numeWeapons = numeWeapons;
        this.loadedWeapons = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getLoadedWeapons() {
        return loadedWeapons;
    }

    public List<String> getUnloadedWeapons() {
        return unloadedWeapons;
    }

    public int getNumeWeapons() {
        return numeWeapons;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
 }
