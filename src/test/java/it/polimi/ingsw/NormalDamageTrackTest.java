package it.polimi.ingsw;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class NormalDamageTrackTest {

    @Test
    public void casoNormale(){
        DamageTrack d= new NormalDamageTrack();
        Player willy= new Player();
        Player gigi= new Player();
        Player pippo= new Player();
        Player anna= new Player();

        d.addDamage(willy, 2);
        d.addMark(gigi, 2);
        d.addDamage(pippo, 3);
        d.addDamage(anna, 4);
        d.addDamage(willy, 2);

        Map<Player, Integer> score= new HashMap<>();
        score.put(willy, 9);
        score.put(pippo, 4);
        score.put(anna, 6);

        assertEquals(d.score(), score);

        Map<Player, Integer> wdy= new HashMap<>();

        wdy.put(willy, 4);
        wdy.put(anna, 4);
        wdy.put(pippo, 3);

        assertEquals(d.whoDamagedYou(), wdy);
        assertEquals(d.getMostPowerfulDamagerIn(wdy), willy);
        wdy.remove(willy);
        assertEquals(d.getMostPowerfulDamagerIn(wdy), anna);

        d.resetAfterDeath();

        assertTrue(d.getMarkMap().get(gigi)==2);

        d.addDamage(gigi, 11);

        assertTrue(d.score().get(gigi)==7);
        d.resetAfterDeath();
        assertNull(d.getMostPowerfulDamagerIn(d.whoDamagedYou()));

        ((NormalDamageTrack) d).increaseSkull();
        ((NormalDamageTrack) d).increaseSkull();

        d.addDamage(willy, 2);
        d.addDamage(gigi, 3);
        d.addDamage(anna, 3);
        d.addDamage(pippo, 3);

        score.clear();
        score.put(willy, 2);
        score.put(gigi, 1);
        score.put(anna, 1);
        score.put(pippo, 1);

        assertEquals(d.score(), score);

        Map<Player, Integer> uccisore = new HashMap<>();
        uccisore.put(pippo, 1);
        assertTrue(d.howDoTheyKilledYou().equals(uccisore));

    }

@Test
    public void casoLimiteTuttoVuoto(){
        NormalDamageTrack d= new NormalDamageTrack();

        assertTrue(d.getAdrenaline()== 0);
        assertTrue(d.score().isEmpty());
        assertTrue(d.whoDamagedYou().isEmpty());
        assertNull(d.getMostPowerfulDamagerIn(d.whoDamagedYou()));
        assertTrue(d.howDoTheyKilledYou().isEmpty());


    }



}