package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.PlayerColor;
import it.polimi.ingsw.server.model.enums.PlayerState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents Player on client
 */
public class PlayerView {
    /**
     * Name of the PlayerView
     */
    private String name;
    /**
     * Color of the PlayerView
     */
    private PlayerColor color;
    /**
     * Current state of PlayerView
     */
    private PlayerState state;
    /**
     * Number of loadedWeapons
     */
    private int numLoadedWeapons;
    /**
     * WeaponViews of unloaded weapons
     */
    private List<WeaponView> unloadedWeapons;
    /**
     * Number of PowerUps
     */
    private int numPowerUps;
    /**
     * Position of PlayerView
     */
    private SquareView squarePosition;
    /**
     * Wallet of the PlayerView
     */
    private Cash wallet;
    /**
     * Skulls on DamageTrack
     */
    private int skulls;
    /**
     * List of PlayerViews who damaged this one
     */
    private List<PlayerView> damageList;
    /**
     * Maps PlayerViews and their number of mark on this PlayerView
     */
    private Map<PlayerView, Integer> markMap;
    /**
     * True if PlayerView is in frenzy mode, else false
     */
    private boolean isFrenzy;

    /**
     * Creates PlayerView with chose name and color, initialing to empty data structures/0/false other attributes
     * @param name Name of this PlayerView
     * @param color Name of this color
     */
    public PlayerView(String name, PlayerColor color) {
        this.name = name;
        this.color = color;
        this.state = PlayerState.IDLE;
        numLoadedWeapons = 0;
        unloadedWeapons = new ArrayList<>();
        numPowerUps = 0;
        squarePosition = null;
        wallet = new Cash(0,0,0);
        skulls = 0;
        damageList = new ArrayList<>();
        markMap = new HashMap<>();
        isFrenzy = false;
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
     * @return color
     */
    public PlayerColor getColor() {
        return color;
    }

    /**
     *
     * @return state
     */
    public PlayerState getState() {
        return state;
    }

    /**
     *
     * @return numLoadedWeapons
     */
    public int getNumLoadedWeapons() {
        return numLoadedWeapons;
    }

    /**
     *
     * @return unloadedWeapons
     */
    public List<WeaponView> getUnloadedWeapons() {
        return unloadedWeapons;
    }

    /**
     *
     * @return numPowerUps
     */
    public int getNumPowerUps() {
        return numPowerUps;
    }

    /**
     *
     * @return squarePosition
     */
    public SquareView getSquarePosition() {
        return squarePosition;
    }

    /**
     *
     * @return wallet
     */
    public Cash getWallet() {
        return wallet;
    }

    /**
     *
     * @return skulls
     */
    public int getSkulls() {
        return skulls;
    }

    /**
     *
     * @return damageList
     */
    public List<PlayerView> getDamageList() {
        return damageList;
    }

    /**
     *
     * @return markMap
     */
    public Map<PlayerView, Integer> getMarkMap() {
        return markMap;
    }

    /**
     *
     * @return isFrenzy
     */
    public boolean isFrenzy() {
        return isFrenzy;
    }

    /**
     * Set state
     * @param state state to be set
     */
    public void setState(PlayerState state) {
        this.state = state;
    }

    /**
     * Set numLoadedWeapons
     * @param numLoadedWeapons numLoadedWeapons to be set
     */
    public void setNumLoadedWeapons(int numLoadedWeapons) {
        this.numLoadedWeapons = numLoadedWeapons;
    }

    /**
     * Set unloadedWeapons
     * @param unloadedWeapons unloadedWeapons to be set
     */
    public void setUnloadedWeapons(List<WeaponView> unloadedWeapons) {
        this.unloadedWeapons = unloadedWeapons;
    }

    /**
     * Set numPowerUps
     * @param numPowerUps numPowerUps to be set
     */
    public void setNumPowerUps(int numPowerUps) {
        this.numPowerUps = numPowerUps;
    }

    /**
     * Set squarePosition
     * @param squarePosition squarePosition to be set
     */
    public void setSquarePosition(SquareView squarePosition) {
        this.squarePosition = squarePosition;
    }

    /**
     * Set wallet
     * @param wallet wallet to be set
     */
    public void setWallet(Cash wallet) {
        this.wallet = wallet;
    }

    /**
     * Set skulls
     * @param skulls skulls to be set
     */
    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    /**
     * Set damageList
     * @param damageList damageList to be set
     */
    public void setDamageList(List<PlayerView> damageList) {
        this.damageList = damageList;
    }

    /**
     * Set markMap
     * @param markMap markMap to be set
     */
    public void setMarkMap(Map<PlayerView, Integer> markMap) {
        this.markMap = markMap;
    }

    /**
     * Set isFrenzy
     * @param frenzy frenzy to be set
     */
    public void setFrenzy(boolean frenzy) {
        isFrenzy = frenzy;
    }

    /**
     *
     * @return String representation of this PlayerView
     */
    @Override
    public String toString() {
        return getName();
    }
}
