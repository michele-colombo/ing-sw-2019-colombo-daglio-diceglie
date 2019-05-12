package it.polimi.ingsw;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DamageTrackTest {
    DamageTrack normalDamageTrack;
    Player first;
    Player second;
    Player third;

    @BeforeEach
    public void prepareTest(){
        normalDamageTrack = new NormalDamageTrack();
        first = new Player("first");
        second = new Player("second");
        third = new Player("third");
    }
    @Test
    public void addMark() {
        final int mark1 = 4;
        final int mark2 = 0;
        final int mark3 = 12;
        final int mark4 = 2;

        normalDamageTrack.addMark(first, mark2);
        assertEquals(0, normalDamageTrack.getMarkMap().get(first));

        normalDamageTrack.addMark(first, mark1);
        assertEquals(3, normalDamageTrack.getMarkMap().get(first));

        normalDamageTrack.addMark(first, mark2);
        assertEquals(3, normalDamageTrack.getMarkMap().get(first));

        normalDamageTrack.addMark(first, mark3);
        assertEquals(3, normalDamageTrack.getMarkMap().get(first));

        normalDamageTrack.addMark(second, mark4);
        assertEquals(2, normalDamageTrack.getMarkMap().get(second));
    }
@Test
    public void controlloCoseStrane(){
        DamageTrack d = new NormalDamageTrack();

        assertFalse(d.getMarkMap()==null);
        Player willy= new Player();
        Player ghidotti= new Player();
        Player piero= new Player();

        d.addDamage(willy, 3);
        d.addDamage(ghidotti, 4);
        d.addMark(piero, 1);
        d.addDamage(piero, 1);
        d.addDamage(willy, 4);

        ArrayList<Player> damage = new ArrayList<>();

        for(int i=0; i<3; i++) damage.add(willy);
        for(int i=0; i<4; i++) damage.add(ghidotti);
        for(int i=0; i<2; i++) damage.add(piero);
        for(int i=0; i<3; i++) damage.add(willy);

        Map<Player, Integer> mark= new HashMap<>();

        Map<Player, Integer> wdy= new HashMap<>();

        wdy.put(willy, 6);
        wdy.put(ghidotti, 4);
        wdy.put(piero, 2);

        assertEquals(d.whoDamagedYou(), wdy );
        assertEquals(d.getDamageList(), damage);
        assertEquals(d.getMarkMap(), mark);
        Map<Player, Integer> temp = new HashMap<Player, Integer>();
        temp.put(willy, 2);
        assertEquals(d.howDoTheyKilledYou(), temp);
        assertEquals(d.getMostPowerfulDamagerIn(wdy), willy);


    }
    @Test
    public void vadoOltreDodici(){
        DamageTrack d= new NormalDamageTrack();

        Player a= new Player();
        Player b= new Player();
        Player c= new Player();


        d.addDamage(a, 5);
        d.addDamage(b, 5);
        d.addDamage(c, 5);

        List<Player> l = new ArrayList<>();
        for(int i=0; i<5; i++) l.add(a);
        for(int i=0; i<5; i++) l.add(b);
        for(int i=0; i<2; i++) l.add(c);

        assertEquals(l, d.getDamageList());
        assertEquals(d.getSkullsNumber(), 0);
        assertEquals(d.score().get(a), (Integer) 9);
        assertEquals(d.score().get(b), (Integer) 6);
        assertEquals(d.score().get(c), (Integer) 4);

        assertTrue(d.getMarkMap().isEmpty());
        assertEquals(d.getAdrenaline(), 2);

        ((NormalDamageTrack) d).increaseSkull();


    }

}