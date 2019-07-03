package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Is the part of board that keeps track of kills and remaining skulls.
 * It embeds part of the logic for final point calculation.
 */
public class KillShotTrack {
    /**
     * keeps the number of skulls currently remaining in the match
     */
    private int skulls;
    /**
     * A support map used to keep track of the killings of each player
     */
    private Map<Player, Integer> killingCounter;
    /**
     * A list to keep track of the order of killing
     */
    private List<Player> killingOrder;
    /**
     * The actual killshot track, each slot is a tuple (Player, number_of_drops)
     */
    private List<Map<Player, Integer>> track;
    /**
     * A constant used to determine the points to give to each player
     */
    private final int biggerScore;

    /**
     * Constructs a killshot track with the specified number of initial skulls.
     * @param skulls Initial skulls
     */
    public KillShotTrack(int skulls){
        this.skulls = skulls;
        killingCounter = new HashMap<>();
        killingOrder = new ArrayList<>();
        track = new ArrayList<>();
        biggerScore = 8;
    }

    /**
     * Returns the ordinal position of killing of hte specified player
     * @param p the player involved
     * @return An integer representing the ordinal position. 0 is the first.
     */
    public int getKillingOrder(Player p){
        return killingOrder.indexOf(p);
    }

    /**
     * Returns the number of drops of the specified player on the killshot track
     * If a player overkills someone, it is counted twice
     * @param p the player involved
     * @return An integer representing the number of drops on the killshot track
     */
    public int getKillingOf(Player p){
        return killingCounter.getOrDefault(p, 0);
    }

    public int getSkulls() {
        return skulls;
    }

    /**
     * Adds the drops corresponding to killing (eventually with overkill) to the killshot track.
     * Support variables are updated, too.
     * @param killed A tuple (player, number_of_drops_to_add)
     */
    public void addKilled(Map<Player, Integer> killed){ //map is actually a tuple
        track.add(killed);
        int temp;
        for (Player p : killed.keySet()){
            if (killingCounter.containsKey(p)){
                temp = killingCounter.get(p);
            } else {
                temp = 0;
            }
            killingCounter.put(p, temp + killed.get(p));

            if (!killingOrder.contains(p)){
                killingOrder.add(p);
            }
        }
    }

    /**
     * Returns the number of points to add to each player. Only players who killed at least once are in the map
     * @return a map (player, points_to_add_to_the_player)
     */
    public Map<Player, Integer> score(){
        int currPoints = biggerScore;
        Map<Player, Integer> result = new HashMap<>();
        Map<Player, Integer> tempCounter = new HashMap<>(killingCounter);
        while (tempCounter.size()>0){
            Player greatestKiller = getGreatestKillerIn(tempCounter);
            result.put(greatestKiller, currPoints);
            tempCounter.remove(greatestKiller);
            currPoints = nextScore(currPoints);
        }
        return result;
    }

    /**
     * A support method used to get the greatest killer (counting overkills) in a certain list of players
     * @param killers The list of players to analyze
     * @return the player who has killed most in the list
     */
    private Player getGreatestKillerIn(Map<Player, Integer> killers){
        int max = -1;
        Player result = null;
        for (Player p : killers.keySet()){
            if (killers.get(p) > max){
                max = killers.get(p);
                result = p;
            } else if (killers.get(p) == max){
                if (result == null || (getKillingOrder(p) > -1 && getKillingOrder(p)<getKillingOrder(result))){
                    result = p;
                }
            }
        }
        return result;
    }

    /**
     * Support method used to get the succession of points to add to the players
     * @param currentScore Points given to the last player scored
     * @return Points to give to the next player to be scored
     */
    private int nextScore(int currentScore){
        if(currentScore == 2 || currentScore == 1){
            return 1;
        }
        return currentScore - 2;
    }

    /**
     * Decreases the number of skulls on the killshot track
     */
    public void removeSkull(){
        skulls--;
        if (skulls <= 0) {
            skulls = 0;
        }
    }

    /**
     * clears the killing counter
     */
    public void clearKillingCounter() {
        killingCounter.clear();
    }

    /**
     * clears the killing order
     */
    public void clearKillingOrder() {
        killingOrder.clear();
    }

    /**
     * clears the kill shot track
     */
    public void clearTrack(){track.clear();}

    /**
     * Getter for the track
     * @return the reference to the actual track
     */
    public List<Map<Player, Integer>> getTrack() {
        return track;
    }

    /**
     * Sets the number of skulls currently on the killshot track
     * @param skulls number of skulls
     */
    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    /**
     * Sets the track
     * @param track the new track to set
     */
    public void setTrack(List<Map<Player, Integer>> track) {
        this.track = track;
    }
}
