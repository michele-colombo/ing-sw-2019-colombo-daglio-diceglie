package it.polimi.ingsw;

import java.util.List;

public class Mode {
    private Mode mustComeBefore;
    private String title;
    private String description;
    private Cash cost;

    private List<Effect> effects;

    public List<Effect> getEffects() {
        return effects;
    }

    public Mode(String title, String description){
        this.title= title;
        this.description= description;
        mustComeBefore= null;
        this.cost = new Cash(0,0,0);
    }

    public Mode(Mode mustComeBefore, String title, String description) {
        this.mustComeBefore = mustComeBefore;
        this.title = title;
        this.description = description;
        this.cost = new Cash(0,0,0);
    }

    public Mode(Mode mustComeBefore, String title, String description, Cash cost) {
        this.mustComeBefore = mustComeBefore;
        this.title = title;
        this.description = description;
        this.cost = cost;
    }

    public void setPrecedent(Mode mustComeBefore){
        this.mustComeBefore= mustComeBefore;
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
}
