package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.message.GenericMessage;
import it.polimi.ingsw.communication.message.MessageVisitable;
import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.communication.message.LoginMessage;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.observer.Observer;
import it.polimi.ingsw.server.controller.timer.InputTimer;
import it.polimi.ingsw.server.controller.timer.LoginTimer;
import java.util.*;
import java.util.logging.Logger;


public class Controller {
    public static final String CORRECTLY_LOGGED_IN = " has correctly logged in";
    public static final String PLAYER_ALREADY_DISCONNECTED = "Player already disconnected!";
    public static final String PLAYER_DISCONNECTED_OK = "a player has been disconnected";
    public static final String FINAL_CLEANING_DONE = "final cleaning done";
    public static final String WRONG_SELECTION_FROM_PLAYER = "wrong selection from player ";
    public static final String WRONG_SELECTION_PLAYER_NOT_EXIST = "wrong selection, player does not exist";
    /**
     * The reference to he game model (central unit which contains rules of the game)
     */
    private final GameModel gameModel;

    /**
     * The timer for login phase.
     * It is started after the third player logins.
     */
    private final Timer loginTimer;

    /**
     * A support boolean to keep track of the starting of the login timer
     */
    private boolean loginTimerStarted;

    /**
     * A map to keep the input timer for each player.
     * The timer is started only when a player has to do something and it is restarted after any interaction.
     */
    private Map<Observer, Timer> timers;

    /**
     * The dafault duratrion of the login timer.
     * It is overidden by the configFile and by the arguments from terminal
     */
    private int loginTimerDuration = 15000;

    /**
     * The dafault duratrion of the iput timer.
     * It is overidden by the configFile and by the arguments from terminal
     */
    private int inputTimerDuration = 20000;

    /**
     * The list of server views set to disconnect.
     */
    private List<ServerView> toDisconnectList;

    /**
     * The list of all server views
     */
    private List<ServerView> serverViews;

    private static final Logger logger = Logger.getLogger(Controller.class.getName());


    /**
     * Builds the controller, initializing its attributes
     * @param gameModel sets the game model to control
     */
    public Controller(GameModel gameModel){
        this.gameModel = gameModel;
        this.loginTimer = new Timer();
        this.loginTimerStarted = false;
        this.timers = new HashMap<>();
        toDisconnectList = new ArrayList<>();
        serverViews = new ArrayList<>();
    }

    /**
     * Sets a server view to be disconnected
     * @param serverView teh server view to disconnect
     */
    public synchronized void setToDisconnect(ServerView serverView){
        if (!toDisconnectList.contains(serverView)){
            toDisconnectList.add(serverView);
        }
    }

    /**
     * Adds a server view, checking that it is not already present
     * @param serverView the server view to add
     */
    public void addServerView(ServerView serverView){
        if (!serverViews.contains(serverView)){
            serverViews.add(serverView);
        }
    }

    /**
     * Stes the login timer duration
     * @param loginTimerDuration duration in milliseconds
     */
    public void setLoginTimerDuration(int loginTimerDuration) {
        if (loginTimerDuration > 0){
            this.loginTimerDuration = loginTimerDuration;
        }
    }

    /**
     * Sets the input timer duration
     * @param inputTimerDuration duration in milliseconds
     */
    public void setInputTimerDuration(int inputTimerDuration) {
        if (inputTimerDuration > 0) {
            this.inputTimerDuration = inputTimerDuration;
        }
    }

    /**
     * Creates a player and adds it to the match.
     * Before adding checks if the name is valid.
     * @param name teh name of the player
     * @param serverView the corresponding server view (observer of the model)
     */
    public synchronized void login(String name, ServerView serverView){
        LoginMessage message = new LoginMessage("Login successful!", true);
        try{
            String newName = name.trim();
            Player newPlayer = gameModel.addPlayer(newName);
            gameModel.attach(newPlayer, serverView);
            if (!gameModel.isMatchInProgress()){
                checkStart();
            }
            logger.info(newName+ CORRECTLY_LOGGED_IN);
        } catch(NameAlreadyTakenException e){
            message = new LoginMessage("Name already taken!", false);
        } catch(GameFullException e){
            message = new LoginMessage("Game full!", false);
        } catch(NameNotFoundException e){
            message = new LoginMessage("Name not found!", false);
        } catch(AlreadyLoggedException e){
            message = new LoginMessage("Player already logged", false);
        } catch(NameEmptyException e){
            message = new LoginMessage("Empty name not allowed!", false);
        }
        finally {
            serverView.update(message);
        }
        if (gameModel.isMatchInProgress()){
            gameModel.getMatch().notifyStartMatchUpdate();
            gameModel.getMatch().notifyFullUpdateAllPlayers();  //could be refined
        }
        finalCleaning();
    }

