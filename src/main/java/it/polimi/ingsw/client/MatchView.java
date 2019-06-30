package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.enums.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchView {
    private LayoutView layout;
    private DecksView decks;
    private MyPlayer myPlayer;
    private List<PlayerView> allPlayers;
    private PlayerView currentPlayer;
    private Map<String, Boolean> connections;

    private int skulls;
    private List<Map<PlayerView, Integer>> track;
    private boolean frenzyOn;

    private ClientParser parser;

    public MatchView(String myName, int layoutConfig, List<String> players, List<PlayerColor> colors, Map<String, Boolean> connections) {
        parser= new ClientParser();

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

    public List<PlayerView> getOtherPlayers(){
        List<PlayerView> result = new ArrayList<>(allPlayers);
        result.remove(myPlayer);
        return result;
    }

    public PlayerView getPlayerFromName(String name){
        for (PlayerView p : allPlayers){
            if (p.getName().equals(name)) return p;
        }
        return null;
    }

    public LayoutView getLayout() {
        return layout;
    }

    public DecksView getDecks() {
        return decks;
    }

    public MyPlayer getMyPlayer() {
        return myPlayer;
    }

    public List<PlayerView> getAllPlayers() {
        return allPlayers;
    }

    public PlayerView getCurrentPlayer() {
        return currentPlayer;
    }

    public int getSkulls() {
        return skulls;
    }

    public List<Map<PlayerView, Integer>> getTrack() {
        return track;
    }

    public boolean isFrenzyOn() {
        return frenzyOn;
    }

    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    public void setTrack(List<Map<PlayerView, Integer>> track) {
        this.track = track;
    }

    public void setFrenzyOn(boolean frenzyOn) {
        this.frenzyOn = frenzyOn;
    }

    public void setCurrentPlayer(PlayerView currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<PlayerView> getPlayersOn(SquareView square){
        List<PlayerView> result = new ArrayList<>();
        for (PlayerView p : getAllPlayers()){
            if (square.equals(p.getSquarePosition())){  //if squarePosition si null it returns false, no problem
                result.add(p);
            }
        }
        return result;
    }

    public Map<String, Boolean> readConnections() {
        return new HashMap<>(connections);
    }
}
