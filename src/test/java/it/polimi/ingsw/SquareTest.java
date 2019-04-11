package it.polimi.ingsw;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.Border.*;
import static it.polimi.ingsw.Color.RED;
import static org.junit.Assert.*;


public class SquareTest {
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
