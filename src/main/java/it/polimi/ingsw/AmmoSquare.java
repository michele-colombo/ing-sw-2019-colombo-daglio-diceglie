package it.polimi.ingsw;

public class AmmoSquare extends Square{

    public void collect(Player p){
        ;
    }

    public AmmoSquare(int x, int y, Border north, Border east, Border south, Border west, Color color){
        super(x, y, north, east, south, west, color);
        isAmmo = true;
    }
}
