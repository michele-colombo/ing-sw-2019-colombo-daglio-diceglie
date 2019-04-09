package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class SpawnSquare extends Square{
    private List<Weapon> weapons;
    public void collect(Player p){
        ;
    }

    public void addWeapon(Weapon w){
        weapons.add(w);
    }

    public void removeWeapon(Weapon w){
        weapons.remove(w);
    }

    public void getWeapons(){
        List<Weapon> w = new ArrayList<>(weapons);
    }

    public boolean isEmpty(){
        if(weapons.size() == 0){
            return true;
        }
        return false;
    }

    public SpawnSquare(int x, int y, Border north, Border east, Border south, Border west, Color color){
        super(x, y, north, east, south, west, color);
        isAmmo = false;
    }
}
