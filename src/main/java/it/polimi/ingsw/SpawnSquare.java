package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class SpawnSquare extends Square{
    private List<Weapon> weapons;

    public SpawnSquare(int x, int y, Border north, Border east, Border south, Border west, Color color){
        super(x, y, north, east, south, west, color);
        isAmmo = false;
    }

    @Override
    public void collect(Player p, StackManager s){
        p.setSelectableWeapons(weapons);
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

    @Override
    public boolean isEmpty(){       //tells if there isn't anything collectable
        return weapons.isEmpty();
    }

    @Override
    public void refill (StackManager s){
        if (weapons.size() < 3){
            Weapon tempWeapon = s.drawWeapon();
            if (tempWeapon != null){
                weapons.add(tempWeapon);
            }
        }
    }
}
