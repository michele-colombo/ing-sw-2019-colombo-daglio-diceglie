package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.Border;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerState;

import java.util.ArrayList;
import java.util.List;

public class SpawnSquare extends Square{
    private List<Weapon> weapons;

    public SpawnSquare(int x, int y, Border north, Border east, Border south, Border west, AmmoColor color){
        super(x, y, north, east, south, west, color);
        weapons = new ArrayList<>();
    }

    public SpawnSquare(){
        super();
        weapons = new ArrayList<>();
    }

    @Override
    public String getFullDescription(){
        StringBuilder result= new StringBuilder();
        result.append("Spawn");
        result.append(super.getFullDescription());
        if (!isEmpty()){
            result.append("Weapons available: \n");
            for(Weapon w : weapons){
                result.append("- "+w.getName()+"\n");
            }
        } else {
            result.append("no weapons available here\n");
        }
        return result.toString();
    }

    @Override
    public boolean collect(Player p, Match m){
        m.getCurrentAction().setCurrSpawnSquare(this);
        p.setState(PlayerState.GRAB_WEAPON);
        p.resetSelectables();
        List<Weapon> weaponsToAdd = new ArrayList<>();
        for (Weapon w : weapons){
            if (p.canAfford(w.getDiscountedCost())){
                weaponsToAdd.add(w);
            }
        }
        if (weaponsToAdd.isEmpty()){
            p.setSelectableCommands(Command.BACK);
        } else {
            p.setSelectableWeapons(weaponsToAdd);
        }
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

    public void clearWeapons(){
        weapons.clear();
    }

    @Override
    public boolean isEmpty(){       //tells if there isn't anything collectable
        return weapons.isEmpty();
    }

    @Override
    public void refill (StackManager s){
        boolean emptyStack= false;

        while (weapons.size() < 3 && emptyStack==false){
            Weapon tempWeapon = s.drawWeapon();
            if (tempWeapon != null){
                weapons.add(tempWeapon);
            }
            else {
                emptyStack= true;
            }
        }
    }
}
