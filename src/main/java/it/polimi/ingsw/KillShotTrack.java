package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KillShotTrack {
    private int skulls;
    private Map<Player, Integer> killingCounter;
    private Map<Player, Integer> killingOrder;
    private int order;
    private List<Map<Player, Integer>> track;
    private final int biggerScore;

    public KillShotTrack(int skulls){
        this.skulls = skulls;
        killingCounter = new HashMap<>();
        killingOrder = new HashMap<>();
        track = new ArrayList<>();
        biggerScore = 8;
        order = 1;
    }

    public int getSkulls() {
        return skulls;
    }

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

            if (!killingOrder.containsKey(p)){
                killingOrder.put(p, order);
                order++;
            }
        }
        removeSkull();
    }

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

    private Player getGreatestKillerIn(Map<Player, Integer> killers){
        int max = -1;
        Player result = null;
        for (Player p : killers.keySet()){
            if (killers.get(p) > max){
                max = killers.get(p);
                result = p;
            } else if (killers.get(p) == max){
                if (result == null || killingOrder.get(p)<killingOrder.get(result)){
                    result = p;
                }
            }
        }
        return result;
    }

    private int nextScore(int currentScore){
        if(currentScore == 2 || currentScore == 1){
            return 1;
        }
        return currentScore - 2;
    }

    private boolean removeSkull(){
        skulls--;
        if (skulls <= 0) {
            skulls = 0;
            return true;
        }
        return false;
    }
}
