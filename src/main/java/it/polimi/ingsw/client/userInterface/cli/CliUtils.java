package it.polimi.ingsw.client.userInterface.cli;

import it.polimi.ingsw.server.model.AmmoTile;
import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.PlayerColor;
import it.polimi.ingsw.server.model.enums.PlayerState;

import java.util.List;
import java.util.Map;

public class CliUtils {
    public static final String DEFAULT_COLOR = "\u001B[0m";
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

    public static <T> void printList(List<T> list){
        System.out.println();
        System.out.println(listToString(list));
    }

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

    public static <K, V> void printMap(Map<K, V> map){
        System.out.println();
        System.out.println(mapToString(map));
    }



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

    public static String[][] prepareBorder(int height, int width){
        return prepareBorder(height, width, DEFAULT_COLOR);
    }

    public static String printColorOf(AmmoColor ammoColor){
        switch (ammoColor){
            case BLUE:
                return "\u001b[34m";
            case RED:
                return "\u001b[31m";
            case YELLOW:
                return "\u001b[33m";
            case GREEN:
                return "\u001b[32m";
            case PURPLE:
                return "\u001b[35m";
            case WHITE:
                return "\u001b[0m";
        }
        return DEFAULT_COLOR;
    }

    public static String printColorOf(PlayerColor playerColor){
        switch (playerColor) {
            case YELLOW:
                return "\u001b[33m";
            case GREY:
                return "\u001b[34m";
            case GREEN:
                return "\u001b[32m";
            case BLUE:
                return "\u001b[36m";
            case VIOLET:
                return "\u001b[35m";
        }
        return DEFAULT_COLOR;
    }

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

    public static String getStateDescription(PlayerState state){
        String result = "";
        switch (state){
            case IDLE:
                result = "You can't do anything at the moment";
                break;
            case CHOOSE_ACTION:
                result = "Choose an action to do";
                break;
            case MOVE_THERE:
                result = "Choose a square to move into";
                break;
            case GRAB_THERE:
                result = "Choose a square to grab from";
                break;
            case GRAB_WEAPON:
                result = "Choose a weapon to grab";
                break;
            case DISCARD_WEAPON:
                result = "You must discard one of your weapon: choose one";
                break;
            case RELOAD:
                result = "Choose a weapons to reload";
                break;
            case PAYING:
                result = "You have to pay for something. You can use your powerups.";
                break;
            case PAYING_ANY:
                result = "Choose which color (or powerup) to pay";
                break;
            case SHOOT_WEAPON:
                result = "Choose a weapon to shoot with";
                break;
            case CHOOSE_MODE:
                result = "Select a mode to build the shoot";
                break;
            case SHOOT_TARGET:
                result = "Choose a target (either a player or a square)";
                break;
            case SPAWN:
                result = "Choose a powerup to discard (and spawn onto)";
                break;
            case USE_POWERUP:
                result = "Choose a powerup to use";
                break;
            default:
                result = "";
        }
        return result;
    }

    public static String[] createLineFromString(String str, String color){
        String[] result = new String[str.length()];
        for (int i=0; i<str.length(); i++){
            result[i] = color + str.charAt(i) + DEFAULT_COLOR;
        }
        return result;
    }

    public static String[] createLineFromString(String str){
        return createLineFromString(str, DEFAULT_COLOR);
    }
}
