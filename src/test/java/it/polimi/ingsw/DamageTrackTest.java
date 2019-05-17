package it.polimi.ingsw;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DamageTrackTest {
    DamageTrack normalDamageTrack;
    Player first;
    Player second;
    Player third;

    @BeforeEach
    public void prepareTest(){
        normalDamageTrack = new NormalDamageTrack();
        first = new Player("first");
        second = new Player("second");
        third = new Player("third");
    }
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

    @Test
    public void damageAndMarks(){
        normalDamageTrack.addMark(first, 2);
        normalDamageTrack.addDamage(second, 2);
        assertTrue(normalDamageTrack.getDamageList().contains(second) && normalDamageTrack.getDamageList().size() == 2);

        normalDamageTrack.addDamage(first, 1);
    }

    @Test
    public void getAdrenaline(){
        normalDamageTrack.addDamage(first, 2);
        assertEquals(0, normalDamageTrack.getAdrenaline());

        normalDamageTrack.addDamage(second, 3);
        assertEquals(1, normalDamageTrack.getAdrenaline());

        normalDamageTrack.addDamage(third, 9);
        assertEquals(2, normalDamageTrack.getAdrenaline());
    }

}