package it.polimi.ingsw;

import it.polimi.ingsw.server.controller.ParserManager;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.*;

import static it.polimi.ingsw.server.model.enums.Border.*;
import static it.polimi.ingsw.server.model.enums.AmmoColor.*;
import static org.junit.jupiter.api.Assertions.*;


public class LayoutTest {
    private Layout layout;
    private ParserManager pm= new ParserManager();

    /**
     * Prepare test
     */
    @BeforeEach
    public void prepareTest(){
        final int layout_config = 2;
        layout = pm.getLayout(layout_config);
    }

    /**
     * Test getVisibleSquare method
     */
    @Test
    public void getVisibleSquare() {
        final int X1 = 3;
        final int Y1 = 0;
        final int SIZE1 = 4;

        final int X2 = 2;
        final int Y2 = 1;
        final int SIZE2 = 7;

        final int X3 = 0;
        final int Y3 = 2;

        List<Square> visibleSquares = layout.getVisibleSquares(layout.getSquare(X1,Y1));
        assertEquals(SIZE1, visibleSquares.size());

        visibleSquares = layout.getVisibleSquares(layout.getSquare(X2, Y2));
        assertEquals(SIZE2, visibleSquares.size());
        assert(visibleSquares.contains(layout.getSquare(X3,Y3)));
    }

    /**
     * Test getSquaresInDirection with two different squares
     */
    @Test
    public void getSquaresInDirectionNotOverlapping() {
        final int X1 = 2;
        final int Y1 = 2;

        final int X2 = 1;
        final int Y2 = 2;

        List<Square> squaresInDirection = layout.getSquaresInDirection(layout.getSquare(X1, Y1), layout.getSquare(X2, Y2));
        assertEquals(2, squaresInDirection.size());
        assertFalse(squaresInDirection.contains(layout.getSquare(X1,Y1)));
    }

    /**
     * Test getSquaresInDirection method with North direction
     */
    @Test
    public void getSquaresInDirectionNorth(){
        final int X = 0;
        final int Y = 1;
        Direction D = Direction.NORTH;

        List<Square> north = layout.getSquaresInDirection(layout.getSquare(X, Y), D);
        assertEquals(2, north.size());
        assertTrue(north.contains(layout.getSquare(X, Y))); //the starting square is included

    }

    /**
     * Test getSquaresInDirection method with SOUTH direction
     */
    @Test
    public void getSquaresInDirectionSouth(){
        final int X = 0;
        final int Y = 1;
        Direction D = Direction.SOUTH;

        List<Square> south = layout.getSquaresInDirection(layout.getSquare(X, Y), D);
        assertEquals(1, south.size());
        assertTrue(south.contains(layout.getSquare(X, Y))); //the starting square is included
    }

    /**
     * Test getSquaresInDirection method with EAST direction
     */
    @Test
    public void getSquaresInDirectionEast(){
        final int X = 0;
        final int Y = 1;
        Direction D = Direction.EAST;

        List<Square> east = layout.getSquaresInDirection(layout.getSquare(X, Y), D);
        assertEquals(4, east.size());
        assertTrue(east.contains(layout.getSquare(X, Y))); //the starting square is included
    }

    /**
     * Test getSquaresInDirection method with WEST direction
     */
    @Test
    public void getSquaresInDirectionWest(){
        final int X = 0;
        final int Y = 1;
        Direction D = Direction.WEST;

        List<Square> west = layout.getSquaresInDirection(layout.getSquare(X, Y), D);
        assertEquals(1, west.size());
        assertTrue(west.contains(layout.getSquare(X, Y))); //the starting square is included
    }

    /**
     * Test getSquaresInDirection with the same square
     */
    @Test
    public void getSquaresInDirectionOverlapping(){
        final int X = 2;
        final int Y = 1;

        List<Square> squaresInDirection = layout.getSquaresInDirection(layout.getSquare(X,Y), layout.getSquare(X,Y));
        assertEquals(6, squaresInDirection.size());
        assertTrue(squaresInDirection.contains(layout.getSquare(X,Y)));
    }

    /**
     * Test getSquaresInDistanceRange method
     */
    @Test
    public void getSquaresInDistanceRange() {
        final int MIN1 = 1;
        final int MAX1 = 2;

        final int X1 = 0;
        final int Y1 = 2;

        final int X2 = 1;
        final int Y2 = 1;

        List<Square> square12 = layout.getSquaresInDistanceRange(layout.getSquare(X1, Y1), MIN1, MAX1);
        assertEquals(4, square12.size());
        assertTrue(square12.contains(layout.getSquare(X2,Y2)));
    }

