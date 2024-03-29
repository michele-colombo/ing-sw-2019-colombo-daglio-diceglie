package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a mode of a weapon. It is composed of some elementary effects.
 */
public class Mode {
    /**
     * index of the mode that must come before this. -1 if this is a standalone mode
     */
    private int mustComeBefore;

    /**
     * titel of the mode
     */
    private String title;

    /**
     * description of this mode
     */
    private String description;

    /**
     * cost of this mode
     */
    private Cash cost;

    /**
     * true if it you cannot shoot without selecting it or another mandatory mode
     */
    public final boolean isMandatory;

    /**
     * list of effects this mode applies
     */
    private List<Effect> effects;

    /**
     * get a clone of the effect list
     * @return list of effects
     */
    public List<Effect> getEffects() {
        return new ArrayList<>(effects);
    }

    /**
     * build a mode without its effects
     * @param isMandatory mandatory field
     * @param mustComeBefore index of mode that has to be selected before this
     * @param title title of the mode
     * @param description description of the mode
     * @param cost cost of the mode
     */
    public Mode(boolean isMandatory, int mustComeBefore, String title, String description, Cash cost){
        this.isMandatory= isMandatory;
        this.mustComeBefore= mustComeBefore;
        this.title= title;
        this.description= description;
        this.cost= cost;

        effects= new ArrayList<>();
    }


    /**
     * set a mode that must come before it
     * @param mustComeBefore index of mode that must come before this to be selectable
     */
    public void setPrecedent(int mustComeBefore){
        this.mustComeBefore= mustComeBefore;
    }

    /**
     * title setter
     * @param title title of the mode
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * description setter
     * @param description description of the mode
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * cost setter
     * @param cost cost of the mode
     */
    public void setCost(Cash cost) {
        this.cost = cost;
    }

    /**
     * add an effect in the list
     * @param effect effect to add
     */
    public void addEffect(Effect effect){
        this.effects.add(effect);
    }

    /**
     *
     * @return title of this mode
     */
    public String getTitle(){
        return title;
    }

    /**
     *
     * @return description of this mode
     */
    public String getDescription(){
        return description;
    }

    /**
     *
     * @return cost of this mode
     */
    public Cash getCost() {
        return cost;
    }

    /**
     *
     * @return the index of mode that must come before
     */
    public int getMustComeBefore() {
        return mustComeBefore;
    }
}
