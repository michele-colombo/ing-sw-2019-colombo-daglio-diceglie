package it.polimi.ingsw;

import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerColor;
import it.polimi.ingsw.server.model.enums.PlayerState;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.enums.PlayerState.*;
import static it.polimi.ingsw.testUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {

    public boolean isNewMatch(GameModel gm){
        //no players is born
        for (Player p : gm.allPlayers()){
            if(p.isBorn()) return false;
        }

        //there's just one player spawing, with 2 selectable powerups
        List<Player> tempPlayers = new ArrayList<>();
        tempPlayers.addAll(gm.getMatch().getPlayers());
        tempPlayers.removeIf(p -> p.getState()!=PlayerState.SPAWN);
        if (1 != tempPlayers.size()) return false;
        Player spawningPlayer = tempPlayers.get(0);
        if (PlayerState.SPAWN != spawningPlayer.getState()) return false;
        if (2 != spawningPlayer.getPowerUps().size()) return false;
        if (2 != spawningPlayer.getSelectablePowerUps().size()) return false;
        if(!spawningPlayer.getSelectableCommands().isEmpty()) return false;

        //other players are in IDLE and have nothing selectable
        tempPlayers.clear();
        tempPlayers.addAll(gm.getMatch().getPlayers());
        tempPlayers.remove(spawningPlayer);
        for (Player p : tempPlayers) {
            if(PlayerState.IDLE != p.getState()) return false;
            if(0 != p.howManySelectables()) return false;
        }
        return true;
    }

    @Test
    public void nextActivePlayerWithFivePlayers() {


        try {
            GameModel gm = new GameModel();
            Player p1, p2, p3, p4, p5;
            p1 = gm.addPlayer("primo giocatore");
            p2 = gm.addPlayer("secondo giocatore");
            p3 = gm.addPlayer("terzo giocatore");
            p4 = gm.addPlayer("quarto giocatore");
            p5 = gm.addPlayer("quinto giocatore");

            gm.startNewMatch();
            Player currP = null;

            currP = gm.nextActivePlayer(null);
            assertEquals(p1, currP);
            currP = gm.nextActivePlayer(null);
            assertEquals(p1, currP);

            currP = gm.nextActivePlayer(p1);
            assertEquals(p2, currP);

            currP = gm.nextActivePlayer(p2);
            assertEquals(p3, currP);

            currP = gm.nextActivePlayer(p3);
            assertEquals(p4, currP);

            currP = gm.nextActivePlayer(p4);
            assertEquals(p5, currP);

            currP = gm.nextActivePlayer(p5);
            assertEquals(p1, currP);

            currP = gm.nextActivePlayer(p3);
            assertEquals(p4, currP);

            currP = gm.nextActivePlayer(null);
            assertEquals(p1, currP);
        } catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void nextActivePlayerWithFourPlayers() {

        try {
            GameModel gm = new GameModel();
            Player p1, p2, p3, p4;
            p1 = gm.addPlayer("primo giocatore");
            p2 = gm.addPlayer("secondo giocatore");
            p3 = gm.addPlayer("terzo giocatore");
            p4 = gm.addPlayer("quarto giocatore");

            gm.startNewMatch();
            Player currP = null;

            currP = gm.nextActivePlayer(null);
            assertEquals(p1, currP);
            currP = gm.nextActivePlayer(null);
            assertEquals(p1, currP);

            currP = gm.nextActivePlayer(p1);
            assertEquals(p2, currP);

            currP = gm.nextActivePlayer(p2);
            assertEquals(p3, currP);

            currP = gm.nextActivePlayer(p3);
            assertEquals(p4, currP);

            currP = gm.nextActivePlayer(p4);
            assertEquals(p1, currP);

            currP = gm.nextActivePlayer(p4);
            assertEquals(p1, currP);

            currP = gm.nextActivePlayer(p3);
            assertEquals(p4, currP);

            currP = gm.nextActivePlayer(null);
            assertEquals(p1, currP);
        } catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void nextActivePlayerWithActivePlayersInADifferentOrder(){
        try {
            GameModel gm = new GameModel();
            Player p1, p2, p3, p4;
            p1 = gm.addPlayer("primo giocatore");
            p2 = gm.addPlayer("secondo giocatore");
            p3 = gm.addPlayer("terzo giocatore");
            p4 = gm.addPlayer("quarto giocatore");

            gm.startNewMatch();

            List<Player> activePlayers = gm.getActivePlayers();
            activePlayers.add(activePlayers.remove(1));
            activePlayers.add(activePlayers.remove(0));
            //now activePlayers is {2,3,1,0}

            Player currP = null;

            currP = gm.nextActivePlayer(null);
            assertEquals(p1, currP);
            currP = gm.nextActivePlayer(null);
            assertEquals(p1, currP);

            currP = gm.nextActivePlayer(p1);
            assertEquals(p2, currP);

            currP = gm.nextActivePlayer(p2);
            assertEquals(p3, currP);

            currP = gm.nextActivePlayer(p3);
            assertEquals(p4, currP);

            currP = gm.nextActivePlayer(p4);
            assertEquals(p1, currP);

            currP = gm.nextActivePlayer(p4);
            assertEquals(p1, currP);

            currP = gm.nextActivePlayer(p3);
            assertEquals(p4, currP);

            currP = gm.nextActivePlayer(null);
            assertEquals(p1, currP);
        } catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void nextActivePlayerWithActivePlayersVarying() {
        try {
            GameModel gm = new GameModel();
            Player p1, p2, p3, p4, p5;
            p1 = gm.addPlayer("primo giocatore");
            p2 = gm.addPlayer("secondo giocatore");
            p3 = gm.addPlayer("terzo giocatore");
            p4 = gm.addPlayer("quarto giocatore");
            p5 = gm.addPlayer("quinto giocatore");

            gm.startNewMatch();
            Player currP = null;

            List<Player> activePlayers = gm.getActivePlayers();
            activePlayers.add(activePlayers.remove(1));
            activePlayers.add(activePlayers.remove(0));
            //now activePlayers is {2,3,1,0}

            currP = gm.nextActivePlayer(null);
            assertEquals(p1, currP);
            currP = gm.nextActivePlayer(null);
            assertEquals(p1, currP);

            currP = gm.nextActivePlayer(p1);
            assertEquals(p2, currP);

            currP = gm.nextActivePlayer(p2);
            assertEquals(p3, currP);

            currP = gm.nextActivePlayer(p3);
            assertEquals(p4, currP);

            currP = gm.nextActivePlayer(p4);
            assertEquals(p5, currP);

            //p1 has disconnected
            gm.getActivePlayers().remove(p1);
            currP = gm.nextActivePlayer(p5);
            assertEquals(p2, currP);

            currP = gm.nextActivePlayer(p2);
            assertEquals(p3, currP);

            //p4 has disconnected
            gm.getActivePlayers().remove(p4);
            currP = gm.nextActivePlayer(p3);
            assertEquals(p5, currP);

            //p1 has riconnected
            gm.getActivePlayers().add(p1);
            currP = gm.nextActivePlayer(p5);
            assertEquals(p1, currP);

            //p4 has riconnected
            //p2 and p3 have disconnected
            gm.getActivePlayers().add(p4);
            gm.getActivePlayers().remove(p2);
            gm.getActivePlayers().remove(p3);
            currP = gm.nextActivePlayer(p1);
            assertEquals(p4, currP);

            currP = gm.nextActivePlayer(p4);
            assertEquals(p5, currP);

            currP = gm.nextActivePlayer(p5);
            assertEquals(p1, currP);

            //p3 has riconnected
            gm.getActivePlayers().add(p3);
            currP = gm.nextActivePlayer(p1);
            assertEquals(p3, currP);

            printList(gm.getActivePlayers());

        } catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void grabFromAmmoSquares(){
        try {
            GameModel gm = new GameModel();
            Player p1, p2, p3;
            p1 = gm.addPlayer("primo giocatore");
            p2 = gm.addPlayer("secondo giocatore");
            p3 = gm.addPlayer("terzo giocatore");

            gm.startNewMatch();
            Player currP = null;
            Match match = gm.getMatch();

            Player tempPlayer = match.getPlayers().get(0);
            assertEquals(PlayerState.SPAWN, tempPlayer.getState());
            assertEquals(null, match.getCurrentPlayer());
            System.out.println(tempPlayer.selectablesToString());
            assertEquals(2, tempPlayer.getSelectablePowerUps().size());    //can select two powerups
            PowerUp selectedPowerUp = tempPlayer.getSelectablePowerUps().get(0);
            PowerUp notSelectedPowerUp = tempPlayer.getSelectablePowerUps().get(1);

            gm.spawn(tempPlayer, selectedPowerUp); //he select the first (in order to discard and respawn onto)

            assertEquals(match.getCurrentPlayer(), tempPlayer); //he becomes the current player
            assertEquals(selectedPowerUp.getColor(), match.getCurrentPlayer().getSquarePosition().getColor());  //he is now on the spawnSquare of the discarded powerUp
            assertFalse(match.getCurrentPlayer().getPowerUps().contains(selectedPowerUp));  //the selected powerUp is discarded
            assertTrue(match.getCurrentPlayer().getPowerUps().contains(notSelectedPowerUp));    //the notSelected powerUp belongs to the player now
            assertEquals(PlayerState.CHOOSE_ACTION, match.getCurrentPlayer().getState());       //he can now choose the action to take
            System.out.println(match.getCurrentPlayer().getSquarePosition().getFullDescription());
            System.out.println(match.getCurrentPlayer().getPowerUps());
            printSel(tempPlayer);

            gm.performAction(tempPlayer, tempPlayer.getSelectableActions().get(1));     //he wants to grab

            assertEquals(GRAB_THERE, match.getCurrentPlayer().getState());  //he is in GRAB_THERE state
            System.out.println(match.getCurrentPlayer().selectablesToString());
            Square firstSquareSelected = tempPlayer.getSelectableSquares().get(0);  //he wants to grab in the first square (ammoSquare)
            Cash tempCash = new Cash(((AmmoSquare) firstSquareSelected).getAmmo().getAmmos());
            assertTrue(new Cash(0, 0, 0).isEqual(tempPlayer.getWallet()));

            gm.grabThere(tempPlayer, firstSquareSelected);         //he grabs in the first square (ammoSquare)

            assertEquals(PlayerState.CHOOSE_ACTION, tempPlayer.getState());
            assertTrue(tempCash.isEqual(tempPlayer.getWallet()));
            assertEquals(firstSquareSelected, tempPlayer.getSquarePosition());
            System.out.println(tempPlayer.getPowerUps());
            System.out.println(tempPlayer.getWallet());
            Cash previousCash = new Cash(tempPlayer.getWallet());
            printSel(tempPlayer);

            gm.performAction(tempPlayer, tempPlayer.getSelectableActions().get(1));     //he wants to grab (second action)

            assertEquals(GRAB_THERE, tempPlayer.getState());
            printSel(tempPlayer);
            assertFalse(tempPlayer.getSelectableSquares().contains(firstSquareSelected));
            assertEquals(2, match.getActionsCompleted());

            gm.grabThere(tempPlayer, tempPlayer.getSelectableSquares().get(0));

            System.out.println(tempPlayer.getPowerUps());
            System.out.println(tempPlayer.getWallet());
            printSel(tempPlayer);
        } catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void exceptionTest(){
        GameModel gm = new GameModel();
        try{
            Player p1, p2, p3, p4, p5;
            p1 = gm.addPlayer("first");
            p2 = gm.addPlayer("second");
            p3 = gm.addPlayer("third");
            p4 = gm.addPlayer("fourth");
        } catch(Exception e){
            assertTrue(false);
        }

        assertThrows(NameAlreadyTakenException.class, () -> gm.addPlayer("first"));

        try{
            Player p5 = gm.addPlayer("fifth");

        } catch(Exception e){
            assertTrue(false);
        }

        assertThrows(GameFullException.class, () -> gm.addPlayer("last"));
    }

    @Test
    public void StartNewMatchTest(){
        GameModel gm = new GameModel();

        Player p1, p2, p3, p4, p5;
        try {
            gm.addPlayer("andrea");
            gm.addPlayer("alessandro");
            gm.addPlayer("marco ");
            gm.addPlayer("giacomo");
            gm.addPlayer("luca");
        } catch (Exception e){
            assertTrue(false);
        }

        assertEquals(5, gm.getNumberOfPlayers());

        gm.startNewMatch();

        assertNotNull(gm.getMatch());
        assertTrue(gm.getMatch().getPlayers().containsAll(gm.allPlayers()));
        assertTrue(gm.allPlayers().containsAll(gm.getMatch().getPlayers()));

        //each player has a color and each player's color is unique
        List<PlayerColor> colorsGiven = new ArrayList<>();
        for (Player p : gm.getMatch().getPlayers()){
            assertNotNull(p.getColor());
            assertFalse(colorsGiven.contains(p.getColor()));
            colorsGiven.add(p.getColor());
        }

        assertNotNull(gm.getMatch().getLayout());
        assertNotNull(gm.getMatch().getStackManager());
        assertNotNull(gm.getMatch().getKillShotTrack());

        assertTrue(isNewMatch(gm));
    }


    @Test
    public void GrabAndMoveTestFivePlayers(){
        GameModel gm = new GameModel();
        try {
            Player p1, p2, p3, p4, p5;
            List<Player> newPlayers = new ArrayList<>();
            newPlayers.add(p1 = gm.addPlayer("antonio"));
            newPlayers.add(p2 = gm.addPlayer("gianfranco"));
            newPlayers.add(p3 = gm.addPlayer("enrico"));
            newPlayers.add(p4 = gm.addPlayer("matteo"));
            newPlayers.add(p5 = gm.addPlayer("evila"));

            assertEquals(newPlayers.size(), gm.getNumberOfPlayers());

            gm.startNewMatch();

            gm.spawn(p1, p1.getSelectablePowerUps().get(0));
            gm.performAction(p1, p1.getSelectableActions().get(1)); //grab
            Square s1 = p1.getSelectableSquares().get(1);
            Cash cash1 = new Cash(((AmmoSquare) s1).getAmmo().getAmmos());
            gm.grabThere(p1, s1);
            gm.performAction(p1, p1.getSelectableActions().get(0)); //move
            s1 = p1.getSelectableSquares().get(1);
            gm.moveMeThere(p1, s1);
            if (p1.getState() == PlayerState.CHOOSE_ACTION) gm.endTurn();
            assertTrue(p1.isBorn());
            assertTrue(cash1.isEqual(p1.getWallet()));
            assertEquals(s1, p1.getSquarePosition());

            for (Player p : gm.getSpawningPlayers()) {
                System.out.println(p + " " + p.getName());
            }
            assertEquals(1, gm.getSpawningPlayers().size());
            gm.spawn(p2, p2.getSelectablePowerUps().get(0));

            assertEquals(0, gm.getSpawningPlayers().size());
            assertTrue(p2.isBorn());
            System.out.println(p2.getState());
            printSel(p2);
            assertEquals(PlayerState.CHOOSE_ACTION, p2.getState());

            assertFalse(p3.isBorn());
            assertFalse(p4.isBorn());
            assertFalse(p5.isBorn());

            //checks if game can go on regularly
            gm.performAction(p2, p2.getSelectableActions().get(0));   //move
            assertEquals(PlayerState.MOVE_THERE, p2.getState());
            gm.moveMeThere(p2, p2.getSelectableSquares().get(1));
            assertEquals(PlayerState.CHOOSE_ACTION, p2.getState());
            gm.performAction(p2, p2.getSelectableActions().get(1));  //grab
            assertEquals(GRAB_THERE, p2.getState());
            Square s2 = p2.getSelectableSquares().get(1);
            Cash cash2 = new Cash(((AmmoSquare) s2).getAmmo().getAmmos());
            gm.grabThere(p2, s2);
            if (p2.getState() == PlayerState.CHOOSE_ACTION) gm.endTurn();


            assertEquals(PlayerState.SPAWN, p3.getState());
            gm.spawn(p3, p3.getSelectablePowerUps().get(1));
            //Backup snapshot = new Backup(gm.getMatch());
            //snapshot.saveOnFile("snapshot1");
            System.out.println(p3.getState());
            printSel(p3);
            assertEquals(PlayerState.CHOOSE_ACTION, p3.getState());
            gm.performAction(p3, p3.getSelectableActions().get(0));   //move
            assertEquals(PlayerState.MOVE_THERE, p3.getState());
            gm.moveMeThere(p3, p3.getSelectableSquares().get(3));
            assertEquals(PlayerState.CHOOSE_ACTION, p3.getState());
            gm.performAction(p3, p3.getSelectableActions().get(1));  //grab
            Square s3 = p3.getSelectableSquares().get(1);
            Cash cash3 = new Cash(((AmmoSquare) s3).getAmmo().getAmmos());
            gm.grabThere(p3, s3);
            if (p3.getState() == PlayerState.CHOOSE_ACTION) gm.endTurn();

            assertEquals(PlayerState.SPAWN, p4.getState());
            //assertEquals(2, p4.getSelectablePowerUps().size());
            //assertEquals(2, p4.getPowerUps().size());
            gm.spawn(p4, p4.getSelectablePowerUps().get(1));
            gm.performAction(p4, p4.getSelectableActions().get(1));   //grab
            Square s4 = p4.getSelectableSquares().get(1);
            Cash cash4 = new Cash(((AmmoSquare) s4).getAmmo().getAmmos());
            gm.grabThere(p4, s4);
            gm.performAction(p4, p4.getSelectableActions().get(1));  //grab
            s4 = p4.getSelectableSquares().get(0);
            cash4.deposit(((AmmoSquare) s4).getAmmo().getAmmos());
            gm.grabThere(p4, s4);
            assertEquals(s4, p4.getSquarePosition());
            if (p4.getState() == PlayerState.CHOOSE_ACTION) gm.endTurn();
        } catch (Exception e){
            assertTrue(false);
        }

    }

    @Test
    public void StartMatchTwoTimesWithValidBackup(){
        GameModel gm = new GameModel();

        try {
            Player p1, p2, p3, p4, p5;
            List<Player> newPlayers = new ArrayList<>();
            newPlayers.add(p1 = gm.addPlayer("antonio"));
            newPlayers.add(p2 = gm.addPlayer("gianfranco"));
            newPlayers.add(p3 = gm.addPlayer("enrico"));
            newPlayers.add(p4 = gm.addPlayer("matteo"));
            newPlayers.add(p5 = gm.addPlayer("evila"));

            assertEquals(newPlayers.size(), gm.getNumberOfPlayers());

            gm.startNewMatch();

            gm.spawn(p1, p1.getSelectablePowerUps().get(0));
            gm.performAction(p1, p1.getSelectableActions().get(1)); //grab
            Square s1 = p1.getSelectableSquares().get(1);
            Cash cash1 = new Cash(((AmmoSquare) s1).getAmmo().getAmmos());
            gm.grabThere(p1, s1);
            gm.performAction(p1, p1.getSelectableActions().get(0)); //move
            s1 = p1.getSelectableSquares().get(1);
            gm.moveMeThere(p1, s1);
            if (p1.getState() == PlayerState.CHOOSE_ACTION) gm.endTurn();

            gm.spawn(p2, p2.getSelectablePowerUps().get(0));

            GameModel gm2 = new GameModel();
            Player p21, p22, p23, p24, p25;
            List<Player> newPlayers2 = new ArrayList<>();
            newPlayers2.add(p21 = gm2.addPlayer("antonio"));
            newPlayers2.add(p22 = gm2.addPlayer("gianfranco"));
            newPlayers2.add(p23 = gm2.addPlayer("enrico"));
            newPlayers2.add(p24 = gm2.addPlayer("matteo"));
            newPlayers2.add(p25 = gm2.addPlayer("evila"));

            //checks player that object are different (rather obvious)
            for (Player p : newPlayers2) {
                assertFalse(newPlayers.contains(p));
            }
            for (Player p : newPlayers) {
                assertFalse(newPlayers2.contains(p));
            }

            System.out.println("new game: 2");
            gm2.startMatch();

            //match is created correctly
            StackManager sm = gm2.getMatch().getStackManager();
            assertEquals(36, sm.getOriginalAmmoTiles().size());
            assertEquals(24, sm.getOriginalPowerUps().size());
            assertEquals(21, sm.getOriginalWeaponArsenal().size());
            assertFalse(sm.getAmmoTilesActiveStack().contains(null));
            assertFalse(sm.getAmmoTilesWasteStack().contains(null));
            assertFalse(sm.getPowerUpActiveStack().contains(null));
            assertFalse(sm.getPowerUpWasteStack().contains(null));
            assertFalse(sm.getWeaponActiveStack().contains(null));

            //new match's players are identical (same objects) to gameModel's
            assertTrue(gm2.getMatch().getPlayers().containsAll(gm2.allPlayers()));
            assertTrue(gm2.allPlayers().containsAll(gm2.getMatch().getPlayers()));

            //since players' name are equal to the ones of the previous match, previous match is resumed
            assertTrue(p21.isBorn());
            assertTrue(cash1.isEqual(p21.getWallet()));
            assertEquals(s1, p21.getSquarePosition());

            assertTrue(p22.isBorn());
            assertEquals(PlayerState.CHOOSE_ACTION, p22.getState());

            assertFalse(p23.isBorn());
            assertFalse(p24.isBorn());
            assertFalse(p25.isBorn());

            //checks if game can go on regularly
            gm2.performAction(p22, p22.getSelectableActions().get(0));   //move
            assertEquals(PlayerState.MOVE_THERE, p22.getState());
            gm2.moveMeThere(p22, p22.getSelectableSquares().get(1));
            assertEquals(PlayerState.CHOOSE_ACTION, p22.getState());
            gm2.performAction(p22, p22.getSelectableActions().get(1));  //grab
            assertEquals(GRAB_THERE, p22.getState());
            Square s2 = p22.getSelectableSquares().get(1);
            Cash cash2 = new Cash(((AmmoSquare) s2).getAmmo().getAmmos());
            gm2.grabThere(p22, s2);
            if (p22.getState() == PlayerState.CHOOSE_ACTION) gm2.endTurn();


            assertEquals(PlayerState.SPAWN, p23.getState());
            gm2.spawn(p23, p23.getSelectablePowerUps().get(1));
            //Backup snapshot = new Backup(gm2.getMatch());
            //snapshot.saveOnFile("snapshot1");
            assertEquals(PlayerState.CHOOSE_ACTION, p23.getState());
            gm2.performAction(p23, p23.getSelectableActions().get(0));   //move
            assertEquals(PlayerState.MOVE_THERE, p23.getState());
            gm2.moveMeThere(p23, p23.getSelectableSquares().get(3));
            assertEquals(PlayerState.CHOOSE_ACTION, p23.getState());
            gm2.performAction(p23, p23.getSelectableActions().get(1));  //grab
            Square s3 = p23.getSelectableSquares().get(1);
            Cash cash3 = new Cash(((AmmoSquare) s3).getAmmo().getAmmos());
            gm2.grabThere(p23, s3);
            if (p23.getState() == PlayerState.CHOOSE_ACTION) gm2.endTurn();

            assertEquals(PlayerState.SPAWN, p24.getState());
            assertEquals(2, p24.getSelectablePowerUps().size());
            assertEquals(2, p24.getPowerUps().size());
            gm2.spawn(p24, p24.getSelectablePowerUps().get(1));
            gm2.performAction(p24, p24.getSelectableActions().get(1));   //grab
            Square s4 = p24.getSelectableSquares().get(1);
            Cash cash4 = new Cash(((AmmoSquare) s4).getAmmo().getAmmos());
            gm2.grabThere(p24, s4);
            gm2.performAction(p24, p24.getSelectableActions().get(1));  //grab
            s4 = p24.getSelectableSquares().get(0);
            cash4.deposit(((AmmoSquare) s4).getAmmo().getAmmos());
            gm2.grabThere(p24, s4);
            assertEquals(s4, p24.getSquarePosition());
            if (p24.getState() == PlayerState.CHOOSE_ACTION) gm2.endTurn();

            //checks if the current match can be reloaded again (if player are the same)
            GameModel gm3 = new GameModel();
            Player p31, p32, p33, p34, p35;
            List<Player> newPlayers3 = new ArrayList<>();
            newPlayers3.add(p31 = gm3.addPlayer("antonio"));
            newPlayers3.add(p32 = gm3.addPlayer("gianfranco"));
            newPlayers3.add(p33 = gm3.addPlayer("enrico"));
            newPlayers3.add(p34 = gm3.addPlayer("matteo"));
            newPlayers3.add(p35 = gm3.addPlayer("evila"));

            assertFalse(sm.getAmmoTilesActiveStack().contains(null));
            assertFalse(sm.getAmmoTilesWasteStack().contains(null));
            assertFalse(sm.getPowerUpActiveStack().contains(null));
            assertFalse(sm.getPowerUpWasteStack().contains(null));
            assertFalse(sm.getWeaponActiveStack().contains(null));

            System.out.println("New game: 3");
            gm3.startMatch();

            //match is created correctly
            sm = gm2.getMatch().getStackManager();
            assertEquals(36, sm.getOriginalAmmoTiles().size());
            assertEquals(24, sm.getOriginalPowerUps().size());
            assertEquals(21, sm.getOriginalWeaponArsenal().size());
            assertFalse(sm.getAmmoTilesActiveStack().contains(null));
            assertFalse(sm.getAmmoTilesWasteStack().contains(null));
            assertFalse(sm.getPowerUpActiveStack().contains(null));
            assertFalse(sm.getPowerUpWasteStack().contains(null));
            assertFalse(sm.getWeaponActiveStack().contains(null));

            //new match's players are identical (same objects) to gameModel's
            assertTrue(gm3.getMatch().getPlayers().containsAll(gm3.allPlayers()));
            assertTrue(gm3.allPlayers().containsAll(gm3.getMatch().getPlayers()));

            //since players' name are equal to the ones of the previous match, previous match is resumed
            assertTrue(p31.isBorn());
            assertTrue(cash1.isEqual(p31.getWallet()));
            assertEquals(s1, p31.getSquarePosition());

            assertTrue(p32.isBorn());
            assertTrue(cash2.isEqual(p32.getWallet()));
            assertEquals(s2, p32.getSquarePosition());

            assertTrue(p33.isBorn());
            assertTrue(cash3.isEqual(p33.getWallet()));
            assertEquals(s3, p33.getSquarePosition());

            assertTrue(p34.isBorn());
            assertTrue(cash4.isEqual(p34.getWallet()));
            assertEquals(s4, p34.getSquarePosition());

            assertFalse(p35.isBorn());
            assertEquals(PlayerState.SPAWN, p35.getState());
            assertEquals(2, p35.getSelectablePowerUps().size());
        } catch (Exception e){
            assertTrue(false);
        }
    }

    private InputStream searchInTestResources(String name){
        InputStream result= getClass().getClassLoader().getResourceAsStream("savedGamesForTests/" + name + ".json");
        return result;
    }

    @Test
    public void startMatchWithDifferentPlayers(){
        //players were: antonio, gianfranco, enrico, matteo, evila
        //enrico inserts a slightly different name, therefore a new game starts
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources( "currentBackupForTest"));

        GameModel gm2 = new GameModel();
        Player p21, p22, p23, p24, p25;
        try {
            List<Player> newPlayers2 = new ArrayList<>();
            newPlayers2.add(p21 = gm2.addPlayer("antonio"));
            newPlayers2.add(p22 = gm2.addPlayer("gianfranco"));
            newPlayers2.add(p23 = gm2.addPlayer("enrici"));
            newPlayers2.add(p24 = gm2.addPlayer("matteo"));
            newPlayers2.add(p25 = gm2.addPlayer("evila"));

            gm2.startMatch();

            assertTrue(isNewMatch(gm2));
        } catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void startMatchWithSamePlayers(){
        //players were: antonio, gianfranco, enrico, matteo, evila
        //players are recreated the same, in teh same order,
        // therefore the old match is resumed
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("currentBackupForTest"));

        GameModel gm2 = new GameModel();
        Player p21, p22, p23, p24, p25;
        try {
            List<Player> newPlayers2 = new ArrayList<>();
            newPlayers2.add(p21 = gm2.addPlayer("antonio"));
            newPlayers2.add(p22 = gm2.addPlayer("gianfranco"));
            newPlayers2.add(p23 = gm2.addPlayer("enrico"));
            newPlayers2.add(p24 = gm2.addPlayer("matteo"));
            newPlayers2.add(p25 = gm2.addPlayer("evila"));
            gm2.startMatch();
        } catch (Exception e){
            assertTrue(false);
        }

        assertFalse(isNewMatch(gm2));
        assertEquals(new Backup(gm.getMatch()), new Backup(gm2.getMatch()));
    }

    @Test
    public void startMatchSamePlayersDifferentOrder(){
        //players were: antonio, gianfranco, enrico, matteo, evila
        //new players have the same names, but in a different order.
        //Nevertheless, the old match is resumed
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("currentBackupForTest"));

        GameModel gm2 = new GameModel();
        Player p21, p22, p23, p24, p25;
        try {
            List<Player> newPlayers2 = new ArrayList<>();
            newPlayers2.add(p25 = gm2.addPlayer("evila"));
            newPlayers2.add(p23 = gm2.addPlayer("enrico"));
            newPlayers2.add(p21 = gm2.addPlayer("antonio"));
            newPlayers2.add(p24 = gm2.addPlayer("matteo"));
            newPlayers2.add(p22 = gm2.addPlayer("gianfranco"));
            gm2.startMatch();
        } catch (Exception e){
            assertTrue(false);
        }

        assertFalse(isNewMatch(gm2));
        assertEquals(new Backup(gm.getMatch()), new Backup(gm2.getMatch()));
    }

    @Test
    public void startMatchDifferentPlayersDifferentOrder(){
        //players were: antonio, gianfranco, enrico, matteo, evila
        //they are in a different order, but one of the new players has a different name
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("currentBackupForTest"));

        GameModel gm2 = new GameModel();
        Player p21, p22, p23, p24, p25;
        List<Player> newPlayers2 = new ArrayList<>();
        try {
            newPlayers2.add(p25 = gm2.addPlayer("evila"));
            newPlayers2.add(p21 = gm2.addPlayer("antonio"));
            newPlayers2.add(p24 = gm2.addPlayer("matteo"));
            newPlayers2.add(p22 = gm2.addPlayer("gianfranco"));
            newPlayers2.add(p23 = gm2.addPlayer("osvaldo"));
            gm2.startMatch();
        } catch (Exception e){
            assertTrue(false);
        }

        assertTrue(isNewMatch(gm2));
    }

    @Test
    public void startMatchOnePlayerLess(){
        //players were: antonio, gianfranco, enrico, matteo, evila
        //one of the old players doesn't login again, therefore a new match is started
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("currentBackupForTest"));

        GameModel gm2 = new GameModel();
        Player p21, p22, p23, p24, p25;
        try {
            List<Player> newPlayers2 = new ArrayList<>();
            newPlayers2.add(p21 = gm2.addPlayer("antonio"));
            newPlayers2.add(p22 = gm2.addPlayer("gianfranco"));
            newPlayers2.add(p23 = gm2.addPlayer("enrici"));
            newPlayers2.add(p24 = gm2.addPlayer("matteo"));
            gm2.startMatch();

            assertTrue(isNewMatch(gm2));
            assertEquals(4, gm2.allPlayers().size());
            assertTrue(gm2.allPlayers().containsAll(newPlayers2));
            assertTrue(newPlayers2.containsAll(gm2.allPlayers()));

        } catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void grabWeaponTest(){
        //first has wallet: 100 + yellow powerup. In bluespawnpoint there are: shockwave (000), machine gun (010) e railgun (101).
        //He moves in the blue spawnpoint (2-1), grabs railgun and pays with a powerup (only railgun and shockwave selectable)
        //Then he grabs shockwave (doesn't pay anything)
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("grabWeaponTestBefore"));
        Match match = gm.getMatch();
        Layout layout = gm.getMatch().getLayout();
        StackManager sm = gm.getMatch().getStackManager();
        Player first = gm.getPlayerByName("first");
        Player second = gm.getPlayerByName("second");
        printSel(first);

            List<Weapon>temp = new ArrayList<>();
            temp.add(sm.getWeaponFromName("Shockwave"));
            temp.add(sm.getWeaponFromName("Railgun"));
            temp.add(sm.getWeaponFromName("Machine gun"));
            assertTrue(layout.getSpawnPoint(AmmoColor.BLUE).getWeapons().containsAll(temp));
            assertTrue(temp.containsAll(layout.getSpawnPoint(AmmoColor.BLUE).getWeapons()));
            assertEquals(CHOOSE_ACTION, first.getState());

        gm.performAction(first, first.getSelectableActions().get(1)); //first grabs

            assertTrue(first.getSelectableSquares().contains(layout.getSquare(2,2)));
            assertEquals(GRAB_THERE, first.getState());

        gm.grabThere(first, layout.getSquare(2,2));

            temp = new ArrayList<>();
            temp.add(sm.getWeaponFromName("Shockwave"));
            temp.add(sm.getWeaponFromName("Railgun"));
            assertTrue(first.getSelectableWeapons().containsAll(temp));
            assertTrue(temp.containsAll(first.getSelectableWeapons()));
            assertEquals(GRAB_WEAPON, first.getState());

        gm.grabWeapon(first, sm.getWeaponFromName("Railgun"));

            printSel(first);
            assertEquals(PAYING, first.getState());
            assertFalse(first.getSelectableCommands().contains(Command.OK));
            assertEquals(1, first.getSelectablePowerUps().size());

        gm.payWith(first, first.getSelectablePowerUps().get(0));

            printSel(first);
            assertEquals(CHOOSE_ACTION, first.getState());

        gm.performAction(first, first.getSelectableActions().get(1));   //he grabs again

            assertEquals(GRAB_THERE, first.getState());
            assertTrue(first.getSelectableSquares().contains(layout.getSquare(2,2)));

        gm.grabThere(first, layout.getSquare(2,2));

            assertEquals(1, first.getSelectableWeapons().size());
            assertTrue(first.getSelectableWeapons().contains(sm.getWeaponFromName("Shockwave")));
            assertEquals(GRAB_WEAPON, first.getState());

        gm.grabWeapon(first, sm.getWeaponFromName("Shockwave"));

            //since first has neither unloadedweapons nor 'your-action-powerups', his turn ends
            assertEquals(second, match.getCurrentPlayer());
    }

    @Test
    public void grabWeaponWithDiscarding() {
        //second has wallet: 220 without powerups and 3 weapons. In yellow spawnpoint there are: plasma gun (001), thor (010), rocket launcher(010).
        //He moves there (3-0), grabs thor,but has to discard one weapon. He discards cyberblade.
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("grabWeaponWithDiscardingBefore"));
        Match match = gm.getMatch();
        Layout layout = gm.getMatch().getLayout();
        StackManager sm = gm.getMatch().getStackManager();
        Player second = gm.getPlayerByName("second");
        printSel(second);

            List<Weapon>temp = new ArrayList<>();
            temp.add(sm.getWeaponFromName("Plasma gun"));
            temp.add(sm.getWeaponFromName("T.H.O.R."));
            temp.add(sm.getWeaponFromName("Rocket launcher"));
            assertTrue(layout.getSpawnPoint(AmmoColor.YELLOW).getWeapons().containsAll(temp));
            assertTrue(temp.containsAll(layout.getSpawnPoint(AmmoColor.YELLOW).getWeapons()));
            assertEquals(CHOOSE_ACTION, second.getState());

        gm.performAction(second, second.getSelectableActions().get(1)); //he grabs

            assertTrue(second.getSelectableSquares().contains(layout.getSquare(3,0)));
            assertEquals(GRAB_THERE, second.getState());

        gm.grabThere(second, layout.getSquare(3,0));

            temp = new ArrayList<>();
            temp.add(sm.getWeaponFromName("T.H.O.R."));
            temp.add(sm.getWeaponFromName("Rocket launcher"));
            assertTrue(second.getSelectableWeapons().containsAll(temp));
            assertTrue(temp.containsAll(second.getSelectableWeapons()));
            assertEquals(GRAB_WEAPON, second.getState());

        gm.grabWeapon(second, sm.getWeaponFromName("T.H.O.R."));

            printSel(second);
            assertEquals(DISCARD_WEAPON, second.getState());
            assertFalse(second.getSelectableCommands().contains(Command.OK));
            temp = new ArrayList<>();
            temp.add(sm.getWeaponFromName("T.H.O.R."));
            temp.add(sm.getWeaponFromName("Electroscythe"));
            temp.add(sm.getWeaponFromName("Cyberblade"));
            temp.add(sm.getWeaponFromName("Shotgun"));
            assertTrue(second.getSelectableWeapons().containsAll(temp));
            assertTrue(temp.containsAll(second.getSelectableWeapons()));

        gm.discardWeapon(second, sm.getWeaponFromName("Cyberblade"));

            assertEquals(CHOOSE_ACTION, second.getState());
            temp.remove(sm.getWeaponFromName("Cyberblade"));
            assertTrue(second.getWeapons().containsAll(temp));
            assertTrue(temp.containsAll(second.getWeapons()));
            assertEquals(new Cash(2, 1,0), second.getWallet());
    }

    @Test
    public void cantGrabAnythingTest(){
        //third has wallet: 000 + yellow powerup. In redspawnpoin there are: zx-2 (010), furnace (100), power glove (100).
        //since he can't grab anything, he can only go back to action choice
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("cantGrabAnythingBefore"));
        Match match = gm.getMatch();
        Layout layout = gm.getMatch().getLayout();
        StackManager sm = gm.getMatch().getStackManager();
        Player third = gm.getPlayerByName("third");
        printSel(third);

            List<Weapon>temp = new ArrayList<>();
            temp.add(sm.getWeaponFromName("ZX-2"));
            temp.add(sm.getWeaponFromName("Furnace"));
            temp.add(sm.getWeaponFromName("Power glove"));
            assertTrue(layout.getSpawnPoint(AmmoColor.RED).getWeapons().containsAll(temp));
            assertTrue(temp.containsAll(layout.getSpawnPoint(AmmoColor.RED).getWeapons()));
            assertEquals(CHOOSE_ACTION, third.getState());

        gm.performAction(third, third.getSelectableActions().get(1)); //he grabs

            assertTrue(third.getSelectableSquares().contains(layout.getSquare(0,1)));
            assertEquals(GRAB_THERE, third.getState());

        gm.grabThere(third, layout.getSquare(0,1));

            assertTrue(third.getSelectableWeapons().isEmpty());
            assertEquals(1, third.getSelectableCommands().size());
            assertTrue(third.getSelectableCommands().contains(Command.BACK));

        gm.restore();

            //everything is brought back as it was
            assertEquals(Backup.initFromFile(searchInTestResources("cantGrabAnythingBefore")), new Backup(gm.getMatch()));
    }

    @Test
    public void currentPlayerDisconnectsDuringAction(){
        GameModel gm = new GameModel();
        gm.resumeMatchFromFile(searchInTestResources("genericState1"));
        Match match = gm.getMatch();
        Layout layout = gm.getMatch().getLayout();
        StackManager sm = gm.getMatch().getStackManager();

        Player first = gm.getPlayerByName("first");
        Player second = gm.getPlayerByName("second");
        Player third = gm.getPlayerByName("third");
        Player fourth = gm.getPlayerByName("fourth");

            printSituation(match);

        gm.performAction(first, first.getSelectableActions().get(1));//grab
        startTimers(gm);

            assertTrue(first.getSelectableSquares().contains(layout.getSquare(2,2)));

        gm.grabThere(first, layout.getSquare(2,2));
        startTimers(gm);

            printSel(first);
            assertTrue(first.getSelectableWeapons().contains(sm.getWeaponFromName("Heatseeker")));

        gm.grabWeapon(first, sm.getWeaponFromName("Heatseeker"));
        startTimers(gm);

            assertTrue(first.getSelectablePowerUps().contains(sm.getPowerUpFromString("16-Tagback grenade-YELLOW")));

        gm.payWith(first, sm.getPowerUpFromString("16-Tagback grenade-YELLOW"));
        startTimers(gm);
        disconnectPlayer(gm, first);
        startTimers(gm);

            assertEquals(second, match.getCurrentPlayer());
            assertEquals(IDLE, first.getState());
            printSituation(match);
            boolean result = Backup.initFromFile(searchInTestResources("currentPlayerDisconnectsDuringActionAfter")).equals(new Backup(gm.getMatch()));
    }
}