    /**
     * Manages the selection of a square by a player.
     * According to the state of the player, calls a proper method on the model to update it.
     * @param selection the index of the square among the list of selectable squares of the player
     * @param serverView the server view corresponding to the player which has made the selection
     */
    public synchronized void squareSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
        Square sq;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                sq = p.getSelectableSquares().get(selection);
            } catch (IndexOutOfBoundsException e) {
                selectionError(serverView);
                return;
            }
            switch (p.getState()) {
                case GRAB_THERE:
                    gameModel.grabThere(p, sq);
                    break;
                case MOVE_THERE:
                    gameModel.moveMeThere(p, sq);
                    break;
                case SHOOT_TARGET:
                    gameModel.shootTarget(p, null, sq);
                    break;
                case USE_POWERUP:
                    gameModel.choosePowerUpTarget(p, null, sq);
                    break;
                default:
                    selectionError(serverView);
                    return;
            }

        } catch (NoSuchObserverException e) {
            //todo: what if there is no such serverView?
        }
        finalCleaning();
    }

    /**
     * Manages the selection of an action by a player.
     * According to the state of the player, calls a proper method on the model to update it.
     * @param selection the index of the action among the list of selectable actions of the player
     * @param serverView the server view corresponding to the player which has made the selection
     */
    public synchronized void actionSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
        Action act;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                act = p.getSelectableActions().get(selection);
            } catch (IndexOutOfBoundsException e) {
                selectionError(serverView);
                return;
            }
            switch (p.getState()) {
                case CHOOSE_ACTION:
                    gameModel.performAction(p, act);
                    break;
                default:
                    selectionError(serverView);
                    return;
            }
        } catch (NoSuchObserverException e){
            //todo (should not occur)
        }
        finalCleaning();
    }

    /**
     * Manages the selection of a player by a player.
     * According to the state of the player, calls a proper method on the model to update it.
     * @param selection the index of the player among the list of selectable players of the player
     * @param serverView the server view corresponding to the player which has made the selection
     */
    public synchronized void playerSelected(int selection, ServerView serverView) {
    removeTimer(serverView);
        Player pl;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                pl = p.getSelectablePlayers().get(selection);
            } catch (IndexOutOfBoundsException e) {
                selectionError(serverView);
                return;
            }
            switch (p.getState()) {
                case SHOOT_TARGET:
                    gameModel.shootTarget(p, pl, null);
                    break;
                case USE_POWERUP:
                    gameModel.choosePowerUpTarget(p, pl, null);
                    break;
                default:
                    selectionError(serverView);
                    return;
            }
        } catch (NoSuchObserverException e){
            //todo
        }
        finalCleaning();
    }

    /**
     * Manages the selection of a weapon by a player.
     * According to the state of the player, calls a proper method on the model to update it.
     * @param selection the index of the weapon among the list of selectable weapons of the player
     * @param serverView the server view corresponding to the player which has made the selection
     */
    public synchronized void weaponSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
        Weapon wp;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                wp = p.getSelectableWeapons().get(selection);
            } catch (IndexOutOfBoundsException e) {
                selectionError(serverView);
                return;
            }
            switch (p.getState()) {
                case GRAB_WEAPON:
                    gameModel.grabWeapon(p, wp);
                    break;
                case DISCARD_WEAPON:
                    gameModel.discardWeapon(p, wp);
                    break;
                case SHOOT_WEAPON:
                    gameModel.shootWeapon(p, wp);
                    break;
                case RELOAD:
                    gameModel.reloadWeapon(p, wp);
                    break;
                default:
                    selectionError(serverView);
                    return;
            }
        } catch (NoSuchObserverException e){
            //todo
        }
        finalCleaning();
    }

    /**
     * Manages the selection of a mode by a player.
     * According to the state of the player, calls a proper method on the model to update it.
     * @param selection the index of the mode among the list of selectable modes of the player
     * @param serverView the server view corresponding to the player which has made the selection
     */
    public synchronized void modeSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
        Mode mod;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                mod = p.getSelectableModes().get(selection);
            } catch (IndexOutOfBoundsException e) {
                selectionError(serverView);
                return;
            }
            switch (p.getState()) {
                case CHOOSE_MODE:
                    gameModel.addMode(p, mod);
                    break;
                default:
                    selectionError(serverView);
                    return;
            }
        } catch (NoSuchObserverException e){
            //todo
        }
        finalCleaning();
    }

    /**
     * Manages the selection of a command by a player.
     * According to the state of the player, calls a proper method on the model to update it.
     * @param selection the index of the command among the list of selectable commands of the player
     * @param serverView the server view corresponding to the player which has made the selection
     */
    public synchronized void commandSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
        Command cmd;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                cmd = p.getSelectableCommands().get(selection);
            } catch (IndexOutOfBoundsException e) {
                selectionError(serverView);
                return;
            }
            if (cmd == Command.OK) {
                switch (p.getState()) {
                    case PAYING:
                        gameModel.completePayment(p);
                        break;
                    case CHOOSE_ACTION:
                        gameModel.endTurn();
                        break;
                    case CHOOSE_MODE:
                        gameModel.confirmModes(p);
                        break;
                    case USE_POWERUP:
                        gameModel.dontUsePowerUp(p);
                        break;
                    case SHOOT_TARGET:
                        gameModel.shootTarget(p, null, null);
                        break;
                    case RELOAD:
                        gameModel.nextMicroAction();
                        break;
                    default:
                        selectionError(serverView);
                        return;
                }
            } else if (cmd == Command.BACK) {
                gameModel.restore();
            }
        } catch (NoSuchObserverException e){
            //todo
        }
        finalCleaning();
    }

    /**
     * Manages the selection of a color by a player.
     * According to the state of the player, calls a proper method on the model to update it.
     * @param selection the index of the color among the list of selectable colors of the player
     * @param serverView the server view corresponding to the player which has made the selection
     */
    public synchronized void colorSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
        AmmoColor col;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                col = p.getSelectableColors().get(selection);
            } catch (IndexOutOfBoundsException e) {
                selectionError(serverView);
                return;
            }
            switch (p.getState()) {
                case PAYING_ANY:
                    gameModel.payAny(p, col);
                    break;
                default:
                    selectionError(serverView);
                    return;
            }
        } catch (NoSuchObserverException e){
            //todo
        }
        finalCleaning();
    }

    /**
     * Manages the selection of a powerup by a player.
     * According to the state of the player, calls a proper method on the model to update it.
     * @param selection the index of the powerup among the list of selectable powerups of the player
     * @param serverView the server view corresponding to the player which has made the selection
     */
    public synchronized void powerUpSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
        PowerUp po;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                po = p.getSelectablePowerUps().get(selection);
            } catch (IndexOutOfBoundsException e) {
                selectionError(serverView);
                return;
            }
            switch (p.getState()) {
                case SPAWN:
                    gameModel.spawn(p, po);
                    break;
                case PAYING:
                    gameModel.payWith(p, po);
                    break;
                case PAYING_ANY:
                    gameModel.payAny(p, po);
                    break;
                case USE_POWERUP:
                    gameModel.usePowerUp(p, po);
                    break;
                default:
                    selectionError(serverView);
                    return;
            }
        } catch (NoSuchObserverException e){
            //todo (should not occur)
        }
        finalCleaning();
    }

    /**
     * Disconnects the server view and its corresponding player.
     * When a player is disconnected remains in the match (can receive damages), but skips his turns.
     * @param serverView the server view to disconnect
     */
    public synchronized void disconnectPlayer(ServerView serverView){
        try{
            Player playerToDisconnect = gameModel.getPlayerByObserver(serverView);
            gameModel.deactivate(playerToDisconnect);
            gameModel.detach(serverView);
            serverViews.remove(serverView);
            logger.info(PLAYER_DISCONNECTED_OK);
        } catch(NoSuchObserverException e){
            logger.warning(PLAYER_ALREADY_DISCONNECTED);
        } finally {
            serverView.shutDown();
            removeTimer(serverView);
            if(checkStopMatch()){
                gameModel.endGame();
            }
        }
    }

    /**
     * Checks if there are to few players and the match has to be stopped.
     * @return true if the match has to be stopped
     */
    private boolean checkStopMatch(){
        if(gameModel.tooFewPlayers() && gameModel.isMatchInProgress()){
            return true;
        }
        return false;
    }

    /**
     * Starts the match
     */
    public void startMatch(){
        gameModel.startMatch();
        finalCleaning();
    }

    /**
     * Checks if the match has to be started (there are 5 players) or could start, because there are at least 3 players.
     * In this case it starts the login timer.
     */
    private void checkStart(){
        if(!gameModel.tooFewPlayers() && gameModel.howManyActivePlayers() < 5 && !loginTimerStarted && !gameModel.isMatchInProgress()){
            loginTimer.schedule(new LoginTimer(this), loginTimerDuration);
            loginTimerStarted = true;
        } else if(gameModel.howManyActivePlayers() == 5){
            startMatch();
            loginTimer.cancel();
            loginTimerStarted = false;
        }
        //todo notificare del conto alla rovescia?
    }

    /**
     * Gets the game model
     * @return the reference to the game model
     */
    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * Sets the flag of the login timer to false
     */
    public void setLoginTimerStarted(){
        loginTimerStarted = false;
    }

    /**
     * It is the final control done after every player's interaction and update of model.
     * Disconnect all the players set to disconnect.
     * Checks if the player from which an interaction is expected are actually online.
     * If this is not the case, their action is simulated.
     * Finally it starts the timeout for players online from which an interaction is required.
     */
    public synchronized void finalCleaning(){
        /*
        for (ServerView serverView : toDisconnectList){
            disconnectPlayer(serverView);
        }
        */

        int i = 0;
        while (i<toDisconnectList.size()){
            disconnectPlayer(toDisconnectList.get(i));
            i++;
        }


        toDisconnectList.clear();

        if (!gameModel.isGameOver()) {
            gameModel.notifyConnectionUpdate();
            if (gameModel.isMatchInProgress()) {
                while (!gameModel.getActivePlayers().containsAll(gameModel.getWaitingFor())) {
                    List<Player> toFakeList = new ArrayList<>();
                    for (Player p : gameModel.getWaitingFor()) {
                        if (!gameModel.getActivePlayers().contains(p)) {
                            toFakeList.add(p);
                        }
                    }
                    for (Player p : toFakeList) {
                        gameModel.fakeAction(p);
                    }
                }
                for (Player p : gameModel.getWaitingFor()) {
                    addTimer(gameModel.getObserver(p)); //addTimer checks if there is no timer active for the corresponding observer
                }
                gameModel.getMatch().notifySelectablesUpdateAllPlayers();
            }
        } else {
            for (ServerView serverView : new ArrayList<>(serverViews)){
                disconnectPlayer(serverView);
            }
        }
        logger.info(FINAL_CLEANING_DONE);
    }

    /**
     * Adds the input timer for a server view, only if no one was already present
     * @param observer the server view involved by the timer
     */
    private void addTimer(Observer observer){
        if(timers.get(observer) == null){
            Timer timer = new Timer();
            timers.put(observer, timer);
            timer.schedule(new InputTimer((ServerView)observer), inputTimerDuration);
        }
    }

    /**
     * Removes and stops the timer for a server view
     * @param observer the server view to which the timer belonged
     */
    private void removeTimer(Observer observer){
        if(timers.get(observer) != null){
            timers.get(observer).cancel();
        }
        timers.remove(observer);
    }

    /**
     * Notifies and updates observers in case of a selection error.
     * This kind of error should never be possible in normal conditions,
     * because a player can only selects among the selectable items sent by the model.
     * @param observer the server view responsible of the error
     */
    private void selectionError(Observer observer){
        try {
            MessageVisitable message = new GenericMessage("Selection not permitted, retry.");
            gameModel.notify(message, observer);
            logger.warning(WRONG_SELECTION_FROM_PLAYER + gameModel.getPlayerByObserver(observer).getName());
        } catch (NoSuchObserverException e){
            logger.warning(WRONG_SELECTION_PLAYER_NOT_EXIST);
        }
        gameModel.getMatch().notifyFullUpdateAllPlayers();
    }
}
