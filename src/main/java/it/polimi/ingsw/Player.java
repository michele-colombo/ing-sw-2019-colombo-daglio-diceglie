package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.MustDiscardWeaponException;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.PlayerColor.*;
import static it.polimi.ingsw.PlayerState.*;

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
    private PlayerState state;
    /**
     * Next state of player, used after a payment.
     */
    private PlayerState nextState;
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
    /**
     * List of the commands selectable by the player in a specific moment.
     */
    private List<Command> selectableCommands;

    /**
     * It is false until the player is spawned for the first time
     */
    private boolean isBorn;

    /**
     * When final frenzy starts, it is set according to the position towards the first player
     */
    private boolean isBeforeFirst;

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
        isBorn = false;

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
     * Test only constructor. Uses default name and color.
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
     * Add a certain increment of points to a player
     * @param increment Non-negative integer
     */
    public void addPoints(int increment) {
        assert (increment >= 0) : "trying to add negative points to a player";
        points += increment;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public PlayerState getNextState() {
        return nextState;
    }

    public void setNextState(PlayerState nextState) {
        this.nextState = nextState;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    /**
     * Returns the squares the player is on
     * @return the reference to the actual square, not a clone
     */
    public Square getSquarePosition() {
        return squarePosition;
    }

    public void setSquarePosition(Square squarePosition) {
        this.squarePosition = squarePosition;
    }

    public List<Weapon> getSelectableWeapons() {
        return selectableWeapons;
    }

    /**
     * Sets a clone of the parameter as the weapons selectable by the player in a specific moment.
     * Objects in the list are the reference to the actual (external) object
     * @param selectableWeapons a clone of the list is created and then set
     */
    public void setSelectableWeapons(List<Weapon> selectableWeapons) {
        this.selectableWeapons = new ArrayList<>();
        this.selectableWeapons.addAll(selectableWeapons);
    }

    public List<Square> getSelectableSquares() {
        return selectableSquares;
    }

    /**
     * Sets a clone of the parameter as the squares selectable by the player in a specific moment
     * Objects in the list are the reference to the actual (external) object
     * @param selectableSquares a clone of the list is created and then set
     */
    public void setSelectableSquares(List<Square> selectableSquares) {
        this.selectableSquares = new ArrayList<>();
        this.selectableSquares.addAll(selectableSquares);
    }

    public List<Player> getSelectablePlayers() {
        return selectablePlayers;
    }

    /**
     * Sets a clone of the parameter as the players selectable by the player in a specific moment
     * Objects in the list are the reference to the actual (external) object
     * @param selectablePlayers a clone of the list is created and then set
     */
    public void setSelectablePlayers(List<Player> selectablePlayers) {
        this.selectablePlayers = new ArrayList<>();
        this.selectablePlayers.addAll(selectablePlayers);
    }

    public List<Mode> getSelectableModes() {
        return selectableModes;
    }

    /**
     * Sets a clone of the parameter as the modes selectable by the player in a specific moment
     * Objects in the list are the reference to the actual (external) object
     * @param selectableModes a clone of the list is created and then set
     */
    public void setSelectableModes(List<Mode> selectableModes) {
        this.selectableModes = new ArrayList<>();
        this.selectableModes.addAll(selectableModes);
    }

    public List<Action> getSelectableActions() {
        return selectableActions;
    }

    /**
     * Sets a clone of the parameter as the actions selectable by the player in a specific moment
     * Objects in the list are the reference to the actual (external) object
     * @param selectableActions a clone of the list is created and then set
     */
    public void setSelectableActions(List<Action> selectableActions) {
        this.selectableActions = new ArrayList<>();
        this.selectableActions.addAll(selectableActions);
    }

    public List<Color> getSelectableColors() {
        return selectableColors;
    }

    /**
     * Sets a clone of the parameter as the colors selectable by the player in a specific moment
     * Objects in the list are the reference to the actual (external) object
     * @param selectableColors a clone of the list is created and then set
     */
    public void setSelectableColors(List<Color> selectableColors) {
        this.selectableColors = new ArrayList<>();
        this.selectableColors.addAll(selectableColors);
    }

    public List<PowerUp> getSelectablePowerUps() {
        return selectablePowerUps;
    }

    /**
     * Sets a clone of the parameter as the powerups selectable by the player in a specific moment
     * Objects in the list are the reference to the actual (external) object
     * @param selectablePowerUps a clone of the list is created and then set
     */
    public void setSelectablePowerUps(List<PowerUp> selectablePowerUps) {
        this.selectablePowerUps = new ArrayList<>();
        this.selectablePowerUps.addAll(selectablePowerUps);
    }


    public List<Command> getSelectableCommands() {
        return selectableCommands;
    }

    public void setSelectableCommands(List<Command> selectableCommands) {
        this.selectableCommands = new ArrayList<>();
        this.selectableCommands.addAll(selectableCommands);
    }

    /**
     * Clears all the selectable lists of the player.
     * Since list were cloned, the lists that were passed as parameters are untouched.
     */
    public void resetSelectables(){
        selectableWeapons.clear();
        selectableSquares.clear();
        selectableModes.clear();
        selectablePowerUps.clear();
        selectablePlayers.clear();
        selectableColors.clear();
        selectableActions.clear();
        selectableCommands.clear();
    }

    /**
     * Returns the list of weapons owned by a player in a specific moment.
     * They are maximum 3 or temporary 4 (before discarding)
     * @return the reference to the actual list (can be modified)
     */
    public List<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * Adds a weapon to the player.
     * @param w the weapon to be added
     * //throws MustDiscardWeaponException if the player has 4 weapons after adding (weapon added anyway)
     */
    public void addWeapon (Weapon w) {
        if (w != null && weapons.indexOf(w) == -1){
            weapons.add(w);
            if (weapons.size() > 3) {
                //throw new MustDiscardWeaponException();
            }
        } else {
            assert(false): "trying to add null or already present weapon";
        }
    }

    /**
     * Gets the weapons currently loaded
     * @return temporary generated list of loaded weapons
     */
    public List<Weapon> getLoadedWeapons(){
        List<Weapon> result = new ArrayList<>();
        for (Weapon w : weapons){
            if (w.isLoaded()) {
                result.add(w);
            }
        }
        return result;
    }

    /**
     * Gets the weapons currently unloaded
     * @return temporary generated list of unloaded weapons
     */
    public List<Weapon> getUnloadedWeapons(){
        List<Weapon> result = new ArrayList<>();
        for (Weapon w : weapons){
            if (!w.isLoaded()) {
                result.add(w);
            }
        }
        return result;
    }

    /**
     * Removes a weapon from the player, if it is present
     * @param w weapon to remove
     * @return false if weapon was absent
     */
    public boolean discardWeapon(Weapon w){
        if (w != null){
            return weapons.remove(w);
        } else {
            assert(false): "trying to remove null weapon";
            return false;
        }
    }

    /**
     * Gets the powerup currently holded by the player
     * @return reference to the actual list of PowerUp
     */
    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    /**
     * adds a powerup to the player
     * @param po powerup to add
     */
    public void addPowerUp(PowerUp po){
        if (po != null){
            powerUps.add(po);
        } else {
            assert(false): "trying to add a null powerup";
        }
    }

    /**
     * Removes a powerup from the player, if it is present
     * @param po powerup to remove
     * @return false if the powerup was absent
     */
    public boolean discardPowerUp(PowerUp po){
        if (po != null){
            return powerUps.remove(po);
        } else {
            assert (false) : "trying to remove null powerup";
            return false;
        }
    }

    /**
     * Gets ammos owned by the player
     * @return the reference to the actual object (can be modified)
     */
    public Cash getWallet() {
        return wallet;
    }

    /**
     * Gets the debit of the player in a specific moment
     * @return the reference to the actual object (can be modified)
     */
    public Cash getPending() {
        return pending;
    }

    /**
     * Gets the credit (from powerups and ammos) the player has chose to pay in a specific moment
     * @return the reference to the actual object (can be modified)
     */
    public Cash getCredit() {
        return credit;
    }

    public void setPending(Cash pending) {
        this.pending = pending;
    }

    public void setCredit(Cash credit) {
        this.credit = credit;
    }

    /**
     * Verifies if the player can afford a certain payment, considering his current ammos and powerups
     * @param amount payment amount
     * @return true if the player could afford that payment
     */
    public boolean canAfford(Cash amount){       //tells you if the total cash of a player (ammos + powerUps) are more than a certain sum
        Cash temp = new Cash();
        for (PowerUp po : powerUps){
            temp = temp.sum(new Cash(po.getColor(), 1));
        }
        temp = temp.sum(wallet);
        temp = temp.sum(credit);
        return temp.greaterEqual(amount);
    }

    /**
     * Substitutes player's damageTrack with a FrenzyDamageTrack.
     * It does not resetAfterDeath the damageTrack.
     * Can be called on the player even if he has already a FrenzyDamageTrack.
     */
    public void switchToFrenzy(){
        damageTrack = new FrenzyDamageTrack(damageTrack);
    }

    /**
     * Gets player's damageTrack
     * @return reference to the actual object (can be modified)
     */
    public DamageTrack getDamageTrack() {
        return damageTrack;
    }

    public boolean isBorn() {
        return isBorn;
    }

    public void setBorn(boolean born) {
        isBorn = born;
    }

    public boolean isBeforeFirst() {
        return isBeforeFirst;
    }

    /**
     * It is set when switching to final frenzy
     * @param beforeFirst
     */
    public void setBeforeFirst(boolean beforeFirst) {
        isBeforeFirst = beforeFirst;
    }

    /**
     * Returns the number of power ups of a specified type owned by the player
     * @param type
     * @return
     */
    public int howManyPowerUps(PowerUpType type){
        int result = 0;
        for (PowerUp po : powerUps){
            if (po.getType() == type) result++;
        }
        return result;
    }

    public boolean isAlive(){
        if(getDamageTrack().getDamageList().size() < 11){
            return true;
        }
        return false;
    }
}
