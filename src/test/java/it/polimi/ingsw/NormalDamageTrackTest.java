package it.polimi.ingsw;


import it.polimi.ingsw.server.model.DamageTrack;
import it.polimi.ingsw.server.model.NormalDamageTrack;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NormalDamageTrackTest {
    private DamageTrack normalDamageTrack;
    private Player first;
    private Player second;
    private Player third;
    private Player fourth;

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
     * Test addDamage method
     */
    @Test
    public void addDamage(){
        normalDamageTrack.addDamage(first, 5);
        normalDamageTrack.addDamage(second, 2);
        normalDamageTrack.addDamage(third, 2);

        assertEquals(5, normalDamageTrack.whoDamagedYou().get(first));
        assertEquals(2, normalDamageTrack.whoDamagedYou().get(second));
        assertEquals(2, normalDamageTrack.whoDamagedYou().get(third));
        normalDamageTrack.addDamage(first, 5);
        assertNotEquals(10, normalDamageTrack.whoDamagedYou().get(first)); //false because max damage is 12
        assertEquals(8, normalDamageTrack.whoDamagedYou().get(first));
    }

    /**
     * Test reset damage track when it reaches at least 10 damage
     */
    @Test
    public void damageAndReset(){
        normalDamageTrack.addDamage(first, 5);
        normalDamageTrack.addDamage(second, 2);
        normalDamageTrack.addDamage(third, 2);

        assertEquals(first, normalDamageTrack.getDamageList().get(0));

        normalDamageTrack.addDamage(first, 1);
        assertEquals(6, normalDamageTrack.whoDamagedYou().get(first));

        assertEquals(0, normalDamageTrack.getSkullsNumber());
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
     * Test score method
     */
    @Test
    public void score(){
        final Integer score1 = 9;
        final Integer score2 = 6;
        final Integer score3 = 4;
        final Integer score4 = null;

        Map<Player, Integer> score = normalDamageTrack.score();
        assertTrue(score.size() == 0);

        normalDamageTrack.addDamage(first, 7);
        normalDamageTrack.addDamage(second, 2);
        normalDamageTrack.addDamage(third, 2);
        normalDamageTrack.addDamage(fourth, 0);

        score = normalDamageTrack.score();
        assertEquals(score1, score.get(first));
        assertEquals(score2, score.get(second));
        assertEquals(score3, score.get(third));
        assertEquals(score4, score.get(fourth));
    }

    /**
     * Test howTheyKilledYou method
     */
    @Test
    public void killingAndDamage(){
        final Integer score1 = 4;
        final Integer score2 = 2;
        final Integer score3 = 3;
        final Integer score4 = 1;

        final Integer how1 = 1; //normal kill
        final Integer how2 = 2; //overkill


        normalDamageTrack.addDamage(first, score1);
        normalDamageTrack.addDamage(second, score2);
        normalDamageTrack.addDamage(third, score2);
        normalDamageTrack.addDamage(first, score3);

        assertEquals(how1, normalDamageTrack.howDoTheyKilledYou().get(first));

        normalDamageTrack.addDamage(first, score4);
        assertEquals(how2, normalDamageTrack.howDoTheyKilledYou().get(first));

        normalDamageTrack.addDamage(first, score1);
        assertEquals(how2, normalDamageTrack.howDoTheyKilledYou().get(first));

        Map<Player, Integer> damage = normalDamageTrack.whoDamagedYou();
        assertEquals(score1 + score3 + score4, damage.get(first));

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