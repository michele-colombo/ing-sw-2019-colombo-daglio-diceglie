package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class SpawnSquare extends Square{
    private List<Weapon> weapons;

    public SpawnSquare(int x, int y, Border north, Border east, Border south, Border west, Color color){
        super(x, y, north, east, south, west, color);
        isAmmo = false;
        weapons = new ArrayList<>();
    }

    @Override
    public boolean collect(Player p, Match m){
        m.getCurrentAction().setCurrSpawnSquare(this);
        p.setState(PlayerState.GRAB_WEAPON);
        p.resetSelectables();
        p.setSelectableWeapons(weapons);    //TODO: what if there are no weapons?
        return false;
    }

    public void addWeapon(Weapon w){
        weapons.add(w);
    }

    public void removeWeapon(Weapon w){
        weapons.remove(w);
    }

    public List<Weapon> getWeapons(){
        return weapons;
    }

    @Override
    public boolean isEmpty(){       //tells if there isn't anything collectable
        return weapons.isEmpty();
    }

    @Override
    public void refill (StackManager s){
        while (weapons.size() < 3){
            Weapon tempWeapon = s.drawWeapon();
            if (tempWeapon != null){
                weapons.add(tempWeapon);
            }
        }
    }
}
