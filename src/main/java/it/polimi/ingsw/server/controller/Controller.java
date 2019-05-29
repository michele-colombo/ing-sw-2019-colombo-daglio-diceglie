package it.polimi.ingsw.server.controller;

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

import static it.polimi.ingsw.server.ServerMain.*;


public class Controller {

    private final GameModel gameModel;
    private final Timer loginTimer;
    private boolean loginTimerStarted;
    private Map<Observer, Timer> timers;
    private long loginTimerDuration = 15000;
    private long inputTimerDuration = 1000000;
    private List<ServerView> toDisconnectList;


    public Controller(GameModel gameModel){
        this.gameModel = gameModel;
        this.loginTimer = new Timer();
        this.loginTimerStarted = false;
        this.timers = new HashMap<>();
        toDisconnectList = new ArrayList<>();
    }

    public void setToDisconnect(ServerView serverView){
        if (!toDisconnectList.contains(serverView)){
            toDisconnectList.add(serverView);
        }
    }

    public void setLoginTimerDuration(long loginTimerDuration) {
        if (loginTimerDuration > 0){
            this.loginTimerDuration = loginTimerDuration;
        }
    }

    public void setInputTimerDuration(long inputTimerDuration) {
        if (inputTimerDuration > 0){
            this.inputTimerDuration = inputTimerDuration;
        }
    }

    public synchronized void login(String name, ServerView serverView){
        System.out.println("Message received");
        LoginMessage message = new LoginMessage("Login successful!", true, false);
        try{
            Player newPlayer = gameModel.addPlayer(name);
            gameModel.attach(newPlayer, serverView);
            timers.put(serverView, null);
            checkStart();
            System.out.println("Player "+name+" has correctly logged in");
            //todo se ci sono 5 player e la partita non è ancora iniziata, allora deve iniziare
            //todo se ci sono 3 player attivi e partita non iniziata, starta il countdown
            gameModel.notifyConnectionUpdate();
        } catch(NameAlreadyTakenException e){
            message = new LoginMessage("Name already taken!", false, false);
        } catch(GameFullException e){
            message = new LoginMessage("Game full!", false, true);
        } catch(NameNotFoundException e){
            message = new LoginMessage("Name not found!", false, false);
        } catch(AlreadyLoggedException e){
            message = new LoginMessage("Player already logged", false, true);
        }
        finally {
            serverView.update(message);
        }
    }


