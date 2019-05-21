package it.polimi.ingsw.server.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum AmmoColor {
    BLUE,
    YELLOW,
    RED,
    WHITE,
    PURPLE,
    GREEN;

    public static List<AmmoColor> getAmmoColors(){
        List<AmmoColor> result = new ArrayList<>();
        result.add(BLUE);
        result.add(RED);
        result.add(YELLOW);
        return result;
    }
}
