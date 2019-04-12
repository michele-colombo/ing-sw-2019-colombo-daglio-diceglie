package it.polimi.ingsw;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.PlayerColor.*;
import static org.junit.Assert.*;

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
}