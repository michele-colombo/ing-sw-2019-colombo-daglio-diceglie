package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Weapon{
    private String name;
    private boolean isLoaded;
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

        isLoaded= true;
        discountedCost= cost.subtract(new Cash(color, 1));

        myModes= new ArrayList<Mode>();
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

    public boolean isLoaded() {
        return isLoaded;
    }

    public void Reload(boolean loaded) {
        isLoaded = true;
    }

    public List<Mode> getSelectableModes(List<Mode> alreadySelected){
        return null;
    }
}
