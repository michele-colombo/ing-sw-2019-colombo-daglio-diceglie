package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.PlayerColor.*;
import static it.polimi.ingsw.TypeState.*;


public class Player {
    private final String name;
    private final PlayerColor color;
    private int points;
    private TypeState state;
    private TypeState nextState;
    private boolean isFirstPlayer;
    private List<Weapon> weapons;
    private List<PowerUp> powerUps;
    private Square square;
    private DamageTrack damageTrack;

    private Cash wallet;
    private Cash pending;
    private Cash credit;

    private List<Weapon> selectableWeapons;
    private List<Square> selectableSquares;
    private List<Player> selectablePlayers;
    private List<Mode> selectableModes;
    private List<Action> selectableActions;
    private List<Color> selectableColors;
    private List<PowerUp> selectablePowerUps;
    //selectableCommands (ok, back, goAhead)

    public Player(String name, PlayerColor color) {
        this.name = name;
        this.color = color;
        points = 0;
        isFirstPlayer = false;
        weapons = new ArrayList<>();
        powerUps = new ArrayList<>();
        state = IDLE;
        damageTrack = new NormalDamageTrack();

        wallet = new Cash();
        pending = new Cash();
        credit = new Cash();

        selectableActions = new ArrayList<>();
        selectableColors = new ArrayList<>();
        selectableModes = new ArrayList<>();
        selectablePlayers = new ArrayList<>();
        selectablePowerUps = new ArrayList<>();
        selectableSquares = new ArrayList<>();
        selectableWeapons = new ArrayList<>();
    }

    public Player(){        //TEST constructor
        this("Nome di prova", GREY);
    }

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int increment) {
        points += increment;
    }

    public TypeState getState() {
        return state;
    }

    public void setState(TypeState state) {
        this.state = state;
    }

    public TypeState getNextState() {
        return nextState;
    }

    public void setNextState(TypeState nextState) {
        this.nextState = nextState;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public List<Weapon> getSelectableWeapons() {
        return selectableWeapons;
    }

    public void setSelectableWeapons(List<Weapon> selectableWeapons) {
        this.selectableWeapons = selectableWeapons;
    }

    public List<Square> getSelectableSquares() {
        return selectableSquares;
    }

    public void setSelectableSquares(List<Square> selectableSquares) {
        this.selectableSquares = selectableSquares;
    }

    public List<Player> getSelectablePlayers() {
        return selectablePlayers;
    }

    public void setSelectablePlayers(List<Player> selectablePlayers) {
        this.selectablePlayers = selectablePlayers;
    }

    public List<Mode> getSelectableModes() {
        return selectableModes;
    }

    public void setSelectableModes(List<Mode> selectableModes) {
        this.selectableModes = selectableModes;
    }

    public List<Action> getSelectableActions() {
        return selectableActions;
    }

    public void setSelectableActions(List<Action> selectableActions) {
        this.selectableActions = selectableActions;
    }

    public List<Color> getSelectableColors() {
        return selectableColors;
    }

    public void setSelectableColors(List<Color> selectableColors) {
        this.selectableColors = selectableColors;
    }

    public List<PowerUp> getSelectablePowerUps() {
        return selectablePowerUps;
    }

    public void setSelectablePowerUps(List<PowerUp> selectablePowerUps) {
        this.selectablePowerUps = selectablePowerUps;
    }

    public void resetSelectables(){
        selectableWeapons.clear();
        selectableSquares.clear();
        selectableModes.clear();
        selectablePowerUps.clear();
        selectablePlayers.clear();
        selectableColors.clear();
        selectableActions.clear();
    }


    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void addWeapon (Weapon w){
        if (weapons.indexOf(w) == -1){
            weapons.add(w);
        }
    }

    public List<Weapon> getLoadedWeapons(){
        List<Weapon> result = new ArrayList<>();
        for (Weapon w : weapons){
            if (w.isLoaded()) {
                result.add(w);
            }
        }
        return result;
    }

    public List<Weapon> getUnloadedWeapons(){
        List<Weapon> result = new ArrayList<>();
        for (Weapon w : weapons){
            if (!w.isLoaded()) {
                result.add(w);
            }
        }
        return result;
    }

    public boolean discardWeapon(Weapon w){
        return weapons.remove(w);
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public boolean discardPowerUp(PowerUp po){
        return powerUps.remove(po);
    }

    public Cash getWallet() {
        return wallet;
    }

    public Cash getPending() {
        return pending;
    }

    public Cash getCredit() {
        return credit;
    }

    public void setPending(Cash pending) {
        this.pending = pending;
    }

    public void setCredit(Cash credit) {
        this.credit = credit;
    }

    public boolean canAfford(Cash c){       //tells you if the total cash of a player (ammos + powerUps) are more than a certain sum
        Cash temp = new Cash();
        for (PowerUp po : powerUps){
            temp = temp.sum(new Cash(po.getColor(), 1));
        }
        temp = temp.sum(wallet);
        return temp.greaterEqual(c);
    }

    public void switchToFrenzy(){
        damageTrack = new FrenzyDamageTrack(damageTrack);
        damageTrack.resetAfterDeath();
    }
}
