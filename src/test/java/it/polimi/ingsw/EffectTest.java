package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EffectTest {

    @Test
    public void startTest(){

        Player paolo= new Player();
        Player gio= new Player();
        Player ste= new Player();

        Match m= new Match();

        paolo.setSquarePosition(m.getLayout().getSquare(0, 1));
        gio.setSquarePosition(m.getLayout().getSquare(0, 2));
        ste.setSquarePosition(m.getLayout().getSquare(1, 1));

        m.addPlayer(paolo);
        m.addPlayer(gio);
        m.addPlayer(ste);

        List<Square> posizioni= new ArrayList<>();
        posizioni.add(m.getLayout().getSquare(0, 1));

        assertTrue(m.getPlayersOn(posizioni).contains(paolo));


        m.setCurrentPlayer(paolo);

        Effect eff= new Effect(0, 1, 0, -1, 4, 1, 1, 2, 0, 0, 0);

        eff.start(paolo, m);

        assertTrue(paolo.getSelectablePlayers().contains(gio));
        assertTrue(paolo.getSelectablePlayers().contains(ste));
        assertTrue(paolo.getSelectablePlayers().size() == 2);

        assertTrue(paolo.getSelectableSquares().isEmpty());

        gio.setSquarePosition(m.getLayout().getSquare(3, 0));

        eff.start(paolo, m);
        assertTrue(paolo.getSelectablePlayers().contains(ste));
        assertTrue(paolo.getSelectablePlayers().size() == 1);
    }

    @Test
    public void quiteAllSituations(){
        Match m= new Match();

        Player a= new Player("Aldo", PlayerColor.GREEN);
        Player b= new Player("Biagio", PlayerColor.GREY);
        Player c= new Player("Carlo", PlayerColor.YELLOW);
        Player d= new Player("Dario", PlayerColor.VIOLET);
        Player e= new Player("Elena", PlayerColor.BLUE);

        m.addPlayer(a);
        m.addPlayer(b);
        m.addPlayer(c);
        m.addPlayer(d);
        m.addPlayer(e);

        a.setSquarePosition(m.getLayout().getSquare(0,1));
        b.setSquarePosition(m.getLayout().getSquare(1,2));
        c.setSquarePosition(m.getLayout().getSquare(2,1));
        d.setSquarePosition(m.getLayout().getSquare(3,0));
        e.setSquarePosition(m.getLayout().getSquare(3,2));

        //not visible
        Effect effetto= new Effect(0, 0, -1, -1, 4, 1, 1, 2, 0, 0, 0);
        for(Player pl: m.getPlayers()){
            effetto.start(pl, m);
        }

        List<Player> foundByHand= new ArrayList<>();

        foundByHand.add(c);
        foundByHand.add(d);
        foundByHand.add(e);

        assertTrue(a.getSelectablePlayers().containsAll(foundByHand));
        assertTrue(a.getSelectablePlayers().size()== 3);

        foundByHand.clear();
        foundByHand.add(a);
        foundByHand.add(b);
        foundByHand.add(e);

        assertTrue(d.getSelectablePlayers().containsAll(foundByHand));
        assertTrue(d.getSelectablePlayers().size() == 3);

        for(Player p: m.getPlayers()){
            p.resetSelectables();
        }

        Effect direzioneCard= new Effect(0, 2, 0, -1, 4, 1, 1, 2, 0, 2, 0);

        direzioneCard.start(c, m);

        foundByHand.clear();
        foundByHand.add(a);

        assertTrue(c.getSelectablePlayers().containsAll(foundByHand) && c.getSelectablePlayers().size()== 1);
        // direzioneCard.applyOn(c, c.getSelectablePlayers().get(0), null, m);


    }
}
