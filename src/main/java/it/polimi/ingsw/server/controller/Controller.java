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
    private int loginTimerDuration = 15000;
    private int inputTimerDuration = 20000;
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

    public void setLoginTimerDuration(int loginTimerDuration) {
        if (loginTimerDuration > 0){
            this.loginTimerDuration = loginTimerDuration;
        }
    }

    public void setInputTimerDuration(int inputTimerDuration) {
        if (inputTimerDuration > 0){
            this.inputTimerDuration = inputTimerDuration;
        }
    }

    public synchronized void login(String name, ServerView serverView){
        LoginMessage message = new LoginMessage("Login successful!", true);
        try{
            String newName = name.trim();
            Player newPlayer = gameModel.addPlayer(newName);
            gameModel.attach(newPlayer, serverView);
            checkStart();
            System.out.println("[OK] player "+newName+" has correctly logged in");
            //todo se ci sono 5 player e la partita non è ancora iniziata, allora deve iniziare
            //todo se ci sono 3 player attivi e partita non iniziata, starta il countdown
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


    public synchronized void squareSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
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
    }

    public synchronized void actionSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
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
    }

    public synchronized void playerSelected(int selection, ServerView serverView) {
    removeTimer(serverView);
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
    }

    public synchronized void weaponSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
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
    }

    public synchronized void modeSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
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
    }

    public synchronized void commandSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
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
                    case RELOAD:
                        gameModel.nextMicroAction();
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
        //todo:  update
    }

    public synchronized void colorSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
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
        //todo: update
    }

    public synchronized void powerUpSelected(int selection, ServerView serverView) {
        removeTimer(serverView);
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
        //todo: update
    }

    public synchronized void disconnectPlayer(ServerView serverView){
        try{
            Player playerToDisconnect = gameModel.getPlayerByObserver(serverView);
            gameModel.deactivate(playerToDisconnect);
            gameModel.detach(serverView);
        } catch(NoSuchObserverException e){
            System.out.println("Player already disconnected!");
        } finally {
            removeTimer(serverView);
            checkStopMatch();

            System.out.println("I'm in controller.disconnectPlayer");

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
        //gameModel.getMatch().notifyStartMatchUpdate();
        //gameModel.getMatch().notifyFullUpdateAllPlayers();
        finalCleaning();
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
    public synchronized void finalCleaning(){//era synchronized ma dava problemi
        for (ServerView serverView : toDisconnectList){
            disconnectPlayer(serverView);
        }
        toDisconnectList.clear();
        gameModel.notifyConnectionUpdate();
        if(gameModel.isMatchInProgress()) {
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
            //gameModel.getMatch().notifyFullUpdateAllPlayers();
            gameModel.getMatch().notifySelectablesUpdateAllPlayers();
        }
        System.out.println("[OK] final cleaning done");


    }

    private void addTimer(Observer observer){
        if(timers.get(observer) == null){
            Timer timer = new Timer();
            timers.put(observer, timer);
            timer.schedule(new InputTimer((ServerView)observer), inputTimerDuration);
        }
    }

    private void removeTimer(Observer observer){
        if(timers.get(observer) != null){
            timers.get(observer).cancel();
        }
        timers.remove(observer);
    }
}
