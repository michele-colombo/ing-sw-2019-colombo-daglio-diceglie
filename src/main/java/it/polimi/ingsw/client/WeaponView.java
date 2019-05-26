package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.AmmoColor;

import java.util.List;

public class WeaponView {
    private String name;
    private Cash cost;
    private AmmoColor color;
    private List<ModeView> myModes;
    private String imageName;

    public String getName() {
        return name;
    }

    public Cash getCost() {
        return cost;
    }

    public AmmoColor getColor() {
        return color;
    }

    public List<ModeView> getMyModes() {
        return myModes;
    }

    public Cash getDiscountedCost() {
        return cost.subtract(new Cash(color, 1));
    }

    public String getImageName() {
        return imageName;
    }

    //TEST
    public String getDescription(){
        StringBuilder result= new StringBuilder();
        result.append(name.toUpperCase());
        result.append(" (buying cost: " + getDiscountedCost()+")");
        result.append(" (cost: "+getCost()+")\n");
        for(ModeView m : myModes){
            result.append(m.getTitle() + ": " + m.getDescription() + "\n");
        }

        return result.toString();
    }

    public ModeView getModeFromString(String str){
        for (ModeView m : myModes){
            if (m.getTitle().equals(str)){
                return m;
            }
        }
        return null;
    }
}
