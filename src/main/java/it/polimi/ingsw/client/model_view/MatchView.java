package it.polimi.ingsw.client.model_view;

import it.polimi.ingsw.client.ClientParser;
import it.polimi.ingsw.server.model.enums.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents Match on client
 */
public class MatchView {
    /**
     * LayoutView of the match
     */
    private LayoutView layout;
    /**
     * DecksView of the match
     */
    private DecksView decks;
    /**
     * Representation of this client, during the match
     */
    private MyPlayer myPlayer;
    /**
     * Contains all players of the match
     */
    private List<PlayerView> allPlayers;
    /**
     * Current player of the match
     */
    private PlayerView currentPlayer;
    /**
     * Players' connection state during the match: true if online, false if offline
     */
    private Map<String, Boolean> connections;
    /**
     * Current skulls of the match
     */
    private int skulls;
    /**
     * Map a PlayerView with her number of kills on killshotTrack
     */
    private List<Map<PlayerView, Integer>> track;
    /**
     * True if match is in frenzy mode, else false
     */
    private boolean frenzyOn;

    /**
     * Creates a MatchView, initializing all attributes with given values
     * @param myName Player's name associated to this client
     * @param layoutConfig Layout configuration of the match
     * @param players All players of the match
     * @param colors Colors of the player of the match
     * @param connections Players' connection state of the match
     */
    public MatchView(String myName, int layoutConfig, List<String> players, List<PlayerColor> colors, Map<String, Boolean> connections) {
        ClientParser parser= new ClientParser();

        decks= new DecksView(parser.getWeapons(), parser.getPowerUps(), parser.getAmmoTiles());
        layout= parser.getLayout(layoutConfig);

        allPlayers = new ArrayList<>();
        int i = 0;
        for (String name : players){
            if (name.equals(myName)){
                myPlayer = new MyPlayer(name, colors.get(i));
                allPlayers.add(myPlayer);
            } else {
                allPlayers.add(new PlayerView(name, colors.get(i)));
            }
            i++;
        }
        track = new ArrayList<>();
        this.connections = connections;
        frenzyOn = false;
    }

    /**
     * Gets playerViews whose name if different from myPlayer's one
     * @return All PlayerViews whose name is different from myPlayer's one
     */
    public List<PlayerView> getOtherPlayers(){
        List<PlayerView> result = new ArrayList<>(allPlayers);
        result.remove(myPlayer);
        return result;
    }

    /**
     * Look for PlayerView whose associated name is name
     * @param name Name of searched PlayerView
     * @return A PlayerView whose corresponding name is name, if exists, else null
     */
    public PlayerView getPlayerFromName(String name){
        for (PlayerView p : allPlayers){
            if (p.getName().equals(name)) return p;
        }
        return null;
    }

    /**
     * Gets layout
     * @return layout
     */
    public LayoutView getLayout() {
        return layout;
    }

    /**
     * Gets decks
     * @return decks
     */
    public DecksView getDecks() {
        return decks;
    }

    /**
     * Gets myPlayer
     * @return myPlayer
     */
    public MyPlayer getMyPlayer() {
        return myPlayer;
    }

    /**
     * Gets allPlayers
     * @return allPlayers
     */
    public List<PlayerView> getAllPlayers() {
        return allPlayers;
    }

    /**
     * Gets currentPlayer
     * @return currentPlayer
     */
    public PlayerView getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets skulls
     * @return skulls
     */
    public int getSkulls() {
        return skulls;
    }

    /**
     * Gets track
     * @return track
     */
    public List<Map<PlayerView, Integer>> getTrack() {
        return track;
    }

    /**
     * Returns frenzyOn
     * @return frenzyOn
     */
    public boolean isFrenzyOn() {
        return frenzyOn;
    }

    /**
     * Set skulls
     * @param skulls Number to be set
     */
    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    /**
     * Set track
     * @param track track to be set
     */
    public void setTrack(List<Map<PlayerView, Integer>> track) {
        this.track = track;
    }

    /**
     * Set frenzyOn
     * @param frenzyOn frenzyOn to be set
     */
    public void setFrenzyOn(boolean frenzyOn) {
        this.frenzyOn = frenzyOn;
    }

    /**
     * Set current player
     * @param currentPlayer currentPlayer to be set
     */
    public void setCurrentPlayer(PlayerView currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     *  Looks for PlayerViews whose position is square
     * @param square SquareView of which to return PlayerViews on
     * @return A list of PlayerViews, whose position is square
     */
    public List<PlayerView> getPlayersOn(SquareView square){
        List<PlayerView> result = new ArrayList<>();
        for (PlayerView p : getAllPlayers()){
            if (square.equals(p.getSquarePosition())){  //if squarePosition si null it returns false, no problem
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Gets a new Map, containing all elements of connections
     * @return A new Map, containing all elements of connections
     */
    public Map<String, Boolean> readConnections() {
        return new HashMap<>(connections);
    }
}
