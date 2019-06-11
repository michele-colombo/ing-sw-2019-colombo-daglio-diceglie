package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class DamageTrack {
    /**
     * Ordered list of damages.
     */
    private List<Player> damageList;

    /**
     * the mark count of the player
     */
    private Map<Player, Integer> markMap;

    /**
     * when true mark can become damages, otherwise not.
     * It is used as a support variable for correct damaging.
     */
    private boolean markToDamages;

    /**
     * the number of skulls on the damage track
     */
    private int skullsNumber;

    /**
     * the bigger score a damage-giver could ever acquire damaging a player with this damage track
     */
    private final int biggerScore;

    /**
     * points given to the first damage-giver
     */
    private final int firstBlood;

    /**
     * sets BIGGERSCORE and FIRSTBLOOD. Instantiate damageList and markMap and initialize skullNumber
     * @param biggerScore depends on the damage list type
     * @param firstBlood depends on the damage list type
     */

    protected DamageTrack(int biggerScore, int firstBlood){
        damageList= new ArrayList<>();
        markMap = new HashMap<>();
        markToDamages = true;
        skullsNumber = 0;
        this.biggerScore = biggerScore;
        this.firstBlood = firstBlood;
    }


    protected void setMarkMap(Map<Player, Integer> markMap) {
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

    protected void setSkullsNumber(int skullsNumber) {
        this.skullsNumber = skullsNumber;
    }

    public int getBiggerScore() {
        return biggerScore;
    }

    public boolean isMarkToDamages() {
        return markToDamages;
    }

    public void setMarkToDamages(boolean markToDamages) {
        this.markToDamages = markToDamages;
    }

    protected int getFirstBlood() {
        return firstBlood;
    }


    /**
     * reset the damagelist to empty
     */
    public void resetDamages(){
        this.damageList.clear();
    }

    /**
     * add marks to the damage track
     * @param p player who gives his marks
     * @param markToAdd number of marks given
     */
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

    /**
     * add damages to the damage track
     * @param p player who gives the damages
     * @param damage number of damages to add
     */
    public void addDamage(Player p, int damage){
        int toAdd;

        if(markMap.containsKey(p) && markToDamages){
            toAdd= damage +  this.markMap.get(p);
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

    /**
     * return the killer and the damages he gave for killing the player (1 or 2)
     * @return the killer and the damages of the killing
     */
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

    /**
     *
     * @return the damagers and the number of damages for each
     */
    public Map<Player, Integer> whoDamagedYou(){
        Map<Player, Integer> result = new HashMap<>();
        for(int i=0; i< damageList.size(); i++){
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

    /**
     *
     * @param damagers the list where to find the most powerful
     * @return the most powerful damager
     */
    private Player getMostPowerfulDamagerIn(Map<Player, Integer> damagers){

        Integer massimo = 0;

        Player result = null;
        for(Player p : damagers.keySet()) {
            if (damagers.get(p) > massimo) {
                result = p;
                massimo = damagers.get(p);
            } else if (damagers.get(p).equals(massimo)) { //PRIMA C'ERA ==
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

    /**
     *
     * @return a map <Damager, score> that describes how many points to give for each player
     */
    public Map<Player, Integer> score(){
        if(getDamageList().isEmpty()){
            return new HashMap<>();
        }

        Map<Player, Integer> damagers = this.whoDamagedYou();
        Map<Player, Integer> result = new HashMap<>();
        int currentScore= getBiggerScore();

        for(int i=0; i<this.getSkullsNumber(); i++){
            currentScore= nextScore(currentScore);
        }

        Player firstBlooder= this.getDamageList().get(0);

        while(damagers.size()>0){
            Player mostPowerful= this.getMostPowerfulDamagerIn(damagers);
            result.put(mostPowerful, currentScore);
            currentScore= nextScore(currentScore);
            damagers.remove(mostPowerful);
        }

        result.replace(firstBlooder, result.get(firstBlooder) + getFirstBlood());

        return result;
    }

    public void setDamageList(List<Player> damageList) {
        this.damageList = damageList;
    }

    /**
     *
     * @return 0-> no adrenaline; 1-> low adrenaline; 2->high adrenaline
     */
    public abstract int getAdrenaline();


    /**
     * reset the damage track after a player death (same marks, same skulls, no damages)

     */
    public abstract void resetAfterDeath();

/*
    public void givePoints(){
        for(Player p : this.score().keySet()){
            p.addPoints((int) this.score().get(p));
        }
    }

*/

    public abstract boolean isFrenzy();


}
