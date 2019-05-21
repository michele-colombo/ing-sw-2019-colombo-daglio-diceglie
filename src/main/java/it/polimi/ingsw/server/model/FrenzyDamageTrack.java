package it.polimi.ingsw.server.model;

public class FrenzyDamageTrack extends DamageTrack {
    private static final int BIGGERSCORE = 2;
    private static final int FIRSTBLOOD = 0;

    public FrenzyDamageTrack(){
        super(BIGGERSCORE, FIRSTBLOOD);
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
