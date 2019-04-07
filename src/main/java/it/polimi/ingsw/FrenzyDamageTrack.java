package it.polimi.ingsw;

import java.util.HashMap;

public class FrenzyDamageTrack extends DamageTrack {


    public FrenzyDamageTrack(){
        super();
        setBiggerScore(2);
        setMaxReceivers(4);
        setFirstBlood(0);
    }

    public int getAdrenaline(){
        return 3;
    }


}
