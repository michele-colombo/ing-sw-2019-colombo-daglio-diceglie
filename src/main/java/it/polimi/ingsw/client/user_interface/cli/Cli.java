package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.ClientExceptions.WrongSelectionException;
import it.polimi.ingsw.client.user_interface.UserInterface;
import static it.polimi.ingsw.client.user_interface.cli.CliUtils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The command line user interface
 */
public class Cli implements UserInterface {

    /**
     * width of the 'window' where the game is displayed
     */
    public static final int PLAYING_WINDOW_WIDTH = 150;

    /**
     * height of the 'window' where the game is displayed
     */
    public static final int PLAYING_WINDOW_HEIGHT = 39;
    public static final String QUIT = "quit";
    public static final String DISCONNECT = "disconnect";
    public static final String SOCKET = "socket";
    public static final String RMI = "rmi";
    public static final String CHOOSE_CONNECTION_ERROR_MESSAGE = "Please insert either 'socket' or 'rmi'";
    public static final String WAIT_MESSAGE = "Wait for other players to join";
    public static final String MANUAL_NOT_AVAILABLE = "manual not available for the inserted item";
    public static final String INVALID_SELECTION = "invalid selection, retry!";
    public static final String YOU_CAN_SELECT_A_SQUARE = "You can select a square specifying coordinates to separate integers or";
    public static final String INSERT_SELECTION_NUMBER = "insert the selection number";
    public static final String WELCOME_TO_ADRENALINE = "welcome to Adrenaline";
    public static final String INSERT_CONNECTION_TYPE = "please insert the type of connection: socket or rmi";
    public static final String LOGIN = "login";
    public static final String INSERT_NICKNAME = "Please insert your nickname. If you want to reconnect, make sure you insert the previous nickname.";

    /**
     * the possible states in which the cli can be
     */
    private enum CliState{
        ASK_CONNECTION, ASK_LOGIN, LOGGED, IDLE, PLAY
    }

    /**
     * Reference to the client
     */
    private Client client;

    /**
     * the window where the game is displayed
     */
    private PlayingWindow playingWindow;

    /**
     * a list with the unique IDs of selectable items. Used to send the user's selection to the server
     */
    private List<String> selectableIds;

    /**
     * the runnable used to get input from the user
     */
    private Runnable reader;

    /**
     * flag that contains the state of activity of the cli
     */
    private boolean isActive;

    /**
     * the current state of cli
     */
    private CliState state;

    /**
     * Builds a cli and runs the reader of user's input
     */
    public Cli(){
        state = CliState.IDLE;

        this.client = new Client(this);
        showConnectionSelection();

        selectableIds = new ArrayList<>();
        isActive = true;
        reader = new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (isActive){
                    String input = scanner.nextLine();
                    if (QUIT.equalsIgnoreCase(input)){
                        client.shutDown();
                        isActive= false;
                    } else if (DISCONNECT.equalsIgnoreCase(input)){
                        client.shutDown();
                    } else {
                        switch (state) {
                            case ASK_CONNECTION:
                                if (input.equalsIgnoreCase(SOCKET) || input.equalsIgnoreCase(RMI)) {
                                    client.createConnection(input);
                                } else {
                                    System.out.println(CHOOSE_CONNECTION_ERROR_MESSAGE);
                                }
                                break;
                            case ASK_LOGIN:
                                input = input.trim();
                                client.chooseName(input);
                                state = CliState.IDLE;    //in order to prevent a player sends a login event twice
                                break;
                            case LOGGED:
                                System.out.println(WAIT_MESSAGE);
                                break;
                            case IDLE:
                                break;
                            case PLAY:
                                input = input.trim();
                                if (input.matches("man [\\w\\d-. ]+")){
                                    WeaponView w = client.getMatch().getDecks().getWeaponFromName(input.substring(4).trim());
                                    if (w != null){
                                        System.out.println(getPrettyManWeapon(w));
                                    } else {
                                        PowerUpView po = client.getMatch().getDecks().getPowerUpFromString(input.substring(4).trim());
                                        if (po != null){
                                            System.out.println(po.getDescription());
                                        } else {
                                            System.out.println(MANUAL_NOT_AVAILABLE);
                                        }
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
                                            SquareView square = client.getMatch().getLayout().getSquare(x, y);
                                            if (square != null) {
                                                client.selected(square.toString());
                                            }
                                        } catch (WrongSelectionException e2) {
                                            System.out.println(INVALID_SELECTION);
                                        } catch (NumberFormatException | IndexOutOfBoundsException e2) {
                                            System.out.println(YOU_CAN_SELECT_A_SQUARE);
                                        }
                                        System.out.println(INSERT_SELECTION_NUMBER);
                                    } catch (WrongSelectionException | IndexOutOfBoundsException e) {
                                        playingWindow.show();
                                        System.out.println(INVALID_SELECTION);
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
                WELCOME_TO_ADRENALINE, BLUE,
                INSERT_CONNECTION_TYPE);
        window.show();
        state = CliState.ASK_CONNECTION;
    }

    @Override
    public void showLogin() {
        Window window = new TitleAndTextWindow(30, 10,
                LOGIN, BLUE,
                INSERT_NICKNAME);
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

    @Override
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

    @Override
    public void showAndAskSelection(){
        selectableIds.clear();
        playingWindow.fullUpdate(client.getMatch());
        playingWindow.show();
        state = CliState.PLAY;
    }

    @Override
    public void updateConnection() {
        if (client.getMatch() != null){
            System.out.println("Il match è diverso da null!");
        } else {
            printConnectedPlayers();
        }
    }

    @Override
    public void updateLayout() {
        playingWindow.getLayoutBox().update(client.getMatch());
    }

    @Override
    public void updateKillshotTrack() {
        playingWindow.getKillShotTrackBox().update(client.getMatch());
    }

    @Override
    public void updateCurrentPlayer() {
        for (MiniBox playerBox : playingWindow.getAllPlayers()){
            playerBox.update(client.getMatch());
        }
    }

    @Override
    public void updatePlayer(PlayerView updated) {
        for (MiniBox playerBox : playingWindow.getAllPlayers()){
            playerBox.update(client.getMatch());
        }
    }

    @Override
    public void updatePayment() {
        playingWindow.getMyInfoBox().update(client.getMatch());
    }

    @Override
    public void updateWeapons(PlayerView player) {
        playingWindow.getMyWeaponBox().update(client.getMatch());
        for (MiniBox playerBox : playingWindow.getAllPlayers()){
            playerBox.update(client.getMatch());
        }
    }

    @Override
    public void updatePowerUp(PlayerView player) {
        playingWindow.getMyPowerUpBox().update(client.getMatch());
        for (MiniBox playerBox : playingWindow.getAllPlayers()){
            playerBox.update(client.getMatch());
        }
    }

    @Override
    public void updateDamage(PlayerView player) {
        for (MiniBox playerBox : playingWindow.getAllPlayers()){
            playerBox.update(client.getMatch());
        }
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
        playingWindow = new PlayingWindow(PLAYING_WINDOW_WIDTH, PLAYING_WINDOW_HEIGHT, matchView, this);
        state = CliState.PLAY;
    }

}
