package it.polimi.ingsw;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import static it.polimi.ingsw.Border.*;
import static it.polimi.ingsw.Color.*;
import static it.polimi.ingsw.Direction.EAST;
import static it.polimi.ingsw.Direction.WEST;
import static org.junit.Assert.*;

public class LayoutTest {

    @Test
    public void getVisibleSquare() {
        Layout layout = new Layout();
        Square testSquare = new SpawnSquare(1, 1, WALL, DOOR, WALL, WALL, RED);
        Square eastDoor = new SpawnSquare(2, 1, DOOR, DOOR, DOOR, DOOR, RED);
        layout.addSquare(testSquare);
        layout.addSquare(eastDoor);

        Room room1 = new Room();
        room1.addSquare(testSquare);
        testSquare.setRoom(room1);

        Room room2 = new Room();
        room2.addSquare(eastDoor);
        eastDoor.setRoom(room2);

        List<Square> empty = (layout.getVisibleSquares(testSquare));
        assertEquals(2, empty.size());
    }

    @Test
    public void getSquaresInDirection() {
        Layout layout = new Layout();
        Square startingSquare = new SpawnSquare(2,2, WALL, WALL, WALL, WALL, RED);
        layout.addSquare(startingSquare);
        layout.addSquare(new SpawnSquare(3,2, WALL, WALL, WALL, WALL, RED));
        assertEquals(true , layout.existSquare(3,2));
        assertEquals(2, layout.getSquaresInDirection(startingSquare, EAST).size());
        assertEquals(1, layout.getSquaresInDirection(startingSquare, WEST).size());
    }

    @Test
    public void getSquaresInDistanceRange() {
        Layout layout = new Layout();

        Square startingSquare = new SpawnSquare(1,1, WALL, WALL, WALL, DOOR, RED);
        layout.addSquare(startingSquare);
        Room startingRoom = new Room();
        startingRoom.addSquare(startingSquare);
        startingSquare.setRoom(startingRoom);
        assertEquals(0, layout.getSquaresInDistanceRange(startingSquare, 2, 3).size());

        Square s1 = new SpawnSquare(0,1, WALL, DOOR, DOOR, WALL, RED);
        Room room1 = new Room();
        room1.addSquare(s1);
        s1.setRoom(room1);

        Square s2 = new SpawnSquare(0,0, DOOR, DOOR, WALL, WALL, RED);
        room1.addSquare(s2);
        s2.setRoom(room1);

        Square s3 = new SpawnSquare(1,0, WALL, WALL, WALL, DOOR, RED);
        Room room2 = new Room();
        room2.addSquare(s3);
        s3.setRoom(room2);

        layout.addSquare(s1);
        layout.addSquare(s2);
        layout.addSquare(s3);
        assertEquals(2, layout.getSquaresInDistanceRange(startingSquare, 2, 4).size());
        assertEquals(1, layout.getSquaresInDistanceRange(startingSquare, 1, 1).size());
    }

    @Test
    public void getSquaresInDistanceOnLayout2(){
        Layout layout = new Layout();
        layout.initLayout(2);

        Square centre = layout.getSquare(2,1);
        List<Square> expected = new ArrayList<>();
        expected.add(layout.getSquare(2,1));
        expected.add(layout.getSquare(1,2));
        expected.add(layout.getSquare(2,2));
        expected.add(layout.getSquare(3,2));
        expected.add(layout.getSquare(3,1));
        expected.add(layout.getSquare(1,0));
        expected.add(layout.getSquare(2,0));
        expected.add(layout.getSquare(3,0));

        assertTrue(expected.containsAll(layout.getSquaresInDistanceRange(centre, 0,2)));
        assertTrue(layout.getSquaresInDistanceRange(centre, 0,2).containsAll(expected));
    }

    @Test
    public void getHorizontalSquareLine() {
        Layout layout = new Layout();
        assertEquals(0, layout.getHorizontalSquareLine(2).size());

        layout.addSquare(new SpawnSquare(1, 1, WALL, WALL, WALL, WALL, BLUE));
        layout.addSquare(new SpawnSquare(2, 1, WALL, WALL, WALL, WALL, BLUE));
        layout.addSquare(new SpawnSquare(0, 1, WALL, WALL, WALL, WALL, BLUE));
        assertEquals(3, layout.getHorizontalSquareLine(1).size());

        layout.addSquare(new SpawnSquare(2, 2, WALL, WALL, WALL, WALL, BLUE));
        assertEquals(3, layout.getHorizontalSquareLine(1).size());
        assertEquals(1, layout.getHorizontalSquareLine(2).size());
    }

