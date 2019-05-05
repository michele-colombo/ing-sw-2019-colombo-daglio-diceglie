package it.polimi.ingsw;

import it.polimi.ingsw.Cash;
import it.polimi.ingsw.Mode;
import it.polimi.ingsw.Weapon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Color.*;

public class WeaponTest {

    @Test
    public void initWeaponDescription(){
        Weapon w= new Weapon("Martello ionico", new Cash(YELLOW, 1), YELLOW);
        Mode base= new Mode(false, -1, "modalità base", "Dai 2 danni a 1 bersaglio nel quadrato in cui ti trovi", new Cash());

        Mode alt= new Mode(false, 0, "modalita' polverizzare", "dai 3 danni a 1 bersaglio nel quadrato in cui ti trovi, poi muovi quel bersaglio di 0, 1, 2 quadrati in una direzione", new Cash());

        w.addMode(base);
        w.addMode(alt);

        System.out.println(w.getDescription());
    }

    @Test
    public void discounted(){
        Weapon arma = new Weapon("Gingillo", new Cash(2, 0, 0), BLUE);
        assertTrue(arma.getDiscountedCost().isEqual(new Cash(1, 0, 0)));
    }

    @Test
    public void selectableModes(){
        Weapon arma= new Weapon("Gingillo", new Cash(1, 1, 0), BLUE);
        Mode base= new Mode(false, -1, "modalità base", "Dai 2 danni a 1 bersaglio nel quadrato in cui ti trovi", new Cash());
        Mode alt= new Mode(false, 0, "modalita' polverizzare", "dai 3 danni a 1 bersaglio nel quadrato in cui ti trovi, poi muovi quel bersaglio di 0, 1, 2 quadrati in una direzione", new Cash());

        arma.addMode(base);
        arma.addMode(alt);

        List<Mode> foundByHand= new ArrayList<>();
        foundByHand.add(base);

        assertEquals(foundByHand, arma.getSelectableModes(new ArrayList<>()));

        foundByHand.clear();
        foundByHand.add(alt);

        List<Mode> temp= new ArrayList<>();
        temp.add(base);
        assertEquals(foundByHand, arma.getSelectableModes(temp));

    }

    @Test
    public void selectableModesFromBuilder(){
        List<Weapon> arsenale= new WeaponBuilder().getWeapons();

        Weapon distrutt= arsenale.get(0);
        Weapon mitra= arsenale.get(1);
        Weapon torp = arsenale.get(2);
        Weapon plasma = arsenale.get(3);

        List<Mode> already= new ArrayList<>();
        List<Mode> expected= new ArrayList<>();

        expected.add(distrutt.getMyModes().get(0));
        assertEquals(expected, distrutt.getSelectableModes(already));

        already.add(distrutt.getMyModes().get(0));
        expected.clear();
        expected.add(distrutt.getMyModes().get(1));

        assertEquals(expected, distrutt.getSelectableModes(already));

        already.clear();
        expected.clear();
        expected.add(plasma.getMyModes().get(0));
        expected.add(plasma.getMyModes().get(1));

        assertEquals(expected, plasma.getSelectableModes(already));



    }


    @Test
    public void ancoraSelectableModesZX(){
        Weapon z = new WeaponBuilder().getWeapons().get(16);  //zx-2

        List<Mode> alreadySelected= new ArrayList<>();
        List<Mode> foundByHand= new ArrayList<>();

        foundByHand.addAll(z.getMyModes());

        assertEquals(z.getSelectableModes(alreadySelected), foundByHand);

        alreadySelected.add(z.getMyModes().get(0));
        foundByHand.clear();

        assertEquals(z.getSelectableModes(alreadySelected), foundByHand);
        assertEquals(z.getSelectableModes(alreadySelected), new ArrayList<>());

    }

    @Test
    public void selectableModesspadaFotonica(){
        Weapon z = new WeaponBuilder().getWeapons().get(15);  //spada fotonica

        List<Mode> alreadySelected= new ArrayList<>();
        List<Mode> foundByHand= new ArrayList<>();

        Mode m1= z.getMyModes().get(0);
        Mode m2= z.getMyModes().get(1);
        Mode m3= z.getMyModes().get(2);

        foundByHand.add(m1);
        foundByHand.add(m2);

        assertEquals(z.getSelectableModes(alreadySelected), foundByHand);

        alreadySelected.add(m2);
        foundByHand.remove(m2);

        assertEquals(z.getSelectableModes(alreadySelected), foundByHand);

        alreadySelected.clear();
        alreadySelected.add(m1);

        foundByHand.clear();
        foundByHand.add(m2);
        foundByHand.add(m3);

        assertEquals(z.getSelectableModes(alreadySelected), foundByHand);

        alreadySelected.add(m2);
        foundByHand.remove(m2);

        assertEquals(z.getSelectableModes(alreadySelected), foundByHand);


    }




}
