package it.polimi.ingsw;


import it.polimi.ingsw.server.model.DamageTrack;
import it.polimi.ingsw.server.model.NormalDamageTrack;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DamageTrackTest {
    DamageTrack normalDamageTrack;
    Player first;
    Player second;
    Player third;

    /**
     * Prepare DamageTrackTest instantiating three damageTracks
     */
    @BeforeEach
    public void prepareTest(){
        final String firstName = "first";
        final String secondName = "second";
        final String thirdName = "third";

        normalDamageTrack = new NormalDamageTrack();
        first = new Player(firstName);
        second = new Player(secondName);
        third = new Player(thirdName);
    }

    /**
     * Test addMark method
     */
    @Test
    public void addMark() {
        final int mark1 = 4;
        final int mark2 = 0;
        final int mark3 = 12;
        final int mark4 = 2;

        final int assert1 = 0;
        final int assert2 = 3;
        final int assert3 = 3;
        final int assert4 = 3;
        final int assert5 = 2;

        normalDamageTrack.addMark(first, mark2);
        assertEquals(assert1, normalDamageTrack.getMarkMap().get(first));

        normalDamageTrack.addMark(first, mark1);
        assertEquals(assert2, normalDamageTrack.getMarkMap().get(first));

        normalDamageTrack.addMark(first, mark2);
        assertEquals(assert3, normalDamageTrack.getMarkMap().get(first));

        normalDamageTrack.addMark(first, mark3);
        assertEquals(assert4, normalDamageTrack.getMarkMap().get(first));

        normalDamageTrack.addMark(second, mark4);
        assertEquals(assert5, normalDamageTrack.getMarkMap().get(second));
    }

    /**
     * Test reset damage track when it reaches at least 10 damage
     */
    @Test
    public void damageAndReset(){
        normalDamageTrack.addDamage(first, 5);
        normalDamageTrack.addDamage(second, 2);
        normalDamageTrack.addDamage(third, 2);

        assertEquals(5, normalDamageTrack.whoDamagedYou().get(first));
        assertEquals(first, normalDamageTrack.getDamageList().get(0));

        normalDamageTrack.addDamage(first, 1);
        assertEquals(6, normalDamageTrack.whoDamagedYou().get(first));

        normalDamageTrack.resetAfterDeath();
        assertEquals(0, normalDamageTrack.getDamageList().size());
        assertEquals(1, normalDamageTrack.getSkullsNumber());

    }

    /**
     * Test adding damage from a player after it got marks from him
     */
    @Test
    public void damageAndMarks(){
        normalDamageTrack.addMark(first, 2);
        normalDamageTrack.addDamage(second, 2);
        assertTrue(normalDamageTrack.getDamageList().contains(second) && normalDamageTrack.getDamageList().size() == 2);

        normalDamageTrack.addDamage(first, 1);
        assertTrue(normalDamageTrack.getDamageList().size() == 5);
        assertEquals(3, normalDamageTrack.whoDamagedYou().get(first)); //one damage + two from marks
    }

    /**
     * Test getAdrenaline, when it has to returns 0
     */
    @Test
    public void getAdrenaline(){
        normalDamageTrack.addDamage(first, 2);
        assertEquals(0, normalDamageTrack.getAdrenaline()); //no adrenaline

        normalDamageTrack.addDamage(second, 3);
        assertEquals(1, normalDamageTrack.getAdrenaline()); //first type of adrenaline

        normalDamageTrack.addDamage(third, 9);
        assertEquals(2, normalDamageTrack.getAdrenaline()); //second type of adrenaline
    }

}