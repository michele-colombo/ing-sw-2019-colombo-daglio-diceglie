package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Weapon{
    private String name;
    private Cash cost;
    private Cash discountedCost;
    private Color color;

    List<Mode> myModes;

    public Weapon() {
    }

    public Weapon(String name, Cash cost, Color color) {
        this.name = name;
        this.cost = cost;
        this.color = color;
        discountedCost= cost.subtract(new Cash(color, 1));
        myModes= new ArrayList<Mode>();
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public String getDescription(){
        StringBuilder result= new StringBuilder();
        result.append(name.toUpperCase() + "\n");

        for(Mode m : myModes){
            result.append(m.getTitle() + ": " + m.getDescription() + "\n");
        }

        return result.toString();
    }

    public void addMode(Mode modeToAdd){
        myModes.add(modeToAdd);
    }

    public List<Mode> getSelectableModes(List<Mode> alreadySelected){
        List<Mode> result= new ArrayList<>();
        for(Mode modeInWeapon: this.myModes){
            if(modeInWeapon.getMustComeBefore() == null || alreadySelected.contains(modeInWeapon.getMustComeBefore())){
                result.add(modeInWeapon);
            }
        }

        result.removeAll(alreadySelected);

        return result;
    }

    public Cash getDiscountedCost() {
        return discountedCost;
    }

    public Cash getCost() {
        return cost;
    }
}
