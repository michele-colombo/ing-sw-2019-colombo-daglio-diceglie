package it.polimi.ingsw.server.model;


import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Direction;

import java.io.*;
import java.util.*;


import static it.polimi.ingsw.server.model.enums.Border.*;
import static it.polimi.ingsw.server.model.enums.Direction.*;

/**
 * This class represents the layout of the game
 */
public class Layout {
    /**
     * Contains all squares of the selected layout; max size is 12
     */
    //private List<Square> squares;

    /**
     * Contains all the spawn squares of the selected layout,
     * size is always 3
     */
    private List<SpawnSquare> spawnSquares;

    /**
     * Contains all the ammo squares of the selected layout,
     * size is up to 9
     */
    private List<AmmoSquare> ammoSquares;

    /**
     * A matrix: if the square at coordinates x and y is in squares, then existingSquares[x][y] is 1, otherwise it's 0
     */
    private int[][] existingSquare;
    /**
     * It's the path of the json file in order to create a layout
     */
    private String jsonFileFolder;

    /**
     * an integer representing the chosen layoutConfiguration (used for saving)
     */
    private int layoutConfiguration;

    /**
     * Returns a list with all the squares of the layout, both of type {@link AmmoSquare} and {@link SpawnSquare}
     * @return cloned list with all squares
     */
    public List<Square> getSquares() {
        List <Square> squares = new ArrayList<>();
        squares.addAll(ammoSquares);
        squares.addAll(spawnSquares);
        return squares;
    }

    /**
     * Returns all the squares that are, at most, 3 movements far from startingSquare and, if it has door, also every square in the room
     * on the other side of the door and, in the end, it return the squares in the room of startingSquare
     * @param startingSquare The square from which calculates the visible ones; squares contains it
     * @return An ArrayList containing visible squares
     */
    public List<Square> getVisibleSquares(Square startingSquare){
        List<Square> visible = new ArrayList<>(startingSquare.getRoom().getSquaresInRoom());

        if(startingSquare.getNorth() == DOOR){
            visible.addAll(getSquare(startingSquare.getX(), startingSquare.getY() + 1).getRoom().getSquaresInRoom());
        }
        if(startingSquare.getEast() == DOOR){
            visible.addAll(getSquare(startingSquare.getX() + 1, startingSquare.getY()).getRoom().getSquaresInRoom());
        }
        if(startingSquare.getSouth() == DOOR){
            visible.addAll(getSquare(startingSquare.getX(), startingSquare.getY() - 1).getRoom().getSquaresInRoom());
        }
        if(startingSquare.getWest() == DOOR){
            visible.addAll(getSquare(startingSquare.getX() - 1, startingSquare.getY()).getRoom().getSquaresInRoom());
        }
        return visible;
    }

    /**
     * Returns all the squares that are in the specified direction, starting from startingSquare (included)
     * @param startingSquare The square from which calculates the direction
     * @param d Selected direction
     * @return An ArrayList containing the found squares
     */
    public List<Square> getSquaresInDirection(Square startingSquare, Direction d){
        List<Square> result = new ArrayList<>() ;
        if(d == NORTH){
            result.addAll(getNorthernSquares(startingSquare));
        }
        else if(d == EAST){
                result.addAll(getEasternSquares(startingSquare));
           }
        else if(d == SOUTH){
                result.addAll(getSouthernSquares(startingSquare));
        }
        else if(d == WEST){
                result.addAll(getWesternSquares(startingSquare));
        }
        result.add(startingSquare);
        return result;
    }

    /**
     * Returns all the squares in the direction specified by startingSquare (not included) and pointingSquare (included), if they're aligned or,
     * if they're overlapping, it returns the all square in the four cardinal direction, starting from startingSquare
     * @param startingSquare The first square from which calculate the direction
     * @param pointingSquare The second square from which calculate the direction
     * @return An ArrayList containing all the found squares; it could be empty
     */
    public List<Square> getSquaresInDirection(Square startingSquare, Square pointingSquare){
        if(startingSquare.equals(pointingSquare)){
            return getCardinalSquares(startingSquare);
        }
        if(startingSquare.getX() == pointingSquare.getX()){
            if(startingSquare.getY() > pointingSquare.getY()){
                return getSouthernSquares(startingSquare);
            }
            else{
                return getNorthernSquares(startingSquare);
            }
        }
        else if(startingSquare.getY() == pointingSquare.getY()){
            if(startingSquare.getX() > pointingSquare.getX()){
                return getWesternSquares(startingSquare);
            }
            else{
                return getEasternSquares(startingSquare);
            }
        }
        return new ArrayList<>();
    }

    /**
     * Returns all the squares whose distance, from startingSquare, is between min (included) and max (included)
     * @param startingSquare The square from which calculates the distance
     * @param min Lowest distance
     * @param max Highest distance
     * @return An ArrayList containing all the found squares; it could be empty
     */
    public List<Square> getSquaresInDistanceRange(Square startingSquare, int min, int max){
        List<Square> inRange = new ArrayList<>();
        for(Square sq : getSquares()){
            if((getDistance(startingSquare, sq) >= min) && (getDistance(startingSquare, sq) <= max)){
                inRange.add(sq);
            }
        }
        return inRange;
    }

