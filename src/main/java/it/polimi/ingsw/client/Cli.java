package it.polimi.ingsw.client;

import it.polimi.ingsw.Match;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.Weapon;
import it.polimi.ingsw.server.message.UpdateMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Cli implements ClientView {
    private Client client;

    public Cli(){
        this.client = null;
        askLogin();
    }

    public void printLoginMessage(String text, boolean loginSuccessful){
        System.out.println(text);
        if(!loginSuccessful){
            System.out.println("Insert a new name: ");
            try{
                client.chooseName(readStringFromUser());
            } catch(IOException e){
                System.out.println("Error while reading input from user!");
            }
        }
    }

    public void printDisconnectionMessage(String text){
        System.out.println(text);
    }


    public void askLogin() {

        System.out.println("Choose: socket or rmi?");
        boolean ok = false;

        String choice = "";
        String name = "";

        while(!ok){
            choice  = new Scanner(System.in).nextLine();

            if(choice.equalsIgnoreCase("socket") || choice.equalsIgnoreCase("rmi")){
                ok = true;
            }
            else{
                System.out.println("Illegal answer. Please insert only socket or rmi");
            }
        }



        System.out.println("Insert your name");
        try{
            name = readStringFromUser();
        }
        catch (IOException e){
            System.out.println("Something went wrong IOException");
        }

        this.client = new Client(this);
        client.login(choice, name);
    }


    private String readStringFromUser() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
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

    //TEST-ONLY METHOD
    public static void showUpdate(UpdateMessage update){
        for (UpdateMessage.OtherPlayerInfo otherPlayer : update.getOtherPlayers()){
            System.out.println("OTHER PLAYERS:");
            if (update.getCurrentPlayer().equals(otherPlayer.getName())) System.out.print("-> ");
            else System.out.print("   ");
            System.out.println(otherPlayer.getName()+" in "+otherPlayer.getSquarePosition() + " with " + otherPlayer.getWallet() + " state: " + otherPlayer.getState());
            System.out.println("        powerups: "+otherPlayer.getNumPowerUps());
            System.out.print("        unloaded weapons:");
            for (String wStr : otherPlayer.getUnloadedWeapons()){
                System.out.print("    - "+wStr);
            }
            System.out.println();
            System.out.println("        Damages: " + listToString(otherPlayer.getDamageList()));
            System.out.println("        Marks: "+ mapToString(otherPlayer.getMarkMap()));
        }

        UpdateMessage.MyPlayerInfo me = update.getMyPlayerInfo();
        System.out.println("\n\nME:");
        if (update.getCurrentPlayer().equals(me.getName())) System.out.println("It's my turn!");
        System.out.println(me.getName()+" in "+me.getSquarePosition() + " with " + me.getWallet() + " state: " + me.getState() + " points: " + me.getPoints());
        if (!me.getPending().equals("b:0|r:0|y:0")) System.out.println("You have to pay "+me.getPending()+" (you have already paid "+me.getCredit()+")");
        System.out.println("        powerups: "+listToString(me.getPowerUps()));
        System.out.print("        weapons:");
        for (Map.Entry<String, Boolean> entry : me.getWeapons().entrySet()){
            if (entry.getValue() == true) System.out.print("\t- "+entry.getKey()+"(LOADED)");
            else System.out.print("\t- "+entry.getKey()+"(UNLOADED)");
        }
        System.out.println();
        System.out.println("        Damages: " + listToString(me.getDamageList()));
        System.out.println("        Marks: "+ mapToString(me.getMarkMap()));

        StringBuilder result = new StringBuilder();
        if (!me.getSelectableSquares().isEmpty()) {
            result.append("sq:");
            result.append(listToString(me.getSelectableSquares()));
            result.append("\n");
        }
        if (!me.getSelectableWeapons().isEmpty()) {
            result.append("wp:");
            result.append(listToString(me.getSelectableWeapons()));
            result.append("\n");
        }
        if (!me.getSelectablePowerUps().isEmpty()) {
            result.append("pow:\n");
            result.append(listToString(me.getSelectablePowerUps()));
            result.append("\n");
        }
        if (!me.getSelectablePlayers().isEmpty()) {
            result.append("pl:\n");
            result.append(listToString(me.getSelectablePlayers()));
            result.append("\n");
        }
        if (!me.getSelectableCommands().isEmpty()) {
            result.append("cmd:\n");
            result.append(listToString(me.getSelectableCommands()));
            result.append("\n");
        }
        if (!me.getSelectableModes().isEmpty()) {
            result.append("mod:\n");
            result.append(listToString(me.getSelectableModes()));
            result.append("\n");
        }
        if (!me.getSelectableActions().isEmpty()) {
            result.append("act:\n");
            result.append(listToString(me.getSelectableActions()));
            result.append("\n");
        }
        if (!me.getSelectableColors().isEmpty()) {
            result.append("col:\n");
            result.append(listToString(me.getSelectableColors()));
            result.append("\n");
        }
        System.out.print(result.toString());


    }
}
