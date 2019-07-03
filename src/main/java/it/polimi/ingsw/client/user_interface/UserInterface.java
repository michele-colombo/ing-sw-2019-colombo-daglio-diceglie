package it.polimi.ingsw.client.user_interface;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;

import java.util.Map;

/**
 * Implemented by Cli and Gui, make them updates
 */
public interface UserInterface {

    /**
     * Notifys the starting of the match
     * @param matchView update of Match
     */
    void updateStartMatch(MatchView matchView);

    /**
     * Shows connection selection, at the starting of Cli/Gui
     */
    void showConnectionSelection();

    /**
     * Shows login interface
     */
    void showLogin();

    /**
     * Shows LoginMessage received from server, after an attempting of login
     * @param text content of the message
     * @param loginSuccessful true if login is ok, else false
     */
    void printLoginMessage(String text, boolean loginSuccessful);

    /**
     * Actives user interface, so that a player can make a choice
     */
    void showAndAskSelection();

    /**
     * Updates players' connection state
     */
    void updateConnection();

    /**
     * Updates layout
     */
    void updateLayout();

    /**
     * Updates killshot track
     */
    void updateKillshotTrack();

    /**
     * Updates current player
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
     * Updates list of selectables of the client
     */
    void updateSelectables();

    /**
     * Show error
     * @param message text to be shown
     */
    void printError(String message);

    /**
     * Shows final ranks
     * @param rank maps each playerView her final rank
     * @param points maps each playerView with her points
     */
    void showGameOver(Map<PlayerView, Integer> rank, Map<PlayerView, Integer> points);

}