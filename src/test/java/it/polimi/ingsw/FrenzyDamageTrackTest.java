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
        assertFalse(d.getMarkList()== null);
        assertFalse(d.getDamageList()== null);

    }

}