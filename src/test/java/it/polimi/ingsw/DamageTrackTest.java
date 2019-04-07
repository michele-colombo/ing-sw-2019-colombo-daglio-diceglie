package it.polimi.ingsw;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DamageTrackTest {


    public void controllaLaDamage() {
        Player blinga= new Player();
        Player appoggio= new Player();
        Player marco= new Player();

        DamageTrack d= new NormalDamageTrack();

        d.addMark(blinga, 1);
        d.addDamage(blinga, 5);
        d.addDamage(appoggio, 3);
        d.addMark(appoggio, 2);
        d.addMark(marco, 1);

        Map<Player, Integer> mark= new HashMap<>();
        ArrayList<Player> damage= new ArrayList<>();

        mark.put(appoggio, 2);
        mark.put(marco, 1);

        damage.add(blinga);
        damage.add(blinga);
        damage.add(blinga);
        damage.add(blinga);
        damage.add(blinga);
        damage.add(blinga);

        damage.add(appoggio);
        damage.add(appoggio);
        damage.add(appoggio);

        assertTrue(mark.equals(d.getMarkList()));

        assertTrue(damage.equals(d.getDamageList()));

    }

    public void controlloCoseStrane(){
        DamageTrack d = new NormalDamageTrack();

        assertFalse(d.getMarkList()==null);
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
        assertEquals(d.getMarkList(), mark);
        assertEquals(d.whoKilledYou(), willy);
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

        assertTrue(d.getMarkList().isEmpty());
        assertEquals(d.getAdrenaline(), 2);


    }

}