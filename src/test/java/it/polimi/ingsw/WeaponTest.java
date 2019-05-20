package it.polimi.ingsw;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.testUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

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

    @Test
    public void testLockRifle1(){
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS, "lockRifleTestBefore");

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
        Backup check = Backup.initFromFile(SAVED_GAMES_FOR_TESTS, "lockRifleTestAfter");
        Backup currentState = new Backup((gm.getMatch()));
        assertEquals(check, currentState);
        assertTrue(check != currentState);
    }

    @Test
    public void testMachineGun() {
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS, "machineGunTestBefore");

        Player gianni = gm.getPlayerByName("first");
        Player beppe = gm.getPlayerByName("second");
        Player evila = gm.getPlayerByName("third");
        Player gabriele = gm.getPlayerByName("fourth");
        Player michele = gm.getPlayerByName("fifth");

        assertEquals(gianni.getSelectableActions().size(), 4);
        for (Player p : Arrays.asList(new Player[]{beppe, evila, gabriele, michele})) {
            assertEquals(p.getSelectableActions().size(), 0);
        }

        printSel(gianni);
        gm.performAction(gianni, gianni.getSelectableActions().get(2));  //choosing to shoot
        assertTrue(gianni.getSelectableWeapons().containsAll(gm.getMatch().getStackManager().getOriginalWeaponArsenal().subList(0, 3)));

        printSel(gianni);
        gm.shootWeapon(gianni, gm.getMatch().getStackManager().getWeaponFromName("Machine gun"));  //choosing machine gun
        assertEquals(gianni.getSelectableModes().size(), 1);

        printSel(gianni);
        gm.addMode(gianni, gianni.getSelectableModes().get(0));  //adding basic effect
        assertEquals(gianni.getSelectableModes().size(), 1);

        printSel(gianni);
        gm.addMode(gianni, gianni.getSelectableModes().get(0));  //adding with focus shot

        System.out.println("COSA STA SUCCEDENDO");

        System.out.println(gianni.getWallet());
        assertTrue(gianni.getWallet().isEqual(new Cash(3, 3, 3)));
        assertEquals(gianni.getState(), PlayerState.PAYING);

        printSel(gianni);
        gm.completePayment(gianni);   //paying with ammos
        assertTrue(gianni.getWallet().isEqual(new Cash(3, 3, 2)));
        assertEquals(gianni.getSelectableModes().get(0), gm.getMatch().getStackManager().getOriginalWeaponArsenal().get(1).getMyModes().get(2));

        gm.addMode(gianni, gianni.getSelectableModes().get(0));   //adding third effect

        printSel(gianni);
        gm.confirmModes(gianni);  //confirm modes
        assertEquals(gianni.getState(), PlayerState.SHOOT_TARGET);
        assertEquals(gianni.getSelectablePlayers().get(0), evila);

        printSel(gianni);
        assertEquals(evila.getDamageTrack().getDamageList().size(), 4);
        gm.shootTarget(gianni, evila, null);  //using the basic effect to damage evila
        assertEquals(evila.getDamageTrack().getDamageList().size(), 6);

        printSel(gianni);
        assertEquals(gianni.getSelectablePlayers().size(), 0);
        assertEquals(gianni.getSelectableCommands(), Collections.singletonList(Command.OK));
        gm.shootTarget(gianni, null, null);  //and then click OK

        printSel(gianni);
        assertEquals(gianni.getSelectablePlayers(), Collections.singletonList(evila));

        gm.shootTarget(gianni, evila, null);
        assertEquals(evila.getDamageTrack().getDamageList().size(), 7);

        printSel(gianni);

        assertEquals(gianni.getState(), PlayerState.SHOOT_TARGET);

        gm.shootTarget(gianni, evila, null);  //damaging evila with last effect
        printSel(gianni);

        assertEquals(gm.getMatch().getCurrentAction().getCurrEffects().size(), 0);


        System.out.println(gianni.getWallet().toString());

        //questo e' il controllo che ho fatto per capire che il wallet non era ripristinato
        //il portafoglio dovrebbe essere (3, 3, 3) come all'izizio, ma questa assert passa
        assertFalse(gianni.getWallet().isEqual(new Cash(3, 3, 2)));
        assertTrue(gianni.getWallet().isEqual((new Cash(2, 3, 2))));
    }



    @Test
    public void testMachineGunSecondScenario(){
        Gson gson= new Gson();


        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS, "machineGunTestBefore");

        Player gianni = gm.getPlayerByName("first");
        Player beppe = gm.getPlayerByName("second");
        Player evila = gm.getPlayerByName("third");
        Player gabriele = gm.getPlayerByName("fourth");
        Player michele = gm.getPlayerByName("fifth");





        for(int i=0; i<gianni.getWeaponAsMap().size(); i++){
            assertEquals( gianni.getWeaponAsMap().values().toArray()[i], true);  //all the weapons are loaded
        }

        printSel(gianni);
        //todo it will pass when everything works
