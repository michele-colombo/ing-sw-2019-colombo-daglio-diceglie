package it.polimi.ingsw;

import it.polimi.ingsw.Cash;
import it.polimi.ingsw.Mode;
import it.polimi.ingsw.Weapon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.Color.*;

public class WeaponTest {
    private static final String testBackupPath = "./src/test/resources/savedGamesForTests/";

    public void printSel(Player p){
        System.out.println(p.selectablesToString());
    }



    public <T> void printList(List<T> list){
        System.out.println("printing a list:");
        for (T t : list){
            if (t == null){
                System.out.println("NULL!!!!");
            } else {
                System.out.println(t);
            }
        }
    }

    public <K, V> void printMap(Map<K, V> map){
        System.out.println("printing a map:");
        for (Map.Entry<K, V> entry : map.entrySet()){
            if (entry.getKey() == null) System.out.print("null -> ");
            else System.out.print(entry.getKey()+" -> ");
            if (entry.getValue() == null) System.out.println("null");
            else System.out.println(entry.getValue());
        }
    }

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

    @Test
    public void testLockRifle1(){
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(testBackupPath, "lockRifleTestBefore");

        Player p1 = gm.getPlayerByName("first");
        Player p2 = gm.getPlayerByName("second");
        Player p3 = gm.getPlayerByName("third");
        Player p4 = gm.getPlayerByName("fourth");
        printSel(p1);
        gm.performAction(p1, p1.getSelectableActions().get(2)); //shoot
        printSel(p1);
        gm.shootWeapon(p1, p1.getSelectableWeapons().get(0));
        printSel(p1);
        //assert(only base mode selectable here)
        gm.addMode(p1, p1.getSelectableModes().get(0));
        printSel(p1);
        //assert(can select second lock or confirm)
        gm.addMode(p1, p1.getSelectableModes().get(0));
        printSel(p1);
        gm.confirmModes(p1);
        printSel(p1);
        assertTrue(p1.getSelectablePlayers().contains(p2));
        assertTrue(p1.getSelectablePlayers().contains(p4));
        //assert(p1 ha tot danni)
        //assert(p1 ha tot marchi)
        printList(p2.getDamageTrack().getDamageList());
        printMap(p2.getDamageTrack().getMarkMap());
        gm.shootTarget(p1, p2, null);
        //assert(p1 ora ha tot danni)
        //assert(p1 ora ha tot marchi)
        printList(p2.getDamageTrack().getDamageList());
        printMap(p2.getDamageTrack().getMarkMap());
        printSel(p1);
        assert(p1.getSelectablePlayers().contains(p4));
        printList(p4.getDamageTrack().getDamageList());
        printMap(p4.getDamageTrack().getMarkMap());
        gm.shootTarget(p1, p4, null);
        //assertions...
        printList(p4.getDamageTrack().getDamageList());
        printMap(p4.getDamageTrack().getMarkMap());
        printSel(p1);
        Backup check = Backup.initFromFile(testBackupPath, "lockRifleTestAfter");
        Backup currentState = new Backup((gm.getMatch()));
        assertEquals(check, currentState);
        assertTrue(check != currentState);
    }




}
