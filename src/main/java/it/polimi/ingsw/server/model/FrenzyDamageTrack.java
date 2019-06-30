package it.polimi.ingsw.server.model;

public class FrenzyDamageTrack extends DamageTrack {
    private static final int BIGGER_SCORE = 2;
    private static final int FIRST_BLOOD = 0;

    public FrenzyDamageTrack(){
        super(BIGGER_SCORE, FIRST_BLOOD);
    }

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
