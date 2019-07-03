package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.ModeView;
import it.polimi.ingsw.client.WeaponView;
import it.polimi.ingsw.server.model.AmmoTile;
import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.PlayerColor;

import java.util.List;
import java.util.Map;

/**
 * a class of useful method for cli
 */
public class CliUtils {
    public static final String DEFAULT_COLOR = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String DROP = "\u25cf";
    public static final String SKULL = "\u2620";
    public static final String AMMO = "\u25a0";
    public static final String POWERUP_SYMBOL = "P";
    public static final String PLAYER_SYMBOL = "\u258a";
    public static final String VERT = "\u2502";
    public static final String HOR = "\u2500";
    public static final String CORNER_TL = "\u250c";
    public static final String CORNER_TR = "\u2510";
    public static final String CORNER_BL = "\u2514";
    public static final String CORNER_BR = "\u2518";
    public static final String DIAG_CORNER_TL = "\u256d";
    public static final String DIAG_CORNER_TR = "\u256e";
    public static final String DIAG_CORNER_BL = "\u2570";
    public static final String DIAG_CORNER_BR = "\u256f";
    public static final String C_DOT = "\u22c5";
    public static final String FULL_BLOCK = "\u2588";
    public static final String OK_COLOR = "\u001b[32m";
    public static final String WRONG_COLOR = "\u001b[31m";

    /**
     * Builds a human readable string with the passed list
     * @param list the list to stringify
     * @param <T> the type of objects of the list
     * @return a human readable string
     */
    public static <T> String listToString(List<T> list){
        StringBuilder result = new StringBuilder();
        for (T t : list){
            if (t == null){
                result.append("    null;");
            } else {
                result.append("    "+t.toString()+";");
            }
        }
        return result.toString();
    }

    /**
     * Prints a human readable list of objects
     * @param list the list to print
     * @param <T> the type of objects of the list
     */
    public static <T> void printList(List<T> list){
        System.out.println();
        System.out.println(listToString(list));
    }

