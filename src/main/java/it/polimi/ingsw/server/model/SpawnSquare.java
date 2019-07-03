package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.Border;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerState;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends square and represents the spawn points, which also contains weapons to collect
 */
public class SpawnSquare extends Square{
    /**
     * the list of weapons in the spawn point
     */
    private List<Weapon> weapons;

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
    public SpawnSquare(int x, int y, Border north, Border east, Border south, Border west, AmmoColor color){
        super(x, y, north, east, south, west, color);
        weapons = new ArrayList<>();
    }

    /**
     * Builds the square using the default constructor and initialize the weapon list
     */
    public SpawnSquare(){
        super();
        weapons = new ArrayList<>();
    }

    /**
     * Gets a description of the content of the square
     * @return a string with description (contains \n)
     */
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

    /**
     * Puts the affordable weapons of the square in the selectable items of the player.
     * If it cannot afford anyone, the BACK is activated
     * @param p the player which is collecting
     * @param m the current match
     * @return false, because micoaction is not concluded
     */
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

    /**
     * Adds a weapon to the list of weapons of the spawn square.
     * Maximum number of weapons checked by the caller
     * @param w the weapon to add
     */
    public void addWeapon(Weapon w){
        weapons.add(w);
    }

    /**
     * Removes a weapon from the spawn point
     * @param w the weapons to remove
     */
    public void removeWeapon(Weapon w){
        weapons.remove(w);
    }

    /**
     * Gets all the weapons of the spawn point
     * @return the list of all the weapons
     */
    public List<Weapon> getWeapons(){
        return weapons;
    }

    /**
     * Removes all the weapons from the spawn point
     */
    public void clearWeapons(){
        weapons.clear();
    }

    /**
     * Checks if there is at least one weapon in the spawn point
     * @return true if the spawn point is empty
     */
    @Override
    public boolean isEmpty(){       //tells if there isn't anything collectable
        return weapons.isEmpty();
    }

    /**
     * Refills the spawn point with new weapons (if there are)
     * @param s the stack manager from which draw the weapons
     */
    @Override
    public void refill (StackManager s){
        boolean emptyStack= false;

        while (weapons.size() < 3 && !emptyStack){
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
