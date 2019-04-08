package it.polimi.ingsw;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class NormalDamageTrackTest {

    @Test
    public void controllaScore() {
        DamageTrack d = new NormalDamageTrack();

        d.setSkullsNumber(1);

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

        Map<Player, Integer> sco = new HashMap<>();
        sco.put(willy, 7);
        sco.put(ghidotti, 4);
        sco.put(piero, 2);

        assertEquals(d.whoDamagedYou(), wdy );
        assertEquals(d.getDamageList(), damage);
        assertEquals(d.getMarkMap(), mark);
        assertEquals(d.howDoTheyKilledYou(), willy);
        assertEquals(d.getMostPowerfulDamagerIn(wdy), willy);

        assertEquals(sco, d.score());

        d.resetDamages();

        d.setSkullsNumber(d.getSkullsNumber() + 1);

        d.addMark(willy, 2);
        d.addDamage(piero, 5);
        d.addDamage(willy, 1);
        d.addMark(ghidotti, 2);
        d.addDamage(piero, 3);

        Map<Player, Integer> wdy2 = new HashMap<>();
        Map<Player, Integer> score2 = new HashMap<>();
        List<Player> dmgl = new ArrayList<>();
        Map<Player, Integer> mark2 = new HashMap<>();

        wdy2.put(willy, 3);
        wdy2.put(piero, 8);

        for(int i=0; i<5; i++) dmgl.add(piero);
        for(int i=0; i<3; i++) dmgl.add(willy);
        for(int i=0; i<3; i++) dmgl.add(piero);

        mark2.put(ghidotti, 2);

        score2.put(piero, 5);
        score2.put(willy, 2);

        assertEquals(score2, d.score());
        assertEquals(mark2, d.getMarkMap());
        assertEquals(dmgl, d.getDamageList());
        assertEquals(wdy2, d.whoDamagedYou());
        assertEquals(d.getSkullsNumber(), 2);

        d.resetDamages();

        assertEquals(mark2, d.getMarkMap());
        assertEquals(d.getSkullsNumber(), 2);
        assertTrue(d.getDamageList().isEmpty());

    }
    @Test
    public void vadoOltreConMarchi(){
        NormalDamageTrack d= new NormalDamageTrack();

        Player a= new Player();
        Player b= new Player();
        d.increaseSkull();

        d.addMark(a, 3);
        d.addDamage(b, 7);
        d.addDamage(a, 5);

        assertTrue(d.getMarkMap().isEmpty());
        assertEquals(d.score().get(a), (Integer) 4);
        assertEquals(d.score().get(b), (Integer) 7);

    }



}