    /**
     * Returns all the square whose second coordinate is y
     * @param y The selected horizontal line
     * @return An ArrayList containing all the found squares; it could be empty
     */
    public List<Square> getHorizontalSquareLine(int y){
        List<Square> squareLine = new ArrayList<>();
        for(Square sq : getSquares()){
            if(sq.getY() == y){
                squareLine.add(sq);
            }
        }
        return squareLine;
    }

    /**
     * Returns all the squares whose first or second coordinate are the same of startingSquare (included)
     * @param startingSquare The square with selected coordinates
     * @return An ArrayList with all the found squares
     */
    public List<Square> getCardinalSquares(Square startingSquare){
        List<Square> cardinalSquares = new ArrayList<>();
        cardinalSquares.addAll(getNorthernSquares(startingSquare));
        cardinalSquares.addAll(getEasternSquares(startingSquare));
        cardinalSquares.addAll(getSouthernSquares(startingSquare));
        cardinalSquares.addAll(getWesternSquares(startingSquare));
        cardinalSquares.add(startingSquare);
        return cardinalSquares;
    }

    /**
     * Returns all the squares whose first coordinate is x
     * @param x The selected vertical line
     * @return An ArrayList containing all the found squares; it could be empty
     */
    public List<Square> getVerticalSquareLine(int x){
        List<Square> squareLine = new ArrayList<>();
        for(Square sq : getSquares()){
            if(sq.getX() == x){
                squareLine.add(sq);
            }
        }
        return squareLine;
    }

    /**
     * Returns all the squares adjacent but reachable from startingSquare (not included)
     * @param startingSquare The selected square
     * @return An ArrayList with all the found squares;
     */
    public List<Square> getNeighbours(Square startingSquare){
        List<Square> neighbours = new ArrayList<>();
        if(startingSquare.getNorth() != WALL){
            neighbours.add(getSquare(startingSquare.getX(), startingSquare.getY() + 1));
        }
        if(startingSquare.getEast() != WALL){
            neighbours.add(getSquare(startingSquare.getX() + 1, startingSquare.getY()));
        }
        if(startingSquare.getSouth() != WALL){
            neighbours.add(getSquare(startingSquare.getX(), startingSquare.getY() - 1));
        }
        if(startingSquare.getWest() != WALL){
            neighbours.add(getSquare(startingSquare.getX() - 1, startingSquare.getY()));
        }
        return neighbours;
    }

    /**
     * Returns the distance between two squares
     * @param s1 The first selected square
     * @param s2 The second selected square
     * @return An int, representing the distance
     */
    public int getDistance(Square s1, Square s2){
        if(s1.equals(s2)){
            return 0;
        }
        else{
            int distance = 1; //starting distance
            List<Square> queue = getNeighbours(s1);
            List<Square> queuetmp = new ArrayList<>();
            while(!queue.contains(s2)){
                for(Square sq : queue){
                    if(sq != null){
                        queuetmp.addAll(getNeighbours(sq));
                    }
                }
                queue.addAll(queuetmp);
                distance++;
            }
            return distance;
        }
    }

    /**
     * Returns a reference to the square with x and y coordinates
     * @param x Selected coordinate x
     * @param y Selected coordinate y
     * @return The selected square: if the square doesn't exist, it's null;
     */
    public Square getSquare(int x, int y){
        for(Square s : getSquares()){
            if(s.getX() == x && s.getY() == y){
                return s;
            }
        }
        return null;
    }

    /**
     * Returns the SpawnSquare whose color is c
     * @param c The selected color
     * @return A SpawnSquare
     */
    public SpawnSquare getSpawnPoint(AmmoColor c){
        SpawnSquare spawn = null;
        for(SpawnSquare s : spawnSquares){
            if(s.getColor() == c){
                spawn = s;
            }
        }
        return spawn;
    }

    public List<AmmoSquare> getAmmoSquares(){
        return new ArrayList<>(ammoSquares);
    }

    /*/**
     * Returns all the squares that aren't empty (i.e. with ammo or weapons)
     * @return An ArrayList with all the found squares; it could be empty

    public List<Square> getNotEmptySquares(){
        List<Square> s = new ArrayList<>();
        for(Square sq : getSquares()){
            if(!sq.isEmpty()){
                s.add(sq);
            }
        }
        return s;
    }*/

    /**
     * Returns all the squares that are empty (i.e. without ammo or weapon)
     * @return An ArrayList with all the found squares; it could be empty
     */
    public List<Square> getEmptySquares(){
        List<Square> s = new ArrayList<>();
        for(Square sq : getSquares()){
            if(sq.isEmpty()){
                s.add(sq);
            }
        }
        return s;
    }

    /**
     * Returns all the squares that are more northern than startingSquare (not included)
     * @param startingSquare The selected square
     * @return An ArrayList containing all the found square; it could be empty
     */
    private List<Square> getNorthernSquares(Square startingSquare){
        List<Square> result = new ArrayList<>();
        int i = startingSquare.getY() + 1;
        while(i < 3){
            if(existSquare(startingSquare.getX(), i)){
                result.add(getSquare(startingSquare.getX(), i));
            }
            i++;
        }
            return result;
    }

