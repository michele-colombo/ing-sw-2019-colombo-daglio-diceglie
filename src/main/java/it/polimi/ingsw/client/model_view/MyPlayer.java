package it.polimi.ingsw.client.model_view;

import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the Player of this client
 */
public class MyPlayer extends PlayerView {
    /**
     * Point of this client
     */
    private int points;
    /**
     * Map WeaponView of this client; mapping is true if WeaponView is loaded, else false
     */
    private Map<WeaponView, Boolean> weapons;
    /**
     * PowerUpView of this client
     */
    private List<PowerUpView> powerUps;
    /**
     * Cash to be paid by this client
     */
    private Cash pending;
    /**
     * Cash already paid by this client
     */
    private Cash credit;
    /**
     * Selectables WeaponViews by this client
     */
    private List<WeaponView> selectableWeapons;
    /**
     * Selectables SquareViews by this client
     */
    private List<SquareView> selectableSquares;
    /**
     * Selectables PlayerViews by this client
     */
    private List<PlayerView> selectablePlayers;
    /**
     * Selectables ModeViews by this client
     */
    private List<ModeView> selectableModes;
    /**
     * Selectables actions by this client
     */
    private List<String> selectableActions;
    /**
     * Selectables ammos, whose colors is AmmoColors, by this client
     */
    private List<AmmoColor> selectableColors;
    /**
     * Selectables PowerUpViews by this client
     */
    private List<PowerUpView> selectablePowerUps;
    /**
     * Selectables Commands by this client
     */
    private List<Command> selectableCommands;
    /**
     * Current WeaponView used this client
     */
    private WeaponView currWeapon;

    /**
     * Creates MyPlayer with given name and color, and initialing to empty data structures/0/null other attributes
     * @param name name of this client
     * @param color color of this client
     */
    public MyPlayer(String name, PlayerColor color) {
        super(name, color);
        points = 0;
        weapons = new HashMap<>();
        powerUps = new ArrayList<>();
        pending = new Cash(0,0,0);
        credit = new Cash(0,0,0);
        selectableWeapons = new ArrayList<>();
        selectableSquares = new ArrayList<>();
        selectablePlayers = new ArrayList<>();
        selectableModes = new ArrayList<>();
        selectableActions = new ArrayList<>();
        selectableColors = new ArrayList<>();
        selectablePowerUps = new ArrayList<>();
        selectableCommands = new ArrayList<>();
        currWeapon = null;
    }

    /**
     *
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     *
     * @return weapons
     */
    public Map<WeaponView, Boolean> getWeapons() {
        return weapons;
    }

    /**
     *
     * @return powerUps
     */
    public List<PowerUpView> getPowerUps() {
        return powerUps;
    }

    /**
     *
     * @return pending
     */
    public Cash getPending() {
        return pending;
    }

    /**
     *
     * @return credit
     */
    public Cash getCredit() {
        return credit;
    }

    /**
     *
     * @return selectableWeapons
     */
    public List<WeaponView> getSelectableWeapons() {
        return selectableWeapons;
    }

    /**
     *
     * @return selectableSquares
     */
    public List<SquareView> getSelectableSquares() {
        return selectableSquares;
    }

    /**
     *
     * @return selectablePlayers
     */
    public List<PlayerView> getSelectablePlayers() {
        return selectablePlayers;
    }

    /**
     *
     * @return selectableModes
     */
    public List<ModeView> getSelectableModes() {
        return selectableModes;
    }

    /**
     *
     * @return selectableActions
     */
    public List<String> getSelectableActions() {
        return selectableActions;
    }

    /**
     *
     * @return selectableColors
     */
    public List<AmmoColor> getSelectableColors() {
        return selectableColors;
    }

    /**
     *
     * @return selectablePowerUps
     */
    public List<PowerUpView> getSelectablePowerUps() {
        return selectablePowerUps;
    }

    /**
     *
     * @return selectableCommands
     */
    public List<Command> getSelectableCommands() {
        return selectableCommands;
    }

    /**
     *
     * @return currWeapons
     */
    public WeaponView getCurrWeapon() {
        return currWeapon;
    }

    /**
     * Set points
     * @param points to be set
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Set weapons
     * @param weapons weapons to be set
     */
    public void setWeapons(Map<WeaponView, Boolean> weapons) {
        this.weapons = weapons;
    }

    /**
     * Set powerUps
     * @param powerUps powerUps to be set
     */
    public void setPowerUps(List<PowerUpView> powerUps) {
        this.powerUps = powerUps;
    }

    /**
     * Set pending
     * @param pending pending to be set
     */
    public void setPending(Cash pending) {
        this.pending = pending;
    }

    /**
     * Set credit
     * @param credit to be set
     */
    public void setCredit(Cash credit) {
        this.credit = credit;
    }

    /**
     * Set selectableWeapons
     * @param selectableWeapons to be set
     */
    public void setSelectableWeapons(List<WeaponView> selectableWeapons) {
        this.selectableWeapons = selectableWeapons;
    }

    /**
     * Set selectablesSquares
     * @param selectableSquares to be set
     */
    public void setSelectableSquares(List<SquareView> selectableSquares) {
        this.selectableSquares = selectableSquares;
    }

    /**
     * Set selectablesPLayers
     * @param selectablePlayers to be set
     */
    public void setSelectablePlayers(List<PlayerView> selectablePlayers) {
        this.selectablePlayers = selectablePlayers;
    }

    /**
     * Set selectableModes
     * @param selectableModes to be set
     */
    public void setSelectableModes(List<ModeView> selectableModes) {
        this.selectableModes = selectableModes;
    }

    /**
     * Set selectableActions
     * @param selectableActions to be set
     */
    public void setSelectableActions(List<String> selectableActions) {
        this.selectableActions = selectableActions;
    }

    /**
     * Set selectablesColors
     * @param selectableColors to be set
     */
    public void setSelectableColors(List<AmmoColor> selectableColors) {
        this.selectableColors = selectableColors;
    }

    /**
     * Set selectablePowerUps
     * @param selectablePowerUps to be set
     */
    public void setSelectablePowerUps(List<PowerUpView> selectablePowerUps) {
        this.selectablePowerUps = selectablePowerUps;
    }

    /**
     * Set selectablesCommands
     * @param selectableCommands to be set
     */
    public void setSelectableCommands(List<Command> selectableCommands) {
        this.selectableCommands = selectableCommands;
    }

    /**
     * Set currWeapon
     * @param currWeapon to be set
     */
    public void setCurrWeapon(WeaponView currWeapon) {
        this.currWeapon = currWeapon;
    }
}
