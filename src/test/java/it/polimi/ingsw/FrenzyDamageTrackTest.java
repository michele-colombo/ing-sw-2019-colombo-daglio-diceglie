package it.polimi.ingsw;


import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FrenzyDamageTrackTest {
    @Test
    public void testTuttoVuoto(){

        Player p= new Player();
        assertFalse(p == null);

        FrenzyDamageTrack d = new FrenzyDamageTrack();
        assertEquals(d.getSkullsNumber(), 0);
        assertFalse(d.getMarkMap()== null);
        assertFalse(d.getDamageList()== null);

    }
    @Test
    public void testNormalita(){
        Player aldo= new Player();
        Player giovanni= new Player();
        Player giacomo= new Player();
        Player ingconti= new Player();
        DamageTrack old= new NormalDamageTrack();
        old.addMark(aldo, 3);
        old.addDamage(giovanni, 2);
        FrenzyDamageTrack d= new FrenzyDamageTrack(old);

        d.addDamage(aldo, 2);
        d.addDamage(ingconti, 4);

        assertTrue(d.whoDamagedYou().get(aldo)== 5);
        assertTrue(d.score().get(ingconti)== 1);
    }

    @Test
    public void testNazionaleForFrenzy(){
        Player aldo= new Player();
        Player giovanni= new Player();
        Player giacomo= new Player();
        Player ingconti= new Player();

        DamageTrack old= new NormalDamageTrack();
        DamageTrack d= new FrenzyDamageTrack(old);

        Map<Player, Integer> score= new HashMap<>();

        for(int i=0; i<4; i++) {

            d.addDamage(aldo, 3);
            d.addDamage(giovanni, 3);
            d.addDamage(giacomo, 3);
            d.addDamage(ingconti, 3);


            score.put(aldo, 2);
            score.put(giovanni, 1);
            score.put(giacomo, 1);
            score.put(ingconti, 1);

            assertEquals(d.score(), score);

            d.resetAfterDeath();
            score.clear();
        }



    }

}