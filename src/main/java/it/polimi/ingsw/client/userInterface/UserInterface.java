package it.polimi.ingsw.client.userInterface;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;

import java.util.Map;

public interface UserInterface {

    void UpdateStartMatch(MatchView matchView);

    void showConnectionSelection();

    void showLogin();

    void printLoginMessage(String text, boolean loginSuccessful);

    //sets everything up, so that the user can make a selection
    // (renders the complete view of match, display selectables, activates user interaction, ...)
    void showAndAskSelection();

    void updateConnection();

    void updateLayout();

    void updateKillshotTrack();

    void updateCurrentPlayer();

    void updatePlayer(PlayerView updated);

    void updatePayment();

    void updateWeapons(PlayerView player);

    void updatePowerUp(PlayerView player);

    void updateDamage(PlayerView player);

    void updateSelectables();

    void printError(String message);

    void showGameOver(Map<PlayerView, Integer> rank, Map<PlayerView, Integer> points);

}