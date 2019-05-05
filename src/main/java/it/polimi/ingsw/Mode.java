package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Mode {
    private int mustComeBefore;
    private String title;
    private String description;
    private Cash cost;

    public boolean isMandatory;

    private List<Effect> effects;

    public List<Effect> getEffects() {
        return effects;
    }

    public Mode(boolean isMandatory, int mustComeBefore, String title, String description, Cash cost){
        this.isMandatory= isMandatory;
        this.mustComeBefore= mustComeBefore;
        this.title= title;
        this.description= description;
        this.cost= cost;

        effects= new ArrayList<>();
    }




    public void setPrecedent(int mustComeBefore){
        this.mustComeBefore= mustComeBefore;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(Cash cost) {
        this.cost = cost;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public void addEffect(Effect effect){
        this.effects.add(effect);
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public Cash getCost() {
        return cost;
    }

    public int getMustComeBefore() {
        return mustComeBefore;
    }
}
