package it.polimi.ingsw;

import org.junit.Test;

import java.util.List;

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
        assertEquals(1, layout.getSquaresInDistanceRange(startingSquare, 2, 4).size());
        assertEquals(1, layout.getSquaresInDistanceRange(startingSquare, 1, 1).size());
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

        Square aloneSquare = new SpawnSquare(3,3, WALL, WALL, WALL, WALL, RED);
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

        Square blueSpawn = new SpawnSquare(2, 2, WALL, WALL, WALL, WALL, BLUE);
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
        boolean addOk2 = layout.addSquare(new SpawnSquare(3, 1, DOOR, DOOR, DOOR, DOOR, RED));

        assertEquals(true, addOk);
        assertEquals(false, addNotOk);
        assertEquals(true, addOk2);

        layout = new Layout();
        for(int row = 0; row < 4; row++){
            for(int column = 0; column < 4; column++){
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
}