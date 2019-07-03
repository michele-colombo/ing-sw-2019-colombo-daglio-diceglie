package it.polimi.ingsw.server.model;

/**
 * Is (part of) the board of a player when he is in frenzy mode.
 * Maximum points given are 2 and there is no bonus for the first damager.
 * It has no adrenaline levels (returns a default 3 value for adrenaline).
 */
public class FrenzyDamageTrack extends DamageTrack {
    /**
     * maximum score to give when killed
     */
    private static final int BIGGER_SCORE = 2;
    /**
     * score to give to first damager
     */
    private static final int FIRST_BLOOD = 0;

    /**
     * builds a frenzy damage track
     */

    public FrenzyDamageTrack(){
        super(BIGGER_SCORE, FIRST_BLOOD);
    }

    /**
     * builds a frenzy damage track
     * @param oldTrack previous normal damage track
     */
    public FrenzyDamageTrack(DamageTrack oldTrack){
        this();
        this.setMarkMap( oldTrack.getMarkMap());

    }

    @Override
    public int getAdrenaline(){
        return 3;
    }

    @Override
    public void resetAfterDeath(){
        resetDamages();
    }

    @Override
    public boolean isFrenzy() {
        return true;
    }
}
