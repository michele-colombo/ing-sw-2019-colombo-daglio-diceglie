package it.polimi.ingsw;

public class FrenzyDamageTrack extends DamageTrack {
    static final int biggerScore = 2;
    static final int firstBlood = 0;

    public FrenzyDamageTrack(){
        super(biggerScore, firstBlood);
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
