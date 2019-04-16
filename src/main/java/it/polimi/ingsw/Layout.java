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

public class Layout {

    private List<Square> squares;
    private int[][] existingSquare;

    private String jsonFileFolder;

    public List<Square> getSquares() {
        return squares;
    }

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

    public List<Square> getSquaresInDistanceRange(Square startingSquare, int min, int max){
        List<Square> inRange = new ArrayList<>();
        for(Square sq : squares){
            if((getDistance(startingSquare, sq) >= min) && (getDistance(startingSquare, sq) <= max) && getVisibleSquares(startingSquare).contains(sq)){
                inRange.add(sq);
            }
        }
        return inRange;
    }

    public List<Square> getHorizontalSquareLine(int y){
        List<Square> squareLine = new ArrayList<>();
        for(Square sq : squares){
            if(sq.getY() == y){
                squareLine.add(sq);
            }
        }
        return squareLine;
    }

    public List<Square> getCardinalSquares(Square startingSquare){
        List<Square> cardinalSquares = new ArrayList<>();
        cardinalSquares.addAll(getNorthernSquares(startingSquare));
        cardinalSquares.addAll(getEasternSquares(startingSquare));
        cardinalSquares.addAll(getSouthernSquares(startingSquare));
        cardinalSquares.addAll(getWesternSquares(startingSquare));
        cardinalSquares.add(startingSquare);
        return cardinalSquares;
    }

    public List<Square> getVerticalSquareLine(int x){
        List<Square> squareLine = new ArrayList<>();
        for(Square sq : squares){
            if(sq.getX() == x){
                squareLine.add(sq);
            }
        }
        return squareLine;
    }

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

    public Square getSquare(int x, int y){
        Square found = null;
        for(Square s : squares){
            if(s.getX() == x && s.getY() == y){
                return s;
            }
        }
        return found;
    }

    public Square getSpawnPoint(Color c){
        Square spawn = null;
        for(Square s : squares){
            if(!s.isAmmo() && s.getColor() == c){
                spawn = s;
            }
        }
        return spawn;
    }

    public List<Square> getNotEmptySquares(){
        List<Square> s = new ArrayList<>();
        for(Square sq : squares){
            if(sq.isEmpty()){
                s.add(sq);
            }
        }
        return s;

    }

    public boolean addSquare(Square s){
        if(!existSquare(s.getX(), s.getY()) && squares.size() < 16){
            squares.add(s);
            existingSquare[s.getX()][s.getY()] = 1;
            return true;
        }
        return false;
    }

    private List<Square> getNorthernSquares(Square startingSquare){
        List<Square> squares = new ArrayList<>();
        int i = startingSquare.getY() + 1;
        while(i < 4){
            if(existSquare(startingSquare.getX(), i)){
                squares.add(getSquare(startingSquare.getX(), i));
            }
            i++;
        }
            return squares;
    }

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

    public boolean existSquare(int x, int y){
        if(existingSquare[x][y] == 1){
            return true;
        }
        return false;
    }

    public Layout(){
        squares = new ArrayList<>();
        existingSquare = new int[4][4];
        for(int row = 0; row < 4; row++){
            for(int column = 0; column < 4; column++){
                existingSquare[row][column] = 0;
            }
        }
    }

    public Layout(String jsonFileFolder){
        squares = new ArrayList<>();
        existingSquare = new int[4][4];
        for(int row = 0; row < 4; row++){
            for(int column = 0; column < 4; column++){
                existingSquare[row][column] = 0;
            }
        }
        this.jsonFileFolder= new String(jsonFileFolder);
    }


    /**
     * le configurazioni del tabellone rifereite al manuale di gioco sono:
     * -0 == piccola "ottima per 4 o 5 giocatori"
     * -1 == quella grande che e' disegnata su entrambe le pagine
     * -1 == piccola "ottima per qualsiasi numero di giocatori
     * -1 == picoola "ottima per 3 o 4 giocatori"
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



    }

