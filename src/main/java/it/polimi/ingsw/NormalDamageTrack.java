package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashMap;

public class NormalDamageTrack extends DamageTrack {
    static final int biggerScore = 8;
    static final int firstBlood = 1;

    public NormalDamageTrack(){
        super(biggerScore, firstBlood);
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
