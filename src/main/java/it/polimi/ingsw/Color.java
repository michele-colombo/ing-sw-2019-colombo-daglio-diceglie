package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public enum Color {
    BLUE,
    YELLOW,
    RED,
    WHITE,
    PURPLE,
    GREEN;

    public static List<Color> getAmmoColors(){
        List<Color> result = new ArrayList<>();
        result.add(BLUE);
        result.add(RED);
        result.add(YELLOW);
        return result;
    }
}
