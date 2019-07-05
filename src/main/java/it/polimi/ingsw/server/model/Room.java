package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    /**
     * list of room's squares
     */
    private List<Square> squares;

    /**
     * builds a room
     */
    public Room(){
        squares = new ArrayList<>();
    }

    /**
     * add squares to room
     * @param s square to add
     */
    public void addSquare(Square s){
        squares.add(s);
    }

    /**
     * get all squares in room
     * @return list of squares in this room
     */
    public List<Square> getSquaresInRoom(){
        return new ArrayList<>(squares);
    }
}
