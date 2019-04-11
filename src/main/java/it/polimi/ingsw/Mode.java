package it.polimi.ingsw;

import java.util.List;

public class Mode {
    private Mode mustComeBefore;
    private String title;
    private String description;

    private List<Effect> effects;

    public List<Effect> getEffects() {
        return effects;
    }

    public Mode(String title, String description){
        this.title= title;
        this.description= description;
        mustComeBefore= null;
    }

    public Mode(Mode mustComeBefore, String title, String description) {
        this.mustComeBefore = mustComeBefore;
        this.title = title;
        this.description = description;
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
}
