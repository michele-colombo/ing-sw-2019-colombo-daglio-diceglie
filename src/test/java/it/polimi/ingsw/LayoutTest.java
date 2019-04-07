package it.polimi.ingsw;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Border.*;
import static it.polimi.ingsw.Color.*;
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

        List<Square> empty = (layout.getVisibleSquare(testSquare));
        assertEquals(2, empty.size());
    }

    @Test
    public void getSquaresInDirection() {
    }

    @Test
    public void getSquaresInDistanceRange() {
    }

    @Test
    public void getHorizontalSquareLine() {
    }

    @Test
    public void getVerticalSquareLine() {
    }

    @Test
    public void getNeighbours() {
    }

    @Test
    public void getDistance() {
    }

    @Test
    public void getSquare() {
        Layout layout = new Layout();
        Square s = layout.getSquare(0,0);
        assertTrue(s == null);

        layout.addSquare(new SpawnSquare(0, 0, DOOR, DOOR, WALL, WALL, RED));
        s = layout.getSquare(0,0);
        assertTrue(s.getX() == 0 && s.getY() == 0);
    }

    @Test
    public void getSpawnPoint() {
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

        /*layout = new Layout();
        for(int row = 0; row < 4; row++){
            for(int column = 0; column < 4; column++){
                layout.addSquare(new SpawnSquare(row, column, DOOR, DOOR, WALL, WALL, RED));
            }
        }
        addOk = layout.addSquare(new SpawnSquare(0, 0, DOOR, DOOR, WALL, WALL, RED));
        assertEquals(false, addOk);*/
    }

    @Test
    public void existSquare(){
        Layout layout = new Layout();
        layout.addSquare(new SpawnSquare(0, 0, DOOR, DOOR, WALL, WALL, RED));
        assertEquals(true, layout.existSquare(0, 0));
        layout.addSquare(new SpawnSquare(2, 2, DOOR, DOOR, WALL, WALL, RED));
        assertEquals(true, layout.existSquare(2,2));
    }
}