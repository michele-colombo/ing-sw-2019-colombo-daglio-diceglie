package it.polimi.ingsw.server.model;

/**
 * Is (part of) the board of a player when he is in frenzy mode.
 * Maximum points given are 8 and there is a bonus point for the first damager.
 * It contains skulls and has different level of adrenaline.
 */
public class NormalDamageTrack extends DamageTrack {
    static final int BIGGER_SCORE = 8;
    static final int FIRST_BLOOD = 1;

    public NormalDamageTrack(){
        super(BIGGER_SCORE, FIRST_BLOOD);
    }


    @Override
    public int getAdrenaline(){
        if(this.getDamageList().size() >= 6){
            return 2;
        }
        if(this.getDamageList().size() >= 3){
            return 1;
        }
        return 0;
    }

    public void increaseSkull(){
            this.setSkullsNumber(this.getSkullsNumber() + 1);
    }

    @Override
    public void resetAfterDeath(){
        resetDamages();
        increaseSkull();
    }

    @Override
    public boolean isFrenzy() {
        return false;
    }
}
