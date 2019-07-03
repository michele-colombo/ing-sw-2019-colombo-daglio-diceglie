package it.polimi.ingsw;

import it.polimi.ingsw.server.controller.ParserManager;
import it.polimi.ingsw.server.exceptions.ApplyEffectImmediatelyException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EffectTest {

    /**
     * Tests creation and application of a basic effect
     */
    @Test
    public void startTest(){
        Effect effect= new Effect(0, 1, -1, -1, -1, 1, 1, -1, -1, 0, 2, 2, -1);

        ParserManager pm= new ParserManager();
        Match m = new Match(pm.getLayout(0), 5, pm.getStackManager());
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

        Player sparatore= m.getCurrentPlayer();
        try {
            effect.start(sparatore, m);
        } catch (ApplyEffectImmediatelyException e){}

        assertTrue(sparatore.getSelectablePlayers().contains(gianni));

        effect.applyOn(sparatore, gianni, null, m);


        Effect opt= new Effect(0, 1, -1, -1, 1, 1, 0, -1, -1, 0, 1, 0, -1);
        try {
            opt.start(sparatore, m);
        } catch (ApplyEffectImmediatelyException e){}

        opt.applyOn(sparatore, null, null, m);
        assertEquals(1, m.getCurrentAction().getDamaged().size());
        assertTrue(m.getCurrentAction().getDamaged().contains(gianni));

        assertEquals(2, gianni.getDamageTrack().getDamageList().size());
        assertTrue(gianni.getDamageTrack().getDamageList().contains(anna));


        assertTrue(gianni.getDamageTrack().getMarkMap().containsKey(anna));
        assertEquals(2, gianni.getDamageTrack().getMarkMap().get(anna));

    }



}
