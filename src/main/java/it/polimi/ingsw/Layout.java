package it.polimi.ingsw;


import com.google.gson.Gson;

import javax.security.auth.login.Configuration;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Logger;

import static it.polimi.ingsw.Border.*;
import static it.polimi.ingsw.Direction.*;

/**
 * This class represents the layout of the game
 */
public class Layout {
    /**
     * Contains all squares of the selected layout; max size is 12
     */
    private List<Square> squares;
    /**
     * A matrix: if the square at coordinates x and y is in squares, then existingSquares[x][y] is 1, otherwise it's 0
     */
    private int[][] existingSquare;
    /**
     * It's the path of the json file in order to create a layout
     */
    private String jsonFileFolder;

    public List<Square> getSquares() {
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
        List<Square> squares = new ArrayList<>() ;
        if(d == NORTH){
            squares.addAll(getNorthernSquares(startingSquare));
        }
        else if(d == EAST){
                squares.addAll(getEasternSquares(startingSquare));
           }
        else if(d == SOUTH){
                squares.addAll(getSouthernSquares(startingSquare));
        }
        else if(d == WEST){
                squares.addAll(getWesternSquares(startingSquare));
        }
        squares.add(startingSquare);
        return squares;
    }

    /**
     * Returns all the squares in the direction specified by startingSquare (not included) and pointingSquare (included), if they're aligned or,
     * if they're overlapping, it returns the all square in the four cardinal direction, starting from startingSquare
     * @param startingSquare The first square from which calculate the direction
     * @param pointingSquare The second square from which calculate the direction
     * @return An ArrayList containing all the found squares; it could be empty
     */
    //TODO controllare se startingSquare sia o no da ritornare
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
                return getEasternSquares(startingSquare);
            }
            else{
                return getWesternSquares(startingSquare);
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
        for(Square sq : squares){
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
        for(Square sq : squares){
            if(sq.getY() == y){
                squareLine.add(sq);
            }
        }
        return squareLine;
    }

    /**
     * Returns all the squares whose first or second coordinate are the same of startingSquare (not included)
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
        for(Square sq : squares){
            if(sq.getX() == x){
                squareLine.add(sq);
            }
        }
        return squareLine;
    }

    /**
     * Returns all the squares adjacent to startingSquare
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
        Square found = null;
        for(Square s : squares){
            if(s.getX() == x && s.getY() == y){
                return s;
            }
        }
        return found;
    }

    /**
     * Returns the SpawnSquare whose color is c
     * @param c The selected color
     * @return A SpawnSquare
     */
    public SpawnSquare getSpawnPoint(Color c){
        SpawnSquare spawn = null;
        for(Square s : squares){
            if(!s.isAmmo() && s.getColor() == c){
                spawn = (SpawnSquare) s;
            }
        }
        return spawn;
    }

    /**
     * Returns all the squares that aren't empty (i.e. with ammo or weapons)
     * @return An ArrayList with all the found squares; it could be empty
     */
    public List<Square> getNotEmptySquares(){
        List<Square> s = new ArrayList<>();
        for(Square sq : squares){
            if(!sq.isEmpty()){
                s.add(sq);
            }
        }
        return s;
    }

    /**
     * Returns all the squares that are empty (i.e. without ammo or weapon)
     * @return An ArrayList with all the found squares; it could be empty
     */
    public List<Square> getEmptySquares(){
        List<Square> s = new ArrayList<>();
        for(Square sq : squares){
            if(sq.isEmpty()){
                s.add(sq);
            }
        }
        return s;
    }

    /**
     * Return true if the square is added, otherwise false
     * @param s It's the square to be added
     * @return A boolean
     */
    public boolean addSquare(Square s){
        if(!existSquare(s.getX(), s.getY()) && squares.size() < 12){
            squares.add(s);
            existingSquare[s.getX()][s.getY()] = 1;
            return true;
        }
        return false;
    }

    /**
     * Returns all the squares that are more northern than startingSquare (not included)
     * @param startingSquare The selected square
     * @return An ArrayList containing all the found square; it could be empty
     */
    private List<Square> getNorthernSquares(Square startingSquare){
        List<Square> squares = new ArrayList<>();
        int i = startingSquare.getY() + 1;
        while(i < 3){
            if(existSquare(startingSquare.getX(), i)){
                squares.add(getSquare(startingSquare.getX(), i));
            }
            i++;
        }
            return squares;
    }

    /**
     * Returns all the squares that are more eastern than startingSquare (not included)
     * @param startingSquare The selected square
     * @return An ArrayList containing all the found squares; it could be empty
     */
    private List<Square> getEasternSquares(Square startingSquare){
        List<Square> squares = new ArrayList<>();
        int i = startingSquare.getX() + 1;
        while(i < 4){
            if(existSquare(i, startingSquare.getY())){
                squares.add(getSquare(i, startingSquare.getY()));
            }
            i++;
        }
        return squares;
    }

    /**
     * Returns all the squares that are more southern than startingSquare (not included)
     * @param startingSquare The selected square
     * @return An ArrayList containing all the found squares; it could be empty
     */
    private List<Square> getSouthernSquares(Square startingSquare){
        List<Square> squares = new ArrayList<>();
        int i = startingSquare.getY() - 1;
        while(i > -1){
            if(existSquare(startingSquare.getX(), i)){
                squares.add(getSquare(startingSquare.getX(), i));
            }
            i--;
        }
        return squares;
    }

    /**
     * Returns all the squares that are more western than startingSquare (not included)
     * @param startingSquare The selected square
     * @return An ArrayList containing all the found squares; it could be empty
     */
    private List<Square> getWesternSquares(Square startingSquare){
        List<Square> squares = new ArrayList<>();
        int i = startingSquare.getX() - 1;
        while(i > -1){
            if(existSquare(i, startingSquare.getY())){
                squares.add(getSquare(i, startingSquare.getY()));
            }
            i--;
        }
        return squares;
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
     * Create square as a new ArrayList and set every cell of existingSquare to zero
     */
    public Layout(){
        squares = new ArrayList<>();
        existingSquare = new int[4][3];
        for(int row = 0; row < 4; row++){
            for(int column = 0; column < 3; column++){
                existingSquare[row][column] = 0;
            }
        }
    }

    /**
     * Create square as a new ArrayList, set every cell of existingSquare to zero and set this.jsonFileFolder to jsonFileFolder
     * @param jsonFileFolder It's the path of the json file in order to create a layout
     */
    public Layout(String jsonFileFolder){
        squares = new ArrayList<>();
        existingSquare = new int[4][3];
        for(int row = 0; row < 4; row++){
            for(int column = 0; column < 3; column++){
                existingSquare[row][column] = 0;
            }
        }
        this.jsonFileFolder= new String(jsonFileFolder);
    }


    /**
     * le configurazioni del tabellone rifereite al manuale di gioco sono:
     * -0 == piccola "ottima per 4 o 5 giocatori"
     * -1 == quella grande che e' disegnata su entrambe le pagine
     * -2 == piccola "ottima per qualsiasi numero di giocatori
     * -3 == picoola "ottima per 3 o 4 giocatori"
     *
     * @param config configuration code
     * @return true if the configuration exists, false otherwise
     */
    public boolean initLayout(int config){

        Gson gson= new Gson();

        List<Square> ammoSquares= new ArrayList<>();
        List<Square> spawnSquares= new ArrayList<>();


        String configFilePath;

        if(config < 0 || config>3){
            return false;
        }


        File file= new File(getClass().getClassLoader().getResource("layoutConfig/layoutConfig" + config + ".json").getFile());




        try (Scanner sc = new Scanner(file)){
            Square[] tempAmmo;
            Square[] tempSpawn;



            tempAmmo = gson.fromJson(sc.nextLine(), AmmoSquare[].class);
            ammoSquares.addAll(Arrays.asList(tempAmmo));

            tempSpawn = gson.fromJson(sc.nextLine(), SpawnSquare[].class);
            spawnSquares.addAll(Arrays.asList(tempSpawn));

            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();

        }



        this.squares.clear();
        for(int i=0; i< this.existingSquare.length; i++){
            for(int j=0; j< this.existingSquare[0].length; j++){
                 this.existingSquare[i][j]= 0;
            }
        }


        for(Square s : ammoSquares){
            addSquare(s);
        }
        for(Square s : spawnSquares){
            addSquare(s);
        }

        instantiateRooms();





        return true;


    }

    private void instantiateRooms(){
        List<Square> squaresWithNoRoomYet= new ArrayList<>();

        squaresWithNoRoomYet.addAll(squares);

        while (!squaresWithNoRoomYet.isEmpty()){
            Square current= squaresWithNoRoomYet.get(0);
            Room roomToInstantiate= new Room();

            List<Square> found = new ArrayList<Square>();
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
        for (Square s : squares){
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



    }

