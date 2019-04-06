package it.polimi.ingsw;

public abstract class Square {
    private int x;
    private int y;
    private Border nord;
    private Border est;
    private Border sud;
    private Border ovest;
    private Room room;
    private Color color;
    private boolean isAmmo;
    private boolean isEmpty;

    public Square(){;}

    public abstract boolean isAmmo();

    abstract void collect(Player p);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Border getNord() {
        return nord;
    }

    public Border getEst() {
        return est;
    }

    public Border getSud() {
        return sud;
    }

    public Border getOvest() {
        return ovest;
    }

    public Room getRoom() {
        return room;
    }

    public Color getColor() {
        return color;
    }

    public boolean isEmpty(){
        return isEmpty;
    }
}
