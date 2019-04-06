package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private List<Square> squares;

    public Room(){
        squares = new ArrayList<>();
    }

    public void addSquare(Square s){
        squares.add(s);
    }

    public List<Square> getSquaresInRoom(){
        return new ArrayList<>(squares);
    }
}
