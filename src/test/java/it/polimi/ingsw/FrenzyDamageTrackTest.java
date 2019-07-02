package it.polimi.ingsw;


import it.polimi.ingsw.server.model.DamageTrack;
import it.polimi.ingsw.server.model.FrenzyDamageTrack;
import it.polimi.ingsw.server.model.NormalDamageTrack;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class FrenzyDamageTrackTest {
    private DamageTrack normalDamageTrack;
    private DamageTrack frenzyDamageTrack;
    private Player first;


    /**
     * preares a damageTrack (in frenzy mode) for the tests
     */
    @BeforeEach
    public void prepareTest(){
        final int mark1 = 2;

        first = new Player("first");
        normalDamageTrack = new NormalDamageTrack();
        normalDamageTrack.addMark(first, mark1);
        normalDamageTrack.resetAfterDeath();
        frenzyDamageTrack = new FrenzyDamageTrack(normalDamageTrack);
    }


    /**
     * Tests the adding of a mark
     */
    @Test
    public void frenzyFromNormal(){
        final int mark1 = 2;

        assertTrue(frenzyDamageTrack.getMarkMap().get(first) == mark1);
    }

    /**
     * Tests adding of damages and reset
     */
    @Test
    public void reset(){
        final int damage1 = 6;
        final int damage2 = 3;
        final int damage3 = 2;
        final int reset = 0;

        frenzyDamageTrack.addDamage(first, damage1);
        frenzyDamageTrack.addDamage(first, damage2);
        frenzyDamageTrack.addDamage(first, damage3);

        frenzyDamageTrack.resetAfterDeath();

        assertTrue(frenzyDamageTrack.getDamageList().size() == reset);
        assertTrue(frenzyDamageTrack.getSkullsNumber() == reset);
    }

}