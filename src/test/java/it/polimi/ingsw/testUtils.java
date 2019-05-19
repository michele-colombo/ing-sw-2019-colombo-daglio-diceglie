package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.AlreadyLoggedException;
import it.polimi.ingsw.exceptions.GameFullException;
import it.polimi.ingsw.exceptions.NameAlreadyTakenException;
import it.polimi.ingsw.exceptions.NameNotFoundException;

import java.util.List;
import java.util.Map;

public class testUtils {
    public static final String SAVED_GAMES_FOR_TESTS = "./src/test/resources/savedGamesForTests/";
    public static final String BACKUP_TEST = "./src/test/resources/backupTest/";

    public static void printSel(Player p){
        System.out.println(p.getName()+":");
        System.out.println(p.getState());
        System.out.println(p.selectablesToString());
    }



    public static <T> void printList(List<T> list){
        System.out.println("printing a list:");
        for (T t : list){
            if (t == null){
                System.out.println("NULL!!!!");
            } else {
                System.out.println(t);
            }
        }
    }

    public static <K, V> void printMap(Map<K, V> map){
        System.out.println("printing a map:");
        for (Map.Entry<K, V> entry : map.entrySet()){
            if (entry.getKey() == null) System.out.print("null -> ");
            else System.out.print(entry.getKey()+" -> ");
            if (entry.getValue() == null) System.out.println("null");
            else System.out.println(entry.getValue());
        }
    }



    public static void addPlayers(GameModel gm, List<Player> players){
        for (Player p : players){
            try {
                gm.addPlayer(p);
            } catch (NameAlreadyTakenException | GameFullException | AlreadyLoggedException | NameNotFoundException e){

            }
        }
    }

}
