package it.polimi.ingsw;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PowerUpType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.enums.PlayerState.*;
import static it.polimi.ingsw.testUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class PowerUpTest {


    /**
     * Tests the use of the powerup "tagback grenade" by simulating a shooting action.
     */
    @Test
    public void tagbackGrenadeTest(){
        //first shoots to fourth (which has a tagback, but can't see first) and fifth (which has a tagback and sees first)
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources( "tagbackGrenadeTestBefore"));
        Match match = gm.getMatch();
        StackManager sm = gm.getMatch().getStackManager();
        Layout layout = gm.getMatch().getLayout();

        Player first = gm.getPlayerByName("first");
        Player second = gm.getPlayerByName("second");
        Player third = gm.getPlayerByName("third");
        Player fourth = gm.getPlayerByName("fourth");
        Player fifth = gm.getPlayerByName("fifth");

            printSel(first);

        gm.performAction(first, first.getSelectableActions().get(2));   //he shoots

            printSel(first);

        gm.shootWeapon(first, sm.getWeaponFromName("Machine gun"));

            printSel(first);

        gm.addMode(first, first.getSelectableModes().get(0));

            printSel(first);

        gm.confirmModes(first);

            printSel(first);
            List<Player> temp = new ArrayList<>();
            temp.add(second);
            temp.add(third);
            temp.add(fourth);
            temp.add(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, fifth, null);

            printSel(first);
            temp.remove(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, fourth, null);

            assertEquals(IDLE, fourth.getState());
            assertEquals(1, fourth.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertFalse(fourth.hasSelectables());   //because he can't see first
            assertFalse(first.hasSelectables());
            assertEquals(USE_POWERUP, first.getState());
            assertEquals(USE_POWERUP, fifth.getState());
            assertEquals(1, fifth.getSelectablePowerUps().size());
            assertTrue(fifth.getSelectableCommands().contains(Command.OK));
            assertFalse(first.getDamageTrack().getMarkMap().containsKey(fifth));

        gm.usePowerUp(fifth, fifth.getSelectablePowerUps().get(0));

            assertEquals(1, first.getDamageTrack().getMarkMap().get(fifth));
            assertEquals(CHOOSE_ACTION, first.getState());
            assertEquals(IDLE, fifth.getState());
            assertFalse(fifth.hasSelectables());

    }

    /**
     * Tests the use of the powerup "tagback grenade" by simulating a shooting action.
     * During the action two players disconnects.
     * Of course disconnection here is intended from a model point of view (no network involved),
     * which means the players become inactive
     */
    @Test
    public void tagbackGrenadeTestWithDisconnection(){
        //first shoots to fourth (which has a tagback, but can't see first) and fifth (which has a tagback and sees first)
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("tagbackGrenadeTestBefore"));
        Match match = gm.getMatch();
        StackManager sm = gm.getMatch().getStackManager();
        Layout layout = gm.getMatch().getLayout();

        Player first = gm.getPlayerByName("first");
        Player second = gm.getPlayerByName("second");
        Player third = gm.getPlayerByName("third");
        Player fourth = gm.getPlayerByName("fourth");
        Player fifth = gm.getPlayerByName("fifth");

            printSel(first);

        gm.performAction(first, first.getSelectableActions().get(2));   //he shoots
        finalCleaning(gm);

            printSel(first);

        gm.shootWeapon(first, sm.getWeaponFromName("Machine gun"));
        finalCleaning(gm);

            printSel(first);

        gm.addMode(first, first.getSelectableModes().get(0));
        finalCleaning(gm);
        disconnectPlayer(gm, fifth);
        finalCleaning(gm);
        disconnectPlayer(gm, third);
        finalCleaning(gm);

            printSel(first);

        gm.confirmModes(first);
        finalCleaning(gm);

            printSel(first);
            List<Player> temp = new ArrayList<>();
            temp.add(second);
            temp.add(third);
            temp.add(fourth);
            temp.add(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, fifth, null);
        finalCleaning(gm);

            printSel(first);
            temp.remove(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, fourth, null);
        finalCleaning(gm);

            printList(match.getWaitingFor());
            printList(gm.getActivePlayers());
            printList(gm.getInactivePlayers());
            assertEquals(IDLE, fourth.getState());
            assertEquals(1, fourth.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertFalse(fourth.hasSelectables());   //because he can't see first
            assertEquals(CHOOSE_ACTION, first.getState());
            assertTrue(first.hasSelectables());
            assertFalse(gm.getWaitingFor().contains(fifth));
            assertTrue(gm.getWaitingFor().contains(first));
            assertFalse(first.getDamageTrack().getMarkMap().containsKey(fifth));
    }

    /**
     * Tests the use of the powerup "tagback grenade" by simulating a shooting action.
     * In this case two players decide to use tagback.
     */
    @Test
    public void testTwoPlayersUseTagback(){
        //first shoots to third (which has a tagback and sees first) and fifth (which has a tagback and sees first)
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("tagbackGrenadeTestBefore"));
        Match match = gm.getMatch();
        StackManager sm = gm.getMatch().getStackManager();
        Layout layout = gm.getMatch().getLayout();

        Player first = gm.getPlayerByName("first");
        Player second = gm.getPlayerByName("second");
        Player third = gm.getPlayerByName("third");
        Player fourth = gm.getPlayerByName("fourth");
        Player fifth = gm.getPlayerByName("fifth");

            printSel(first);

        gm.performAction(first, first.getSelectableActions().get(2));   //he shoots

            printSel(first);

        gm.shootWeapon(first, sm.getWeaponFromName("Machine gun"));

            printSel(first);

        gm.addMode(first, first.getSelectableModes().get(0));

            printSel(first);

        gm.confirmModes(first);

            printSel(first);
            List<Player> temp = new ArrayList<>();
            temp.add(second);
            temp.add(third);
            temp.add(fourth);
            temp.add(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, fifth, null);

            printSel(first);
            temp.remove(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, third, null);

            printMap(first.getDamageTrack().getMarkMap());
            assertEquals(USE_POWERUP, first.getState());
            assertFalse(first.hasSelectables());
            assertEquals(USE_POWERUP, fifth.getState());
            assertEquals(1, fifth.getSelectablePowerUps().size());
            assertTrue(fifth.getSelectableCommands().contains(Command.OK));
            assertFalse(first.getDamageTrack().getMarkMap().containsKey(fifth));
            assertEquals(USE_POWERUP, third.getState());
            assertEquals(1, third.getSelectablePowerUps().size());
            assertTrue(third.getSelectableCommands().contains(Command.OK));
            assertEquals(1, first.getDamageTrack().getMarkMap().get(third));

        gm.usePowerUp(fifth, fifth.getSelectablePowerUps().get(0));

            assertEquals(0, fifth.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertEquals(1, first.getDamageTrack().getMarkMap().get(fifth));
            assertEquals(USE_POWERUP, first.getState());
            assertEquals(IDLE, fifth.getState());
            assertFalse(fifth.hasSelectables());

        gm.usePowerUp(third, third.getSelectablePowerUps().get(0));

            assertEquals(0, third.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertEquals(2, first.getDamageTrack().getMarkMap().get(third));
            assertEquals(CHOOSE_ACTION, first.getState());
            assertEquals(IDLE, third.getState());
            assertFalse(third.hasSelectables());

    }

    /**
     * Tests the use of the powerup "tagback grenade" by simulating a shooting action.
     * Fifth player could use tagback, but it disconnects before, so its decision is faked.
     *
     * Of course disconnection here is intended from a model point of view (no network involved),
     * which means the player simply becomes inactive
     */
    @Test
    public void testWithTagbackAvailableWithOneDisconnection(){
        //first shoots to third (which has a tagback and sees first) and fifth (which has a tagback and sees first)
        //but only third uses the tagback grenade
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("tagbackGrenadeTestBefore"));
        Match match = gm.getMatch();
        StackManager sm = gm.getMatch().getStackManager();
        Layout layout = gm.getMatch().getLayout();

        Player first = gm.getPlayerByName("first");
        Player second = gm.getPlayerByName("second");
        Player third = gm.getPlayerByName("third");
        Player fourth = gm.getPlayerByName("fourth");
        Player fifth = gm.getPlayerByName("fifth");

        gm.performAction(first, first.getSelectableActions().get(2));   //he shoots
        finalCleaning(gm);
        gm.shootWeapon(first, sm.getWeaponFromName("Machine gun"));
        finalCleaning(gm);
        gm.addMode(first, first.getSelectableModes().get(0));
        finalCleaning(gm);
        disconnectPlayer(gm, fifth);
        finalCleaning(gm);
        gm.confirmModes(first);
        finalCleaning(gm);

            List<Player> temp = new ArrayList<>();
            temp.add(second);
            temp.add(third);
            temp.add(fourth);
            temp.add(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, fifth, null);
        finalCleaning(gm);

            temp.remove(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, third, null);
        finalCleaning(gm);

            assertEquals(1, fifth.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertFalse(first.getDamageTrack().getMarkMap().containsKey(fifth));
            assertEquals(USE_POWERUP, first.getState());
            assertEquals(IDLE, fifth.getState());
            assertFalse(fifth.hasSelectables());

        gm.usePowerUp(third, third.getSelectablePowerUps().get(0));
        finalCleaning(gm);

            assertEquals(0, third.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertEquals(2, first.getDamageTrack().getMarkMap().get(third));
            assertEquals(CHOOSE_ACTION, first.getState());
            assertEquals(IDLE, third.getState());
            assertFalse(third.hasSelectables());
    }

    /**
     * Tests the use of the powerup "tagback grenade" by simulating a shooting action.
     * In this case both players that could use it disconnect
     */
    @Test
    public void testWithTagbackAvailableWithTwoDisconnections(){
        //first shoots to third (which has a tagback and sees first) and fifth (which has a tagback and sees first)
        //but only third uses the tagback grenade
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("tagbackGrenadeTestBefore"));
        Match match = gm.getMatch();
        StackManager sm = gm.getMatch().getStackManager();
        Layout layout = gm.getMatch().getLayout();

        Player first = gm.getPlayerByName("first");
        Player second = gm.getPlayerByName("second");
        Player third = gm.getPlayerByName("third");
        Player fourth = gm.getPlayerByName("fourth");
        Player fifth = gm.getPlayerByName("fifth");

        gm.performAction(first, first.getSelectableActions().get(2));   //he shoots
        finalCleaning(gm);
        gm.shootWeapon(first, sm.getWeaponFromName("Machine gun"));
        finalCleaning(gm);
        gm.addMode(first, first.getSelectableModes().get(0));
        finalCleaning(gm);
        disconnectPlayer(gm, fifth);
        finalCleaning(gm);
        gm.confirmModes(first);
        finalCleaning(gm);

            List<Player> temp = new ArrayList<>();
            temp.add(second);
            temp.add(third);
            temp.add(fourth);
            temp.add(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, fifth, null);
        finalCleaning(gm);
        disconnectPlayer(gm, third);
        finalCleaning(gm);

            temp.remove(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, third, null);
        finalCleaning(gm);

            assertEquals(1, fifth.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertFalse(first.getDamageTrack().getMarkMap().containsKey(fifth));
            assertEquals(1, first.getDamageTrack().getMarkMap().get(third));
            assertEquals(CHOOSE_ACTION, first.getState());
            assertEquals(IDLE, fifth.getState());
            assertEquals(IDLE, third.getState());
            assertFalse(fifth.hasSelectables());
            assertFalse(third.hasSelectables());

        printSel(first);
    }

    /**
     * Tests the use of the powerup "tagback grenade" by simulating a shooting action.
     * Only one player chooses to use it.
     */
    @Test
    public void onlyOnePlayerOutOfTwoUsesTagback(){
        //first shoots to third (which has a tagback and sees first) and fifth (which has a tagback and sees first)
        //but only third uses the tagback grenade
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("tagbackGrenadeTestBefore"));
        Match match = gm.getMatch();
        StackManager sm = gm.getMatch().getStackManager();
        Layout layout = gm.getMatch().getLayout();

        Player first = gm.getPlayerByName("first");
        Player second = gm.getPlayerByName("second");
        Player third = gm.getPlayerByName("third");
        Player fourth = gm.getPlayerByName("fourth");
        Player fifth = gm.getPlayerByName("fifth");

        gm.performAction(first, first.getSelectableActions().get(2));   //he shoots
        gm.shootWeapon(first, sm.getWeaponFromName("Machine gun"));
        gm.addMode(first, first.getSelectableModes().get(0));
        gm.confirmModes(first);

            List<Player> temp = new ArrayList<>();
            temp.add(second);
            temp.add(third);
            temp.add(fourth);
            temp.add(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, fifth, null);

            temp.remove(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, third, null);

            assertEquals(USE_POWERUP, first.getState());
            assertFalse(first.hasSelectables());
            assertEquals(USE_POWERUP, fifth.getState());
            assertEquals(1, fifth.getSelectablePowerUps().size());
            assertTrue(fifth.getSelectableCommands().contains(Command.OK));
            assertFalse(first.getDamageTrack().getMarkMap().containsKey(fifth));
            assertEquals(USE_POWERUP, third.getState());
            assertEquals(1, third.getSelectablePowerUps().size());
            assertTrue(third.getSelectableCommands().contains(Command.OK));
            assertEquals(1, first.getDamageTrack().getMarkMap().get(third));

        gm.dontUsePowerUp(fifth);

            assertEquals(1, fifth.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertFalse(first.getDamageTrack().getMarkMap().containsKey(fifth));
            assertEquals(USE_POWERUP, first.getState());
            assertEquals(IDLE, fifth.getState());
            assertFalse(fifth.hasSelectables());

        gm.usePowerUp(third, third.getSelectablePowerUps().get(0));

            assertEquals(0, third.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertEquals(2, first.getDamageTrack().getMarkMap().get(third));
            assertEquals(CHOOSE_ACTION, first.getState());
            assertEquals(IDLE, third.getState());
            assertFalse(third.hasSelectables());
    }

    /**
     * Tests the use of the powerup "tagback grenade" by simulating a shooting action.
     * The current player disconnects while he is receiving tagback grenade.
     */
    @Test
    public void currentPlayerDisconnectsWhileReceivingPowerUp(){
        //first shoots to third (which has a tagback and sees first) and fifth (which has a tagback and sees first)
        //but only third uses the tagback grenade
        //while he is receiving tagback grenade, the current player disconnects
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("tagbackGrenadeTestBefore"));
        Match match = gm.getMatch();
        StackManager sm = gm.getMatch().getStackManager();
        Layout layout = gm.getMatch().getLayout();

        Player first = gm.getPlayerByName("first");
        Player second = gm.getPlayerByName("second");
        Player third = gm.getPlayerByName("third");
        Player fourth = gm.getPlayerByName("fourth");
        Player fifth = gm.getPlayerByName("fifth");

        gm.performAction(first, first.getSelectableActions().get(2));   //he shoots
        finalCleaning(gm);
        gm.shootWeapon(first, sm.getWeaponFromName("Machine gun"));
        finalCleaning(gm);
        gm.addMode(first, first.getSelectableModes().get(0));
        finalCleaning(gm);
        gm.confirmModes(first);
        finalCleaning(gm);
        gm.shootTarget(first, fifth, null);
        finalCleaning(gm);
        gm.shootTarget(first, third, null);
        finalCleaning(gm);

            assertEquals(USE_POWERUP, first.getState());
            assertFalse(first.hasSelectables());
            assertEquals(USE_POWERUP, fifth.getState());
            assertEquals(1, fifth.getSelectablePowerUps().size());
            assertTrue(fifth.getSelectableCommands().contains(Command.OK));
            assertFalse(first.getDamageTrack().getMarkMap().containsKey(fifth));
            assertEquals(USE_POWERUP, third.getState());
            assertEquals(1, third.getSelectablePowerUps().size());
            assertTrue(third.getSelectableCommands().contains(Command.OK));
            assertEquals(1, first.getDamageTrack().getMarkMap().get(third));

        disconnectPlayer(gm, first);
        finalCleaning(gm);
        gm.dontUsePowerUp(fifth);
        finalCleaning(gm);

            assertEquals(1, fifth.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertFalse(first.getDamageTrack().getMarkMap().containsKey(fifth));
            assertEquals(USE_POWERUP, first.getState());
            assertEquals(IDLE, fifth.getState());
            assertFalse(fifth.hasSelectables());

        gm.usePowerUp(third, third.getSelectablePowerUps().get(0));
        finalCleaning(gm);

            assertEquals(0, third.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertEquals(2, first.getDamageTrack().getMarkMap().get(third));
            assertEquals(IDLE, first.getState());
            assertFalse(first.hasSelectables());
            assertEquals(IDLE, third.getState());
            assertFalse(third.hasSelectables());
            assertEquals(CHOOSE_ACTION, second.getState());
            assertTrue(second.hasSelectables());
            assertEquals(second, match.getCurrentPlayer());
            printSel(second);
    }

    /**
     * Tests the use of the powerups "tagback grenade" ban "targeting scope" by simulating a shooting action.
     */
    @Test
    public void tagbackAndTargeting(){
        //first shoots to third (which has a tagback and sees first) and fifth (which has a tagback and sees first)
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("tagbackAndTargetingBefore"));
        Match match = gm.getMatch();
        StackManager sm = gm.getMatch().getStackManager();
        Layout layout = gm.getMatch().getLayout();

        Player first = gm.getPlayerByName("first");
        Player second = gm.getPlayerByName("second");
        Player third = gm.getPlayerByName("third");
        Player fourth = gm.getPlayerByName("fourth");
        Player fifth = gm.getPlayerByName("fifth");

        gm.performAction(first, first.getSelectableActions().get(2));   //he shoots
        gm.shootWeapon(first, sm.getWeaponFromName("Machine gun"));
        gm.addMode(first, first.getSelectableModes().get(0));
        gm.confirmModes(first);

            printSel(first);
            List<Player> temp = new ArrayList<>();
            temp.add(second);
            temp.add(third);
            temp.add(fourth);
            temp.add(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, fifth, null);

            printSel(first);
            temp.remove(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));
            int damagesThird = third.getDamageTrack().getDamageList().size();

        gm.shootTarget(first, third, null);

            assertEquals(damagesThird += 2, third.getDamageTrack().getDamageList().size());  //he had a mark from first player
            assertEquals(USE_POWERUP, first.getState());
            assertTrue(first.getSelectablePowerUps().contains(sm.getPowerUpFromString("13-Targeting scope-RED")));
            assertEquals(1, first.getSelectablePowerUps().size());
            assertTrue(match.getWaitingFor().contains(first));
            assertEquals(1, match.getWaitingFor().size());

        gm.usePowerUp(first, first.getSelectablePowerUps().get(0));

            assertEquals(PAYING_ANY, first.getState());
            assertTrue(match.getWaitingFor().contains(first));
            assertFalse(match.getWaitingFor().contains(third));
            assertFalse(match.getWaitingFor().contains(fifth));
            //first had wallet: (3,3,3) and 3 powerups
            assertTrue(first.getSelectableColors().contains(AmmoColor.BLUE));
            assertTrue(first.getSelectableColors().contains(AmmoColor.RED));
            assertTrue(first.getSelectableColors().contains(AmmoColor.YELLOW));
            assertTrue(!first.getSelectablePowerUps().contains(sm.getPowerUpFromString("13-Targeting scope-RED")));
            assertTrue(first.getSelectablePowerUps().size()>0);

        gm.payAny(first, sm.getPowerUpFromString("18-Newton-YELLOW"));

            assertTrue(match.getWaitingFor().contains(first));
            assertFalse(match.getWaitingFor().contains(third));
            assertFalse(match.getWaitingFor().contains(fifth));

        gm.choosePowerUpTarget(first, third, null);

            assertEquals(damagesThird += 1, third.getDamageTrack().getDamageList().size());
            assertFalse(match.getWaitingFor().contains(first));
            assertTrue(match.getWaitingFor().contains(third));
            assertTrue(match.getWaitingFor().contains(fifth));
            assertEquals(USE_POWERUP, first.getState());
            assertFalse(first.hasSelectables());
            assertEquals(USE_POWERUP, fifth.getState());
            assertEquals(1, fifth.getSelectablePowerUps().size());
            assertTrue(fifth.getSelectableCommands().contains(Command.OK));
            assertFalse(first.getDamageTrack().getMarkMap().containsKey(fifth));
            assertEquals(USE_POWERUP, third.getState());
            assertEquals(1, third.getSelectablePowerUps().size());
            assertTrue(third.getSelectableCommands().contains(Command.OK));
            assertEquals(1, first.getDamageTrack().getMarkMap().get(third));

        gm.usePowerUp(fifth, fifth.getSelectablePowerUps().get(0));

            assertEquals(0, fifth.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertEquals(1, first.getDamageTrack().getMarkMap().get(fifth));
            assertEquals(USE_POWERUP, first.getState());
            assertEquals(IDLE, fifth.getState());
            assertFalse(fifth.hasSelectables());
            assertFalse(match.getWaitingFor().contains(first));
            assertTrue(match.getWaitingFor().contains(third));

        gm.usePowerUp(third, third.getSelectablePowerUps().get(0));

            assertTrue(match.getWaitingFor().contains(first));
            assertEquals(0, third.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertEquals(2, first.getDamageTrack().getMarkMap().get(third));
            assertEquals(CHOOSE_ACTION, first.getState());
            assertEquals(IDLE, third.getState());
            assertFalse(third.hasSelectables());

            Backup b1 = Backup.initFromFile(searchInTestResources( "tagbackAndTargetingAfter"));
            Backup b2 = new Backup(gm.getMatch());

            assertTrue(b1.equals(b2));
    }

    /**
     * Tests the use of the powerups "tagback grenade" and "targeting scope" by simulating a shooting action.
     * Both damaged players use the tagback, while damager does not use the targeting scope
     */
    @Test
    public void tagbackButRefusingTargeting(){
        //first shoots to third (which has a tagback and sees first) and fifth (which has a tagback and sees first)
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("tagbackAndTargetingBefore"));
        Match match = gm.getMatch();
        StackManager sm = gm.getMatch().getStackManager();
        Layout layout = gm.getMatch().getLayout();

        Player first = gm.getPlayerByName("first");
        Player second = gm.getPlayerByName("second");
        Player third = gm.getPlayerByName("third");
        Player fourth = gm.getPlayerByName("fourth");
        Player fifth = gm.getPlayerByName("fifth");

        gm.performAction(first, first.getSelectableActions().get(2));   //he shoots
        gm.shootWeapon(first, sm.getWeaponFromName("Machine gun"));
        gm.addMode(first, first.getSelectableModes().get(0));
        gm.confirmModes(first);

            printSel(first);
            List<Player> temp = new ArrayList<>();
            temp.add(second);
            temp.add(third);
            temp.add(fourth);
            temp.add(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));

        gm.shootTarget(first, fifth, null);

            printSel(first);
            temp.remove(fifth);
            assertTrue(first.getSelectablePlayers().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectablePlayers()));
            int damagesThird = third.getDamageTrack().getDamageList().size();

        gm.shootTarget(first, third, null);

            assertEquals(damagesThird += 2, third.getDamageTrack().getDamageList().size());  //he had a mark from first player
            assertEquals(USE_POWERUP, first.getState());
            assertTrue(first.getSelectablePowerUps().contains(sm.getPowerUpFromString("13-Targeting scope-RED")));
            assertEquals(1, first.getSelectablePowerUps().size());
            assertTrue(match.getWaitingFor().contains(first));
            assertEquals(1, match.getWaitingFor().size());

        gm.dontUsePowerUp(first);

            assertEquals(damagesThird, third.getDamageTrack().getDamageList().size());
            assertFalse(match.getWaitingFor().contains(first));
            assertTrue(match.getWaitingFor().contains(third));
            assertTrue(match.getWaitingFor().contains(fifth));
            assertEquals(USE_POWERUP, first.getState());
            assertFalse(first.hasSelectables());
            assertEquals(USE_POWERUP, fifth.getState());
            assertEquals(1, fifth.getSelectablePowerUps().size());
            assertTrue(fifth.getSelectableCommands().contains(Command.OK));
            assertFalse(first.getDamageTrack().getMarkMap().containsKey(fifth));
            assertEquals(USE_POWERUP, third.getState());
            assertEquals(1, third.getSelectablePowerUps().size());
            assertTrue(third.getSelectableCommands().contains(Command.OK));
            assertEquals(1, first.getDamageTrack().getMarkMap().get(third));

        gm.usePowerUp(fifth, fifth.getSelectablePowerUps().get(0));

            assertEquals(0, fifth.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertEquals(1, first.getDamageTrack().getMarkMap().get(fifth));
            assertEquals(USE_POWERUP, first.getState());
            assertEquals(IDLE, fifth.getState());
            assertFalse(fifth.hasSelectables());
            assertFalse(match.getWaitingFor().contains(first));
            assertTrue(match.getWaitingFor().contains(third));

        gm.usePowerUp(third, third.getSelectablePowerUps().get(0));

            assertTrue(match.getWaitingFor().contains(first));
            assertEquals(0, third.howManyPowerUps(PowerUpType.TAGBACK_GRENADE));
            assertEquals(2, first.getDamageTrack().getMarkMap().get(third));
            assertEquals(CHOOSE_ACTION, first.getState());
            assertEquals(IDLE, third.getState());
            assertFalse(third.hasSelectables());
    }
}