package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

public class Action {
    /**
     * List of microActions composing the current action
     */
    private List<MicroAction> microActions;
    /**
     * Support variable to store a SpawnSquare, if one is selected in the current action.
     * used for grabbing a weapon.
     */
    private SpawnSquare currSpawnSquare;
    /**
     * Support variable to store a Weapon, if one is selected in the current action.
     * Used for paying a weapon and shoot
     */
    private Weapon currWeapon;
    /**
     * Support variable to store a Mode, if one is selected in the current action.
     * Used for payment.
     */
    private Mode currMode;
    /**
     * Stores the modes selected in the current action.
     * Used for create the list of selectable modes, according to their precedence
     */
    private List<Mode> selectedModes;
    /**
     * Support variable to store the effects currently in process.
     * It is updated by adding a mode or using a power up
     */
    private List<Effect> currEffects;
    /**
     * Support variable to keep track of the player damaged (not just marked) in the current shoot action
     */
    private List<Player> damaged;
    /**
     * If true, the action counter has to be incremented.
     * True for actual turn actions (move, grab, shoot)
     */
    private boolean incrementActionCounter;
    /**
     * If true, the player can only reloads weapon.
     * Since the setLoad has to be the last action of the turn, once reloaded one weapon
     * is only possible to setLoad weapons.
     */
    private boolean activateOnlyReloads;

    /**
     * Support variable to store a PowerUp, if one is selected in the current action.
     * Used for paying for the targeting scope
     */
    private PowerUp currPowerUp;

    //added by Giuseppe Diceglie
    /**
     * it's the square chosen by the player, used for shooting.
     */
    private Square chosenSquare;

    /**
     * It's the player chosen during action, used for shooting.
     */
    private Player chosenPlayer;


    /**
     * Constructs an action specifying the effects on the turn status
     * @param incrementActionCounter true if action has to increment the action counter (move, grab, shoot)
     * @param activateOnlyReloads true for setLoad action
     */
    public Action (boolean incrementActionCounter, boolean activateOnlyReloads) {
        microActions = new ArrayList<>();
        selectedModes = new ArrayList<>();
        currEffects = new ArrayList<>();
        damaged = new ArrayList<>();
        this.incrementActionCounter = incrementActionCounter;
        this.activateOnlyReloads = activateOnlyReloads;
        currSpawnSquare = null;
        currWeapon = null;
        currMode = null;
        currPowerUp = null;
    }

    /**
     * Constructs an action specifying the effects on the turn status and adding the first microAction
     * @param incrementActionCounter true if action has to increment the action counter (move, grab, shoot)
     * @param activateOnlyReloads true for setLoad action
     * @param microAction the first microAction of the action
     */
    public Action (boolean incrementActionCounter, boolean activateOnlyReloads, MicroAction microAction) {
        this(incrementActionCounter, activateOnlyReloads);
        add(microAction);
    }

    /**
     * Builds a string according to the microActions present (with the multiplicity for movements)
     * @return the identifier of the action
     */
    @Override
    public String toString(){
        String s = "";
        for (MicroAction ma : microActions){
            s = s + ma.toString();
        }
        return s;
    }

    /**
     * Getter for the current mode
     * @return the mode stored
     */
    public Mode getCurrMode() {
        return currMode;
    }

    /**
     * setter for the current mode
     * @param currMode the mode to set
     */
    public void setCurrMode(Mode currMode) {
        this.currMode = currMode;
    }

    /**
     * Returns the list of microAction
     * @return the reference to the actual list (can be modified)
     */
    public List<MicroAction> getMicroActions() {
        return microActions;
    }

    /**
     * getter for the current weapon (the one relative to action)
     * @return the actual weapon
     */
    public Weapon getCurrWeapon() {
        return currWeapon;
    }

    /**
     * Returns the list of modes selected in the current action
     * @return the reference to the actual list (can be modified)
     */
    public List<Mode> getSelectedModes() {
        return selectedModes;
    }

    /**
     * Returns the list of effects currently present in the action
     * @return the reference to the actual list (can be modified)
     */
    public List<Effect> getCurrEffects() {
        return currEffects;
    }

    /**
     * Adds a microAction to the list. It is used to build the action.
     * @param microAction the microAction to add
     */
    public void add (MicroAction microAction) {
        if (microAction != null) {
            microActions.add(microAction);
        }
    }

    /**
     * Getter for IncrementActionCounter
     * @return true if the action increments the counter (counts as one of the two permitted)
     */
    public boolean isIncrementActionCounter() {
        return incrementActionCounter;
    }

    /**
     * Getter for ActivateOnlyReloads
     * @return true if after this action only reloads are allowed
     */
    public boolean isActivateOnlyReloads() {
        return activateOnlyReloads;
    }

    /**
     * Getter for the current spawnSquare
     * @return the current spawnSquare
     */
    public SpawnSquare getCurrSpawnSquare() {
        return currSpawnSquare;
    }

    /**
     * Setter for the current spawnSquare
     * @param currSpawnSquare
     */
    public void setCurrSpawnSquare(SpawnSquare currSpawnSquare) {
        this.currSpawnSquare = currSpawnSquare;
    }

    /**
     * Setter for the current weapon
     * @param currWeapon
     */
    public void setCurrWeapon(Weapon currWeapon) {
        this.currWeapon = currWeapon;
    }

    /**
     * Setter for the current powerUp
     * @param po
     */
    public void setCurrPowerUp(PowerUp po){
        this.currPowerUp = po;
    }

    /**
     * getter for the current powerUp
     * @return
     */
    public PowerUp getCurrPowerUp() {
        return currPowerUp;
    }

    /**
     * Sets the current effects
     * @param currEffects
     */
    public void setCurrEffects(List<Effect> currEffects) {
        this.currEffects = currEffects;
    }

    //added by Giuseppe Diceglie

    /**
     * Gets the last player damaged during shooting
     * @return null if no player has been already damaged in this action
     */
    public Player getLastDamaged(){
        if(!damaged.isEmpty()) {
            return damaged.get(damaged.size() - 1);
        }
        return null;
    }

    /**
     * Gets the list of player damaged during this action
     * @return an empty list if nobody was damaged during this action
     */
    public List<Player> getDamaged(){
        return damaged;
    }

    /**
     * Adds a player (which has just been damaged)
     * @param damagedToAdd the player just damaged
     */
    public void addDamaged(Player damagedToAdd){
        damaged.add(damagedToAdd);
    }

    /**
     * Gets the chosen square during shooting
     * @return
     */
    public Square getChosenSquare() {
        return chosenSquare;
    }

    /**
     * Gets the chosen player during shooting
     * @return
     */
    public Player getChosenPlayer() {
        return chosenPlayer;
    }

    /**
     * Sets the chosen square. Used for shooting.
     * @param chosenSquare
     */
    public void setChosenSquare(Square chosenSquare) {
        this.chosenSquare = chosenSquare;
    }

    /**
     * Sets the chosen player. used for shooting.
     * @param chosenPlayer
     */
    public void setChosenPlayer(Player chosenPlayer) {
        this.chosenPlayer = chosenPlayer;
    }
}
