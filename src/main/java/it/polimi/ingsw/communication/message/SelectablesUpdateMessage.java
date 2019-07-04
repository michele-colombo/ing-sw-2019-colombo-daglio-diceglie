package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the player's update of selectables weapons, squares, modes, commands, action, colors, players and power ups
 */
public class SelectablesUpdateMessage implements MessageVisitable, Serializable {
    /**
     * Contains the names of selectable weapons
     */
    private List<String> selectableWeapons;
    /**
     * Contains the names of selectable squares
     */
    private List<String> selectableSquares;
    /**
     * Contains the names of selectable modes
     */
    private List<String> selectableModes;
    /**
     * Contains the names of selectable commands
     */
    private List<String> selectableCommands;
    /**
     * Contains the names of selectable actions
     */
    private List<String> selectableActions;
    /**
     * Contains the names of selectable colors
     */
    private List<String> selectableColors;
    /**
     * Contains the names of selectable players
     */
    private List<String> selectablePlayers;
    /**
     * Contains the names of selectable power ups
     */
    private List<String> selectablePowerUps;
    /**
     * Name of player's current weapon
     */
    private String currWeapon;

    /**
     * Creates a SelectablesUpdateMessage with given parameters
     * @param selectableWeapons names of selectables weapons
     * @param selectableSquares names of selectables squares
     * @param selectableModes names of selectables modes
     * @param selectableCommands names of selectables commands
     * @param selectableActions names of selectables actions
     * @param selectableColors names of selectables colors
     * @param selectablePlayers names of selectables players
     * @param selectablePowerUps names of selectables power ups
     * @param currWeapon name of current weapon
     */
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

    /**
     * Gets selectableWeapons
     * @return selectableWeapons
     */
    public List<String> getSelectableWeapons() {
        return selectableWeapons;
    }

    /**
     * Gets selectableSquares
     * @return selectableSquares
     */
    public List<String> getSelectableSquares() {
        return selectableSquares;
    }

    /**
     * Gets selectableModes
     * @return selectableModes
     */
    public List<String> getSelectableModes() {
        return selectableModes;
    }

    /**
     * Gets selectableCommands
     * @return selectableCommands
     */
    public List<String> getSelectableCommands() {
        return selectableCommands;
    }

    /**
     * Gets selectableActions
     * @return selectableActions
     */
    public List<String> getSelectableActions() {
        return selectableActions;
    }

    /**
     * Gets selectableColors
     * @return selectableColors
     */
    public List<String> getSelectableColors() {
        return selectableColors;
    }

    /**
     * Gets selectablePLayers
     * @return selectablePlayers
     */
    public List<String> getSelectablePlayers() {
        return selectablePlayers;
    }

    /**
     * Gets selectablePowerUps
     * @return selectablePowerUps
     */
    public List<String> getSelectablePowerUps() {
        return selectablePowerUps;
    }

    /**
     * Gets currWeapon
     * @return currWeapon
     */
    public String getCurrWeapon() {
        return currWeapon;
    }

    /**
     * Method used to properly recognize dynamic type of Message
     * @param messageVisitor messageVisitor who "visits" this message
     */
    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
