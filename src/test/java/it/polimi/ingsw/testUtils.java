package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.AlreadyLoggedException;
import it.polimi.ingsw.exceptions.GameFullException;
import it.polimi.ingsw.exceptions.NameAlreadyTakenException;
import it.polimi.ingsw.exceptions.NameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class testUtils {
    public static final String SAVED_GAMES_FOR_TESTS = "./src/test/resources/savedGamesForTests/";
    public static final String BACKUP_TEST = "./src/test/resources/backupTest/";

    public static void printSel(Player p){
        System.out.println("\n"+p.getName()+":");
        System.out.println(p.getState());
        System.out.println(p.selectablesToString());
    }

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

    public static <T> void printList(List<T> list){
        System.out.println();
        System.out.println(listToString(list));
    }

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

    public static <K, V> void printMap(Map<K, V> map){
        System.out.println();
        System.out.println(mapToString(map));
    }

    public static void addPlayers(GameModel gm, List<Player> players){
        for (Player p : players){
            try {
                gm.addPlayer(p);
            } catch (NameAlreadyTakenException | GameFullException | AlreadyLoggedException | NameNotFoundException e){

            }
        }
    }

    //this is a fake method to emulete what the real controller does
    public static void disconnectPlayer(GameModel gameModel, Player disconnected){
        if(gameModel.getActivePlayers().contains(disconnected)){    //should always be true, right?
            gameModel.getActivePlayers().remove(disconnected);
            if (gameModel.isMatchInProgress()) gameModel.getInactivePlayers().add(disconnected);
        }
        if(gameModel.isMatchInProgress()){
            gameModel.fakeAction(disconnected);
        }
    }

    //this is a fake method to emulate what the real controller does
    public static void startTimers(GameModel gameModel){
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