    /**
     * Returns all the squares that are more eastern than startingSquare (not included)
     * @param startingSquare The selected square
     * @return An ArrayList containing all the found squares; it could be empty
     */
    private List<Square> getEasternSquares(Square startingSquare){
        List<Square> result = new ArrayList<>();
        int i = startingSquare.getX() + 1;
        while(i < 4){
            if(existSquare(i, startingSquare.getY())){
                result.add(getSquare(i, startingSquare.getY()));
            }
            i++;
        }
        return result;
    }

    /**
     * Returns all the squares that are more southern than startingSquare (not included)
     * @param startingSquare The selected square
     * @return An ArrayList containing all the found squares; it could be empty
     */
    private List<Square> getSouthernSquares(Square startingSquare){
        List<Square> result = new ArrayList<>();
        int i = startingSquare.getY() - 1;
        while(i > -1){
            if(existSquare(startingSquare.getX(), i)){
                result.add(getSquare(startingSquare.getX(), i));
            }
            i--;
        }
        return result;
    }

    /**
     * Returns all the squares that are more western than startingSquare (not included)
     * @param startingSquare The selected square
     * @return An ArrayList containing all the found squares; it could be empty
     */
    private List<Square> getWesternSquares(Square startingSquare){
        List<Square> result = new ArrayList<>();
        int i = startingSquare.getX() - 1;
        while(i > -1){
            if(existSquare(i, startingSquare.getY())){
                result.add(getSquare(i, startingSquare.getY()));
            }
            i--;
        }
        return result;
    }

    /**
     * Returns true if existingSquare[x][y] is 1, otherwise it returns false
     * @param x Coordinate x of the square
     * @param y Coordinate y of the square
     * @return A boolean
     */
    public boolean existSquare(int x, int y){
        if(existingSquare[x][y] == 1){
            return true;
        }
        return false;
    }

    /**
     * Build layout with passed ammoSquare and spawnSquares. Keep track of layoutConfiguration
     */
    public Layout(List<SpawnSquare> spawnSquares, List<AmmoSquare> ammoSquares, int layoutConfiguration) {
        this.spawnSquares = spawnSquares;
        this.ammoSquares = ammoSquares;
        this.layoutConfiguration= layoutConfiguration;

        existingSquare = new int[4][3];

        for(int i=0; i< this.existingSquare.length; i++){
            for(int j=0; j< this.existingSquare[0].length; j++){
                this.existingSquare[i][j]= 0;
            }
        }

        for (Square s : spawnSquares){
            existingSquare[s.getX()][s.getY()] = 1;
        }
        for (Square s : ammoSquares){
            existingSquare[s.getX()][s.getY()] = 1;
        }

    }

    public void instantiateRooms(){
        List<Square> squaresWithNoRoomYet= new ArrayList<>();

        squaresWithNoRoomYet.addAll(getSquares());

        while (!squaresWithNoRoomYet.isEmpty()){
            Square current= squaresWithNoRoomYet.get(0);
            Room roomToInstantiate= new Room();

            List<Square> found = new ArrayList<>();
            boolean finished= false;

            while(!finished){
                found.add(current);

                if(current.getNorth()== OPEN && !found.contains(this.getSquare(current.getX(), current.getY() + 1))){
                    current= this.getSquare(current.getX(), current.getY() + 1);
                }
                else if(current.getEast()== OPEN && !found.contains(this.getSquare(current.getX()+1, current.getY()))){
                    current= this.getSquare(current.getX()+1, current.getY());
                }
                else if(current.getSouth()== OPEN && !found.contains(this.getSquare(current.getX(), current.getY() - 1))){
                    current= this.getSquare(current.getX(), current.getY() - 1);
                }
                else if(current.getWest()== OPEN && !found.contains(this.getSquare(current.getX()-1, current.getY()))){
                    current= this.getSquare(current.getX()-1, current.getY());
                }
                else{
                    finished= true;
                }

            }

            for(Square s : found){
                s.setRoom(roomToInstantiate);
                roomToInstantiate.addSquare(s);
            }

            squaresWithNoRoomYet.removeAll(found);
        }

    }

    public void refillAll(StackManager sm){
        for (Square s : getSquares()){
            s.refill(sm);
        }
    }

    //added by Giuseppe Diceglie
    public List<Square> getFurtherSquares(Square startingSquare, int distMin){
        List<Square> result= new ArrayList<>();
        result.addAll( getSquares());
        if(distMin>0) {
            result.removeAll(getSquaresInDistanceRange(startingSquare, 0, distMin - 1));
        }

        return result;
    }

    public List<Square> getCloserSquares(Square startingSquare, int distMax){
        return getSquaresInDistanceRange(startingSquare, 0, distMax);
    }

    public int getLayoutConfiguration() {
        return layoutConfiguration;
    }

    public Square getSquareFromString(String square){
        for (Square s : getSquares()){
            if (s.toString().equals(square)){
                return s;
            }
        }
        return null;
    }
}

