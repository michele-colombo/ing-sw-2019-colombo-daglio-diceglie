package it.polimi.ingsw;


import it.polimi.ingsw.server.model.KillShotTrack;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class KillShotTrackTest {
    private KillShotTrack killShotTrack;
    private Player first;
    private Player second;
    private Player third;

    @BeforeEach
    public void prepareTest(){
        final int skulls = 8;

        killShotTrack = new KillShotTrack(skulls);
        first = new Player("first");
        second = new Player("second");
        third = new Player("third");
    }

    @Test
    public void SimpleAddingAndScoring() {
        final Integer kill = 1;
        final int order1 = 0;
        final int order2 = 1;
        final int skulls = 5;

        Map<Player, Integer> shot1 = new HashMap<>();
        shot1.put(first, kill);


        Map<Player, Integer> shot2 = new HashMap<>();
        shot2.put(third, kill);

        killShotTrack.addKilled(shot1);
        killShotTrack.removeSkull();
        killShotTrack.addKilled(shot2);
        killShotTrack.removeSkull();
        assertEquals(order1, killShotTrack.getKillingOrder(first));
        assertEquals(order2, killShotTrack.getKillingOrder(third));

        Map<Player, Integer> shot3 = new HashMap<>();
        shot3.put(first, kill);
        killShotTrack.removeSkull();
        killShotTrack.addKilled(shot3);
        assertEquals(order1, killShotTrack.getKillingOrder(first));
        assertEquals(skulls, killShotTrack.getSkulls());
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
        k.removeSkull();

        Map<Player, Integer> kill2 = new HashMap<>();
        kill2.put(p2, 2);
        k.addKilled(kill2);
        k.removeSkull();

        Map<Player, Integer> kill3 = new HashMap<>();
        kill3.put(p4, 1);
        k.addKilled(kill3);
        k.removeSkull();

        Map<Player, Integer> kill4 = new HashMap<>();
        kill4.put(p3, 1);
        k.addKilled(kill4);
        k.removeSkull();

        Map<Player, Integer> kill5 = new HashMap<>();
        kill5.put(p5, 2);
        k.addKilled(kill5);
        k.removeSkull();

        Map<Player, Integer> kill6 = new HashMap<>();
        kill6.put(p2, 1);
        k.addKilled(kill6);
        k.removeSkull();

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