    @Test
    public void getVerticalSquareLine() {
        Layout layout = new Layout();
        assertEquals(0, layout.getVerticalSquareLine(2).size());

        layout.addSquare(new SpawnSquare(1, 0, WALL, WALL, WALL, WALL, BLUE));
        layout.addSquare(new SpawnSquare(1, 1, WALL, WALL, WALL, WALL, BLUE));
        layout.addSquare(new SpawnSquare(1, 2, WALL, WALL, WALL, WALL, BLUE));
        assertEquals(3, layout.getVerticalSquareLine(1).size());

        layout.addSquare(new SpawnSquare(2, 2, WALL, WALL, WALL, WALL, BLUE));
        assertEquals(3, layout.getVerticalSquareLine(1).size());
        assertEquals(1, layout.getVerticalSquareLine(2).size());
    }

    @Test
    public void getNeighbours() {
        Layout layout = new Layout();
        Square startingSquare = new SpawnSquare(0,0, DOOR, WALL, WALL, WALL, RED);

        assertEquals(1, layout.getNeighbours(startingSquare).size());

        Square startingSquare2 = new SpawnSquare(0,1, WALL, DOOR, DOOR, WALL, RED);

        layout.addSquare(startingSquare);
        layout.addSquare(startingSquare2);
        layout.addSquare(new SpawnSquare(1,1, DOOR, WALL, WALL, DOOR, RED));

        assertEquals(1, layout.getNeighbours(startingSquare).size());
        assertEquals(2, layout.getNeighbours(startingSquare2).size());

        Square aloneSquare = new SpawnSquare(2,2, WALL, WALL, WALL, WALL, RED);
        layout.addSquare(aloneSquare);
        assertEquals(0, layout.getNeighbours(aloneSquare).size());
    }

    @Test
    public void getDistance() {
        Layout layout = new Layout();

        Square startingSquare = new SpawnSquare(0,0, WALL, DOOR, WALL, WALL, RED);

        layout.addSquare(new SpawnSquare(1,0, DOOR, WALL, WALL, DOOR, RED));
        layout.addSquare(new SpawnSquare(1,1, WALL, DOOR, DOOR, WALL, RED));
        layout.addSquare(new SpawnSquare(2,1, WALL, DOOR, DOOR, DOOR, RED));
        layout.addSquare(new SpawnSquare(3,1, WALL, WALL, WALL, DOOR, RED));

        Square endingSquare = new SpawnSquare(2,0, DOOR, WALL, WALL, WALL, RED);
        layout.addSquare(endingSquare);

        assertEquals(4, layout.getDistance(startingSquare, endingSquare));
        assertEquals(0, layout.getDistance(startingSquare, startingSquare));
    }

    @Test
    public void getSquare() {
        Layout layout = new Layout();
        Square s = layout.getSquare(0,0);
        assertTrue(s == null);

        s = new SpawnSquare(0, 0, DOOR, DOOR, WALL, WALL, RED);
        layout.addSquare(s);
        assertEquals(s, layout.getSquare(0,0));
    }

    @Test
    public void getSpawnPoint() {
        Layout layout = new Layout();

        assertEquals(null, layout.getSpawnPoint(RED));

        layout.addSquare(new AmmoSquare(0,0, WALL, WALL, WALL, WALL, RED));
        assertEquals(null, layout.getSpawnPoint(RED));

        layout.addSquare(new SpawnSquare(1,1, WALL, WALL, WALL, WALL, RED));
        assertEquals(null, layout.getSpawnPoint(BLUE));

        SpawnSquare blueSpawn = new SpawnSquare(2, 2, WALL, WALL, WALL, WALL, BLUE);
        layout.addSquare(blueSpawn);
        assertEquals(blueSpawn, layout.getSpawnPoint(BLUE));
    }

    @Test
    public void getNotEmptySquares() {
    }

    @Test
    public void addSquare() {
        Layout layout = new Layout();
        boolean addOk = layout.addSquare(new SpawnSquare(0, 0, DOOR, DOOR, WALL, WALL, RED));
        boolean addNotOk = layout.addSquare(new SpawnSquare(0, 0, DOOR, OPEN, WALL, WALL, RED));
        boolean addOk2 = layout.addSquare(new SpawnSquare(2, 1, DOOR, DOOR, DOOR, DOOR, RED));

        assertEquals(true, addOk);
        assertEquals(false, addNotOk);
        assertEquals(true, addOk2);

        layout = new Layout();
        for(int row = 0; row < 4; row++){
            for(int column = 0; column < 3; column++){
                layout.addSquare(new SpawnSquare(row, column, DOOR, DOOR, WALL, WALL, RED));
            }
        }
        addOk = layout.addSquare(new SpawnSquare(0, 0, DOOR, DOOR, WALL, WALL, RED));
        assertEquals(false, addOk);
    }

