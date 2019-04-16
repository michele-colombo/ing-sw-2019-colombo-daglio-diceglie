package it.polimi.ingsw;

public abstract class Square {
    protected int x;
    protected int y;
    protected Border north;
    protected Border east;
    protected Border south;
    protected Border west;
    protected Room room;
    protected Color color;
    protected boolean isAmmo;

    public boolean isAmmo(){
        return isAmmo;
    }

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

    public Color getColor() {
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

    public void setRoom(Room room) {
        this.room = room;
    }

    public Square(int x, int y, Border north, Border east, Border south, Border west, Color color){
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
}
