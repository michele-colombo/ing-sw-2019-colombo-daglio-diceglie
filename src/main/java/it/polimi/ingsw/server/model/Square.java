package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Border;

/**
 * Is the abstract class of squares, which completely describes it as object of the map.
 * Collection (in the sense of grabbing on it) is left to concrete extensions.
 */
public abstract class Square {
    protected int x;
    protected int y;
    protected Border north;
    protected Border east;
    protected Border south;
    protected Border west;
    protected Room room;
    protected AmmoColor color;

    public abstract boolean collect(Player p, Match m);

    public abstract void refill(StackManager s);

    public abstract boolean isEmpty();

    public int getX() { return x; }

    public int getY() {
        return y;
    }

    public Border getNorth() {
        return north;
    }

    public Border getEast() {
        return east;
    }

    public Border getSouth() {
        return south;
    }

    public Border getWest() {
        return west;
    }

    public Room getRoom() {
        return room;
    }

    public AmmoColor getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Square)) return false;
        Square square = (Square) o;
        return getX() == square.getX() &&
                getY() == square.getY();
    }

    @Override
    public int hashCode() {
        return 10 * getY() + getY();
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Constructor with all parameters.
     * @param x x coordinate of square
     * @param y y coordinate of square
     * @param north type of border at north
     * @param east type of border at east
     * @param south type of border at south
     * @param west type of border at west
     * @param color color of the square
     */
    public Square(int x, int y, Border north, Border east, Border south, Border west, AmmoColor color){
        this.x = x;
        this.y = y;
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
        this.color = color;
        this.room = null;
    }

    public Square(){

    }

    public String getFullDescription(){
        StringBuilder result= new StringBuilder();
        result.append("Square "+color.toString()+" in "+x+","+y+"\n");
        result.append("north: "+north.toString());
        result.append(" | east: "+east.toString());
        result.append(" | south: "+south.toString());
        result.append(" | west: "+west.toString()+"\n");
        return result.toString();
    }

    public String getShortDescription(){
        return "Square ("+x+","+y+") ";
    }

    @Override
    public String toString() {
        return "("+x+"-"+y+")";
    }
}
