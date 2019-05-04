package it.polimi.ingsw;

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

    public int getAdrenaline(){
        return 3;
    }

    public void resetAfterDeath(){
        resetDamages();
    }


}
