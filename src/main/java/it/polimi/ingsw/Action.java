package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Action {
    private List<MicroAction> microActions;

    private SpawnSquare currSpawnSquare;
    private Weapon currWeapon;
    private Mode currMode;
    private List<Mode> selectedModes;
    private List<Effect> currEffects;
    private List<Player> damaged;
    private boolean incrementActionCounter;
    private boolean activateOnlyReloads;


    public Action (boolean incrementActionCounter, boolean activateOnlyReloads) {
        microActions = new ArrayList<>();
        selectedModes = new ArrayList<>();
        currEffects = new ArrayList<>();
        this.incrementActionCounter = incrementActionCounter;
        this.activateOnlyReloads = activateOnlyReloads;
    }

    public Action (boolean incrementActionCounter, boolean activateOnlyReloads, MicroAction microAction) {
        this(incrementActionCounter, activateOnlyReloads);
        add(microAction);
    }

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

    public List<MicroAction> getMicroActions() {
        return microActions;
    }

    public Weapon getCurrWeapon() {
        return currWeapon;
    }

    public List<Mode> getSelectedModes() {
        return selectedModes;
    }

    public List<Effect> getCurrEffects() {
        return currEffects;
    }

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

    public void setSelectedModes(List<Mode> selectedModes) {
        this.selectedModes = selectedModes;
    }

    public void setCurrEffects(List<Effect> currEffects) {
        this.currEffects = currEffects;
    }
}
