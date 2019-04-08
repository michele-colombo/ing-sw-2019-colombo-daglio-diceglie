package it.polimi.ingsw;

import java.util.HashMap;

public class FrenzyDamageTrack extends DamageTrack {


    public FrenzyDamageTrack(){
        super();
        setBiggerScore(2);
        setFirstBlood(0);
    }

    public FrenzyDamageTrack(NormalDamageTrack oldTrack){
        this();
        this.setMarkList( oldTrack.getMarkList());

    }

    public int getAdrenaline(){
        return 3;
    }


    public void increaseSkull(){
        this.setSkullsNumber( this.getSkullsNumber());
    }

    public void resetAfterDeath(){
        resetDamages();
    }


}
