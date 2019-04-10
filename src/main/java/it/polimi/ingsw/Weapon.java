package it.polimi.ingsw;

import java.util.List;

public class Weapon{
    private String name;
    private String description;
    private boolean isLoaded;
    private Cash cost;
    private Cash discountedCost;
    private Color color;

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
