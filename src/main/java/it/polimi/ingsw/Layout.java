package it.polimi.ingsw;


import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Border.DOOR;
import static it.polimi.ingsw.Direction.*;

public class Layout {
    private LayoutConfiguration configuration;
    private List<Square> squares;
    private int[][] existingSquare;

    public List<Square> getVisibileSquare(Square startingSquare){
        List<Square> visible = new ArrayList<>(startingSquare.getRoom().getSquaresInRoom());

        try{
            if(startingSquare.getNord() == DOOR){
                visible.addAll(getSquare(startingSquare.getX(), startingSquare.getY() + 1).getRoom().getSquaresInRoom());
            }
            if(startingSquare.getEst() == DOOR){
                visible.addAll(getSquare(startingSquare.getX() + 1, startingSquare.getY()).getRoom().getSquaresInRoom());
            }
            if(startingSquare.getSud() == DOOR){
                visible.addAll(getSquare(startingSquare.getX(), startingSquare.getY() - 1).getRoom().getSquaresInRoom());
            }
            if(startingSquare.getOvest() == DOOR){
                visible.addAll(getSquare(startingSquare.getX() - 1, startingSquare.getY()).getRoom().getSquaresInRoom());
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return visible;
    }

    public List<Square> getSquaresInDirection(Square startingSquare, Direction d){
        List<Square> squares = new ArrayList<>() ;
        if(d == NORD){
            squares.addAll(getNorthernSquares(startingSquare));
        }
        else if(d == EST){
                squares.addAll(getEasternSquares(startingSquare));
           }
        else if(d == SUD){
                squares.addAll(getSouthernSquares(startingSquare));
        }
        else if(d == OVEST){
                squares.addAll(getWesternSquares(startingSquare));
        }
        return squares;
    }

    public List<Square> getSquaresInDistanceRange(Square startingSquare, int min, int max){
        List<Square> inRange = new ArrayList<>();
        for(Square sq : squares){
            if(getDistance(startingSquare, sq) >= min && getDistance(startingSquare, sq) <= max && getVisibileSquare(startingSquare).contains(sq)){
                inRange.add(sq);
            }
        }
        return inRange;
    }

    public int getDistance(Square s1, Square s2){
        return Math.abs(s1.getX() - s2.getX()) + Math.abs(s1.getY() - s2.getY());
    }

    public Square getSquare(int x, int y) throws Exception{
        for(Square s : squares){
            if(s.getX() == x && s.getY() == y){
                return s;
            }
        }
        throw new Exception();
    }

    public Square getSpawnPoint(Color c) throws Exception{
        for(Square s : squares){
            if(s.isAmmo() && s.getColor() == c){
                return s;
            }
        }
        throw new Exception();
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

    private List<Square> getNorthernSquares(Square startingSquare){
        List<Square> squares = new ArrayList<>();
        int i = startingSquare.getY();
        try{
            while(i < 4){
                squares.add(getSquare(startingSquare.getX(), i));
                i++;
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            return squares;
        }
    }

    private List<Square> getEasternSquares(Square startingSquare){
        List<Square> squares = new ArrayList<>();
        int i = startingSquare.getX();
        try{
            while(i < 4){
                squares.add(getSquare(i, startingSquare.getY()));
                i++;
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            return squares;
        }
    }

    private List<Square> getSouthernSquares(Square startingSquare){
        List<Square> squares = new ArrayList<>();
        int i = startingSquare.getY();
        try{
            while(i > -1){
                squares.add(getSquare(startingSquare.getX(), i));
                i--;
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            return squares;
        }
    }

    private List<Square> getWesternSquares(Square startingSquare){
        List<Square> squares = new ArrayList<>();
        int i = startingSquare.getX();
        try{
            while(i > -1){
                squares.add(getSquare(i, startingSquare.getY()));
                i--;
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            return squares;
        }
    }
}
