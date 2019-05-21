package it.polimi.ingsw;



import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Square;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.server.model.enums.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {

    @Test
    public void getPlayersOn() {
        Match match = new Match(3, 8);

        Player p1 = new Player("Paul", GREY);
        Player p2 = new Player("John", YELLOW);
        Player p3 = new Player("Benjamin", GREEN);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);
        p1.setSquarePosition(match.getLayout().getSquare(0,2));
        p2.setSquarePosition(match.getLayout().getSquare(3,3));
        p3.setSquarePosition(match.getLayout().getSquare(2,1));

        List<Square> squaresWithoutPlayers = new ArrayList<>();
        squaresWithoutPlayers.add(match.getLayout().getSquare(3,0));
        squaresWithoutPlayers.add(match.getLayout().getSquare(1,1));

        assertEquals(0, match.getPlayersOn(squaresWithoutPlayers).size());

        List<Square> squaresOccupied = new ArrayList<>(squaresWithoutPlayers);
        squaresOccupied.add(match.getLayout().getSquare(0,2));
        squaresOccupied.add(match.getLayout().getSquare(3,3));
        squaresOccupied.add(match.getLayout().getSquare(2,1));

        assertEquals(3, match.getPlayersOn(squaresOccupied).size());
    }

    @Test
    public void justOneWinner() {
        Match match = new Match(0, 8);

        Player p1 = new Player("Paul", GREY);
        Player p2 = new Player("John", YELLOW);
        Player p3 = new Player("Benjamin", GREEN);
        Player p4 = new Player("Lucas", BLUE);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);
        match.addPlayer(p4);

        p1.addPoints(10);
        p2.addPoints(10);
        p3.addPoints(10);
        p4.addPoints(10);

        Map<Player, Integer> kill1 = new HashMap<>();
        kill1.put(p1, 2);
        match.getKillShotTrack().addKilled(kill1);
        match.getKillShotTrack().removeSkull();

        Map<Player, Integer> kill2 = new HashMap<>();
        kill2.put(p2, 1);
        match.getKillShotTrack().addKilled(kill2);
        match.getKillShotTrack().removeSkull();

        Map<Player, Integer> kill3 = new HashMap<>();
        kill3.put(p3, 1);
        match.getKillShotTrack().addKilled(kill3);
        match.getKillShotTrack().removeSkull();

        assertEquals(1, match.getWinners().size());
        assertTrue(match.getWinners().contains(p1));
    }

    @Test
    public void theWinnerKilledALot(){
        Match match = new Match(0, 8);

        Player p1 = new Player("Paul", GREY);
        Player p2 = new Player("John", YELLOW);
        Player p3 = new Player("Benjamin", GREEN);
        Player p4 = new Player("Lucas", BLUE);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);
        match.addPlayer(p4);

        p1.addPoints(10);
        p2.addPoints(12);
        p3.addPoints(10);
        p4.addPoints(10);

        Map<Player, Integer> kill1 = new HashMap<>();
        kill1.put(p1, 2);
        match.getKillShotTrack().addKilled(kill1);
        match.getKillShotTrack().removeSkull();

        Map<Player, Integer> kill2 = new HashMap<>();
        kill2.put(p2, 1);
        match.getKillShotTrack().addKilled(kill2);
        match.getKillShotTrack().removeSkull();

        Map<Player, Integer> kill3 = new HashMap<>();
        kill3.put(p3, 1);
        match.getKillShotTrack().addKilled(kill3);
        match.getKillShotTrack().removeSkull();

        assertEquals(1, match.getWinners().size());
        assertTrue(match.getWinners().contains(p1));
    }

    @Test
    public void tie(){
        Match match = new Match(0, 8);

        Player p1 = new Player("Paul", GREY);
        Player p2 = new Player("John", YELLOW);
        Player p3 = new Player("Benjamin", GREEN);
        Player p4 = new Player("Lucas", BLUE);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);
        match.addPlayer(p4);

        p1.addPoints(10);
        p2.addPoints(10);
        p3.addPoints(10);
        p4.addPoints(10);

        assertEquals(4, match.getWinners().size());

        p1.addPoints(1);
        p2.addPoints(1);

        assertEquals(2, match.getWinners().size());
        assertTrue(match.getWinners().contains(p1));
        assertTrue(match.getWinners().contains(p2));
    }
}