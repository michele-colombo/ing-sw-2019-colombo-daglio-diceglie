package it.polimi.ingsw;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.PlayerColor.*;
import static it.polimi.ingsw.TypeState.*;

/**
 * A class representing a Player of the game.
 * Each player remains in the game for its whole duration,
 * regardless the actual status of the physical player.
 * This contains the state of Player and his lists of selectable items.
 */
public class Player {
    /**
     * Nickname of the player, guaranteed unique from the constructor caller
     */
    private final String name;
    /**
     * Color of player-related stuffs: blood drops, marker, board, etc.
     */
    private final PlayerColor color;
    /**
     * Non-negative integer containing points of the player
     */
    private int points;
    /**
     * State of the player: what he's doing.
     * It's used by the controller to choose the proper method to invoke
     */
    private TypeState state;
    /**
     * Next state of player, used after a payment.
     */
    private TypeState nextState;
    /**
     * This keeps track of the first player. Useful in final frenzy.
     */
    private boolean isFirstPlayer;
    /**
     * Weapons owned by a player. Can be empty.
     * Contains maximum 3 weapons, or temporary 4 (before discarding).
     */
    private List<Weapon> weapons;
    /**
     * PowerUps owned by a player. can be empty.
     * Contains maxim 3 powerUps, or temporary 4 (before respawining).
     */
    private List<PowerUp> powerUps;
    /**
     * The position of the player on the map.
     */
    private Square squarePosition;
    /**
     * An obect keeping track of damages, marks and adrenaline of the player.
     * Contains methods to score points after death.
     */
    private DamageTrack damageTrack;

    /**
     * Active ammos owned by the player.
     * Each color is maximum 3.
     */
    private Cash wallet;
    /**
     * Represents the temporary debit of a player.
     * Used for payment handling.
     */
    private Cash pending;
    /**
     * Represents the credit of a player during payment operation.
     */
    private Cash credit;

    /**
     * List of the weapons selectable by the player in a specific moment.
     * Used for shooting, grabbing and discarding.
     */
    private List<Weapon> selectableWeapons;
    /**
     * List of the square selectable by the player in a specific moment.
     * Used for moving, grabbing and shooting particular weapons.
     */
    private List<Square> selectableSquares;
    /**
     * List of the players selectable by the player in a specific moment.
     * Used for shooting.
     */
    private List<Player> selectablePlayers;
    /**
     * List of the modes selectable by the player in a specific moment.
     * Used to choose the effects to apply in a specific shoot
     */
    private List<Mode> selectableModes;
    /**
     * List of the action selectable by the player in a specific moment.
     */
    private List<Action> selectableActions;
    /**
     * List of the ammos selectable by the player in a specific moment.
     * Used only for paying an ammo of any color (targeting scope)
     */
    private List<Color> selectableColors;
    /**
     * List of the powerUps selectable by the player in a specific moment.
     * Used for respawining, using powerUps and paying.
     */
    private List<PowerUp> selectablePowerUps;
    //selectableCommands (ok, back, goAhead)

    /**
     * Sets name and color. Instantiate attributes and sets default values.
     * @param name Uniqueness checked by constructor caller
     * @param color Uniqueness checked by the caller
     */
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

    /**
     * Test oriented constructor. Uses default name and color.
     */
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

    /**
     * Add points of player of a certain increment
     * @param increment Non-negative integer
     */
    public void addPoints(int increment) {
        if (increment < 0) throw new InvalidParameterException("increment must be non-negative");
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

    public Square getSquarePosition() {
        return squarePosition;
    }

    public void setSquarePosition(Square squarePosition) {
        this.squarePosition = squarePosition;
    }

    public List<Weapon> getSelectableWeapons() {
        return selectableWeapons;
    }

    public void setSelectableWeapons(List<Weapon> selectableWeapons) {
        this.selectableWeapons = new ArrayList<>();
        this.selectableWeapons.addAll(selectableWeapons);
    }

    public List<Square> getSelectableSquares() {
        return selectableSquares;
    }

    public void setSelectableSquares(List<Square> selectableSquares) {
        this.selectableSquares = new ArrayList<>();
        this.selectableSquares.addAll(selectableSquares);
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

    public void addPowerUp(PowerUp po){
        powerUps.add(po);
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

    public boolean canAfford(Cash amount){       //tells you if the total cash of a player (ammos + powerUps) are more than a certain sum
        Cash temp = new Cash();
        for (PowerUp po : powerUps){
            temp = temp.sum(new Cash(po.getColor(), 1));
        }
        temp = temp.sum(wallet);
        return temp.greaterEqual(amount);
    }

    public void switchToFrenzy(){
        damageTrack = new FrenzyDamageTrack(damageTrack);
        damageTrack.resetAfterDeath();
    }

    public DamageTrack getDamageTrack() {
        return damageTrack;
    }
}
