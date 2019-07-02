package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's weapons update
 */
public class WeaponsUpdateMessage implements MessageVisitable, Serializable {
    /**
     * Updated players's name
     */
    private String name;
    /**
     * Names of loaded weapons
     */
    private List<String> loadedWeapons;
    /**
     * Names of unloaded weapons
     */
    private List<String> unloadedWeapons;
    /**
     * Player's number of weapon
     */
    private int numeWeapons;

    /**
     * Creates a WeaponUpdateMessage with given parameters
     * @param name updated player's name
     * @param loadedWeapons names of loaded weapons
     * @param unloadedWeapons names of unloaded weapons
     * @param numeWeapons player's number of weapon
     */
    public WeaponsUpdateMessage(String name, List<String> loadedWeapons, List<String> unloadedWeapons, int numeWeapons) {
        this.name = name;
        this.loadedWeapons = loadedWeapons;
        this.unloadedWeapons = unloadedWeapons;
        this.numeWeapons = numeWeapons;
    }

    /**
     * Creates a WeaponUpdateMessage with given parameters, initializing loadedWeapons
     * to a new empty list
     * @param name updated player's name
     * @param unloadedWeapons names of unloaded weapons
     * @param numeWeapons player's number of weapon
     */
    public WeaponsUpdateMessage(String name, List<String> unloadedWeapons, int numeWeapons) {
        this.name = name;
        this.unloadedWeapons = unloadedWeapons;
        this.numeWeapons = numeWeapons;
        this.loadedWeapons = new ArrayList<>();
    }

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return loadedWeapons
     */
    public List<String> getLoadedWeapons() {
        return loadedWeapons;
    }

    /**
     *
     * @return unloadedWeapons
     */
    public List<String> getUnloadedWeapons() {
        return unloadedWeapons;
    }

    /**
     *
     * @return numeWeapons
     */
    public int getNumeWeapons() {
        return numeWeapons;
    }

    /**
     * Method used to properly recognize dynamic type of Message
     * @param messageVisitor messageVisitor who "visits" this message
     */
    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
 }
