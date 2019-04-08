package it.polimi.ingsw;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.Color.*;
import static it.polimi.ingsw.PlayerColor.*;
import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void canAfford() {
        Player p = new Player("SignorTest", PlayerColor.GREY);
        PowerUp po1 = new PowerUp(Color.BLUE);
        PowerUp po2 = new PowerUp(Color.YELLOW);
        PowerUp po3 = new PowerUp(Color.BLUE);
        p.addPowerUp(po1);
        p.addPowerUp(po2);
        p.addPowerUp(po3);
        p.getWallet().deposit(new Cash(3, 1, 1));
        assertTrue(p.getWallet().isEqual(new Cash(3, 1, 1)));
        assertTrue(p.canAfford(new Cash(5, 1, 2)));
        assertFalse(p.canAfford(new Cash(6, 1, 2)));
        assertFalse(p.canAfford(new Cash(5, 2, 2)));
        assertFalse(p.canAfford(new Cash(5, 1, 3)));
        p.discardPowerUp(po1);
        assertFalse(p.canAfford(new Cash(5, 1, 2)));
        assertTrue(p.canAfford(new Cash(4,1,2)));
        assertTrue(p.canAfford(new Cash(2, 1, 2)));
        assertTrue(p.canAfford(new Cash(0,0,0)));
        assertFalse(p.canAfford(new Cash(0, 2, 0)));
        assertTrue(p.canAfford(new Cash(0,0,2)));
    }


    @Test
    public void switchToFrenzy() {
        Player p1 = new Player("Aleksej", GREY);
        Player p2 = new Player("Sasha", VIOLET);
        Player p3 = new Player("Aglaja", PlayerColor.YELLOW);
        Player p4 = new Player("Dmitrij", GREEN);
        p1.getDamageTrack().addMark(p2, 2);
        p1.getDamageTrack().addMark(p3, 3);
        p1.getDamageTrack().addDamage(p4, 5);
        assertEquals(1, p1.getDamageTrack().getAdrenaline());
        List<Player> test = new ArrayList<>();
        test.add(p4);
        test.add(p4);
        test.add(p4);
        test.add(p4);
        test.add(p4);
        assertEquals(p1.getDamageTrack().getDamageList(), test);
        Map<Player, Integer> temp = new HashMap<>();
        temp.put(p2, 2);
        temp.put(p3, 3);
        assertEquals(p1.getDamageTrack().getMarkMap(), temp);
        p1.switchToFrenzy();
        assertEquals(p1.getDamageTrack().getMarkMap(), temp);
        test.clear();
        assertEquals(p1.getDamageTrack().getDamageList(), test);
        assertEquals(p1.getDamageTrack().getAdrenaline(), 3);
        assertEquals(p1.getDamageTrack().getBiggerScore(), 2);


    }
}