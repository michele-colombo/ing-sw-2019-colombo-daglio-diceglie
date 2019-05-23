package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPlayer extends PlayerView {
    private int points;
    private Map<WeaponView, Boolean> weapons;
    private List<PowerUpView> powerUps;
    private Cash pending;
    private Cash credit;
    private List<WeaponView> selectableWeapons;
    private List<SquareView> selectableSquares;
    private List<PlayerView> selectablePlayers;
    private List<ModeView> selectableModes;
    private List<String> selectableActions; //todo: fare una classe ActionView?
    private List<AmmoColor> selectableColors;
    private List<PowerUpView> selectablePowerUps;
    private List<Command> selectableCommands;

    public MyPlayer(String name, PlayerColor color) {
        super(name, color);
        weapons = new HashMap<>();
        powerUps = new ArrayList<>();
        selectableWeapons = new ArrayList<>();
        selectableSquares = new ArrayList<>();
        selectablePlayers = new ArrayList<>();
        selectableModes = new ArrayList<>();
        selectableActions = new ArrayList<>();
        selectableColors = new ArrayList<>();
        selectablePowerUps = new ArrayList<>();
        selectableCommands = new ArrayList<>();
    }

    public int getPoints() {
        return points;
    }

    public Map<WeaponView, Boolean> getWeapons() {
        return weapons;
    }

    public List<PowerUpView> getPowerUps() {
        return powerUps;
    }

    public Cash getPending() {
        return pending;
    }

    public Cash getCredit() {
        return credit;
    }

    public List<WeaponView> getSelectableWeapons() {
        return selectableWeapons;
    }

    public List<SquareView> getSelectableSquares() {
        return selectableSquares;
    }

    public List<PlayerView> getSelectablePlayers() {
        return selectablePlayers;
    }

    public List<ModeView> getSelectableModes() {
        return selectableModes;
    }

    public List<String> getSelectableActions() {
        return selectableActions;
    }

    public List<AmmoColor> getSelectableColors() {
        return selectableColors;
    }

    public List<PowerUpView> getSelectablePowerUps() {
        return selectablePowerUps;
    }

    public List<Command> getSelectableCommands() {
        return selectableCommands;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setWeapons(Map<WeaponView, Boolean> weapons) {
        this.weapons = weapons;
    }

    public void setPowerUps(List<PowerUpView> powerUps) {
        this.powerUps = powerUps;
    }

    public void setPending(Cash pending) {
        this.pending = pending;
    }

    public void setCredit(Cash credit) {
        this.credit = credit;
    }

    public void setSelectableWeapons(List<WeaponView> selectableWeapons) {
        this.selectableWeapons = selectableWeapons;
    }

    public void setSelectableSquares(List<SquareView> selectableSquares) {
        this.selectableSquares = selectableSquares;
    }

    public void setSelectablePlayers(List<PlayerView> selectablePlayers) {
        this.selectablePlayers = selectablePlayers;
    }

    public void setSelectableModes(List<ModeView> selectableModes) {
        this.selectableModes = selectableModes;
    }

    public void setSelectableActions(List<String> selectableActions) {
        this.selectableActions = selectableActions;
    }

    public void setSelectableColors(List<AmmoColor> selectableColors) {
        this.selectableColors = selectableColors;
    }

    public void setSelectablePowerUps(List<PowerUpView> selectablePowerUps) {
        this.selectablePowerUps = selectablePowerUps;
    }

    public void setSelectableCommands(List<Command> selectableCommands) {
        this.selectableCommands = selectableCommands;
    }
}
