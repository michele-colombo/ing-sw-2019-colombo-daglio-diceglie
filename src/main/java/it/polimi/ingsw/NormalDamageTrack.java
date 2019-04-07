package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashMap;

public class NormalDamageTrack extends DamageTrack {



    public NormalDamageTrack(){
        super();
        setBiggerScore(8);
        setFirstBlood(1);
        setMaxReceivers(6);
    }



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
        if(getSkullsNumber() < 6){
            this.setSkullsNumber(this.getSkullsNumber() + 1);
        }
    }



}
