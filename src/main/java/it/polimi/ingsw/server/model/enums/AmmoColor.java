package it.polimi.ingsw.server.model.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Colors of ammos and squares.
 * Note that ammos can be only blue, red and yellow, but are in fact the same blue, red and yellow of spawn squares
 */
public enum AmmoColor {
    BLUE,
    YELLOW,
    RED,
    WHITE,
    PURPLE,
    GREEN;

    /**
     * Gets the colors of ammos
     * @return a list of colors
     */
    public static List<AmmoColor> getAmmoColors(){
        List<AmmoColor> result = new ArrayList<>();
        result.add(BLUE);
        result.add(RED);
        result.add(YELLOW);
        return result;
    }
}
