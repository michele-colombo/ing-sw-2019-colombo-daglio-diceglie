package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.max;

public abstract class DamageTrack {
    private ArrayList<Player> damageList;
    private HashMap<Player, Integer> markList;
    private int skullsNumber;

    private int biggerScore;
    private int firstBlood;
    private int maxReceivers;

    public DamageTrack(){
        damageList= new ArrayList<>();
        markList= new HashMap<>();
        skullsNumber= 0;
    }

    public void setBiggerScore(int biggerScore) {
        this.biggerScore = biggerScore;
    }

    public void setFirstBlood(int firstBlood) {
        this.firstBlood = firstBlood;
    }

    public void setMaxReceivers(int maxReceivers) {
        this.maxReceivers = maxReceivers;
    }

    public List<Player> getDamageList() {
        return damageList;
    }

    public Map<Player, Integer> getMarkList() {
        return markList;
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

    public int getMaxReceivers() {
        return maxReceivers;
    }

    public void resetDamages(){
        this.damageList.clear();
    }

    public void addMark(Player p, int markToAdd){
        Integer targets;

        if( markList.containsKey(p) ){
            targets = markList.get(p);
            targets = targets + markToAdd;
            if( targets > 3){
                targets = 3;
            }

            markList.replace(p, targets);
        }
        else{
            if(markToAdd > 3){
                targets = 3;
            }
            else{
                targets = markToAdd;
            }
            markList.put(p, targets);
        }
    }

    public void addDamage(Player p, int damage){
        int toAdd;

        if(getMarkList().containsKey(p)){       //why the getter is used instead of the private attribute markList?
            toAdd= damage +  this.getMarkList().get(p);
            this.getMarkList().remove(p);
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

    public Player whoKilledYou(){

        if(damageList.size() >= 11){
            return damageList.get( damageList.size() - 1);
        }

        return null;
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
        for(Player p : damagers.keySet()){
            if(damagers.get(p) >= massimo && (result == null || this.getDamageList().indexOf(p)< this.getDamageList().indexOf(result))){
                result = p;
                massimo= damagers.get(p);
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
        Map<Player, Integer> damagers = this.whoDamagedYou();
        Map<Player, Integer> result = new HashMap<>();
        int currentScore= getBiggerScore();

        for(int i=0; i<this.getSkullsNumber(); i++){
            currentScore= nextScore(currentScore);
        }

        int scoreReceivers = getMaxReceivers() - this.getSkullsNumber();

        Player firstBlooder= this.getDamageList().get(0);

        int i=0;
        while(i< scoreReceivers && damagers.size()>0){
            Player mostPowerful= this.getMostPowerfulDamagerIn(damagers);
            result.put(mostPowerful, currentScore);
            currentScore= nextScore(currentScore);
            damagers.remove(mostPowerful);
        }

        result.replace(firstBlooder, result.get(firstBlooder) + getFirstBlood());

        return result;
    }

    public abstract int getAdrenaline();

/*
    public void givePoints(){
        for(Player p : this.score().keySet()){
            p.addPoints((int) this.score().get(p));
        }
    }

*/


}