    public synchronized void squareSelected(int selection, ServerView serverView) {
        removeObserverFromTimers(serverView);
        Square sq;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                sq = p.getSelectableSquares().get(selection);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("wrong square selection");
                return; //todo: what to do? create a responseMessage? or errorMessage?
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
                    System.out.println("selected a square in the wrong state");
                    //todo: what to do?
                    return;
            }

        } catch (NoSuchObserverException e) {
            //todo: what if there is no such serverView?
        }
        finalCleaning();
        gameModel.getMatch().notifyFullUpdateAllPlayers();
    }

    public synchronized void actionSelected(int selection, ServerView serverView) {
        removeObserverFromTimers(serverView);
        Action act;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                act = p.getSelectableActions().get(selection);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("wrong action selection");
                return; //todo (should not occur)
            }
            switch (p.getState()) {
                case CHOOSE_ACTION:
                    gameModel.performAction(p, act);
                    break;
                default:
                    System.out.println("selected an action in the wrong state");
                    return; //todo (should not occur)
            }
        } catch (NoSuchObserverException e){
            //todo (should not occur)
        }
        finalCleaning();
        gameModel.getMatch().notifyFullUpdateAllPlayers();
        //todo: update
    }

    public synchronized void playerSelected(int selection, ServerView serverView) {
    removeObserverFromTimers(serverView);
        Player pl;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                pl = p.getSelectablePlayers().get(selection);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("wrong player selection");
                return; //todo
            }
            ;
            switch (p.getState()) {
                case SHOOT_TARGET:
                    gameModel.shootTarget(p, pl, null);
                    break;
                case USE_POWERUP:
                    gameModel.choosePowerUpTarget(p, pl, null);
                    break;
                default:
                    System.out.println("selected a player in the wrong state");
                    return; //todo
            }
        } catch (NoSuchObserverException e){
            //todo
        }
        finalCleaning();
        gameModel.getMatch().notifyFullUpdateAllPlayers();
        //todo: update
    }

    public synchronized void weaponSelected(int selection, ServerView serverView) {
        removeObserverFromTimers(serverView);
        Weapon wp;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                wp = p.getSelectableWeapons().get(selection);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("wrong weapon selection");
                return; //todo
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
                    System.out.println("selected a weapon in the wrong state");
                    return; //todo
            }
        } catch (NoSuchObserverException e){
            //todo
        }
        finalCleaning();
        gameModel.getMatch().notifyFullUpdateAllPlayers();
        //todo: update
    }

    public synchronized void modeSelected(int selection, ServerView serverView) {
        removeObserverFromTimers(serverView);
        Mode mod;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                mod = p.getSelectableModes().get(selection);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("wrong mode selection");
                return; //todo
            }
            switch (p.getState()) {
                case CHOOSE_MODE:
                    gameModel.addMode(p, mod);
                    break;
                default:
                    System.out.println("selected a mode in the wrong state");
                    return; //todo
            }
        } catch (NoSuchObserverException e){
            //todo
        }
        finalCleaning();
        gameModel.getMatch().notifyFullUpdateAllPlayers();
        //todo: update
    }

    public synchronized void commandSelected(int selection, ServerView serverView) {
        removeObserverFromTimers(serverView);
        Command cmd;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                cmd = p.getSelectableCommands().get(selection);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("wrong command selection");
                return; //todo
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
                    default:
                        System.out.println("selected OK in the wrong state");
                        return; //todo
                }
            } else if (cmd == Command.BACK) {
                gameModel.restore();
            }
        } catch (NoSuchObserverException e){
            //todo
        }
        finalCleaning();
        gameModel.getMatch().notifyFullUpdateAllPlayers();
        //todo:  update
    }

    public synchronized void colorSelected(int selection, ServerView serverView) {
        removeObserverFromTimers(serverView);
        AmmoColor col;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                col = p.getSelectableColors().get(selection);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("wrong color selection");
                return; //todo
            }
            ;
            switch (p.getState()) {
                case PAYING_ANY:
                    gameModel.payAny(p, col);
                    break;
                default:
                    System.out.println("selected a color in the wrong state");
                    return; //todo
            }
        } catch (NoSuchObserverException e){
            //todo
        }
        finalCleaning();
        gameModel.getMatch().notifyFullUpdateAllPlayers();
        //todo: update
    }

    public synchronized void powerUpSelected(int selection, ServerView serverView) {
        removeObserverFromTimers(serverView);
        PowerUp po;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                po = p.getSelectablePowerUps().get(selection);
            } catch (IndexOutOfBoundsException e) {
                //todo (should not occur)
                System.out.println("wrong powerup selection");
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
                    //todo (should not occur)
                    System.out.println("selected a powerup in the wrong state");
                    return;
            }
        } catch (NoSuchObserverException e){
            //todo (should not occur)
        }
        finalCleaning();
        gameModel.getMatch().notifyFullUpdateAllPlayers();
        //todo: update
    }

    public synchronized void disconnectPlayer(ServerView serverView){
        try{
            Player playerToDisconnect = gameModel.getPlayerByObserver(serverView);
            gameModel.deactivate(playerToDisconnect);
            gameModel.detach(serverView);
            if(gameModel.isMatchInProgress()){
                gameModel.fakeAction(playerToDisconnect);
            }//todo controllo se devo togliere il timer (michele: "looks like this 'todo' has already been satisfied (look below)")
        } catch(NoSuchObserverException e){
            System.out.println("Player already disconnected!");
        } finally {
            if (checkStopMatch()){

            } else {
                removeObserverFromTimers(serverView);
                //finalCleaning();
                gameModel.notifyConnectionUpdate();
                gameModel.getMatch().notifyFullUpdateAllPlayers();
                System.out.println(listToString(gameModel.getActivePlayers()));
                System.out.println(listToString(gameModel.getInactivePlayers()));
            }
        }
    }

    private boolean checkStopMatch(){
        if(gameModel.tooFewPlayers() && gameModel.isMatchInProgress()){
            //todo stop match and print scores
            //todo gamemodel.gameOver()
            List<Player> players = gameModel.getMatch().getWinners();
            String rank = gameModel.getMatch().getRank();
            //todo creare un messaggio di disconnessione e un altro che incapsula la classifica finale
            //todo fermare tutti i timer
        }
        return false;
    }

    public void startMatch(){
        gameModel.startMatch();
        gameModel.getMatch().notifyStartMatchUpdate();
        gameModel.getMatch().notifyFullUpdateAllPlayers();
        finalCleaning();
        //todo notificare i player che la partita inizia
    }

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

    public GameModel getGameModel() {
        return gameModel;
    }

    public void setLoginTimerStarted(){
        loginTimerStarted = false;
    }

    //todo: added by michele, check!
    public void finalCleaning(){//era synchronized ma dava problemi
        for (ServerView serverView : toDisconnectList){
            disconnectPlayer(serverView);
        }
        if(!gameModel.isMatchInProgress()) {
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
        }
    }

    private void addTimer(Observer observer){
        if(timers.get(observer) == null){
            Timer timer = new Timer();
            timers.put(observer, timer);
            timers.get(observer).schedule(new InputTimer((ServerView)observer), inputTimerDuration); //todo sistemare i cast
        }
    }

    private void removeTimer(Observer observer){
        //todo: check
        if(timers.get(observer) != null){
            timers.get(observer).cancel();
        }
        timers.replace(observer, null);
    }

    private void removeObserverFromTimers(Observer observer){
        removeTimer(observer);
        timers.remove(observer);        //when disconnecting the map entry MUST be completely removed
    }
}
