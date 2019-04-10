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




        JsonObject obj= new JsonObject();
        JsonParser parser= new JsonParser();
        JsonArray array= new JsonArray();

        JsonElement e= new JsonObject();

/*

        try {
            BufferedWriter w = new BufferedWriter(new FileWriter("/home/giuseppe/square.txt"));
            w.write(g.toJson(listaAmmo));
            w.close();
        } catch (IOException inscrittura) {
        }

        */

        try{
            BufferedReader r = new BufferedReader(new FileReader("/home/giuseppe/square.txt"));
            Square[] temp= new AmmoSquare[40];
            temp= g.fromJson(r.readLine(), AmmoSquare[].class);
            listaRitornata= Arrays.asList(temp);
        } catch (IOException inlettura) {
        }

        assertEquals(listaAmmo, listaRitornata);













    }
}