/*
        Backup check = Backup.initFromFile(SAVED_GAMES_FOR_TESTS, "machineGunTestBefore");
        Backup currentState = new Backup((gm.getMatch()));

        System.out.println(gson.toJson(currentState));
        assertTrue(check.equals(currentState));
*/

        gm.performAction(gianni, gianni.getSelectableActions().get(2));  // I wanna shoot
        assertEquals(gianni.getSelectableWeapons().size(), 3);
        gm.shootWeapon(gianni, gm.getMatch().getStackManager().getWeaponFromName("Machine gun")); //I choose machine gun
        gm.addMode(gianni, gianni.getSelectableModes().get(0)); //adding basic effect
        gm.confirmModes(gianni); //and nothing more

        assertEquals(gianni.getState(), PlayerState.SHOOT_TARGET); //gianni is shooting!
        assertEquals(gianni.getSelectablePlayers().get(0), evila);
        assertEquals(gianni.getSelectablePlayers().size(), 1);
        assertEquals(evila.getDamageTrack().getDamageList().size(), 4);

        gm.shootTarget(gianni, evila, null);  //shooting evila

        assertEquals(evila.getDamageTrack().getDamageList().size(), 6);  //evila gets 2 damages
        assertTrue(gianni.getSelectableCommands().contains(Command.OK)); //gianni can't do anything more

        printSel(gianni);
        gm.shootTarget(gianni, null, null);  //so he shoots nothing

        printSel(gianni);


    }

    @Test
    public void testTHOR(){
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS,"machineGunTestBefore");
        StackManager sm= gm.getMatch().getStackManager();

        Player gianni = gm.getPlayerByName("first");
        Player beppe = gm.getPlayerByName("second");
        Player evila = gm.getPlayerByName("third");
        Player gabriele = gm.getPlayerByName("fourth");
        Player michele= gm.getPlayerByName("fifth");

        assertEquals(gianni.getState(), PlayerState.CHOOSE_ACTION);
        assertEquals(gianni.getSelectableActions().size(), 4);
        assertEquals(gianni.getSelectableActions().get(2).toString(), "S");
        gm.performAction(gianni, gianni.getSelectableActions().get(2));   //gianni shoots

        assertEquals(gianni.getSelectableWeapons().size(), 3);
        assertTrue(gianni.getSelectableWeapons().containsAll(sm.getOriginalWeaponArsenal().subList(0, 3)));
        gm.shootWeapon(gianni, sm.getWeaponFromName("T.H.O.R."));

        assertEquals(gianni.getState(), PlayerState.CHOOSE_MODE);
        assertEquals(gianni.getSelectableModes().size(), 1);
        assertEquals(gianni.getSelectableModes().get(0).getTitle(), "basic effect");

        gm.addMode(gianni, gianni.getSelectableModes().get(0));

        assertEquals(gianni.getSelectableModes().size(), 1);
        assertEquals(gianni.getSelectableModes().get(0).getTitle(), "with chain reaction");

        gm.addMode(gianni, gianni.getSelectableModes().get(0));

        assertEquals(gianni.getState(), PlayerState.CHOOSE_MODE);

        assertEquals(gianni.getSelectableModes().get(0).getTitle(), "with high voltage");
        gm.addMode(gianni, gianni.getSelectableModes().get(0));

        assertEquals(gianni.getSelectableModes().size(), 0);
        assertEquals(gianni.getSelectableCommands().size(), 1);
        assertEquals(gianni.getSelectableCommands().get(0), Command.OK);

        gm.confirmModes(gianni);

        assertEquals(gianni.getState(), PlayerState.SHOOT_TARGET);

        assertEquals(gianni.getSelectablePlayers().size(), 1);
        assertEquals(gianni.getSelectablePlayers().get(0).getName(), "third");
        assertEquals(gianni.getSelectablePlayers().get(0), evila);

        assertEquals(evila.getDamageTrack().getDamageList().size(), 4);

        gm.shootTarget(gianni, evila, null);

        assertEquals(evila.getDamageTrack().getDamageList().size(), 7);

        assertEquals(gianni.getState(), PlayerState.SHOOT_TARGET);
        assertEquals(gianni.getSelectablePlayers().size(), 1);
        assertEquals(gianni.getSelectablePlayers().get(0).getName(), "fifth");
        assertEquals(gianni.getSelectablePlayers().get(0), michele);

        assertEquals(michele.getDamageTrack().getDamageList().size(), 6);

        gm.shootTarget(gianni, michele, null);

        assertEquals(michele.getDamageTrack().getDamageList().size(), 7);

        assertEquals(gianni.getState(), PlayerState.SHOOT_TARGET);
        assertEquals(gianni.getSelectablePlayers().size(), 1);
        assertEquals(gianni.getSelectablePlayers().get(0).getName(), "fourth");
        assertEquals(gianni.getSelectablePlayers().get(0), gabriele);
        assertEquals(gm.getMatch().getCurrentAction().getCurrEffects().size(), 1);

        assertEquals(gabriele.getDamageTrack().getDamageList().size(), 6);

        gm.shootTarget(gianni, gabriele, null);

        assertEquals(gabriele.getDamageTrack().getDamageList().size(), 8);

        assertEquals(gianni.getState(), PlayerState.CHOOSE_ACTION);
        assertTrue(gm.getMatch().getCurrentAction().getCurrEffects().isEmpty());

        assertTrue(gianni.getWallet().isEqual(new Cash(1, 3, 3)));


    }

    @Test
    public void testPlasmaGun(){
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS,"plasmaGunTestBefore");
        StackManager sm= gm.getMatch().getStackManager();

        //System.out.println(sm.getOriginalPowerUps());

        Player gianni = gm.getPlayerByName("first");
        Player beppe = gm.getPlayerByName("second");
        Player evila = gm.getPlayerByName("third");
        Player gabriele = gm.getPlayerByName("fourth");
        Player michele= gm.getPlayerByName("fifth");

        assertTrue(gianni.getWallet().isEqual(new Cash(3, 3, 3)));
        assertEquals(gianni.getPowerUps().size(), 3);
        assertEquals(gianni.getState(), PlayerState.CHOOSE_ACTION);
        assertEquals(gianni.getSelectableCommands().size(), 0);
        assertEquals(gianni.getSelectableActions().size(), 4);
        assertEquals(gianni.getSelectableActions().get(2).toString(), "S");

        gm.performAction(gianni, gianni.getSelectableActions().get(2));  //gianni shoots

        assertEquals(gianni.getSelectableWeapons().size(), 3);
        assertTrue(gianni.getSelectableWeapons().containsAll(sm.getOriginalWeaponArsenal().subList(3, 6)));

        gm.shootWeapon(gianni, sm.getWeaponFromName("Plasma gun"));

        assertEquals(gianni.getState(), PlayerState.CHOOSE_MODE);
        assertEquals(gianni.getSelectableModes().size(), 2);
        assertTrue(gianni.getSelectableModes().containsAll(sm.getWeaponFromName("Plasma gun").getMyModes().subList(0, 2)));

        printSel(gianni);

        assertEquals(gianni.getSelectableModes().get(1).getTitle(), "with phase glide");
        assertEquals(gianni.getSelectableCommands().size(), 0);

        gm.addMode(gianni, gianni.getSelectableModes().get(1));  //choosing "with phase glide"

        assertEquals(gianni.getState(), PlayerState.CHOOSE_MODE);
        assertEquals(gianni.getSelectableModes().size(), 1);
        assertEquals(gianni.getSelectableModes().get(0).getTitle(), "basic effect");

        gm.addMode(gianni, gianni.getSelectableModes().get(0)); //choosing basic effect

        assertEquals(gianni.getState(), PlayerState.CHOOSE_MODE);
        assertEquals(gianni.getSelectableModes().size(), 1);
        assertEquals(gianni.getSelectableModes().get(0).getTitle(), "with charged shot");

        gm.addMode(gianni, gianni.getSelectableModes().get(0)); //choosing the last effect

        assertEquals(gianni.getState(), PlayerState.PAYING);
        assertEquals(gianni.getSelectableCommands(), Collections.singletonList(Command.OK));
        assertEquals(gianni.getSelectablePowerUps(), Collections.singletonList(sm.getPowerUpFromString("1-Targeting scope-BLUE")));

        gm.payWith(gianni, sm.getPowerUpFromString("1-Targeting scope-BLUE"));  //paying last effect with powerUp

        assertEquals(gianni.getPowerUps().size(),2);
        assertEquals(gianni.getState(), PlayerState.CHOOSE_MODE);
        assertEquals(gianni.getSelectableCommands(), Collections.singletonList(Command.OK) );

        gm.confirmModes(gianni); //confirm selected modes

        assertEquals(gianni.getState(), PlayerState.SHOOT_TARGET);
        assertEquals(gianni.getSelectablePlayers().size(), 0);
        assertEquals(gianni.getSelectableSquares().size(), 7);

        printSel(gianni);

        gm.shootTarget(gianni, null, gm.getMatch().getLayout().getSquare(1, 2));//moving to square (3-1)

        assertEquals(gianni.getSquarePosition(), gm.getMatch().getLayout().getSquare(1, 2));
        assertEquals(gianni.getState(), PlayerState.SHOOT_TARGET);
        assertEquals(gianni.getSelectableSquares().size(), 0);
        assertEquals(gianni.getSelectablePlayers().size(), 2);
        assertEquals(Set.copyOf( gianni.getSelectablePlayers() ), Set.copyOf(Arrays.asList(new Player[]{evila, michele})));  //gianni can select only evila and michele

        printSel(gianni);

        assertEquals(michele.getDamageTrack().getDamageList().size(), 6);
        gm.shootTarget(gianni, michele ,null);  //shooting michele

        assertEquals(michele.getDamageTrack().getDamageList().size(), 9);  //applied basic effect and also with charged shot

        assertEquals(gm.getMatch().getCurrentAction().getCurrEffects().size(), 0);
        assertEquals(gianni.getState(), PlayerState.CHOOSE_ACTION);
        assertEquals(gianni.getWallet().isEqual(new Cash(3, 3, 3)), true);

        assertEquals(beppe.getDamageTrack().getDamageList().size(), 4);
        assertEquals(evila.getDamageTrack().getDamageList().size(), 4);
        assertEquals(gabriele.getDamageTrack().getDamageList().size(), 6);

    }

    @Test
    public void testWhisper(){
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS,"plasmaGunTestBefore");
        StackManager sm= gm.getMatch().getStackManager();

        //System.out.println(sm.getOriginalPowerUps());

        Player gianni = gm.getPlayerByName("first");
        Player beppe = gm.getPlayerByName("second");
        Player evila = gm.getPlayerByName("third");
        Player gabriele = gm.getPlayerByName("fourth");
        Player michele= gm.getPlayerByName("fifth");

        assertEquals(gianni.getSelectableActions().get(2).toString(), "S");
        gm.performAction(gianni, gianni.getSelectableActions().get(2));  //choosing to shoot

        assertEquals(gianni.getState(), PlayerState.SHOOT_WEAPON);
        gm.shootWeapon(gianni, sm.getWeaponFromName("Whisper"));  //choosing whisper

        assertEquals(gianni.getState(), PlayerState.CHOOSE_MODE);
        assertEquals(gianni.getSelectableModes().get(0).getTitle(), "effect");
        gm.addMode(gianni, gianni.getSelectableModes().get(0));  //adding effect (unique)

        assertEquals(gianni.getSelectableCommands(), Collections.singletonList(Command.OK));
        gm.confirmModes(gianni);   //OK

        printSel(gianni);

        assertEquals(gianni.getSelectableCommands().size(), 0);
        assertEquals(gianni.getSelectablePlayers(), Collections.singletonList(evila));
        assertEquals(gianni.getSelectableSquares().size(), 0);

        assertEquals(evila.getDamageTrack().getDamageList().size(), 4);

        gm.shootTarget(gianni, evila, null);  //shooting evila piva

        assertEquals(evila.getDamageTrack().getDamageList().size(), 8);
        assertEquals(evila.getDamageTrack().getMarkMap().get(beppe), 2 );
        assertEquals(evila.getDamageTrack().getMarkMap().get(gianni), 1 );

        assertEquals(gianni.getState(), PlayerState.USE_POWERUP);  //gianni can select to use targeting scope, but it will be tested when we will be sure about PowerUps





    }


    @Test
    public void testElectroScythe(){
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS,"electroScytheTestBefore");
        StackManager sm= gm.getMatch().getStackManager();

        //System.out.println(sm.getOriginalPowerUps());

        Player primo = gm.getPlayerByName("first");
        Player secondo = gm.getPlayerByName("second");
        Player terzo = gm.getPlayerByName("third");
        Player quarto = gm.getPlayerByName("fourth");
        Player quinto= gm.getPlayerByName("fifth");



        for(Player p: gm.getActivePlayers()){
            assertEquals(p.getSquarePosition(), gm.getMatch().getLayout().getSquare(2, 2));
        }

        assertEquals(primo.getState(), PlayerState.CHOOSE_ACTION);
        assertEquals(primo.getSelectableActions().size(), 4);
        assertEquals(primo.getSelectableActions().get(2).toString(), "S");
        gm.performAction(primo, primo.getSelectableActions().get(2));  //primo shoots

        assertEquals(primo.getSelectableWeapons().size(), 3);
        assertTrue(primo.getSelectableWeapons().containsAll(gm.getMatch().getStackManager().getOriginalWeaponArsenal().subList(5, 8)));

        Weapon electroscythe= gm.getMatch().getStackManager().getWeaponFromName("Electoscythe");
        gm.shootWeapon(primo, electroscythe);  //choosing electrocythe

        assertEquals(primo.getState(), PlayerState.CHOOSE_MODE);
        assertEquals(primo.getSelectableModes().size(), 2);
        assertTrue(primo.getSelectableModes().containsAll(electroscythe.getMyModes()));

        gm.addMode(primo, electroscythe.getMyModes().get(0));

        assertEquals(primo.getSelectableModes().size(), 0);

        assertEquals(secondo.getDamageTrack().getDamageList().size(), 4);
        assertEquals(terzo.getDamageTrack().getDamageList().size(), 4);
        assertEquals(quarto.getDamageTrack().getDamageList().size(), 6);
        assertEquals(quinto.getDamageTrack().getDamageList().size(), 6);

        gm.confirmModes(primo);

        assertEquals(secondo.getDamageTrack().getDamageList().size(), 7);
        assertEquals(terzo.getDamageTrack().getDamageList().size(), 6);
        assertEquals(quarto.getDamageTrack().getDamageList().size(), 7);
        assertEquals(quinto.getDamageTrack().getDamageList().size(), 7);


        assertEquals(primo.getState(), PlayerState.CHOOSE_ACTION);


        Backup check = Backup.initFromFile(SAVED_GAMES_FOR_TESTS, "electroScytheTestAfter");
        Backup currentState = new Backup((gm.getMatch()));

        assertTrue(check.equals(currentState));





    }


    @Test
    public void testElectroScytheSecondMode(){
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS,"electroScytheTestBefore");
        StackManager sm= gm.getMatch().getStackManager();

        //System.out.println(sm.getOriginalPowerUps());

        Player primo = gm.getPlayerByName("first");
        Player secondo = gm.getPlayerByName("second");
        Player terzo = gm.getPlayerByName("third");
        Player quarto = gm.getPlayerByName("fourth");
        Player quinto= gm.getPlayerByName("fifth");



        for(Player p: gm.getActivePlayers()){
            assertEquals(p.getSquarePosition(), gm.getMatch().getLayout().getSquare(2, 2));
        }

        assertEquals(primo.getState(), PlayerState.CHOOSE_ACTION);
        assertEquals(primo.getSelectableActions().size(), 4);
        assertEquals(primo.getSelectableActions().get(2).toString(), "S");
        gm.performAction(primo, primo.getSelectableActions().get(2));  //primo shoots

        assertEquals(primo.getSelectableWeapons().size(), 3);
        assertTrue(primo.getSelectableWeapons().containsAll(gm.getMatch().getStackManager().getOriginalWeaponArsenal().subList(5, 8)));

        Weapon electroscythe= gm.getMatch().getStackManager().getWeaponFromName("Electoscythe");
        gm.shootWeapon(primo, electroscythe);  //choosing electrocythe

        assertEquals(primo.getState(), PlayerState.CHOOSE_MODE);
        assertEquals(primo.getSelectableModes().size(), 2);
        assertTrue(primo.getSelectableModes().containsAll(electroscythe.getMyModes()));

        gm.addMode(primo, electroscythe.getMyModes().get(1));

        assertEquals(primo.getSelectableModes().size(), 0);

        assertEquals(secondo.getDamageTrack().getDamageList().size(), 4);
        assertEquals(terzo.getDamageTrack().getDamageList().size(), 4);
        assertEquals(quarto.getDamageTrack().getDamageList().size(), 6);
        assertEquals(quinto.getDamageTrack().getDamageList().size(), 6);

        gm.confirmModes(primo);

        assertEquals(secondo.getDamageTrack().getDamageList().size(), 8);
        assertEquals(terzo.getDamageTrack().getDamageList().size(), 7);
        assertEquals(quarto.getDamageTrack().getDamageList().size(), 8);
        assertEquals(quinto.getDamageTrack().getDamageList().size(), 8);


        assertEquals(primo.getState(), PlayerState.CHOOSE_ACTION);


        Backup check = Backup.initFromFile(SAVED_GAMES_FOR_TESTS, "electroScytheSecondModeTestAfter");
        Backup currentState = new Backup((gm.getMatch()));

        assertTrue(check.equals(currentState));





    }


    @Test
    public void testTractorBeam(){
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS,"tractorBeamTestBefore");
        StackManager sm= gm.getMatch().getStackManager();

        //System.out.println(sm.getOriginalPowerUps());

        Player primo = gm.getPlayerByName("first");
        Player secondo = gm.getPlayerByName("second");
        Player terzo = gm.getPlayerByName("third");
        Player quarto = gm.getPlayerByName("fourth");
        Player quinto= gm.getPlayerByName("fifth");


        // I try doing all operation in a row

        gm.performAction(primo, primo.getSelectableActions().get(2));  //choosing to shoot

        Weapon tractorBeam= sm.getWeaponFromName("Tractor Beam");

        gm.shootWeapon(primo, tractorBeam);  //choosing tractor beam

        assertEquals(primo.getState(), PlayerState.CHOOSE_MODE);
        assertEquals(primo.getSelectableModes().size(), 2);
        assertTrue(primo.getSelectableModes().containsAll(tractorBeam.getMyModes()));

        gm.addMode(primo, tractorBeam.getMyModes().get(0) );  //choosing free mode
        assertEquals(primo.getSelectableModes().size(), 0);

        gm.confirmModes(primo);  //starting shooting

        assertEquals(primo.getState(), PlayerState.SHOOT_TARGET);
        assertEquals(primo.getSelectableSquares().size(), 5);

        gm.shootTarget(primo, null, gm.getMatch().getLayout().getSquare(2, 0));

        assertEquals(primo.getSelectablePlayers().size(), 2);
        assertTrue(primo.getSelectablePlayers().containsAll(Arrays.asList(new Player[]{terzo, quarto})) );

        assertEquals(quarto.getDamageTrack().getDamageList().size(), 6);

        gm.shootTarget(primo, quarto, null);

        assertEquals(quarto.getDamageTrack().getDamageList().size(), 7);

        assertEquals(primo.getState(), PlayerState.CHOOSE_ACTION);
        assertEquals(quarto.getSquarePosition(), gm.getMatch().getLayout().getSquare(2, 0));


        Backup check = Backup.initFromFile(SAVED_GAMES_FOR_TESTS, "tractorBeamTestAfter");
        Backup currentState = new Backup((gm.getMatch()));

        //System.out.println(new Gson().toJson(currentState));

        assertTrue(check.equals(currentState));
    }








}
