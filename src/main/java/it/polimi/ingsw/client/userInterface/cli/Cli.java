package it.polimi.ingsw.client.userInterface.cli;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.userInterface.UserInterface;

import static it.polimi.ingsw.client.userInterface.cli.CliUtils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Cli implements UserInterface {
    private Client client;
    private PlayingWindow playingWindow;
    private MatchView match;
    private List<String> selectableIds;
    private Runnable reader;
    private boolean isActive;

    public Cli(){
        this.client = null;
        selectableIds = new ArrayList<>();
        askLogin();
        isActive = true;
        reader = new Runnable() {
            @Override
            public void run() {
                boolean accepted = false;
                Scanner scanner = new Scanner(System.in);
                while (isActive){
                    String input = scanner.nextLine();
                    try {
                        int sel = Integer.parseInt(input.trim());
                        client.selected(selectableIds.get(sel));
                        accepted = true;
                    } catch (NumberFormatException e){
                        System.out.println("please insert a number");
                    } catch (WrongSelectionException e){
                        playingWindow.show();
                        System.out.println("invalid selection, retry!");
                    } catch (IndexOutOfBoundsException e){
                        playingWindow.show();
                        System.out.println("invalid selection, retry!");
                    }
                }
            }
        };
        reader.run();
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

    public void initialize(MatchView match){
        this.match = match;
        playingWindow = new PlayingWindow(150, 37, match, this);
    }

    public void showAndAskSelection(){
        selectableIds.clear();
        playingWindow.fullUpdate(match);
        playingWindow.show();
    }

    @Override
    public void updateConnection() {

    }

    @Override
    public void updateLayout() {

    }

    @Override
    public void updateKillshotTrack() {

    }

    @Override
    public void updateCurrentPlayer() {

    }

    @Override
    public void updatePlayer(PlayerView updated) {

    }

    @Override
    public void updatePayment() {

    }

    @Override
    public void updateWeapons(PlayerView player) {

    }

    @Override
    public void updatePowerUp(PlayerView player) {

    }

    @Override
    public void updateDamage(PlayerView player) {

    }

    @Override
    public void updateSelectables() {

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

        try {

            this.client = new Client(this);
            client.login(choice, name);
        }
        catch (RemoteException e){
            System.out.println("Impossible to create client");
            e.printStackTrace();
        }
    }


    private String readStringFromUser() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public void addSelectableId(String id){
        if (id != null){
            selectableIds.add(id);
        }
    }

    public int indexOf(String id){
        return selectableIds.indexOf(id);
    }

    @Override
    public void startMatchUpdate(MatchView matchView) {
        //todo what to do when match starts

    }

    //TEST-ONLY METHOD
    public void showSituation(){
        MatchView match = client.getMatch();
        System.out.println("OTHER PLAYERS:");
        for (PlayerView otherPlayer : match.getOtherPlayers()){
            if (match.getCurrentPlayer() != null && match.getCurrentPlayer().equals(otherPlayer.getName())) System.out.print("-> ");
            else System.out.print("   ");
            System.out.println(otherPlayer.getName()+" in "+otherPlayer.getSquarePosition() + " with " + otherPlayer.getWallet() + " state: " + otherPlayer.getState());
            System.out.println("        powerups: "+otherPlayer.getNumPowerUps());
            System.out.print("        unloaded weapons:");
            for (WeaponView w : otherPlayer.getUnloadedWeapons()){
                System.out.print("    - "+w.getName());
            }
            System.out.println();
            System.out.println("        Damages: " + listToString(otherPlayer.getDamageList()));
            System.out.println("        Marks: "+ mapToString(otherPlayer.getMarkMap()));
        }

        MyPlayer me = match.getMyPlayer();
        System.out.println("\n\nME:");
        if (match.getCurrentPlayer() != null &&match.getCurrentPlayer().equals(me.getName())) System.out.println("It's my turn!");
        System.out.println(me.getName()+" in "+me.getSquarePosition() + " with " + me.getWallet() + " state: " + me.getState() + " points: " + me.getPoints());
        if (!me.getPending().toString().equals("b:0|r:0|y:0")) System.out.println("You have to pay "+me.getPending()+" (you have already paid "+me.getCredit()+")");
        System.out.println("        powerups: "+listToString(me.getPowerUps()));
        System.out.print("        weapons:");
        for (Map.Entry<WeaponView, Boolean> entry : me.getWeapons().entrySet()){
            if (entry.getValue() == true) System.out.print("\t- "+entry.getKey().getName()+"(LOADED)");
            else System.out.print("\t- "+entry.getKey().getName()+"(UNLOADED)");
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
