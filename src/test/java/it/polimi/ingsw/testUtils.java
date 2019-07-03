package it.polimi.ingsw;

import it.polimi.ingsw.server.exceptions.AlreadyLoggedException;
import it.polimi.ingsw.server.exceptions.GameFullException;
import it.polimi.ingsw.server.exceptions.NameAlreadyTakenException;
import it.polimi.ingsw.server.exceptions.NameNotFoundException;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Weapon;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class testUtils {
    public static final String SAVED_GAMES_FOR_TESTS = "./src/test/resources/savedGamesForTests/";
    public static final String BACKUP_TEST = "./src/test/resources/backupTest/";

    /**
     * Support method to get an InputStream from the name of the saved game
     * @param name teh name of the saved backup to resume
     * @return an inputStream of the file
     */
    public static InputStream searchInTestResources(String name){
        InputStream result= testUtils.class.getClassLoader().getResourceAsStream("savedGamesForTests/" + name + ".json");
        return result;
    }

    /**
     * Prints the selectable item of a player
     * @param p the involved player
     */
    public static void printSel(Player p){
        System.out.println("\n"+p.getName()+":");
        System.out.println(p.getState());
        System.out.println(p.selectablesToString());
    }

    /**
     * Creates a human readable string of a list of objects
     * @param list the list to stringify
     * @param <T> parametric type of the list's objects
     * @return a human readable string
     */
    public static <T> String listToString(List<T> list){
        StringBuilder result = new StringBuilder();
        for (T t : list){
            if (t == null){
                result.append("    null;");
            } else {
                result.append("    "+t+";");
            }
        }
        return result.toString();
    }

    /**
     * Prints a list in a human readable way
     * @param list the list to print
     * @param <T> the type of list's objects
     */
    public static <T> void printList(List<T> list){
        System.out.println();
        System.out.println(listToString(list));
    }

    /**
     * Creates a human readable string of a map of objects
     * @param map the map to stringify
     * @param <K> parametric type of the key objects
     * @param <V> parametric type of the value objects
     * @return
     */
    public static <K, V> String mapToString(Map<K, V> map){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()){
            if (entry.getKey() == null) result.append("    null: ");
            else result.append("    "+entry.getKey()+": ");
            if (entry.getValue() == null) result.append("null;");
            else result.append(entry.getValue()+";");
        }
        return result.toString();
    }

    /**
     * print a map in a human readable way
     * @param map the map to print
     * @param <K> the type of the map's keys
     * @param <V> the type of the map's values
     */
    public static <K, V> void printMap(Map<K, V> map){
        System.out.println();
        System.out.println(mapToString(map));
    }

    /**
     * It sets a player as disconnected.
     * this is a fake method to emulate what the real controller does.
     * @param gameModel the involved game model
     * @param disconnected the player to disconnect
     */
    public static void disconnectPlayer(GameModel gameModel, Player disconnected){
        if(gameModel.getActivePlayers().contains(disconnected)){    //should always be true, right?
            gameModel.getActivePlayers().remove(disconnected);
            if (gameModel.isMatchInProgress()) gameModel.getInactivePlayers().add(disconnected);
        }
        if(gameModel.isMatchInProgress()){
            gameModel.fakeAction(disconnected);
        }
    }

    /**
     * Checks that all player from which an interaction is required are actually active.
     * If this is not the case it simulates their action.
     * this is a fake method to emulate what the real controller does
     * @param gameModel
     */
    public static void finalCleaning(GameModel gameModel){
        while (!gameModel.getActivePlayers().containsAll(gameModel.getWaitingFor())){
            List<Player> toFakeList = new ArrayList<>();
            for (Player p : gameModel.getWaitingFor()){
                if (!gameModel.getActivePlayers().contains(p)){
                    toFakeList.add(p);
                    System.out.println(p+" has been added to: toFakeList");
                }
            }
            printList(toFakeList);
            for (Player p : toFakeList){
                gameModel.fakeAction(p);
            }
            printList(gameModel.getWaitingFor());
            printList(toFakeList);
        }
    }

    /**
     * Prints the global situation of a match
     * @param match the involved match
     */
    public static void printSituation(Match match){
        for (Player tempPlayer : match.getPlayers()){
            System.out.println();
            if (match.getCurrentPlayer() == tempPlayer) System.out.print("-> ");
            else System.out.print("   ");
            System.out.println(tempPlayer.getName()+" in "+tempPlayer.getSquarePosition() + " with " + tempPlayer.getWallet() + " state: " + tempPlayer.getState());
            System.out.println("        powerups: "+listToString(tempPlayer.getPowerUps()));
            System.out.print("        weapons:");
            for (Weapon w : tempPlayer.getWeapons()){
                if (tempPlayer.isLoaded(w)) System.out.print("    - "+w.getName()+"(LOADED)");
                else System.out.print("    - "+w.getName()+"(UNLOADED)");
            }
            System.out.println();
            System.out.println("        Damages: " + listToString(tempPlayer.getDamageTrack().getDamageList()));
            System.out.println("        Marks: "+ mapToString(tempPlayer.getDamageTrack().getMarkMap()));
        }
    }
}
