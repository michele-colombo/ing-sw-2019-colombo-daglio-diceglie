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

    @BeforeEach
    public void prepareTest(){
        normalDamageTrack = new NormalDamageTrack();
        first = new Player("first");
        second = new Player("second");
        third = new Player("third");
        fourth = new Player("fourth");
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

        assertEquals(0, normalDamageTrack.getSkullsNumber());
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

    @Test
    public void killingAndDamage(){
        final Integer score1 = 4;
        final Integer score2 = 2;
        final Integer score3 = 3;
        final Integer score4 = 1;

        final Integer how1 = 1;
        final Integer how2 = 2;


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
}