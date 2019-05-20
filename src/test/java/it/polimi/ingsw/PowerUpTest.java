package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.PlayerState.*;
import static it.polimi.ingsw.testUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class PowerUpTest {

    @Test
    public void tagbackGrenadeTest(){
        //first shoots to fourth (which has a tagback, but can't see first) and fifth (which has a tagback and sees first)
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS, "tagbackGrenadeTestBefore");
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

    @Test
    public void testTwoPlayersUseTagback(){
        //first shoots to third (which has a tagback and sees first) and fifth (which has a tagback and sees first)
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS, "tagbackGrenadeTestBefore");
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

    @Test
    public void onlyOnePlayerOutOfTwoUsesTagback(){
        //first shoots to third (which has a tagback and sees first) and fifth (which has a tagback and sees first)
        //but only third uses the tagback grenade
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS, "tagbackGrenadeTestBefore");
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

    @Test
    public void tagbackAndTargeting(){
        //first shoots to third (which has a tagback and sees first) and fifth (which has a tagback and sees first)
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS, "tagbackAndTargetingBefore");
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
            assertTrue(first.getSelectableColors().contains(Color.BLUE));
            assertTrue(first.getSelectableColors().contains(Color.RED));
            assertTrue(first.getSelectableColors().contains(Color.YELLOW));
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

            Backup b1 = Backup.initFromFile(SAVED_GAMES_FOR_TESTS, "tagbackAndTargetingAfter");
            Backup b2 = new Backup(gm.getMatch());

            assertTrue(b1.equals(b2));
    }

    @Test
    public void tagbackButRefusingTargeting(){
        //first shoots to third (which has a tagback and sees first) and fifth (which has a tagback and sees first)
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(SAVED_GAMES_FOR_TESTS, "tagbackAndTargetingBefore");
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