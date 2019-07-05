package it.polimi.ingsw.client.model_view;

import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.AmmoColor;

import java.util.List;

/**
 * Represents a weapon on client
 */
public class WeaponView {
    /**
     * String used when showing buying cost
     */
    private static final String BUYING_COST = " (buying cost: ";
    /**
     * String used when showing cost
     */
    private static final String COST = " (cost: ";
    /**
     * String used when showing end of cost
     */
    private static final String END_BUYING_COST = ")";
    /**
     * Used to get discounted price
     */
    private static final int DISCOUNTED_PRICE = 1;
    /**
     * Name of the WeaponView
     */
    private String name;
    /**
     * Costs (in ammos) of the WeaponView
     */
    private Cash cost;
    /**
     * Color ot the WeaponView
     */
    private AmmoColor color;
    /**
     * ModeViews of the WeaponViews
     */
    private List<ModeView> myModes;
    /**
     * Image name
     */
    private String imageName;

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return cost
     */
    public Cash getCost() {
        return cost;
    }

    /**
     *
     * @return color
     */
    public AmmoColor getColor() {
        return color;
    }

    /**
     *
     * @return myModes
     */
    public List<ModeView> getMyModes() {
        return myModes;
    }

    /**
     *
     * @return discounted cost (i.e. cost less first ammos of color of WeaponView)
     */
    public Cash getDiscountedCost() {
        return cost.subtract(new Cash(color, DISCOUNTED_PRICE));
    }

    /**
     * Gets image name
     * @return image name
     */
    public String getImageName() {
        return imageName;
    }

    /**
     *
     * @return String description of this WeaponView, containing name, buying cost, full cost and
     * titles and description of her ModeViews
     */
    public String getDescription(){
        StringBuilder result= new StringBuilder();
        result.append(name.toUpperCase());
        result.append(BUYING_COST + getDiscountedCost()+ END_BUYING_COST);
        result.append(COST+getCost()+")\n");
        for(ModeView m : myModes){
            result.append(m.getTitle() + ": " + m.getDescription() + "\n");
        }

        return result.toString();
    }

    /**
     *
     * @param str title of searched ModeView
     * @return ModeView corresponding to given str
     */
    public ModeView getModeFromString(String str){
        for (ModeView m : myModes){
            if (m.getTitle().equals(str)){
                return m;
            }
        }
        return null;
    }
}