    /**
     * Builds a human readable string of the passed map
     * @param map the map to stringify
     * @param <K> the type of the key objects
     * @param <V> the type of the value objects
     * @return a human readable string
     */
    public static <K, V> String mapToString(Map<K, V> map){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()){
            if (entry.getKey() == null) result.append("    null: ");
            else result.append("    "+entry.getKey().toString()+": ");
            if (entry.getValue() == null) result.append("null;");
            else result.append(entry.getValue().toString()+";");
        }
        return result.toString();
    }

    /**
     * Prints a human readable list of objects
     * @param map the list to print
     * @param <K> the type of the key objects
     * @param <V> the type of the value objects
     */
    public static <K, V> void printMap(Map<K, V> map){
        System.out.println();
        System.out.println(mapToString(map));
    }


    /**
     * Prepares an empty rectangle with a border of the selected color
     * @param height the height of the rectangle
     * @param width the width of the rectangle
     * @param color the color of the border
     * @return matrix of strings (escape color - char - escape color)
     */
    public static String[][] prepareBorder(int height, int width, String color){
        String[][] box = new String[height][width];

        box[0][0] = color+CORNER_TL+DEFAULT_COLOR;
        for (int c = 1; c < width - 1; c++) {
            box[0][c] = color+HOR+DEFAULT_COLOR;
        }
        box[0][width - 1] = color+CORNER_TR+DEFAULT_COLOR;

        for (int r = 1; r < height - 1; r++) {
            box[r][0] = color+VERT+DEFAULT_COLOR;
            for (int c = 1; c < width - 1; c++) {
                box[r][c] = " ";
            }
            box[r][width-1] = color+VERT+DEFAULT_COLOR;
        }

        box[height - 1][0] = color+CORNER_BL+DEFAULT_COLOR;
        for (int c = 1; c < width - 1; c++) {
            box[height - 1][c] = color+HOR+DEFAULT_COLOR;
        }

        box[height - 1][width - 1] = color+CORNER_BR+DEFAULT_COLOR;
        return box;
    }

    /**
     * Prepares an empty rectangle with a border of the default color
     * @param height the height of the rectangle
     * @param width the width of the rectangle
     * @return matrix of strings (escape color - char - escape color)
     */
    public static String[][] prepareBorder(int height, int width){
        return prepareBorder(height, width, DEFAULT_COLOR);
    }

    /**
     * For each AmmoColor returns the corresponding escape char
     * @param ammoColor the ammoColor to print
     * @return the ANSI escape char of a color
     */
    public static String printColorOf(AmmoColor ammoColor){
        switch (ammoColor){
            case BLUE:
                return BLUE;
            case RED:
                return RED;
            case YELLOW:
                return YELLOW;
            case GREEN:
                return GREEN;
            case PURPLE:
                return PURPLE;
            case WHITE:
                return DEFAULT_COLOR;
        }
        return DEFAULT_COLOR;
    }

    /**
     * For each PlayerColor returns the corresponding escape char
     * @param playerColor the PlayerColor to print
     * @return the ANSI escape char of a color
     */
    public static String printColorOf(PlayerColor playerColor){
        switch (playerColor) {
            case YELLOW:
                return YELLOW;
            case GREY:
                return BLUE;
            case GREEN:
                return GREEN;
            case BLUE:
                return CYAN;
            case VIOLET:
                return PURPLE;
        }
        return DEFAULT_COLOR;
    }

    /**
     * Transforms an array of strings in a string
     * @param array the Array of Strings (escape color - char - escape color)
     * @return a String composed by all the strings of the array concatenated
     */
    public static String stringifyArrayOfStrings(String[] array){
        StringBuilder string = new StringBuilder();
        for (int i=0; i<array.length; i++){
            string.append(array[i]);
        }
        return string.toString();
    }

    /**
     * Returns an array that represents the amount of ammos contained in the Cash
     * @param cash the cash to visualize
     * @return an array of Strings (escape color - char - escape color)
     */
    public static String[] printCash(Cash cash){
        String[] result = new String[cash.getTotal()];
        int i=0;
        for (AmmoColor color : AmmoColor.getAmmoColors()){
            for (int j=0; j<cash.getAmountOf(color); j++){
                result[i] = printColorOf(color)+AMMO+DEFAULT_COLOR;
                i++;
            }
        }
        return result;
    }

    /**
     * Returns an array that represents the content of the ammoTile
     * @param ammoTile the ammoTile to visualize
     * @return an array of Strings (escape color - char - escape color)
     */
    public static String[] printAmmoTile(AmmoTile ammoTile){
        String[] result = new String[3];
        int i=0;
        for (AmmoColor color : AmmoColor.getAmmoColors()){
            for (int j=0; j<ammoTile.getAmmos().getAmountOf(color); j++){
                result[i] = printColorOf(color)+AMMO+DEFAULT_COLOR;
                i++;
            }
        }
        if (ammoTile.hasPowerUp()){
            result[2] = DEFAULT_COLOR+POWERUP_SYMBOL+DEFAULT_COLOR;
        }
        return result;
    }

    /**
     * Gets a pretty, human readable description of all the modes of a weapon
     * @param weapon the involved weapon
     * @return a string containing the name of the weapon and cost + title + explanation of each mode
     */
    public static String getPrettyManWeapon(WeaponView weapon){
        StringBuilder man = new StringBuilder();
        man.append(BLUE + weapon.getName().toUpperCase()+" "+stringifyArrayOfStrings(printCash(weapon.getDiscountedCost()))+"\n");
        for (ModeView mode : weapon.getMyModes()){
            man.append("   "+stringifyArrayOfStrings(printCash(mode.getCost()))+" "+mode.getTitle().toUpperCase()+": ");
            man.append(mode.getDescription()+"\n");
        }
        return man.toString();
    }

    /**
     * Creates an array of Strings (of the specified color) from a string
     * @param str the string to transform
     * @param color the desired color of the result
     * @return an array of Strings (escape color - char - escape color)
     */
    public static String[] createLineFromString(String str, String color){
        String[] result = new String[str.length()];
        for (int i=0; i<str.length(); i++){
            result[i] = color + str.charAt(i) + DEFAULT_COLOR;
        }
        return result;
    }

    /**
     * Creates an array of Strings (of the default color) from a string
     * @param str the string to transform
     * @return an array of Strings (escape color - char - escape color)
     */
    public static String[] createLineFromString(String str){
        return createLineFromString(str, DEFAULT_COLOR);
    }
}
