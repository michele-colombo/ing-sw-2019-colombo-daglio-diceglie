package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.ServerView;
import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.communication.events.*;
import it.polimi.ingsw.communication.message.DisconnectionMessage;
import it.polimi.ingsw.communication.message.GenericMessage;
import it.polimi.ingsw.communication.message.LoginMessage;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.observer.Observer;
import it.polimi.ingsw.server.controller.timer.InputTimer;
import it.polimi.ingsw.server.controller.timer.LoginTimer;
import java.util.*;


public class Controller implements VisitorServer {

    private final GameModel gameModel;
    private final Timer loginTimer;
    private boolean loginTimerStarted;
    private Map<Observer, Timer> timers;


    public Controller(GameModel gameModel){
        this.gameModel = gameModel;
        this.loginTimer = new Timer();
        this.loginTimerStarted = false;
        this.timers = new HashMap<>();
    }

    @Override
    public synchronized void visit(LoginEvent loginEvent, ServerView serverView){
        LoginMessage message = new LoginMessage("Login successful!", true, false);
        try{
            Player newPlayer = new Player(loginEvent.getName());
            gameModel.addPlayer(newPlayer);
            gameModel.attach(newPlayer, serverView);
            timers.put(serverView, null);
            checkStart();
            //todo se ci sono 5 player e la partita non è ancora iniziata, allora deve iniziare
            //todo se ci sono 3 player attivi e partita non iniziata, starta il countdown

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
            gameModel.notify(message, serverView);
        }
    }

    @Override
    public synchronized void visit(SquareSelectedEvent squareSelectedEvent, ServerView serverView) {
        removeObserverFromTimers(serverView);
        Square sq;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                sq = p.getSelectableSquares().get(squareSelectedEvent.getSelection());
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
        startTimers();
        //todo: update all players
    }

    @Override
    public synchronized void visit(ActionSelectedEvent actionSelectedEvent, ServerView serverView) {
        removeObserverFromTimers(serverView);
        Action act;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                act = p.getSelectableActions().get(actionSelectedEvent.getSelection());
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
        startTimers();
        //todo: update
    }

    @Override
    public synchronized void visit(PlayerSelectedEvent playerSelectedEvent, ServerView serverView) {
    removeObserverFromTimers(serverView);
        Player pl;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                pl = p.getSelectablePlayers().get(playerSelectedEvent.getSelection());
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
        startTimers();
        //todo: update
    }

    @Override
    public synchronized void visit(WeaponSelectedEvent weaponSelectedEvent, ServerView serverView) {
        removeObserverFromTimers(serverView);
        Weapon wp;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                wp = p.getSelectableWeapons().get(weaponSelectedEvent.getSelection());
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
        startTimers();
        //todo: update
    }

    @Override
    public synchronized void visit(ModeSelectedEvent modeSelectedEvent, ServerView serverView) {
        removeObserverFromTimers(serverView);
        Mode mod;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                mod = p.getSelectableModes().get(modeSelectedEvent.getSelection());
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
        startTimers();
        //todo: update
    }

    @Override
    public synchronized void visit(CommandSelectedEvent commandSelectedEvent, ServerView serverView) {
        removeObserverFromTimers(serverView);
        Command cmd;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                cmd = p.getSelectableCommands().get(commandSelectedEvent.getSelection());
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
        startTimers();
        //todo:  update
    }

    @Override
    public synchronized void visit(ColorSelectedEvent colorSelectedEvent, ServerView serverView) {
        removeObserverFromTimers(serverView);
        AmmoColor col;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                col = p.getSelectableColors().get(colorSelectedEvent.getSelection());
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
        startTimers();
        //todo: update
    }

    @Override
    public synchronized void visit(PowerUpSelectedEvent powerUpSelectedEvent, ServerView serverView) {
        removeObserverFromTimers(serverView);
        PowerUp po;
        try {
            Player p = gameModel.getPlayerByObserver(serverView);
            try {
                po = p.getSelectablePowerUps().get(powerUpSelectedEvent.getSelection());
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
        startTimers();
        //todo: update
    }

    public void disconnectPlayer(Observer observer){    //todo: synchronized??
        try{
            Player disconnected = gameModel.getPlayerByObserver(observer);
            if(gameModel.getActivePlayers().contains(disconnected)){    //should always be true, right?
                gameModel.detach(observer);
                gameModel.notifyAll(new DisconnectionMessage("Player " +disconnected.getName() + " has disconnected!"));
            }
            if(gameModel.isMatchInProgress()){
                gameModel.fakeAction(disconnected);
            }//todo controllo se devo togliere il timer (michele: "looks like this 'todo' has already been satisfied (look below)")
        } catch(NoSuchObserverException e){
            System.out.println("Player already disconnected!");
        } finally {
            checkStopMatch();
            removeObserverFromTimers(observer);
            startTimers();
        }
    }

    public void checkStopMatch(){
        if(gameModel.tooFewPlayers() && gameModel.isMatchInProgress()){
            //todo stop match and print scores
            //todo gamemodel.gameOver()
            List<Player> players = gameModel.getMatch().getWinners();
            String rank = gameModel.getMatch().getRank();
            //todo creare un messaggio di disconnessione e un altro che incapsula la classifica finale
            //todo fermare tutti i timer
        }
    }

    public void startMatch(){
        gameModel.startMatch();
        gameModel.notifyAll(new GenericMessage("Match started!"));
        startTimers();
        //todo notificare i player che la partita inizia
    }

    public void checkStart(){
        if(!gameModel.tooFewPlayers() && gameModel.howManyActivePlayers() < 5 && !loginTimerStarted){
            loginTimer.schedule(new LoginTimer(this), 15000);
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
    public void startTimers(){
        while (gameModel.getActivePlayers().containsAll(gameModel.getWaitingFor())){
            List<Player> toFakeList = new ArrayList<>();
            for (Player p : gameModel.getWaitingFor()){
                if (!gameModel.getActivePlayers().contains(p)){
                    toFakeList.add(p);
                }
            }
            for (Player p : toFakeList){
                gameModel.fakeAction(p);
            }
        }
        for (Player p : gameModel.getWaitingFor()){
            addTimer(gameModel.getObserver(p)); //addTimer checks if there is no timer active for the corresponding observer
        }
    }

    public void addTimer(Observer observer){
        if(timers.get(observer) == null){
            Timer timer = new Timer();
            timers.replace(observer, timer);
            timers.get(observer).schedule(new InputTimer((ServerView)observer), 12000); //todo sistemare i cast
        }
    }

    public void removeTimer(Observer observer){
        if(timers.get(observer) != null){
            timers.get(observer).cancel();
        }
        timers.replace(observer, null);
    }

    public void removeObserverFromTimers(Observer observer){
        removeTimer(observer);
        timers.remove(observer);
    }
}