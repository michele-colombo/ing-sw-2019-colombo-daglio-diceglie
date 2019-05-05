package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Weapon{
    private String name;
    private Cash cost;
    private Color color;

    private List<Mode> myModes;

    public Weapon() {
    }

    public Weapon(String name, Cash cost, Color color) {
        this.name = name;
        this.cost = cost;
        this.color = color;
        myModes= new ArrayList<Mode>();
    }

    public List<Mode> getMyModes() {
        return myModes;
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

    public String getDeepDescription(){
        StringBuilder result= new StringBuilder();

        result.append(name.toUpperCase()).append("---").append(getCost().toString()).append("\n");
        for(Mode m : myModes){
            result.append(m.getTitle()).append(": ").append(m.getDescription()).append("-------").append(m.getCost().toString()).append("\n");
            int i=1;
            for(Effect e: m.getEffects()){
                result.append("EFF" + i + ":\n");
                result.append(e.humanString() + "\n");
                i++;
            }
            result.append("\n");
        }

        return result.toString();

    }



    public void addMode(Mode modeToAdd){
        myModes.add(modeToAdd);
    }

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

    public Cash getDiscountedCost() {
        return cost.subtract(new Cash(color, 1));
    }

    public Cash getCost() {
        return cost;
    }
}
