package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.ApplyEffectImmediatelyException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EffectTest {

    @Test
    private Match inizializza(){
        Match m = new Match(0, 5);
        Layout layout= m.getLayout();

        Player anna= new Player("Anna", PlayerColor.BLUE);
        Player gianni= new Player("Gianni", PlayerColor.GREEN);
        Player nino= new Player("Nino", PlayerColor.VIOLET);
        Player luca= new Player("Luca", PlayerColor.GREY);
        Player gigi= new Player("Gigi", PlayerColor.YELLOW);

        m.addPlayer(anna);
        m.addPlayer(gianni);
        m.addPlayer(nino);
        m.addPlayer(luca);
        m.addPlayer(gigi);

        anna.setSquarePosition(layout.getSquare(0, 0));
        gianni.setSquarePosition(layout.getSquare(0, 2));
        nino.setSquarePosition(layout.getSquare(1, 2));
        luca.setSquarePosition(layout.getSquare(2, 0));
        gigi.setSquarePosition(layout.getSquare(3, 2));


        m.setCurrentPlayer(anna);
        m.setCurrentAction(new Action(false, false));

        return m;
    }

    @Test
    private String selectableLists(Match m){
        String result;
        result= "SELECTABLE PLAYERS\n";

        for(Player selp: m.getCurrentPlayer().getSelectablePlayers()) {
            result+= selp.getName() + "\n";
        }
        result+= "SELECTABLE SQUARES\n";
        for(Square sq: m.getCurrentPlayer().getSelectableSquares()){
            result+= sq.getX() + ";" + sq.getY() + "\n";
        }

        result+= "SELECTABLE COMMANDS";
        for(Command selc: m.getCurrentPlayer().getSelectableCommands()){
            result+= selc.toString() + "\n";
        }

        return result;


    }

    @Test
    private String damaged(Match m){
        String res= "";
        for(int i=0; i< m.getCurrentAction().getDamaged().size(); i++){
            res+= m.getCurrentAction().getDamaged().get(i).getName() + "\n";
        }

        return res;

    }

    @Test
    public void startTest(){
        Effect effect= new Effect(0, 1, -1, -1, -1, 1, 1, -1, -1, 0, 2, 2, -1);
        Match m= inizializza();

        Player sparatore= m.getCurrentPlayer();
        try {
            effect.start(sparatore, m);
        } catch (ApplyEffectImmediatelyException e){}

        System.out.println(selectableLists(m));

        Player gianni= m.getCurrentPlayer().getSelectablePlayers().get(0);
        effect.applyOn(sparatore, gianni, null, m);

        System.out.println(damaged(m));

        Effect opt= new Effect(0, 1, -1, -1, 1, 1, 0, -1, -1, 0, 1, 0, -1);
        try {
            opt.start(sparatore, m);
        } catch (ApplyEffectImmediatelyException e){}
        System.out.println(selectableLists(m));

        opt.applyOn(sparatore, null, null, m);
        System.out.println(damaged(m));

        System.out.println("Danni");
        for(Player p: gianni.getDamageTrack().getDamageList()){
            System.out.println(p.getName());
        }

        System.out.println("Marchi");
        for(Player p: gianni.getDamageTrack().getMarkMap().keySet()){
            System.out.println(p.getName());
            System.out.println(gianni.getDamageTrack().getMarkMap().get(p));
        }




    }



}
