package it.polimi.ingsw;



import it.polimi.ingsw.server.controller.ParserManager;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Square;
import org.junit.jupiter.api.Test;

import java.util.*;

import static it.polimi.ingsw.server.model.enums.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;
import static it.polimi.ingsw.testUtils.*;

public class MatchTest {
    ParserManager pm= new ParserManager();

    private Match createMatch(int layoutConfig, int skulls){
        return new Match(pm.getLayout( layoutConfig ), skulls, pm.getStackManager());
    }

    @Test
    public void getPlayersOn() {
        Match match = createMatch(3, 8);

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
        Match match = createMatch(0, 8);

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

        match.scoreFinalPoints();
        printMap(match.getRank());
        printMap(match.getAllPoints());


        assertEquals(1, Collections.frequency(match.getRank().values(), 1));
        assertEquals(1, match.getRank().get(p1));
    }

    @Test
    public void theWinnerKilledALot(){
        Match match = createMatch(0, 8);

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


        match.scoreFinalPoints();
        printMap(match.getRank());
        printMap(match.getAllPoints());

        assertEquals(1, Collections.frequency(match.getRank().values(), 1));
        assertEquals(1, match.getRank().get(p1));
    }

    @Test
    public void tie(){
        Match match = createMatch(0, 8);

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

        match.scoreFinalPoints();
        printMap(match.getRank());
        printMap(match.getAllPoints());

        assertEquals(4, Collections.frequency(match.getRank().values(), 1));

        p1.addPoints(1);
        p2.addPoints(1);

        match.scoreFinalPoints();
        printMap(match.getRank());
        printMap(match.getAllPoints());

        assertEquals(2, Collections.frequency(match.getRank().values(), 1));
        assertEquals(1, match.getRank().get(p1));
        assertEquals(1, match.getRank().get(p2));
    }

    @Test
    public void rankTest1(){
        Match match = createMatch(0, 8);

        Player p1 = new Player("uno", GREY);
        Player p2 = new Player("due", YELLOW);
        Player p3 = new Player("tre", GREEN);
        Player p4 = new Player("quattro", BLUE);
        Player p5 = new Player("cinque", VIOLET);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);
        match.addPlayer(p4);
        match.addPlayer(p5);

        p1.addPoints(20);
        p2.addPoints(19);
        p3.addPoints(24);
        p4.addPoints(26);
        p5.addPoints(23);
        addKilling(match, p2, 1);
        addKilling(match, p1, 2);
        addKilling(match, p3, 1);
        addKilling(match, p4, 1);
        addKilling(match, p1, 2);
        addKilling(match, p3, 1);
        addKilling(match, p4, 1);
        addKilling(match, p2, 1);

        match.scoreFinalPoints();
        printMap(match.getRank());
        printMap(match.getAllPoints());

        assertEquals(1, match.getRank().get(p1));
        assertEquals(4, match.getRank().get(p2));
        assertEquals(2, match.getRank().get(p3));
        assertEquals(3, match.getRank().get(p4));
        assertEquals(5, match.getRank().get(p5));

        assertEquals(28, match.getAllPoints().get(p1));
        assertEquals(25, match.getAllPoints().get(p2));
        assertEquals(28, match.getAllPoints().get(p3));
        assertEquals(28, match.getAllPoints().get(p4));
        assertEquals(23, match.getAllPoints().get(p5));
    }

    @Test
    public void tieBetweenPlayersNotInKillshotTrack(){
        Match match = createMatch(0, 8);

        Player p1 = new Player("uno", GREY);
        Player p2 = new Player("due", YELLOW);
        Player p3 = new Player("tre", GREEN);
        Player p4 = new Player("quattro", BLUE);
        Player p5 = new Player("cinque", VIOLET);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);
        match.addPlayer(p4);
        match.addPlayer(p5);

        p1.addPoints(30);
        p2.addPoints(30);
        p3.addPoints(20);
        p4.addPoints(25);
        p5.addPoints(25);

        addKilling(match, p3, 1);

        match.scoreFinalPoints();
        printMap(match.getRank());
        printMap(match.getAllPoints());

        assertEquals(1, match.getRank().get(p1));
        assertEquals(1, match.getRank().get(p2));
        assertEquals(3, match.getRank().get(p3));
        assertEquals(4, match.getRank().get(p4));
        assertEquals(4, match.getRank().get(p5));

        assertEquals(30, match.getAllPoints().get(p1));
        assertEquals(30, match.getAllPoints().get(p2));
        assertEquals(28, match.getAllPoints().get(p3));
        assertEquals(25, match.getAllPoints().get(p4));
        assertEquals(25, match.getAllPoints().get(p5));
    }


    private void addKilling(Match match, Player p, int n) {
        Map<Player, Integer> kill = new HashMap<>();
        kill.put(p, n);
        match.getKillShotTrack().addKilled(kill);
        match.getKillShotTrack().removeSkull();
    }
}