package it.polimi.ingsw.client.user_interface;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;

import java.util.Map;

public interface UserInterface {

    /**
     * update when match starts
     * @param matchView match of the game
     */
    void updateStartMatch(MatchView matchView);

    /**
     * show selection of connection (socket or rmi)
     */
    void showConnectionSelection();

    /**
     * show login dialog and ask username
     */
    void showLogin();

    /**
     * show the login response of the server
     * @param text text of message
     * @param loginSuccessful true if login goes right
     */
    void printLoginMessage(String text, boolean loginSuccessful);

    /**
     * sets everything up, so that the user can make a selection
     * (renders the complete view of match, display selectables, activates user interaction, ...)
     */
    void showAndAskSelection();

    /**
     * update state of connections of all the players
     */
    void updateConnection();

    /**
     * update layout status
     */
    void updateLayout();

    /**
     * update killshot track
     */
    void updateKillshotTrack();

    /**
     * update  who is the current player
     */
    void updateCurrentPlayer();

    /**
     * update attributes of a player
     * @param updated the player to update
     */
    void updatePlayer(PlayerView updated);

    /**
     * update a payment
     */
    void updatePayment();

    /**
     * update weapons of a player
     * @param player
     */
    void updateWeapons(PlayerView player);

    /**
     * update powerup of a player
     * @param player
     */
    void updatePowerUp(PlayerView player);

    /**
     * update a damage track of a player
     * @param player
     */
    void updateDamage(PlayerView player);

    /**
     * update selectable lists
     */
    void updateSelectables();

    /**
     * show a erron
     * @param message the text of the error
     */
    void printError(String message);

    /**
     * show ranking grid
     * @param rank
     * @param points
     */
    void showGameOver(Map<PlayerView, Integer> rank, Map<PlayerView, Integer> points);

}