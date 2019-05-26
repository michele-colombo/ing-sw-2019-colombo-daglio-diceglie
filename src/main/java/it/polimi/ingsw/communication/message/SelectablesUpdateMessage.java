package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.util.List;

public class SelectablesUpdateMessage extends MessageVisitable {
    private List<String> selectableWeapons;
    private List<String> selectableSquares;
    private List<String> selectableModes;
    private List<String> selectableCommands;
    private List<String> selectableActions;
    private List<String> selectableColors;
    private List<String> selectablePlayers;
    private List<String> selectablePowerUps;
    private String currWeapon;

    public SelectablesUpdateMessage(List<String> selectableWeapons, List<String> selectableSquares, List<String> selectableModes, List<String> selectableCommands, List<String> selectableActions, List<String> selectableColors, List<String> selectablePlayers, List<String> selectablePowerUps, String currWeapon) {
        this.selectableWeapons = selectableWeapons;
        this.selectableSquares = selectableSquares;
        this.selectableModes = selectableModes;
        this.selectableCommands = selectableCommands;
        this.selectableActions = selectableActions;
        this.selectableColors = selectableColors;
        this.selectablePlayers = selectablePlayers;
        this.selectablePowerUps = selectablePowerUps;
        this.currWeapon = currWeapon;
    }

    public List<String> getSelectableWeapons() {
        return selectableWeapons;
    }

    public List<String> getSelectableSquares() {
        return selectableSquares;
    }

    public List<String> getSelectableModes() {
        return selectableModes;
    }

    public List<String> getSelectableCommands() {
        return selectableCommands;
    }

    public List<String> getSelectableActions() {
        return selectableActions;
    }

    public List<String> getSelectableColors() {
        return selectableColors;
    }

    public List<String> getSelectablePlayers() {
        return selectablePlayers;
    }

    public List<String> getSelectablePowerUps() {
        return selectablePowerUps;
    }

    public String getCurrWeapon() {
        return currWeapon;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
