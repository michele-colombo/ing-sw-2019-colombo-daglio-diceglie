package it.polimi.ingsw;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

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

}