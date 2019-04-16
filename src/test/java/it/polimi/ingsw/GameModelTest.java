package it.polimi.ingsw;

import org.ietf.jgss.GSSManager;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameModelTest {

    @Test
    public void nextActivePlayerWithFivePlayers() {
        Player p1 = new Player("primo giocatore", PlayerColor.YELLOW);
        Player p2 = new Player("secondo giocatore", PlayerColor.BLUE);
        Player p3 = new Player("terzo giocatore", PlayerColor.GREEN);
        Player p4 = new Player("quarto giocatore", PlayerColor.GREY);
        Player p5 = new Player("quinto giocatore", PlayerColor.VIOLET);

        GameModel gm = new GameModel();

        gm.addPlayer(p1);
        gm.addPlayer(p2);
        gm.addPlayer(p3);
        gm.addPlayer(p4);
        gm.addPlayer(p5);

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

        gm.addPlayer(p1);
        gm.addPlayer(p2);
        gm.addPlayer(p3);
        gm.addPlayer(p4);

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

    @Test
    public void weaponPayment(){
        Player p1 = new Player("primo giocatore", PlayerColor.YELLOW);
        Player p2 = new Player("secondo giocatore", PlayerColor.BLUE);
        Player p3 = new Player("terzo giocatore", PlayerColor.GREEN);

        GameModel gm = new GameModel();
        gm.addPlayer(p1);
        gm.addPlayer(p2);
        gm.addPlayer(p3);

        gm.initMatch();

    }


}