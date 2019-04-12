package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Action {
    private List<MicroAction> microActions;

    private Weapon currWeapon;
    private List<Mode> selectedModes;
    private List<Effect> currEffects;
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
}
