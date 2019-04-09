package it.polimi.ingsw;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class KillShotTrackTest {

    @Test
    public void SimpleAddingAndScoring() {
        KillShotTrack k = new KillShotTrack(8);
        Player p1 = new Player("nome1", PlayerColor.YELLOW);
        Player p2 = new Player("nome2", PlayerColor.BLUE);
        Player p3 = new Player("nome3", PlayerColor.VIOLET);
        Player p4 = new Player("nome4", PlayerColor.GREY);
        Player p5 = new Player("nome5", PlayerColor.GREEN);
        Map<Player, Integer> kill1 = new HashMap<>();
        kill1.put(p1, 2);
        k.addKilled(kill1);
        Map<Player, Integer> kill2 = new HashMap<>();
        kill2.put(p2, 1);
        k.addKilled(kill2);
        Map<Player, Integer> kill3 = new HashMap<>();
        kill3.put(p2, 1);
        k.addKilled(kill3);
        assertEquals(0, k.getKillingOrder(p1));
        assertEquals(1, k.getKillingOrder(p2));
        assertEquals(2, k.getKillingOf(p1));
        assertEquals(2, k.getKillingOf(p2));
        assertEquals(8, (int)k.score().get(p1));
        assertEquals(6, (int)k.score().get(p2));
    }

    @Test
    public void NoKillingReturs0() {
        KillShotTrack k = new KillShotTrack(8);
        Player p1 = new Player("nome1", PlayerColor.YELLOW);
        Player p2 = new Player("nome2", PlayerColor.BLUE);
        Player p3 = new Player("nome3", PlayerColor.VIOLET);
        Player p4 = new Player("nome4", PlayerColor.GREY);
        Player p5 = new Player("nome5", PlayerColor.GREEN);
        Map<Player, Integer> kill1 = new HashMap<>();
        kill1.put(p1, 1);
        k.addKilled(kill1);
        Map<Player, Integer> kill2 = new HashMap<>();
        kill2.put(p2, 2);
        k.addKilled(kill2);
        Map<Player, Integer> kill3 = new HashMap<>();
        kill3.put(p1, 1);
        k.addKilled(kill3);
        assertEquals(0, k.getKillingOf(p4));
        assertEquals(0, k.getKillingOf(null));
        assertEquals(0, k.getKillingOrder(p1));
        assertEquals(1, k.getKillingOrder(p2));
        assertEquals(2, k.getKillingOf(p1));
        assertEquals(2, k.getKillingOf(p2));
        assertEquals(8, (int)k.score().get(p1));
        assertEquals(6, (int)k.score().get(p2));
    }

    @Test
    public void SimpleAddingAndScoring3() {
        KillShotTrack k = new KillShotTrack(8);
        Player p1 = new Player("nome1", PlayerColor.YELLOW);
        Player p2 = new Player("nome2", PlayerColor.BLUE);
        Player p3 = new Player("nome3", PlayerColor.VIOLET);
        Player p4 = new Player("nome4", PlayerColor.GREY);
        Player p5 = new Player("nome5", PlayerColor.GREEN);

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

        assertEquals(0, k.getKillingOrder(p1));
        assertEquals(1, k.getKillingOrder(p2));
        assertEquals(2, k.getKillingOrder(p3));
        assertEquals(2, k.getKillingOf(p1));
        assertEquals(3, k.getKillingOf(p2));
        assertEquals(3, k.getKillingOf(p3));
        assertEquals(8, (int)k.score().get(p2));
        assertEquals(6, (int)k.score().get(p3));
        assertEquals(4, (int)k.score().get(p1));
    }

    @Test
    public void FiveKillers() {
        KillShotTrack k = new KillShotTrack(8);
        Player p1 = new Player("nome1", PlayerColor.YELLOW);
        Player p2 = new Player("nome2", PlayerColor.BLUE);
        Player p3 = new Player("nome3", PlayerColor.VIOLET);
        Player p4 = new Player("nome4", PlayerColor.GREY);
        Player p5 = new Player("nome5", PlayerColor.GREEN);



        Map<Player, Integer> kill1 = new HashMap<>();
        kill1.put(p1, 1);
        k.addKilled(kill1);

        Map<Player, Integer> kill2 = new HashMap<>();
        kill2.put(p2, 2);
        k.addKilled(kill2);

        Map<Player, Integer> kill3 = new HashMap<>();
        kill3.put(p4, 1);
        k.addKilled(kill3);

        Map<Player, Integer> kill4 = new HashMap<>();
        kill4.put(p3, 1);
        k.addKilled(kill4);

        Map<Player, Integer> kill5 = new HashMap<>();
        kill5.put(p5, 2);
        k.addKilled(kill5);

        Map<Player, Integer> kill6 = new HashMap<>();
        kill6.put(p2, 1);
        k.addKilled(kill6);

        assertEquals(0, k.getKillingOrder(p1));
        assertEquals(1, k.getKillingOrder(p2));
        assertEquals(3, k.getKillingOrder(p3));
        assertEquals(2, k.getKillingOrder(p4));
        assertEquals(4, k.getKillingOrder(p5));

        assertEquals(1, k.getKillingOf(p1));
        assertEquals(3, k.getKillingOf(p2));
        assertEquals(1, k.getKillingOf(p3));
        assertEquals(1, k.getKillingOf(p4));
        assertEquals(2, k.getKillingOf(p5));

        assertEquals(8, (int)k.score().get(p2));
        assertEquals(6, (int)k.score().get(p5));
        assertEquals(4, (int)k.score().get(p1));
        assertEquals(2, (int)k.score().get(p4));
        assertEquals(1, (int)k.score().get(p3));

        assertEquals(2, k.getSkulls());
    }

    //More test will be added

}