    /**
     * Test getHorizontalSquareLine
     */
    @Test
    public void getHorizontalSquareLine() {
        final int Y1 = 0;
        final int Y2 = 1;
        final int Y3 = 2;

        List<Square> horizontalSquares = layout.getHorizontalSquareLine(Y1);
        assertEquals(3, horizontalSquares.size());

        horizontalSquares = layout.getHorizontalSquareLine(Y2);
        assertEquals(4, horizontalSquares.size());

        horizontalSquares = layout.getHorizontalSquareLine(Y3);
        assertEquals(4, horizontalSquares.size());
    }

    /**
     * Test getVerticalSquareLine method
     */
    @Test
    public void getVerticalSquareLine() {
        final int X1 = 0;
        final int X2 = 1;
        final int X3 = 2;
        final int X4 = 3;

        List<Square> verticalSquares = layout.getVerticalSquareLine(X1);
        assertEquals(2, verticalSquares.size());

        verticalSquares = layout.getVerticalSquareLine(X2);
        assertEquals(3, verticalSquares.size());

        verticalSquares = layout.getVerticalSquareLine(X3);
        assertEquals(3, verticalSquares.size());

        verticalSquares = layout.getVerticalSquareLine(X4);
        assertEquals(3, verticalSquares.size());
    }

    /**
     * Test getNeighbours method
     */
    @Test
    public void getNeighbours() {
        final int X1 = 2;
        final int Y1 = 1;

        final int X2 = 0;
        final int Y2 = 1;

        List<Square> neighbours = layout.getNeighbours(layout.getSquare(X1, Y1));
        assertEquals(3, neighbours.size());
        assertFalse(neighbours.contains(layout.getSquare(X1, Y1)));

        neighbours = layout.getNeighbours(layout.getSquare(X2, Y2));
        assertEquals(2, neighbours.size());
        assertFalse(neighbours.contains(layout.getSquare(X2, Y2)));
    }

    /**
     * Test getDistance method with two different squares
     */
    @Test
    public void getDistanceBetweenDifferentSquares() {
        final int X1 = 0;
        final int Y1 = 1;

        final int X2 = 3;
        final int Y2 = 0;

        int distance = layout.getDistance(layout.getSquare(X1, Y1), layout.getSquare(X2, Y2));
        assertEquals(4, distance);
    }

    /**
     * Test getDistance method with same square
     */
    @Test
    public void getDistanceBetweenSameSquares() {
        final int X1 = 0;
        final int Y1 = 1;

        int distance = layout.getDistance(layout.getSquare(X1, Y1), layout.getSquare(X1, Y1));
        assertEquals(0, distance);
    }

    /**
     * Test getSquare method when a square doesn't exists
     */
    @Test
    public void getSquareNull() {
        final int X = 0;
        final int Y = 0;

        Square s = layout.getSquare(X,Y);
        assertEquals(null, s);
    }

    /**
     * Test getSquare method when a square exists
     */
    @Test
    public void getSquareNotNull(){
        final int X = 2;
        final int Y = 0;

        Square s = layout.getSquare(X, Y);
        assertTrue(s.getX() == X && s.getY() == Y);
    }

    /**
     * Test getSpawnPoint method
     */
    @Test
    public void getSpawnPoint() {
        final int X1 = 2;
        final int Y1 = 2;

        final int X2 = 3;
        final int Y2 = 0;

        final int X3 = 0;
        final int Y3 = 1;


        Square blueSpawnPoint = layout.getSpawnPoint(BLUE);
        Square yellowSpawnPoint = layout.getSpawnPoint(YELLOW);
        Square redSpawnPoint = layout.getSpawnPoint(RED);

        assertTrue(blueSpawnPoint.getX() == X1 && blueSpawnPoint.getY() == Y1);
        assertTrue(yellowSpawnPoint.getX() == X2 && yellowSpawnPoint.getY() == Y2);
        assertTrue(redSpawnPoint.getX() == X3 && redSpawnPoint.getY() == Y3);
    }

    /**
     * Test existSquare Method
     */
    @Test
    public void existSquare(){
        final int X1 = 0;
        final int Y1 = 1;

        final int X2 = 0;
        final int Y2 = 0;

        assertTrue(layout.existSquare(X1, Y1));
        assertFalse(layout.existSquare(X2, Y2));
    }

