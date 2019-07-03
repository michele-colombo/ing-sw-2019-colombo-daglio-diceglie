package it.polimi.ingsw.client.user_interface;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;

import java.util.Map;

/**
 * Implemented by Cli and Gui, make them updates
 */
public interface UserInterface {

    void updateStartMatch(MatchView matchView);

    /**
     * Show selection of connection (socket or rmi)
     */
    void showConnectionSelection();

    /**
     * Show login dialog and ask username
     */
    void showLogin();

    /**
     * Shows the login response of the server
     * @param text text of message
     * @param loginSuccessful true if login goes right
     */
    void printLoginMessage(String text, boolean loginSuccessful);

    /**
     * Sets everything up, so that the user can make a selection
     * (renders the complete view of match, display selectables, activates user interaction, ...)
     */
    void showAndAskSelection();

    /**
     * Updates state of connections of all the players
     */
    void updateConnection();

    /**
     * Updates layout status
     */
    void updateLayout();

    /**
     * Updates killshot track
     */
    void updateKillshotTrack();

    /**
     * update  who is the current player
     */
    void updateCurrentPlayer();

    /**
     * Updates given PlayerView
     * @param updated playerView to be updated
     */
    void updatePlayer(PlayerView updated);

    /**
     * Updates what this client has to pay and what has already paid
     */
    void updatePayment();

    /**
     * Updates player's weapons
     * @param player playerView to be updated
     */
    void updateWeapons(PlayerView player);

    /**
     * Updates player's power ups
     * @param player playerView to be updated
     */
    void updatePowerUp(PlayerView player);

    /**
     * Updates player's damage track
     * @param player playerView to be updated
     */
    void updateDamage(PlayerView player);

    /**
     * Updates selectable lists
     */
    void updateSelectables();

    /**
     * show a error
     * @param message the text of the error
     */
    void printError(String message);

    /**
     * show ranking grid
     * @param rank maps each playerView her final rank
     * @param points maps each playerView with her points
     */
    void showGameOver(Map<PlayerView, Integer> rank, Map<PlayerView, Integer> points);

}