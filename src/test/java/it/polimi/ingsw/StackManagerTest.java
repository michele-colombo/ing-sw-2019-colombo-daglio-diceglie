package it.polimi.ingsw;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class StackManagerTest {
@Test
    public void initTest(){
        StackManager sm= new StackManager();

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


    @Test
    public void testRiciclo(){
        List<AmmoTile> l= new ArrayList<AmmoTile>();
        l.add(new AmmoTile(new Cash(1,1,1), false));
        l.add(new AmmoTile(new Cash(1,1,1), false));
        l.add(new AmmoTile(new Cash(1,1,1), false));
        l.add(new AmmoTile(new Cash(1,1,1), false));

        StackManager s= new StackManager();

        s.initAmmoTilesStack(l);

        for(int i=0; i<10; i++) {
            s.trashAmmoTile(s.drawAmmoTile());
        }


        assertTrue(l.containsAll(s.getAmmoTilesActiveStack()));
        assertTrue(l.containsAll(s.getAmmoTilesWasteStack()));

        List<AmmoTile> somma= new ArrayList<AmmoTile>();
        somma.addAll(s.getAmmoTilesActiveStack());
        somma.addAll(s.getAmmoTilesWasteStack());

        assertTrue(l.containsAll(somma));
        assertTrue(somma.containsAll(l));




    }

    @Test
    public void testRicicloImpossibileWeapon(){
    StackManager s= new StackManager();

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


    @Test
    public void proveConArrayList(){
    List<Color> lista= new ArrayList<>();
    Color uno= Color.RED;
    Color due= Color.BLUE;
    Color tre= Color.YELLOW;
    Color quattro= Color.GREEN;

    lista.add(uno);
    lista.add(due);
    lista.add(tre);
    lista.add(quattro);

    assertEquals(lista.get(0), uno);
    assertEquals(lista.size(), 4);
    lista.remove(uno);
    assertEquals(lista.get(0), due);
    assertEquals(lista.size(), 3);

    }

    @Test
    public void buildingStacks(){
    StackManager sm= new StackManager();

    assertTrue(sm.getOriginalWeaponArsenal().get(0).getName() == "Lock rifle");
    }
}