    @Test
    public void initTest(){
        Layout layout= pm.getLayout(0);

        assertTrue( layout.getSquare(0,0).getColor() == AmmoColor.valueOf("WHITE"));
        assertTrue( layout.getDistance(layout.getSquare(0,0), layout.getSquare(1,2)) == 3);


        List<Square> vicini= new ArrayList<Square>();
        vicini.add(layout.getSquare(1,2));
        vicini.add(layout.getSquare(0,1));

        assertEquals(layout.getNeighbours(layout.getSquare(0,2)), vicini );

        layout = pm.getLayout(1);

        vicini.clear();
        vicini.add(layout.getSquare(1, 2));
        vicini.add(layout.getSquare(2, 1));
        vicini.add(layout.getSquare(1, 0));

        assertEquals(layout.getNeighbours(layout.getSquare(1,1)), vicini);


        assertTrue(layout.getSquare(3,1).getColor() == YELLOW);





    }

    @Test
    public void testConfig2and3(){
        Layout layout= pm.getLayout(2);

        layout.getSquare(0,3);
        //layout.getSquare(3,0);

        assertTrue(layout.getDistance(layout.getSquare(0,2), layout.getSquare(3,0)) == 5);

        assertTrue( layout.getSpawnPoint(BLUE).getX() == 2);
        assertTrue(layout.getSpawnPoint(BLUE).getY()== 2);

        assertTrue(layout.getHorizontalSquareLine(0).size()== 3);

        layout= pm.getLayout(3);

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


        Layout layout= pm.getLayout(3);

        assertTrue( layout.getSquare(0,1).getColor().toString() == "RED");

    }

    @Test
    public void whatHappenToRoom(){
        Layout l= pm.getLayout(3);



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
        Layout l= pm.getLayout(2);

        assertTrue( l.getVisibleSquares(l.getSquare(1,1)).contains(l.getSquare(1,0)));
        assertTrue( l.getVisibleSquares(l.getSquare(1,1)).contains(l.getSquare(0,1)));
        assertTrue(l.getVisibleSquares(l.getSquare(1,1)).contains(l.getSquare(1,1)));

        assertTrue( l.getVisibleSquares(l.getSquare(1,1)).size() == 3);

        assertTrue(l.getVisibleSquares(l.getSquare(3,2)).contains(l.getSquare(3,2)));
        assertTrue(l.getVisibleSquares(l.getSquare(3,2)).size() == 8);
    }

    @Test
    public void furthercloser(){
        Layout l= pm.getLayout(0);

        List<Square> trovatiAMano= new ArrayList<>();
        trovatiAMano.add(l.getSquare(2, 0));
        trovatiAMano.add(l.getSquare(3, 1));
        trovatiAMano.add(l.getSquare(3, 0));

        assertEquals(l.getCloserSquares(l.getSquare(3, 0), 1), trovatiAMano);

        trovatiAMano.clear();

        trovatiAMano.add(l.getSquare(0, 1));
        trovatiAMano.add(l.getSquare(3, 1));
        trovatiAMano.add(l.getSquare(3, 0));
        trovatiAMano.add(l.getSquare(2, 1));
        trovatiAMano.add(l.getSquare(3, 2));

        assertTrue(l.getFurtherSquares(l.getSquare(1, 1), 3).containsAll(trovatiAMano));
        assertTrue(trovatiAMano.containsAll( l.getFurtherSquares(l.getSquare(1, 1), 3)));

    }

    @Test
    public void visibleSquares(){
        Layout l= pm.getLayout(2);

        List<Square> trovatiAMano= new ArrayList<>();

        trovatiAMano.add(l.getSquare(0, 2));
        trovatiAMano.add(l.getSquare(1, 2));
        trovatiAMano.add(l.getSquare(2, 2));
        trovatiAMano.add(l.getSquare(0, 1));
        trovatiAMano.add(l.getSquare(1, 1));

        assertTrue(trovatiAMano.containsAll(l.getVisibleSquares(l.getSquare(0, 1))));
        assertTrue(l.getVisibleSquares(l.getSquare(0, 1)).containsAll(trovatiAMano));


        for(Square s : l.getVisibleSquares(l.getSquare(0, 1))){
            System.out.println(s.getX() + " " + s.getY());
        }
    }

    @Test
    public void distanceTest(){
        Layout l= pm.getLayout(3);

        assertEquals(l.getDistance(l.getSquare(1, 1), l.getSquare(3, 1)), 2);

        assertEquals(l.getSquaresInDistanceRange(l.getSquare(1, 1), 0, 2).size(), 8);
    }


}