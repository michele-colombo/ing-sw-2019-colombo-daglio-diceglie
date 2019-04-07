package it.polimi.ingsw;

public class SpawnSquare extends Square{

    public void collect(Player p){
        ;
    }

    public SpawnSquare(int x, int y, Border north, Border east, Border south, Border west, Color color){
        super(x, y, north, east, south, west, color);
        isAmmo = false;
    }
}
