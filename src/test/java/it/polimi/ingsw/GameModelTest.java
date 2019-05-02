package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.exceptions.GameFullException;
import it.polimi.ingsw.exceptions.NameAlreadyTakenException;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {

    @Test
    public void nextActivePlayerWithFivePlayers() {
        Player p1 = new Player("primo giocatore", PlayerColor.YELLOW);
        Player p2 = new Player("secondo giocatore", PlayerColor.BLUE);
        Player p3 = new Player("terzo giocatore", PlayerColor.GREEN);
        Player p4 = new Player("quarto giocatore", PlayerColor.GREY);
        Player p5 = new Player("quinto giocatore", PlayerColor.VIOLET);

        GameModel gm = new GameModel();

        try{
            gm.addPlayer(p1);
            gm.addPlayer(p2);
            gm.addPlayer(p3);
            gm.addPlayer(p4);
            gm.addPlayer(p5);
        } catch(NameAlreadyTakenException e){

        } catch(ColorAlreadyTakenException e){

        } catch(GameFullException e){

        }

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
    }

    @Test
    public void nextActivePlayerWithFourPlayers() {
        Player p1 = new Player("primo giocatore", PlayerColor.YELLOW);
        Player p2 = new Player("secondo giocatore", PlayerColor.BLUE);
        Player p3 = new Player("terzo giocatore", PlayerColor.GREEN);
        Player p4 = new Player("quarto giocatore", PlayerColor.GREY);

        GameModel gm = new GameModel();

        try{
            gm.addPlayer(p1);
            gm.addPlayer(p2);
            gm.addPlayer(p3);
            gm.addPlayer(p4);
        } catch(NameAlreadyTakenException e){

        } catch(ColorAlreadyTakenException e){

        } catch(GameFullException e){

        }

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
    }

    public void printSel(Player p){
        System.out.println(p.selectablesToString());
    }

    @Test
    public void grabFromAmmoSquares(){
        Player p1 = new Player("primo giocatore", PlayerColor.YELLOW);
        Player p2 = new Player("secondo giocatore", PlayerColor.BLUE);
        Player p3 = new Player("terzo giocatore", PlayerColor.GREEN);

        GameModel gm = new GameModel();
        try{
            gm.addPlayer(p1);
            gm.addPlayer(p2);
            gm.addPlayer(p3);
        } catch(NameAlreadyTakenException e){

        } catch(ColorAlreadyTakenException e){

        } catch(GameFullException e){

        }

        gm.initMatch();
        Match match = gm.getMatch();

        List<Weapon> tempWeapons = new ArrayList<>();
        tempWeapons.add(new Weapon("distruttore", new Cash(2,0,0), Color.BLUE));
        tempWeapons.add(new Weapon("mitragliatrice", new Cash(1, 1, 0), Color.BLUE));
        tempWeapons.add(new Weapon("torpedine", new Cash(1,1,0), Color.BLUE));
        tempWeapons.add(new Weapon("cannone Vortex", new Cash(1,1,0), Color.RED));
        tempWeapons.add(new Weapon("vulcanizzatore", new Cash(1,1,0), Color.RED));
        tempWeapons.add(new Weapon("razzo termico", new Cash(0,2,1), Color.RED));
        tempWeapons.add(new Weapon("lanciafiamme", new Cash(0,1,0), Color.RED));
        tempWeapons.add(new Weapon("fucile laser", new Cash(1,0,2), Color.YELLOW));
        tempWeapons.add(new Weapon("spada fotonica", new Cash(0,1,1), Color.YELLOW));
        tempWeapons.add(new Weapon("fucile a pompa", new Cash(0,0,2), Color.YELLOW));
        tempWeapons.add(new Weapon("cyberguanto", new Cash(1,0,1), Color.YELLOW));
        tempWeapons.add(new Weapon("onda d'urto", new Cash(0,0,1), Color.YELLOW));
        match.getStackManager().initWeaponStack(tempWeapons);

        List<PowerUp> tempPowerups = new ArrayList<>();
        for (Color color : Color.getAmmoColors()){
            for (int i=0; i<2; i++){
                tempPowerups.add(new PowerUp(color, PowerUpType.TAGBACK_GRENADE, "Tagback granade"));
                tempPowerups.add(new PowerUp(color, PowerUpType.TARGETING_SCOPE, "Targeting scope"));
                tempPowerups.add(new PowerUp(color, PowerUpType.ACTION_POWERUP, "Newton"));
                tempPowerups.add(new PowerUp(color, PowerUpType.ACTION_POWERUP, "Teleporter"));
            }
        }
        match.getStackManager().initPowerUpStack(tempPowerups);

        List<AmmoTile> tempAmmoTiles = new ArrayList<>();
        for (int i=0; i<4; i++){
            tempAmmoTiles.add(new AmmoTile(new Cash(2, 1, 0), false));
            tempAmmoTiles.add(new AmmoTile(new Cash(0, 1, 2), false));
            tempAmmoTiles.add(new AmmoTile(new Cash(0, 2, 1), false));
            tempAmmoTiles.add(new AmmoTile(new Cash(1, 0, 2), false));
            tempAmmoTiles.add(new AmmoTile(new Cash(2, 0, 1), false));
            tempAmmoTiles.add(new AmmoTile(new Cash(1, 1, 1), false));
            tempAmmoTiles.add(new AmmoTile(new Cash(1, 1, 0), true));
            tempAmmoTiles.add(new AmmoTile(new Cash(1, 0, 1), true));
            tempAmmoTiles.add(new AmmoTile(new Cash(0, 1, 1), true));
        }
        match.getStackManager().initAmmoTilesStack(tempAmmoTiles);

        gm.startMatch();

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
        Player p1 = new Player("first", PlayerColor.YELLOW);
        Player p2 = new Player("second", PlayerColor.BLUE);
        Player p3 = new Player("third", PlayerColor.GREEN);
        Player p4 = new Player("fourth", PlayerColor.GREY);

        GameModel gm = new GameModel();

        try{
            gm.addPlayer(p1);
            gm.addPlayer(p2);
            gm.addPlayer(p3);
            gm.addPlayer(p4);
        } catch(NameAlreadyTakenException e){

        } catch(ColorAlreadyTakenException e){

        } catch(GameFullException e){

        }

        assertThrows(NameAlreadyTakenException.class, () -> gm.addPlayer(new Player("first", PlayerColor.VIOLET)));
        assertThrows(ColorAlreadyTakenException.class, () -> gm.addPlayer(new Player("new", PlayerColor.GREEN)));

        Player p5 = new Player("fifth", PlayerColor.VIOLET);
        try{
            gm.addPlayer(p5);

        } catch(NameAlreadyTakenException e){

        } catch(ColorAlreadyTakenException e){

        } catch(GameFullException e){

        }

        assertThrows(GameFullException.class, () -> gm.addPlayer(new Player("last", PlayerColor.GREEN)));
    }

}