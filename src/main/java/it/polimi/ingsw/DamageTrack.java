package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.max;

public abstract class DamageTrack {
    private List<Player> damageList;
    private Map<Player, Integer> markMap;
    private int skullsNumber;

    private final int biggerScore;
    private final int firstBlood;

    public DamageTrack(int biggerScore, int firstBlood){
        damageList= new ArrayList<>();
        markMap = new HashMap<>();
        skullsNumber = 0;
        this.biggerScore = biggerScore;
        this.firstBlood = firstBlood;
    }

    public void setMarkMap(Map<Player, Integer> markMap) {
        this.markMap = markMap;
    }

    public List<Player> getDamageList() {
        return damageList;
    }

    public Map<Player, Integer> getMarkMap() {
        return markMap;
    }

    public int getSkullsNumber() {
        return skullsNumber;
    }

    public void setSkullsNumber(int skullsNumber) {
        this.skullsNumber = skullsNumber;
    }

    public int getBiggerScore() {
        return biggerScore;
    }


    public int getFirstBlood() {
        return firstBlood;
    }


    public void resetDamages(){
        this.damageList.clear();
    }

    public void addMark(Player p, int markToAdd){
        Integer targets;

        if( markMap.containsKey(p) ){
            targets = markMap.get(p);
            targets = targets + markToAdd;
            if( targets > 3){
                targets = 3;
            }

            markMap.replace(p, targets);
        }
        else{
            if(markToAdd > 3){
                targets = 3;
            }
            else{
                targets = markToAdd;
            }
            markMap.put(p, targets);
        }
    }

    public void addDamage(Player p, int damage){
        int toAdd;

        if(markMap.containsKey(p)){       //why the getter is used instead of the private attribute markMap?
            toAdd= damage +  this.getMarkMap().get(p);
            this.markMap.remove(p);
        }
        else{
            toAdd = damage;
        }

        int i= 0;
        while(damageList.size() < 12 && i<toAdd){
            damageList.add(p);
            i++;
        }
    }

    public Map<Player, Integer> howDoTheyKilledYou(){

        Map result= new HashMap<Player, Integer>();

        if(damageList.size() == 11){
            result.put(damageList.get( damageList.size() - 1), 1);

        }
        if(damageList.size() == 12){
            result.put(damageList.get( damageList.size() - 1), 2);

        }

        return result;
    }

    public Map<Player, Integer> whoDamagedYou(){
        Map<Player, Integer> result = new HashMap<>();
        int i = 0;
        for(i=0; i< damageList.size(); i++){
            Player key = damageList.get(i);
            if(result.containsKey(key)){
                int old = result.get(key);
                result.replace(key, old + 1);
            }
            else{
                result.put(key, 1);
            }
        }
        return result;
    }

    public Player getMostPowerfulDamagerIn(Map<Player, Integer> damagers){

        Integer massimo= 0;

        Player result= null;
        for(Player p : damagers.keySet()) {
            if (damagers.get(p) > massimo) {
                result = p;
                massimo = damagers.get(p);
            } else if (damagers.get(p) == massimo) {
                if (result == null || this.getDamageList().indexOf(p) < this.getDamageList().indexOf(result)) {
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

    public Map<Player, Integer> score(){
        if(getDamageList().isEmpty()){
            return new HashMap<Player, Integer>();
        }

        Map<Player, Integer> damagers = this.whoDamagedYou();
        Map<Player, Integer> result = new HashMap<>();
        int currentScore= getBiggerScore();

        for(int i=0; i<this.getSkullsNumber(); i++){
            currentScore= nextScore(currentScore);
        }

        Player firstBlooder= this.getDamageList().get(0);

        int i=0;
        while(damagers.size()>0){
            Player mostPowerful= this.getMostPowerfulDamagerIn(damagers);
            result.put(mostPowerful, currentScore);
            currentScore= nextScore(currentScore);
            damagers.remove(mostPowerful);
        }

        result.replace(firstBlooder, result.get(firstBlooder) + getFirstBlood());

        return result;
    }

    public abstract int getAdrenaline();

    public abstract void resetAfterDeath();

/*
    public void givePoints(){
        for(Player p : this.score().keySet()){
            p.addPoints((int) this.score().get(p));
        }
    }

*/


}
