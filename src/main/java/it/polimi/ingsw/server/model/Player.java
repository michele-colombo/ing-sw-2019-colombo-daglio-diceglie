package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.server.model.enums.PlayerColor.*;
import static it.polimi.ingsw.server.model.enums.PlayerState.*;

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
     * AmmoColor of player-related stuffs: blood drops, marker, board, etc.
     */
    private PlayerColor color;
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
     * Weapons owned by a player, with their load status. Can be empty.
     * Contains maximum 3 weapons, or temporary 4 (before discarding).
     */
    private Map<Weapon, Boolean> weapons;
    /**
     * PowerUps owned by a player. can be empty.
     * Contains maxim 3 powerUps, or temporary 4 (before respawning).
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
        private boolean isFrenzy;
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
    private List<AmmoColor> selectableColors;
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
     * It is true if player has at least another turn. When false he has completed his last turn
     */
    private boolean hasAnotherTurn;

    /**
     * Builds a player of the specified name. Instantiate attributes and sets default values.
     * Color is not set.
     * @param name Uniqueness checked by constructor caller
     */
    public Player(String name) {
        this.name = name;
        points = 0;
        isFirstPlayer = false;
        hasAnotherTurn = true;
        weapons = new HashMap<>();
        powerUps = new ArrayList<>();
        state = IDLE;
        damageTrack = new NormalDamageTrack();
        isBorn = false;
        isBeforeFirst = false;

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
        selectableCommands = new ArrayList<>();
    }

    /**
     * Test-only constructor. Builds a player with the specified name and color.
     * Name and color uniqueness checked by caller.
     * @param name the name of the player
     * @param color the color of the player
     */
    public Player(String name, PlayerColor color){
        this(name);
        setColor(color);
    }

    /**
     * Sets the color of the player. Uniqueness of the caller is checked by the caller.
     * @param color the playerColor to set
     */
    public void setColor(PlayerColor color) {
        this.color = color;
    }

    /**
     * Gets the name of the player
     * @return the string of the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the color of the player
     * @return an element of the enum PlayerColor
     */
    public PlayerColor getColor() {
        return color;
    }

    /**
     * Gets the points of the player
     * @return the number of points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Add a certain increment of points to a player
     * @param increment Non-negative integer
     */
    public void addPoints(int increment) {
        points += increment;
    }

    /**
     * Gets the state of the player
     * @return the state of the player
     */
    public PlayerState getState() {
        return state;
    }

    /**
     * Sets the state of the player
     * @param state the state to set
     */
    public void setState(PlayerState state) {
        this.state = state;
    }

    /**
     * Gets the next state (after payment) of the player
     * @return the next state
     */
    public PlayerState getNextState() {
        return nextState;
    }

    /**
     * Sets the nest state of the player
     * @param nextState the state to set
     */
    public void setNextState(PlayerState nextState) {
        this.nextState = nextState;
    }

    /**
     * Checks if the player is the first player of the match
     * @return true if it is the first player
     */
    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    /**
     * Sets the flag of first player
     * @param firstPlayer the value to set
     */
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

    /**
     * Sets the square in which is currently the player.
     * @param squarePosition the square to set
     */
    public void setSquarePosition(Square squarePosition) {
        this.squarePosition = squarePosition;
    }

    /**
     * Gets the list of selectable weapons
     * @return the reference to the actual list (can be modified)
     */
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

    /**
     * Gets the list of selectable squares
     * @return  the reference to the actual list (can be modified)
     */
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

    /**
     * Gets the list of selectable players
     * @return  the reference to the actual list (can be modified)
     */
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

    /**
     * Gets the list of selectable modes
     * @return the reference to the actual list (can be modified)
     */
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

    /**
     * Gets the list of selectable actions
     * @return the reference to the actual list (can be modified)
     */
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

    /**
     * Gets the list of selectable colors
     * @return  the reference to the actual list (can be modified)
     */
    public List<AmmoColor> getSelectableColors() {
        return selectableColors;
    }

    /**
     * Sets a clone of the parameter as the colors selectable by the player in a specific moment
     * Objects in the list are the reference to the actual (external) object
     * @param selectableColors a clone of the list is created and then set
     */
    public void setSelectableColors(List<AmmoColor> selectableColors) {
        this.selectableColors = new ArrayList<>();
        this.selectableColors.addAll(selectableColors);
    }

    /**
     * Gets the list of selectable powerups
     * @return the reference to the actual list (can be modified)
     */
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

    /**
     * Gets the list of selectables commanda
     * @return the reference to the actual list (can be modified)
     */
    public List<Command> getSelectableCommands() {
        return selectableCommands;
    }

    /**
     * Sets a clone of the parameter as the commands selectable by the player in a specific moment
     * Objects in the list are the reference to the actual (external) object
     * @param selectableCommands a clone of the list is created and then set
     */
    public void setSelectableCommands(List<Command> selectableCommands) {
        this.selectableCommands = new ArrayList<>();
        this.selectableCommands.addAll(selectableCommands);
    }

    /**
     * Create a list with the parameter and sets it as the command selectable by the player in a specific moment
     * Objects in the list are the reference to the actual (external) object
     * @param co a list containing this command is created and then set
     */
    public void setSelectableCommands(Command co){
        this.selectableCommands = new ArrayList<>();
        this.selectableCommands.add(co);
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
     * observer method to know how many selectable objects the player has overall
     * @return int which is the sum of the size of each selectable list
     */
    public int howManySelectables(){
        return selectableColors.size()+
                selectableActions.size()+
                selectableModes.size()+
                selectableCommands.size()+
                selectablePowerUps.size()+
                selectableWeapons.size()+
                selectableSquares.size()+
                selectablePlayers.size();
    }

    /**
     * Returns a list of weapons owned by a player in a specific moment.
     * They are maximum 3 or temporary 4 (before discarding)
     * @return a clone of the list (modifies do not affect weapons of the player)
     */
    public List<Weapon> getWeapons() {
        return new ArrayList<>(weapons.keySet());
    }

    /**
     * Gets the map of weapons with their charge status
     * @return a map with all the weapons
     */
    public Map<Weapon, Boolean> getWeaponAsMap(){
        return weapons;
    }

    /**
     * Adds a weapon to the player. The maximum number of weapon is managed by the caller.
     * @param w the weapon to add
     */
    public void addWeapon (Weapon w) {
        if (w != null && !getWeapons().contains(w)){
            weapons.put(w, true);
        }
    }

    /**
     * Remove a weapon from the player
     * @param w the weapon to remove
     */
    public void removeWeapon(Weapon w){
        if (w!=null && getWeapons().indexOf(w) != -1){
            weapons.remove(w);
        }
    }

    /**
     * Gets the weapons currently loaded
     * @return clone list of loaded weapons
     */
    public List<Weapon> getLoadedWeapons(){
        List<Weapon> result = new ArrayList<>();
        for (Weapon w : getWeapons()){
            if (weapons.get(w) == true) {
                result.add(w);
            }
        }
        return result;
    }

    /**
     * Gets the weapons currently unloaded
     * @return clone list of unloaded weapons
     */
    public List<Weapon> getUnloadedWeapons(){
        List<Weapon> result = new ArrayList<>();
        for (Weapon w : getWeapons()){
            if (weapons.get(w) == false) {
                result.add(w);
            }
        }
        return result;
    }

    /**
     * Gets the powerups currently hold by the player
     * @return reference to the actual list of PowerUps
     */
    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    /**
     * adds a powerup to the player
     * @param po powerup to add
     */
    public void addPowerUp(PowerUp po){
        if (po != null && !powerUps.contains(po)){
            powerUps.add(po);
        }
    }

    /**
     * Removes a powerup from the player, if it is present
     * @param po powerup to remove
     * @return false if the powerup was already absent
     */
    public boolean removePowerUp(PowerUp po){
        if (po != null){
            return powerUps.remove(po);
        } else {
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

    /**
     * Sets the pending ammos of the player (ammos he has to pay at the moment)
     * @param pending the cash he has to pay
     */
    public void setPending(Cash pending) {
        this.pending = new Cash(pending);
    }

    /**
     * Sets the credit ammos of the player (ammos he has already paid in powerups)
     * @param credit
     */
    public void setCredit(Cash credit) {
        this.credit = new Cash(credit);
    }

    /**
     * Verifies if the player can afford a certain payment, considering his current ammos and powerups
     * @param amount payment amount
     * @return true if the player could afford that payment
     */
    public boolean canAfford(Cash amount){
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

    /**
     * Checks if the player is ever born
     * @return true if the player has spawned at least once
     */
    public boolean isBorn() {
        return isBorn;
    }

    /**
     * Sets the flag isBorn for the player
     * @param born the value to set
     */
    public void setBorn(boolean born) {
        isBorn = born;
    }

    /**
     * Checks  if the player is before the first player
     * @return true if it is before the first player
     */
    public boolean isBeforeFirst() {
        return isBeforeFirst;
    }

    /**
     * Sets the frenzy mode for this player
     * It is set when switching to final frenzy
     * @param beforeFirst value to set
     */
    public void setBeforeFirst(boolean beforeFirst) {
        isBeforeFirst = beforeFirst;
    }

    /**
     * Returns the number of power ups of a specified type owned by the player
     * @param type the type of powerups
     * @return the number of powerups owned
     */
    public int howManyPowerUps(PowerUpType type){
        int result = 0;
        for (PowerUp po : powerUps){
            if (po.getType() == type) result++;
        }
        return result;
    }

    /**
     * Checks if the player is currently alive (it is born and not dead)
     * @return true if it is alive
     */
    public boolean isAlive(){
        if(getDamageTrack().getDamageList().size() < 11){
            return true;
        }
        return false;
    }

    /**
     * Checks if the player has at least an item selectable at the moment
     * @return true if the player has at least one selectable item
     */
    public boolean hasSelectables(){
        return howManySelectables()>0;
    }

    /**
     * Test-only method. It returns a description of the selectables of the player.
     * @return A string with al the selectable items.
     */
    public String selectablesToString(){
        StringBuilder result = new StringBuilder();
        if (!selectableSquares.isEmpty()) {
            result.append("sq:\n");
            for (Square s : selectableSquares) {
                result.append("\t"+selectableSquares.indexOf(s)+")"+s.getShortDescription());
            }
            result.append("\n");
        }
        if (!selectableWeapons.isEmpty()) {
            result.append("wp:\n");
            for (Weapon w : selectableWeapons) {
                result.append("\t"+selectableWeapons.indexOf(w)+")"+w.getName());
                if (state == GRAB_WEAPON) result.append(" ("+w.getDiscountedCost()+")");
            }
            result.append("\n");
        }
        if (!selectablePowerUps.isEmpty()) {
            result.append("pow:\n");
            for (PowerUp po : selectablePowerUps) {
                result.append("\t"+selectablePowerUps.indexOf(po)+")"+po.toString());
            }
            result.append("\n");
        }
        if (!selectablePlayers.isEmpty()) {
            result.append("pl:\n");
            for (Player p : selectablePlayers) {
                result.append("\t"+selectablePlayers.indexOf(p)+")"+p.getName());
            }
            result.append("\n");
        }
        if (!selectableCommands.isEmpty()) {
            result.append("cmd:\n");
            for (Command co : selectableCommands) {
                result.append("\t"+selectableCommands.indexOf(co)+")"+co.toString());
            }
            result.append("\n");
        }
        if (!selectableModes.isEmpty()) {
            result.append("mod:\n");
            for (Mode m : selectableModes) {
                result.append("\t"+selectableModes.indexOf(m)+")"+m.getTitle());
            }
            result.append("\n");
        }
        if (!selectableActions.isEmpty()) {
            result.append("act:\n");
            for (Action a : selectableActions) {
                result.append("\t"+selectableActions.indexOf(a)+")"+a.toString());
            }
            result.append("\n");
        }
        if (!selectableColors.isEmpty()) {
            result.append("col:\n");
            for (AmmoColor c : selectableColors) {
                result.append("\t"+selectableColors.indexOf(c)+")"+c.toString());
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Checks if the specified weapon is loaded
     * @param w the involved weapon
     * @return true if the weapon is loaded
     */
    public boolean isLoaded(Weapon w){
        if (getWeapons().contains(w)){
            return weapons.get(w);
        }
        else return false;
    }

    /**
     * Set the load status of a weapon owned by the player
     * @param w the involved weapon
     * @param load teh value of charge to set
     */
    public void setLoad(Weapon w, boolean load){
        weapons.replace(w, load);
    }

    /**
     * Gets all the powerups of the specified color owned by the player
     * @param colors the involved color
     * @return the list of powerups
     */
    public List<PowerUp> getPowerUpsOfColors(Cash colors){
        List<PowerUp> result = new ArrayList<>();
        for (PowerUp po : powerUps){
            if(colors.containsColor(po.getColor())){
                result.add(po);
            }
        }
        return result;
    }

    /**
     * Gets all the powerups of the specified type owned by the player
     * @param type the involved type
     * @return the list of powerups
     */
    public List<PowerUp> getPowerUpsOfType(PowerUpType type){
        List<PowerUp> result = new ArrayList<>();
        for (PowerUp po : powerUps){
            if(po.getType() == type){
                result.add(po);
            }
        }
        return result;
    }

    /**
     * Sets points of the player
     * @param points the value to set
     */
    public void setPoints(int points){
        this.points = points;
    }

    /**
     * Sets the weapons of the player, with their charge status
     * @param weapons the map of weapon/charge to set
     */
    public void setWeapons(Map<Weapon, Boolean> weapons){
        this.weapons = weapons;
    }

    /**
     * Set the powerups owned by the player
     * @param powerUps the list of powerups to set
     */
    public void setPowerUps(List<PowerUp> powerUps){
        this.powerUps = powerUps;
    }

    /**
     * Sets the damage track of the player
     * @param newDamageTrack the damage track to set
     */
    public void setDamageTrack(DamageTrack newDamageTrack){
        if (damageTrack != null){
            damageTrack = newDamageTrack;
        }
    }

    /**
     * Checks if the player has another turn in the match
     * @return true if it has another turn
     */
    public boolean hasAnotherTurn() {
        return hasAnotherTurn;
    }

    /**
     * Sets the value of the flag hasAnotherTurn
     * @param anotherTurn the value to set
     */
    public void setAnotherTurn(boolean anotherTurn) {
        this.hasAnotherTurn = anotherTurn;
    }

    /**
     * Gets a string with the name of the player
     * @return the name of the player
     */
    @Override
    public String toString() {
        return name;
    }
}
