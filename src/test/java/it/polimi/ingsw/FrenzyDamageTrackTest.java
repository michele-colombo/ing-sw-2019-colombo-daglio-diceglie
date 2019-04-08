package it.polimi.ingsw;

import org.junit.Test;

import static org.junit.Assert.*;

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

}