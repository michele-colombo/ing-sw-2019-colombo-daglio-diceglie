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
        assertTrue(gm.getMatch().getPlayers().containsAll(newPlayers));
        assertTrue(newPlayers.containsAll(gm.getMatch().getPlayers()));

        List<PlayerColor> colorsGiven = new ArrayList<>();
        for (Player p : gm.getMatch().getPlayers()){
            assertNotNull(p.getColor());
            assertFalse(colorsGiven.contains(p.getColor()));
            colorsGiven.add(p.getColor());
        }

        assertNotNull(gm.getMatch().getLayout());
        assertNotNull(gm.getMatch().getStackManager());
        assertNotNull(gm.getMatch().getKillShotTrack());

        //there's just one player spawing, with 2 selectable powerups
        List<Player> tempPlayers = new ArrayList<>();
        tempPlayers.addAll(gm.getMatch().getPlayers());
        tempPlayers.removeIf(p -> p.getState()!=PlayerState.SPAWN);
        assertEquals(1, tempPlayers.size());
        Player spawingPlayer = tempPlayers.get(0);
        assertEquals(2, spawingPlayer.getPowerUps().size());
        assertEquals(2, spawingPlayer.getSelectablePowerUps().size());

        tempPlayers.clear();
        tempPlayers.addAll(gm.getMatch().getPlayers());
        tempPlayers.remove(spawingPlayer);
        for (Player p : tempPlayers) {
            assertEquals(PlayerState.IDLE, p.getState());
            assertEquals(0, p.howManySelectables());
        }
    }

    @Test
    public void StartMatchWithValidBackup(){
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
        Square s1 = p1.getSelectableSquares().get(1);
        gm.grabThere(p1, s1);
        gm.endTurn();

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

        //check player object are different
        for (Player p : newPlayers2){
            assertFalse(newPlayers.contains(p));
        }
        for (Player p : newPlayers){
            assertFalse(newPlayers2.contains(p));
        }

        gm2.startMatch();


    }


}