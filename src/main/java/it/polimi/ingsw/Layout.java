package it.polimi.ingsw;


import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Border.DOOR;
import static it.polimi.ingsw.Border.WALL;
import static it.polimi.ingsw.Color.RED;
import static it.polimi.ingsw.Direction.*;

public class Layout {
    //private LayoutConfiguration configuration;
    private List<Square> squares;
    private int[][] existingSquare;

    public List<Square> getVisibleSquare(Square startingSquare){
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
        return squares;
    }

    public List<Square> getSquaresInDistanceRange(Square startingSquare, int min, int max){
        List<Square> inRange = new ArrayList<>();
        for(Square sq : squares){
            if(getDistance(startingSquare, sq) >= min && getDistance(startingSquare, sq) <= max && getVisibleSquare(startingSquare).contains(sq)){
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
            while(!queue.contains(s2)){
                for(Square sq : queue){
                    queue.addAll(getNeighbours(sq));
                }
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
        if(!existSquare(s.getX(), s.getY())){
            squares.add(s);
            existingSquare[s.getX()][s.getY()] = 1;
            return true;
        }
        return false;
    }

    private List<Square> getNorthernSquares(Square startingSquare){
        List<Square> squares = new ArrayList<>();
        int i = startingSquare.getY();
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
        int i = startingSquare.getX();
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
        int i = startingSquare.getY();
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
        int i = startingSquare.getX();
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
        squares = new ArrayList<>(16);
        existingSquare = new int[4][4];
        for(int row = 0; row < 4; row++){
            for(int column = 0; column < 4; column++){
                existingSquare[row][column] = 0;
            }
        }
    }
}