    @Test
    public void existSquare(){
        Layout layout = new Layout();
        assertEquals(false, layout.existSquare(0,0));
        layout.addSquare(new SpawnSquare(0, 0, DOOR, DOOR, WALL, WALL, RED));
        assertEquals(true, layout.existSquare(0, 0));
        layout.addSquare(new SpawnSquare(2, 2, DOOR, DOOR, WALL, WALL, RED));
        assertEquals(true, layout.existSquare(2,2));
    }

    @Test
    public void initTest(){
        Layout layout= new Layout("/media/data/Media/Documents/Universita/AnnoIII/ProvaFinaleINGSW/Backups/");
        layout.initLayout(0);

        assertTrue( layout.getSquare(0,0).getColor() == Color.valueOf("WHITE"));
        assertTrue( layout.getDistance(layout.getSquare(0,0), layout.getSquare(1,2)) == 3);


        List<Square> vicini= new ArrayList<Square>();
        vicini.add(layout.getSquare(1,2));
        vicini.add(layout.getSquare(0,1));

        assertEquals(layout.getNeighbours(layout.getSquare(0,2)), vicini );

        layout.initLayout(1);

        vicini.clear();
        vicini.add(layout.getSquare(1, 2));
        vicini.add(layout.getSquare(2, 1));
        vicini.add(layout.getSquare(1, 0));

        assertEquals(layout.getNeighbours(layout.getSquare(1,1)), vicini);


        assertTrue(layout.getSquare(3,1).getColor() == YELLOW);





    }

    @Test
    public void testConfig2e3(){
        Layout layout= new Layout("/media/data/Media/Documents/Universita/AnnoIII/ProvaFinaleINGSW/Backups/");

        layout.initLayout(2);

        layout.getSquare(0,3);
        //layout.getSquare(3,0);

        assertTrue(layout.getDistance(layout.getSquare(0,2), layout.getSquare(3,0)) == 5);

        assertTrue( layout.getSpawnPoint(BLUE).getX() == 2);
        assertTrue(layout.getSpawnPoint(BLUE).getY()== 2);

        assertTrue(layout.getHorizontalSquareLine(0).size()== 3);

        layout.initLayout(3);

        assertTrue( layout.getSpawnPoint(RED).getX() == 0);
        assertTrue( layout.getSpawnPoint(RED).getY() == 1);


        assertTrue(layout.getDistance(layout.getSquare(1,2), layout.getSquare(2,1)) == 2);

        List<Square> temporanea= new ArrayList<>();

        temporanea.add(layout.getSquare(1,2));
        temporanea.add(layout.getSquare(1,1));
        temporanea.add(layout.getSquare(1,0));
        temporanea.add(layout.getSquare(2,0));
        temporanea.add(layout.getSquare(3,0));

        assertTrue(layout.getCardinalSquares(layout.getSquare(1,0)).containsAll(temporanea));
        assertTrue(temporanea.containsAll(layout.getCardinalSquares(layout.getSquare(1,0))));


    }

    @Test
    public void provareGetClassResources(){


        Layout layout= new Layout();
        layout.initLayout(3);

        assertTrue( layout.getSquare(0,1).getColor().toString() == "RED");

    }

    @Test
    public void whatHappenToRoom(){
        Layout l= new Layout();
        l.initLayout(3);



        assertTrue(l.getSquare(3,1).getRoom() == l.getSquare(3,0).getRoom());

        assertTrue(l.getSquare(0,2).getRoom() == l.getSquare(1,2).getRoom());
        assertTrue(l.getSquare(0,2).getRoom() == l.getSquare(2,2).getRoom());

        assertTrue(l.getSquare(0,1).getRoom() == l.getSquare(1,1).getRoom());
        assertTrue(l.getSquare(0,1).getRoom() == l.getSquare(2,1).getRoom());


        assertTrue(l.getSquare(1,0).getRoom() == l.getSquare(2,0).getRoom());

        assertFalse(l.getSquare(3,1).getRoom() == l.getSquare(1,2).getRoom() );

        assertFalse(l.getSquare(0,2).getRoom() == l.getSquare(1,1).getRoom());

        assertFalse(l.getSquare(0,1).getRoom() == l.getSquare(2,0).getRoom());





    }

    @Test
    public void testVisibility(){
        Layout l= new Layout();
        l.initLayout(2);

        assertTrue( l.getVisibleSquares(l.getSquare(1,1)).contains(l.getSquare(1,0)));
        assertTrue( l.getVisibleSquares(l.getSquare(1,1)).contains(l.getSquare(0,1)));
        assertTrue(l.getVisibleSquares(l.getSquare(1,1)).contains(l.getSquare(1,1)));

        assertTrue( l.getVisibleSquares(l.getSquare(1,1)).size() == 3);

        assertTrue(l.getVisibleSquares(l.getSquare(3,2)).contains(l.getSquare(3,2)));
        assertTrue(l.getVisibleSquares(l.getSquare(3,2)).size() == 8);
    }
}