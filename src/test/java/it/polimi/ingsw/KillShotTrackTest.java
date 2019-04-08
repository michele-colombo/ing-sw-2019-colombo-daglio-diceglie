package it.polimi.ingsw;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class KillShotTrackTest {

    @Test
    public void SimpleAddingAndScoring() {
        KillShotTrack k = new KillShotTrack(8);
        Player p1 = new Player();
        Player p2 = new Player();
        Map<Player, Integer> kill1 = new HashMap<>();
        kill1.put(p1, 2);
        k.addKilled(kill1);
        Map<Player, Integer> kill2 = new HashMap<>();
        kill2.put(p2, 1);
        k.addKilled(kill2);
        Map<Player, Integer> kill3 = new HashMap<>();
        kill3.put(p2, 1);
        k.addKilled(kill3);
        assertEquals(1, (int)k.getKillingOrder().get(p1));
        assertEquals(2, (int)k.getKillingOrder().get(p2));
        assertEquals(2, (int)k.getKillingCounter().get(p1));
        assertEquals(2, (int)k.getKillingCounter().get(p2));
        assertEquals(8, (int)k.score().get(p1));
        assertEquals(6, (int)k.score().get(p2));
    }

    @Test
    public void SimpleAddingAndScoring2() {
        KillShotTrack k = new KillShotTrack(8);
        Player p1 = new Player();
        Player p2 = new Player();
        Map<Player, Integer> kill1 = new HashMap<>();
        kill1.put(p1, 1);
        k.addKilled(kill1);
        Map<Player, Integer> kill2 = new HashMap<>();
        kill2.put(p2, 2);
        k.addKilled(kill2);
        Map<Player, Integer> kill3 = new HashMap<>();
        kill3.put(p1, 1);
        k.addKilled(kill3);
        assertEquals(1, (int)k.getKillingOrder().get(p1));
        assertEquals(2, (int)k.getKillingOrder().get(p2));
        assertEquals(2, (int)k.getKillingCounter().get(p1));
        assertEquals(2, (int)k.getKillingCounter().get(p2));
        assertEquals(8, (int)k.score().get(p1));
        assertEquals(6, (int)k.score().get(p2));
    }

    @Test
    public void SimpleAddingAndScoring3() {
        KillShotTrack k = new KillShotTrack(8);
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();

        Map<Player, Integer> kill1 = new HashMap<>();
        kill1.put(p1, 1);
        k.addKilled(kill1);

        Map<Player, Integer> kill2 = new HashMap<>();
        kill2.put(p2, 2);
        k.addKilled(kill2);

        Map<Player, Integer> kill3 = new HashMap<>();
        kill3.put(p1, 1);
        k.addKilled(kill3);

        Map<Player, Integer> kill4 = new HashMap<>();
        kill4.put(p3, 1);
        k.addKilled(kill4);

        Map<Player, Integer> kill5 = new HashMap<>();
        kill5.put(p3, 2);
        k.addKilled(kill5);

        Map<Player, Integer> kill6 = new HashMap<>();
        kill6.put(p2, 1);
        k.addKilled(kill6);

        assertEquals(1, (int)k.getKillingOrder().get(p1));
        assertEquals(2, (int)k.getKillingOrder().get(p2));
        assertEquals(3, (int)k.getKillingOrder().get(p3));
        assertEquals(2, (int)k.getKillingCounter().get(p1));
        assertEquals(3, (int)k.getKillingCounter().get(p2));
        assertEquals(3, (int)k.getKillingCounter().get(p3));
        assertEquals(8, (int)k.score().get(p2));
        assertEquals(6, (int)k.score().get(p3));
        assertEquals(4, (int)k.score().get(p1));
    }

    //More test will be added

}