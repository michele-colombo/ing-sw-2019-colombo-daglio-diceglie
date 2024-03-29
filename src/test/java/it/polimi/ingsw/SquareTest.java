package it.polimi.ingsw;

import com.google.gson.*;

import it.polimi.ingsw.server.model.AmmoSquare;
import it.polimi.ingsw.server.model.SpawnSquare;
import it.polimi.ingsw.server.model.Square;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.enums.Border.*;
import static it.polimi.ingsw.server.model.enums.AmmoColor.RED;
import static org.junit.jupiter.api.Assertions.*;


public class SquareTest {
    /**
     * Really simple test to get used to gson.
     * Further tests on square in gameModelTest and layoutTest
     */
    @Test
    public void testgson() {

        Gson g = new Gson();

        List<Square> listaAmmo= new ArrayList<>();
        List<Square> listaRitornata= new ArrayList<>();

        Square sq = new AmmoSquare(2, 4, WALL, OPEN, DOOR, OPEN, RED);
        Square sq2= new AmmoSquare(0, 0, OPEN, OPEN, OPEN, OPEN, RED);

        Square spawn= new SpawnSquare(4, 6, WALL, OPEN, WALL, WALL, RED);

        listaAmmo.add(sq);
        listaAmmo.add(sq2);

        assertEquals(g.fromJson(g.toJson(sq), AmmoSquare.class), sq);
        assertEquals(g.fromJson(g.toJson(sq2), AmmoSquare.class), sq2);
        assertEquals(g.fromJson(g.toJson(spawn), SpawnSquare.class), spawn);



    }
}
