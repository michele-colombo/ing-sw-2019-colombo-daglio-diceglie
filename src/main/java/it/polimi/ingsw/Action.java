package it.polimi.ingsw;

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
     * Since the reload has to be the last action of the turn, once reloaded one weapon
     * is only possible to reload weapons.
     */
    private boolean activateOnlyReloads;

    /**
     * Support variable to store a PowerUp, if one is selected in the current action.
     * Used for paying for the targeting scope
     */
    private PowerUp currPowerUp;

    //added by Giuseppe Diceglie
    /**
     * it's the square chosen by the player. useful for many weapons
     */
    private Square chosenSquare;

    private int waitingFor;


    /**
     * Constructs an action specifying the effects on the turn status
     * @param incrementActionCounter true if action has to increment the action counter (move, grab, shoot)
     * @param activateOnlyReloads true for reload action
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
     * @param activateOnlyReloads true for reload action
     * @param microAction the first microAction of the action
     */
    public Action (boolean incrementActionCounter, boolean activateOnlyReloads, MicroAction microAction) {
        this(incrementActionCounter, activateOnlyReloads);
        add(microAction);
    }

    /**
     * Builds a string according to the microActions present (with the multiplicity for movements)
     * @return
     */
    @Override
    public String toString(){
        String s = "";
        for (MicroAction ma : microActions){
            s = s + ma;
        }
        return s;
    }

    public Mode getCurrMode() {
        return currMode;
    }

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
     * @param microAction
     */
    public void add (MicroAction microAction) {
        if (microAction != null) {
            microActions.add(microAction);
        }
    }

    public boolean isIncrementActionCounter() {
        return incrementActionCounter;
    }

    public boolean isActivateOnlyReloads() {
        return activateOnlyReloads;
    }

    public SpawnSquare getCurrSpawnSquare() {
        return currSpawnSquare;
    }

    public void setCurrSpawnSquare(SpawnSquare currSpawnSquare) {
        this.currSpawnSquare = currSpawnSquare;
    }

    public void setCurrWeapon(Weapon currWeapon) {
        this.currWeapon = currWeapon;
    }

    public void setCurrPowerUp(PowerUp po){
        this.currPowerUp = po;
    }

    public PowerUp getCurrPowerUp() {
        return currPowerUp;
    }

    public void setSelectedModes(List<Mode> selectedModes) {
        this.selectedModes = selectedModes; //should set a shallow copy instead?
    }

    public void setCurrEffects(List<Effect> currEffects) {
        this.currEffects = currEffects; //should set a shallow copy instead?
    }

    public void addEffects(List<Effect> effects){
        this.currEffects.addAll(effects);
    }

    public int getWaitingFor() {
        return waitingFor;
    }

    public void setWaitingFor(int waitingFor) {
        this.waitingFor = waitingFor;
    }

    public void incrWaitingFor(int incr){
        this.waitingFor += incr;
    }

    //added by Giuseppe Diceglie

    public Player getLastDamaged(){
        if(!damaged.isEmpty()) {
            return damaged.get(damaged.size() - 1);
        }
        return null;
    }

    public List<Player> getDamaged(){
        return damaged;
    }

    public void addDamaged(Player damagedToAdd){
        damaged.add(damagedToAdd);
    }

    public Square getChosenSquare() {
        return chosenSquare;
    }
}
