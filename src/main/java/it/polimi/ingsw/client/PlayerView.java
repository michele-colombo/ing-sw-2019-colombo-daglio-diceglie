package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.PlayerColor;
import it.polimi.ingsw.server.model.enums.PlayerState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerView {
    private String name;
    private PlayerColor color;
    private PlayerState state;
    private int numLoadedWeapons;
    private List<WeaponView> unloadedWeapons;
    private int numPowerUps;
    private SquareView squarePosition;
    private Cash wallet;

    private int skulls;
    private List<PlayerView> damageList;
    private Map<PlayerView, Integer> markMap;
    private boolean isFrenzy;

    public PlayerView(String name, PlayerColor color) {
        this.name = name;
        this.color = color;
        unloadedWeapons = new ArrayList<>();
        damageList = new ArrayList<>();
        markMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }

    public PlayerState getState() {
        return state;
    }

    public int getNumLoadedWeapons() {
        return numLoadedWeapons;
    }

    public List<WeaponView> getUnloadedWeapons() {
        return unloadedWeapons;
    }

    public int getNumPowerUps() {
        return numPowerUps;
    }

    public SquareView getSquarePosition() {
        return squarePosition;
    }

    public Cash getWallet() {
        return wallet;
    }

    public int getSkulls() {
        return skulls;
    }

    public List<PlayerView> getDamageList() {
        return damageList;
    }

    public Map<PlayerView, Integer> getMarkMap() {
        return markMap;
    }

    public boolean isFrenzy() {
        return isFrenzy;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setNumLoadedWeapons(int numLoadedWeapons) {
        this.numLoadedWeapons = numLoadedWeapons;
    }

    public void setUnloadedWeapons(List<WeaponView> unloadedWeapons) {
        this.unloadedWeapons = unloadedWeapons;
    }

    public void setNumPowerUps(int numPowerUps) {
        this.numPowerUps = numPowerUps;
    }

    public void setSquarePosition(SquareView squarePosition) {
        this.squarePosition = squarePosition;
    }

    public void setWallet(Cash wallet) {
        this.wallet = wallet;
    }

    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    public void setDamageList(List<PlayerView> damageList) {
        this.damageList = damageList;
    }

    public void setMarkMap(Map<PlayerView, Integer> markMap) {
        this.markMap = markMap;
    }

    public void setFrenzy(boolean frenzy) {
        isFrenzy = frenzy;
    }
}
