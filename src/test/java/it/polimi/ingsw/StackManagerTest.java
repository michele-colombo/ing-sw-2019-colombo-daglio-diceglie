package it.polimi.ingsw;


import it.polimi.ingsw.server.ParserManager;
import it.polimi.ingsw.server.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StackManagerTest {
    /**
     * Tests lists initilization
     */
    @Test
    public void initTest(){
        StackManager sm= new ParserManager().getStackManager();

        Weapon arma1= new Weapon();
        Weapon arma2= new Weapon();

        ArrayList<Weapon> lista= new ArrayList<>();
        lista.add(arma1);
        lista.add(arma2);
        lista.add(arma2);
        lista.add(arma1);

        sm.initWeaponStack(lista);
        Weapon a= sm.drawWeapon();
        int i= 0;
        while(a != null){
            assertTrue(a== arma1 || a== arma2);
            a= sm.drawWeapon();
            i++;
        }

        assertTrue(a== null);
        assertTrue(i==4);


    }


    /**
     * Tests that ammo tiles discarded are resumed once the ammo tile stack gets empty.
     * (So ammo tiles are actually infinite)
     */
    @Test
    public void testRiciclo(){
        List<AmmoTile> l= new ArrayList<AmmoTile>();
        l.add(new AmmoTile(new Cash(1,1,1), false));
        l.add(new AmmoTile(new Cash(1,1,1), false));
        l.add(new AmmoTile(new Cash(1,1,1), false));
        l.add(new AmmoTile(new Cash(1,1,1), false));

        StackManager s= new ParserManager().getStackManager();

        s.initAmmoTilesStack(l);

        for(int i=0; i<10; i++) {
            s.trashAmmoTile(s.drawAmmoTile());
        }


        assertTrue(l.containsAll(s.getAmmoTilesActiveStack()));
        assertTrue(l.containsAll(s.getAmmoTilesWasteStack()));

        List<AmmoTile> somma= new ArrayList<>();
        somma.addAll(s.getAmmoTilesActiveStack());
        somma.addAll(s.getAmmoTilesWasteStack());

        assertTrue(l.containsAll(somma));
        assertTrue(somma.containsAll(l));




    }

    /**
     * Tests that once weapon stack gets empty is not possible to draw weapons anymore
     * (Weapons can run out)
     */
    @Test
    public void testRicicloImpossibileWeapon(){
    StackManager s= new ParserManager().getStackManager();

    List<Weapon> w= new ArrayList<>();
    w.add(new Weapon());
    w.add(new Weapon());
    w.add(new Weapon());
    w.add(new Weapon());
    w.add(new Weapon());

    s.initWeaponStack(w);

    for(int i=0; i<5; i++){
        assertTrue(w.contains(s.drawWeapon()));
    }
    assertNull(s.drawWeapon());
    assertTrue(s.getWeaponActiveStack().isEmpty());

    }

    /**
     * Tests the initializations of stacks (of weapons, ammo tiles and powerups) from json file
     */
    @Test
    public void buildingStacks(){
    StackManager sm= new ParserManager().getStackManager();

    assertEquals(sm.getOriginalWeaponArsenal().get(0).getName(), "Lock rifle");

    assertEquals(sm.getOriginalWeaponArsenal().size(), 21);
    assertEquals(sm.getOriginalAmmoTiles().size(), 36);
    assertEquals(sm.getOriginalPowerUps().size(), 24);

    for(int i=0; i<36; i++){
        AmmoTile at = sm.drawAmmoTile();
        sm.trashAmmoTile(at);
    }

    assertEquals(sm.getAmmoTilesActiveStack().size(), 0);
    assertEquals(sm.getAmmoTilesWasteStack().size(), 36);
    AmmoTile a = sm.drawAmmoTile();
    sm.trashAmmoTile(a);
    assertEquals(sm.getAmmoTilesActiveStack().size(), 35);
    assertEquals(sm.getAmmoTilesWasteStack().size(), 1);

    for(int i=0; i<73; i++){
        AmmoTile at = sm.drawAmmoTile();
        sm.trashAmmoTile(at);
    }
    sm.discardPowerUp(sm.drawPowerUp());
    assertEquals(sm.getPowerUpActiveStack().size(), 23);
    }

    /**
     * Tests the loading of powerups from json
     */
    @Test
    public void powerUpsLoadingTest(){
    StackManager sm= new ParserManager().getStackManager();
    assertEquals(sm.getOriginalPowerUps().size(), 24);

    for(int i=0; i< sm.getOriginalPowerUps().size(); i++) {
        assertEquals(sm.getOriginalPowerUps().get(i).toString().charAt(0), String.valueOf(i).charAt(0));
        assertTrue(sm.getOriginalPowerUps().get(i).getEffects().size()!=0);
    }

    }

    /**
     * Tests the loading of weaopons from json
     */
    @Test
    public void weaponsLoadingTest(){
    StackManager sm= new ParserManager().getStackManager();

    assertEquals(sm.getOriginalWeaponArsenal().size(), 21);

    for(int i=0; i<sm.getOriginalWeaponArsenal().size(); i++){
        assertTrue( sm.getOriginalWeaponArsenal().get(i).getSelectableModes(new ArrayList<>()).size() >0);
    }

    assertEquals(sm.getOriginalWeaponArsenal().get(1).getName(), "Machine gun");
    }
}