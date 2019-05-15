package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {
    private static final String testGamePath = "./src/test/resources/";

    public void addPlayers(GameModel gm, List<Player> players){
        for (Player p : players){
            try {
                gm.addPlayer(p);
            } catch (NameAlreadyTakenException | GameFullException | AlreadyLoggedException | NameNotFoundException e){

            }
        }
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

    @Test
    public void nextActivePlayerWithFivePlayers() {
        GameModel gm = new GameModel();
        Player p1, p2, p3, p4, p5;
        List<Player> newPlayers = new ArrayList<>();
        newPlayers.add(p1 = new Player("primo giocatore"));
        newPlayers.add(p2 = new Player("secondo giocatore"));
        newPlayers.add(p3 = new Player("terzo giocatore"));
        newPlayers.add(p4 = new Player("quarto giocatore"));
        newPlayers.add(p5 = new Player("quinto giocatore"));

        addPlayers(gm, newPlayers);
        gm.startNewMatch();
        Player currP = null;

        try {
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
        } catch (GameOverException e){}
    }

    @Test
    public void nextActivePlayerWithFourPlayers() {
        GameModel gm = new GameModel();
        Player p1, p2, p3, p4, p5;
        List<Player> newPlayers = new ArrayList<>();
        newPlayers.add(p1 = new Player("primo giocatore"));
        newPlayers.add(p2 = new Player("secondo giocatore"));
        newPlayers.add(p3 = new Player("terzo giocatore"));
        newPlayers.add(p4 = new Player("quarto giocatore"));

        addPlayers(gm, newPlayers);
        gm.startNewMatch();
        Player currP = null;

        try {
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
        } catch (GameOverException e){}
    }

    public void printSel(Player p){
        System.out.println(p.selectablesToString());
    }

    @Test
    public void grabFromAmmoSquares(){
        GameModel gm = new GameModel();
        Player p1, p2, p3, p4, p5;
        List<Player> newPlayers = new ArrayList<>();
        newPlayers.add(p1 = new Player("primo giocatore"));
        newPlayers.add(p2 = new Player("secondo giocatore"));
        newPlayers.add(p3 = new Player("terzo giocatore"));

        addPlayers(gm, newPlayers);

        gm.startNewMatch();
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

        assertEquals(PlayerState.GRAB_THERE, match.getCurrentPlayer().getState());  //he is in GRAB_THERE state
        System.out.println(match.getCurrentPlayer().selectablesToString());
        Square firstSquareSelected = tempPlayer.getSelectableSquares().get(0);  //he wants to grab in the first square (ammoSquare)
        Cash tempCash = ((AmmoSquare)firstSquareSelected).getAmmo().getAmmos();
        assertTrue(new Cash(0,0,0).isEqual(tempPlayer.getWallet()));

        gm.grabThere(tempPlayer, firstSquareSelected);         //he grabs in the first square (ammoSquare)

        assertEquals(PlayerState.CHOOSE_ACTION, tempPlayer.getState());
        assertTrue(tempCash.isEqual(tempPlayer.getWallet()));
        assertEquals(firstSquareSelected, tempPlayer.getSquarePosition());
        System.out.println(tempPlayer.getPowerUps());
        System.out.println(tempPlayer.getWallet());
        Cash previousCash = tempPlayer.getWallet();
        printSel(tempPlayer);

        gm.performAction(tempPlayer, tempPlayer.getSelectableActions().get(1));     //he wants to grab (second action)

        assertEquals(PlayerState.GRAB_THERE, tempPlayer.getState());
        printSel(tempPlayer);
        assertFalse(tempPlayer.getSelectableSquares().contains(firstSquareSelected));
        assertEquals(2, match.getActionsCompleted());

        gm.grabThere(tempPlayer, tempPlayer.getSelectableSquares().get(0));

        System.out.println(tempPlayer.getPowerUps());
        System.out.println(tempPlayer.getWallet());
        printSel(tempPlayer);
    }

    @Test
    public void exceptionTest(){
        GameModel gm = new GameModel();

        Player p1 = new Player("first", PlayerColor.YELLOW);
        Player p2 = new Player("second", PlayerColor.BLUE);
        Player p3 = new Player("third", PlayerColor.GREEN);
        Player p4 = new Player("fourth", PlayerColor.GREY);

        try{
            gm.addPlayer(p1);
            gm.addPlayer(p2);
            gm.addPlayer(p3);
            gm.addPlayer(p4);
        } catch(NameAlreadyTakenException | GameFullException | AlreadyLoggedException | NameNotFoundException e){
        }

        assertThrows(NameAlreadyTakenException.class, () -> gm.addPlayer(new Player("first", PlayerColor.VIOLET)));

        Player p5 = new Player("fifth", PlayerColor.VIOLET);
        try{
            gm.addPlayer(p5);

        } catch(NameAlreadyTakenException | GameFullException | AlreadyLoggedException | NameNotFoundException e){
        }

        assertThrows(GameFullException.class, () -> gm.addPlayer(new Player("last", PlayerColor.GREEN)));
    }

    @Test
    public void StartNewMatchTest(){
        GameModel gm = new GameModel();

        Player p1, p2, p3, p4, p5;
        List<Player> newPlayers = new ArrayList<>();
        newPlayers.add(p1 = new Player("andrea"));
        newPlayers.add(p2 = new Player("alessandro"));
        newPlayers.add(p3 = new Player("marco"));
        newPlayers.add(p4 = new Player("giacomo"));
        newPlayers.add(p5 = new Player("luca"));

        addPlayers(gm, newPlayers);

        assertEquals(newPlayers.size(), gm.getNumberOfPlayers());

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

        //no players is born
        for (Player p : gm.allPlayers()){
            assertFalse(p.isBorn());
        }

        //there's just one player spawing, with 2 selectable powerups
        List<Player> tempPlayers = new ArrayList<>();
        tempPlayers.addAll(gm.getMatch().getPlayers());
        tempPlayers.removeIf(p -> p.getState()!=PlayerState.SPAWN);
        assertEquals(1, tempPlayers.size());
        Player spawningPlayer = tempPlayers.get(0);
        assertEquals(2, spawningPlayer.getPowerUps().size());
        assertEquals(2, spawningPlayer.getSelectablePowerUps().size());
        assertTrue(spawningPlayer.getSelectableCommands().isEmpty());

        //other players are in IDLE and have nothing selectable
        tempPlayers.clear();
        tempPlayers.addAll(gm.getMatch().getPlayers());
        tempPlayers.remove(spawningPlayer);
        for (Player p : tempPlayers) {
            assertEquals(PlayerState.IDLE, p.getState());
            assertEquals(0, p.howManySelectables());
        }
    }


    @Test
    public void GrabAndMoveTestFivePlayers(){
        GameModel gm = new GameModel();

        Player p1, p2, p3, p4, p5;
        List<Player> newPlayers = new ArrayList<>();
        newPlayers.add(p1 = new Player("antonio"));
        newPlayers.add(p2 = new Player("gianfranco"));
        newPlayers.add(p3 = new Player("enrico"));
        newPlayers.add(p4 = new Player("matteo"));
        newPlayers.add(p5 = new Player("evila"));

        addPlayers(gm, newPlayers);

        assertEquals(newPlayers.size(), gm.getNumberOfPlayers());

        gm.startNewMatch();

        gm.spawn(p1, p1.getSelectablePowerUps().get(0));
        gm.performAction(p1, p1.getSelectableActions().get(1)); //grab
        Square s1 =  p1.getSelectableSquares().get(1);
        Cash cash1 = ((AmmoSquare)s1).getAmmo().getAmmos();
        gm.grabThere(p1, s1);
        gm.performAction(p1, p1.getSelectableActions().get(0)); //move
        s1 = p1.getSelectableSquares().get(1);
        gm.moveMeThere(p1, s1);
        if (p1.getState() == PlayerState.CHOOSE_ACTION) gm.endTurn();
        assertTrue(p1.isBorn());
        assertTrue(cash1.isEqual(p1.getWallet()));
        assertEquals(s1, p1.getSquarePosition());

        for (Player p : gm.getSpawningPlayers()){
            System.out.println(p+" "+p.getName());
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
        assertEquals(PlayerState.GRAB_THERE, p2.getState());
        Square s2 = p2.getSelectableSquares().get(1);
        Cash cash2 = ((AmmoSquare)s2).getAmmo().getAmmos();
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
        Cash cash3 = new Cash(((AmmoSquare)s3).getAmmo().getAmmos());
        gm.grabThere(p3, s3);
        if (p3.getState() == PlayerState.CHOOSE_ACTION) gm.endTurn();

        assertEquals(PlayerState.SPAWN, p4.getState());
        //assertEquals(2, p4.getSelectablePowerUps().size());
        //assertEquals(2, p4.getPowerUps().size());
        gm.spawn(p4, p4.getSelectablePowerUps().get(1));
        gm.performAction(p4, p4.getSelectableActions().get(1));   //grab
        Square s4 = p4.getSelectableSquares().get(1);
        Cash cash4 = ((AmmoSquare)s4).getAmmo().getAmmos();
        gm.grabThere(p4, s4);
        gm.performAction(p4, p4.getSelectableActions().get(1));  //grab
        s4 = p4.getSelectableSquares().get(0);
        cash4 = cash4.sum(((AmmoSquare)s4).getAmmo().getAmmos());
        gm.grabThere(p4, s4);
        assertEquals(s4, p4.getSquarePosition());
        if (p4.getState() == PlayerState.CHOOSE_ACTION) gm.endTurn();
    }

    @Test
    public void StartMatchTwoTimesWithValidBackup(){
        GameModel gm = new GameModel();

        Player p1, p2, p3, p4, p5;
        List<Player> newPlayers = new ArrayList<>();
        newPlayers.add(p1 = new Player("antonio"));
        newPlayers.add(p2 = new Player("gianfranco"));
        newPlayers.add(p3 = new Player("enrico"));
        newPlayers.add(p4 = new Player("matteo"));
        newPlayers.add(p5 = new Player("evila"));

        addPlayers(gm, newPlayers);

        assertEquals(newPlayers.size(), gm.getNumberOfPlayers());

        gm.startNewMatch();

        gm.spawn(p1, p1.getSelectablePowerUps().get(0));
        gm.performAction(p1, p1.getSelectableActions().get(1)); //grab
        Square s1 =  p1.getSelectableSquares().get(1);
        Cash cash1 = ((AmmoSquare)s1).getAmmo().getAmmos();
        gm.grabThere(p1, s1);
        gm.performAction(p1, p1.getSelectableActions().get(0)); //move
        s1 = p1.getSelectableSquares().get(1);
        gm.moveMeThere(p1, s1);
        if (p1.getState() == PlayerState.CHOOSE_ACTION) gm.endTurn();

        gm.spawn(p2, p2.getSelectablePowerUps().get(0));

        GameModel gm2 = new GameModel();
        Player p21, p22, p23, p24, p25;
        List<Player> newPlayers2 = new ArrayList<>();
        newPlayers2.add(p21 = new Player("antonio"));
        newPlayers2.add(p22 = new Player("gianfranco"));
        newPlayers2.add(p23 = new Player("enrico"));
        newPlayers2.add(p24 = new Player("matteo"));
        newPlayers2.add(p25 = new Player("evila"));

        addPlayers(gm2, newPlayers2);

        //checks player that object are different (rather obvious)
        for (Player p : newPlayers2){
            assertFalse(newPlayers.contains(p));
        }
        for (Player p : newPlayers){
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
        assertEquals(PlayerState.GRAB_THERE, p22.getState());
        Square s2 = p22.getSelectableSquares().get(1);
        Cash cash2 = ((AmmoSquare)s2).getAmmo().getAmmos();
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
        Cash cash3 = ((AmmoSquare)s3).getAmmo().getAmmos();
        gm2.grabThere(p23, s3);
        if (p23.getState() == PlayerState.CHOOSE_ACTION) gm2.endTurn();

        assertEquals(PlayerState.SPAWN, p24.getState());
        assertEquals(2, p24.getSelectablePowerUps().size());
        assertEquals(2, p24.getPowerUps().size());
        gm2.spawn(p24, p24.getSelectablePowerUps().get(1));
        gm2.performAction(p24, p24.getSelectableActions().get(1));   //grab
        Square s4 = p24.getSelectableSquares().get(1);
        Cash cash4 = new Cash (((AmmoSquare)s4).getAmmo().getAmmos());
        gm2.grabThere(p24, s4);
        gm2.performAction(p24, p24.getSelectableActions().get(1));  //grab
        s4 = p24.getSelectableSquares().get(0);
        cash4.deposit(((AmmoSquare)s4).getAmmo().getAmmos());
        gm2.grabThere(p24, s4);
        assertEquals(s4, p24.getSquarePosition());
        if (p24.getState() == PlayerState.CHOOSE_ACTION) gm2.endTurn();

        //checks if the current match can be reloaded again (if player are the same)
        GameModel gm3 = new GameModel();
        Player p31, p32, p33, p34, p35;
        List<Player> newPlayers3 = new ArrayList<>();
        newPlayers3.add(p31 = new Player("antonio"));
        newPlayers3.add(p32 = new Player("gianfranco"));
        newPlayers3.add(p33 = new Player("enrico"));
        newPlayers3.add(p34 = new Player("matteo"));
        newPlayers3.add(p35 = new Player("evila"));

        addPlayers(gm3, newPlayers3);

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
    }



}