package it.polimi.ingsw.client.userInterface.cli;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.userInterface.UserInterface;
import static it.polimi.ingsw.client.userInterface.cli.CliUtils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Cli implements UserInterface {
    private enum CliState{
        ASK_CONNECTION, ASK_LOGIN, LOGGED, IDLE, PLAY
    }
    private Client client;
    private PlayingWindow playingWindow;
    private MatchView match;
    private List<String> selectableIds;
    private Runnable reader;
    private boolean isActive;
    private CliState state;

    public Cli(){
        state = CliState.IDLE;

        this.client = new Client(this);
        showConnectionSelection();

        selectableIds = new ArrayList<>();
        isActive = true;
        reader = new Runnable() {
            @Override
            public void run() {
                boolean accepted = false;
                Scanner scanner = new Scanner(System.in);
                while (isActive){
                    String input = scanner.nextLine();
                    if ("quit".equalsIgnoreCase(input)){
                        client.shutDown();
                        isActive= false;
                    } else if ("disconnect".equalsIgnoreCase(input)){
                        client.shutDown();
                        showConnectionSelection();
                    } else {
                        switch (state) {
                            case ASK_CONNECTION:
                                if (input.equalsIgnoreCase("socket") || input.equalsIgnoreCase("rmi")) {
                                    client.createConnection(input);
                                } else {
                                    System.out.println("Please insert either 'socket' or 'rmi'");
                                }
                                break;
                            case ASK_LOGIN:
                                input = input.trim();
                                client.chooseName(input);
                                state = CliState.IDLE;    //in order to prevent a player sends a login event twice
                                break;
                            case LOGGED:
                                System.out.println("Wait for other players to join");
                                break;
                            case IDLE:
                                break;
                            case PLAY:
                                input = input.trim();
                                if (input.matches("man [\\w\\d-. ]+")){
                                    WeaponView w = match.getDecks().getWeaponFromName(input.substring(4).trim());
                                    if (w != null){
                                        System.out.println(getPrettyManWeapon(w));
                                    }
                                } else {
                                    try {
                                        int sel = Integer.parseInt(input);
                                        client.selected(selectableIds.get(sel));
                                        state = CliState.IDLE;
                                    } catch (NumberFormatException e) {
                                        try {
                                            String[] coordinates = input.split("[^\\d^\\w]+");
                                            int x = Integer.parseInt(coordinates[0].trim());
                                            int y = Integer.parseInt(coordinates[1].trim());
                                            SquareView square = match.getLayout().getSquare(x, y);
                                            if (square != null) {
                                                client.selected(square.toString());
                                            }
                                        } catch (WrongSelectionException e2) {
                                            System.out.println("invalid selection, retry!");
                                        } catch (NumberFormatException | IndexOutOfBoundsException e2) {
                                            System.out.println("You can select a square specifying coordinates to separate integers or");
                                        }
                                        System.out.println("insert the selection number");
                                    } catch (WrongSelectionException | IndexOutOfBoundsException e) {
                                        playingWindow.show();
                                        System.out.println("invalid selection, retry!");
                                    }
                                }
                        }
                    }
                }
            }
        };
        reader.run();
    }

    @Override
    public void showConnectionSelection() {
        Window window = new TitleAndTextWindow(30, 10,
                "welcome to Adrenaline", BLUE,
                "please insert the type of connection: socket or rmi");
        window.show();
        state = CliState.ASK_CONNECTION;
    }

    @Override
    public void showLogin() {
        Window window = new TitleAndTextWindow(30, 10,
                "login", BLUE,
                "Please insert your nickname. If you want to reconnect, make sure you insert the previous nickname.");
        window.show();
        state = CliState.ASK_LOGIN;
    }

    public void printConnectedPlayers(){
        String connectedPlayers = "These are the players currently online:";
        for (Map.Entry<String, Boolean> entry : client.getConnections().entrySet()){
            connectedPlayers = connectedPlayers+" "+entry.getKey()+",";
        }
        connectedPlayers = connectedPlayers.substring(0, connectedPlayers.length()-1);
        Window window = new TitleAndTextWindow(30, 10,
                "PLAYERS ONLINE", BLUE,
                connectedPlayers);
        window.show();
    }

    public void printLoginMessage(String text, boolean loginSuccessful){
        if (loginSuccessful){
            Window window = new TitleAndTextWindow(30, 10,
                    "LOGIN SUCCESSFUL!", OK_COLOR,
                    text+" Please wait for the game to start. If you disconnect now, you will be completely removed from game.");
            window.show();
            state = CliState.LOGGED;
        } else {
            Window window = new TitleAndTextWindow(30, 10,
                    "LOGIN FAILED!", WRONG_COLOR,
                    text+" Please insert a new name or disconnect.");
            window.show();
            showLogin();
            state = CliState.ASK_LOGIN;
        }
    }

    public void showAndAskSelection(){
        selectableIds.clear();
        playingWindow.fullUpdate(match);
        playingWindow.show();
        state = CliState.PLAY;
    }

    @Override
    public void updateConnection() {
        if (match != null){
        } else {
            printConnectedPlayers();
        }
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

    @Override
    public void printError(String message) {
        System.out.println(RED + message + DEFAULT_COLOR);

    }

    @Override
    public void showGameOver(Map<PlayerView, Integer> rank, Map<PlayerView, Integer> points) {
        List<String> rankEntries = new ArrayList<>();
        for (Map.Entry<PlayerView, Integer> entry : rank.entrySet()){
            rankEntries.add(entry.getKey().getName()+
                    " has arrived "+
                    entry.getValue()+
                    "° with "+
                    points.get(entry.getKey())+
                    " points!");
        }
        Window window = new TitleAndListWindow(50, 10,
                "GAME OVER", RED,
                rankEntries);
        window.show();
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
    public void updateStartMatch(MatchView matchView) {
        this.match = matchView;
        playingWindow = new PlayingWindow(150, 37, match, this);
        state = CliState.PLAY;
    }

    //TEST-ONLY METHOD
    public void showSituation(){
        MatchView match = client.getMatch();
        System.out.println("OTHER PLAYERS:");
        for (PlayerView otherPlayer : match.getOtherPlayers()){
            if (match.getCurrentPlayer() != null && match.getCurrentPlayer().getName().equals(otherPlayer.getName())) System.out.print("-> ");
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
        if (match.getCurrentPlayer() != null &&match.getCurrentPlayer().getName().equals(me.getName())) System.out.println("It's my turn!");
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
