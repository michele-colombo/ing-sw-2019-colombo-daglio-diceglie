package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.AmmoColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Is teh weapon object, it is made out of modes which contains elementary effects.
 */
public class Weapon{
    /**
     * the name of the weapon
     */
    private String name;

    /**
     * the whole cost of the weapon
     */
    private Cash cost;

    /**
     * the color of the weapon
     */
    private AmmoColor color;

    /**
     * modes of wich the weapon is composed
     */
    private List<Mode> myModes;

    /**
     * fake builder for ancient tests
     */
    public Weapon() {
    }

    /**
     * build a weapon with some of its charateristics
     * @param name
     * @param cost
     * @param color
     */
    public Weapon(String name, Cash cost, AmmoColor color) {
        this.name = name;
        this.cost = cost;
        this.color = color;
        myModes= new ArrayList<Mode>();
    }

    /**
     *
     * @return the modes of the weapon
     */
    public List<Mode> getMyModes() {
        return myModes;
    }

    /**
     *
     * @return the name of the weapon
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the color of the weapon
     */
    public AmmoColor getColor() {
        return color;
    }

    /**
     * it builds the entire description of the weapon (the text on the manual)
     * @return the weapon description
     */
    public String getDescription(){
        StringBuilder result= new StringBuilder();
        result.append(name.toUpperCase() + "\n");

        for(Mode m : myModes){
            result.append(m.getTitle() + ": " + m.getDescription() + "\n");
        }

        return result.toString();
    }

    /**
     * add a mode in myModes list. For ancient tests
     * @param modeToAdd
     */
    public void addMode(Mode modeToAdd){
        myModes.add(modeToAdd);
    }

    /**
     * calculate which mode can be selected
     * @param alreadySelected already selected modes
     * @return the selectable modes
     */
    public List<Mode> getSelectableModes(List<Mode> alreadySelected){
        List<Mode> result= new ArrayList<>();
        List<Mode> eligibles= new ArrayList<>();
        eligibles.addAll(this.myModes);

        boolean alreadMandatorySelected= false;

        for(Mode mod: alreadySelected){
            if(mod.isMandatory == true){
                alreadMandatorySelected= true;
            }
        }

        if(alreadMandatorySelected){
            for(Mode mod: this.myModes){
                if(mod.isMandatory){
                    eligibles.remove(mod);
                }
            }
        }

        for(Mode modeInWeapon: eligibles){
            if( modeInWeapon.getMustComeBefore() == -1 || alreadySelected.contains(myModes.get(modeInWeapon.getMustComeBefore()))){
                result.add(modeInWeapon);
            }
        }

        result.removeAll(alreadySelected);

        return result;
    }

    /**
     *
     * @return the cost to pay when picking up the card from a spawnPoint
     */
    public Cash getDiscountedCost() {
        return cost.subtract(new Cash(color, 1));
    }

    /**
     *
     * @return the whole cost of the weapon. It's the cost to reload it
     */
    public Cash getCost() {
        return cost;
    }
}
