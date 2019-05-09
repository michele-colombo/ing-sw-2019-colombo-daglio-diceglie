package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.server.message.MessageVisitable;
import it.polimi.ingsw.server.observer.Observable;
import it.polimi.ingsw.server.observer.Observer;
import java.util.*;


import static it.polimi.ingsw.PlayerState.*;

public class GameModel implements Observable {
    /**
     * List containing the active players (not disconnected) of the match
     */
    private List<Player> activePlayers;

    /**
     * List containing the inactive players (disconnected voluntarily or due to inactivity)
     */
    private List<Player> inactivePlayers;

    /**
     * List containing the players currently spawning (after death or before born)
     */
    private List<Player> spawningPlayers;

    /**
     * the current match in progress
     */
    private Match match;
    private boolean matchInProgress;
    private Backup currBackup;
    private Map<Player, Observer> observers;
    private final String backupName = "currentBackup";


    public GameModel(){
        activePlayers = new ArrayList<>();
        inactivePlayers = new ArrayList<>();
        spawningPlayers = new ArrayList<>();
        match = null;
        matchInProgress = false;
        currBackup = null;
        observers = new HashMap<>();
    }

    /**
     * Gets the current match in progress
     * @return the actual reference to match
     */
    public Match getMatch() {
        return match;
    }

    public boolean isMatchInProgress() {
        return matchInProgress;
    }

    public void addPlayer (Player p) throws NameAlreadyTakenException, GameFullException {
        if((activePlayers.size() + inactivePlayers.size()) < 5){
            if(!nameTaken(p.getName())){
                activePlayers.add(p);
            } else{
                throw new NameAlreadyTakenException();
            }
        } else {
            throw new GameFullException();
        }
    }

    /**
     *
     * @return true if a new match begins, false if a saved one is restored
     */
    public boolean startMatch(){
        if (Backup.isBackupAvailable(backupName)){
            Backup tempBackup = Backup.initFromFile(backupName);
            if (tempBackup.getPlayerNames().containsAll(getAllPlayerNames()) && getAllPlayerNames().containsAll(tempBackup.getPlayerNames())){
                //all names match
                int layoutConfig = tempBackup.getLayoutConfig();
                match = new Match(layoutConfig, 8);
                for (Player p : allPlayers()){ //activePlayers only? or all players?
                    match.addPlayer(p);
                }
                tempBackup.restore(match);
                matchInProgress = true;
                actionCompleted();
                return false;
            } else {
                //saved backup has at least one different name
                startNewMatch();
            }
        } else {
            //there is no backup available
            startNewMatch();
        }
        return true;
    }

    public void startNewMatch(){
        //todo: choose layout configuration
        int layoutConfig = 2;
        //todo: take skulls number from config file
        match = new Match(layoutConfig);
        for (Player p : allPlayers()){ //activePlayers only? or all players?
            match.addPlayer(p);
            p.setState(IDLE);
            p.resetSelectables();
        }
        match.getLayout().refillAll(match.getStackManager());
        for (Player p : allPlayers()){
            p.setColor(PlayerColor.values()[allPlayers().indexOf(p)]);
        }
        matchInProgress = true;
        beginNextTurn();
    }

    //test only method!
    public void resumeMatchFromFile(String path, String name){
        Backup savedBackup = Backup.initFromFile(path, name);
        int layoutConfig = savedBackup.getLayoutConfig();
        match = new Match(layoutConfig);
        for (String playerName : savedBackup.getPlayerNames()){
            try {
                Player newPlayer = new Player(playerName);
                addPlayer(newPlayer);
                match.addPlayer(newPlayer);
            } catch (NameAlreadyTakenException | GameFullException e1){

            }
        }
        savedBackup.restore(match);
        matchInProgress = true;
        actionCompleted();
    }

    private void prepareForSpawning(Player p, boolean firstSpawn){
        p.addPowerUp(match.getStackManager().drawPowerUp());    //p.getPowerUps.size() can be 4 in this moment
        if (firstSpawn){
            p.addPowerUp(match.getStackManager().drawPowerUp());
        }
        p.setState(SPAWN);
        spawningPlayers.add(p);
        p.resetSelectables();
        p.setSelectablePowerUps(p.getPowerUps());
    }

    public void spawn(Player p, PowerUp po){
        p.removePowerUp(po);
        match.getStackManager().discardPowerUp(po);
        match.spawn(p, po.getColor());
        p.setBorn(true);    //could be moved inside Player.spawn()
        p.setState(IDLE);
        p.resetSelectables();
        spawningPlayers.remove(p);
        if (spawningPlayers.isEmpty()) {      //if there are no other player still (re)spawning
            beginNextTurn();
        }
    }

    private void beginNextTurn(){
        Player nextP;
        nextP = nextActivePlayer(match.getCurrentPlayer());
        if (!nextP.isBorn()){
            prepareForSpawning(nextP, true);
        } else {
            match.setCurrentPlayer(nextP);
            nextP.setState(CHOOSE_ACTION);
            nextP.resetSelectables();
            nextP.setSelectableActions(match.initSelectableActions(nextP));
            saveSnapshot(match);
        }
    }

    /**
     * Returns the next active player.
     * The call with null player is used to get the first player (beginning of the match),
     * otherwise currP must be contained in activePlayers
     * @param currP If not null, must be contained in activePlayers
     * @return Next active player
     */
    public Player nextActivePlayer(Player currP){
        //todo: check if frenzyOn && nextPlayer = lastPlayer
        int index;
        if (currP == null){
            return activePlayers.get(0);    //if there's no curr player, return the first of the list
        } else {
            index = activePlayers.indexOf(currP);
            assert (index != -1);
            if (index == activePlayers.size()-1) {
                return activePlayers.get(0);
            } else {
                return activePlayers.get(index + 1);
            }
        }
    }

    public void performAction(Player p, Action a){
        match.setCurrentAction(a);
        match.updateTurnStatus(a);
        try {
            match.getCurrentAction().getMicroActions().get(0).act(match, p);    //the microAction sets player state
        } catch (NextMicroActionException nmae){
            nextMicroAction();
        }
    }

    public void grabThere (Player p, Square s){
        p.setSquarePosition(s);
        if (s.collect(p, match)){     //collect sets player state, if necessary
            nextMicroAction();
        }
    }

    public void grabWeapon(Player p, Weapon w){
        match.getCurrentAction().setCurrWeapon(w);
        p.setPending(w.getDiscountedCost());
        if (p.getCredit().isEqual(p.getPending())){
            p.getPending().setZero();
            p.getCredit().setZero();
            p.addWeapon(w);
            match.getCurrentAction().getCurrSpawnSquare().removeWeapon(w);
            if (p.getWeapons().size() > 3){
                p.setState(DISCARD_WEAPON);
                p.resetSelectables();
                p.setSelectableWeapons(p.getWeapons());
            } else {
                nextMicroAction();
            }
        } else {
            p.setNextState(GRAB_WEAPON);
            setupForPaying(p);
        }
    }

    public void discardWeapon(Player p, Weapon w){
        match.getCurrentAction().getCurrSpawnSquare().addWeapon(w);
        p.removeWeapon(w);
        //unload
        nextMicroAction();
    }

    public void moveMeThere(Player p, Square s){
        p.setSquarePosition(s);
        nextMicroAction();
    }

    public void shootWeapon(Player p, Weapon w){
        match.getCurrentAction().setCurrWeapon(w);
        p.setState(CHOOSE_MODE);
        p.resetSelectables();
        p.setSelectableModes(w.getSelectableModes(match.getCurrentAction().getSelectedModes()));
    }

    public void addMode(Player p, Mode m){
        p.setPending(m.getCost());
        if (p.getCredit().isEqual(p.getPending())){
            p.getPending().setZero();
            p.getCredit().setZero();
            match.getCurrentAction().getSelectedModes().add(m);
            match.getCurrentAction().getCurrEffects().addAll(m.getEffects());
            p.setState(CHOOSE_MODE);
            p.resetSelectables();
            p.setSelectableModes(match.getCurrentAction().getCurrWeapon().getSelectableModes(match.getCurrentAction().getSelectedModes()));
            p.setSelectableCommands(Command.OK);
        } else {
            p.setNextState(CHOOSE_MODE);
            match.getCurrentAction().setCurrMode(m);
            setupForPaying(p);
        }
    }

    public void confirmModes(Player p){
        p.setState(SHOOT_TARGET);
        p.resetSelectables();
        p.setLoad(match.getCurrentAction().getCurrWeapon(), false);
        try {
            match.getCurrentAction().getCurrEffects().get(0).start(p, match);
        } catch (ApplyEffectImmediatelyException e){
            shootTarget(p, null, null); //todo: check
        }
    }

    public void shootTarget(Player p, Player targetP, Square targetS){
        List<Effect> effects = match.getCurrentAction().getCurrEffects();
        Effect temp = effects.get(0).applyOn(p, targetP, targetS, match);
        if (temp != null){
            effects.add(1, temp);
        }
        effects.remove(0);
        if (effects.isEmpty()){
            nextMicroAction();
        } else {
            p.setState(SHOOT_TARGET);
            p.resetSelectables();
            try {
                effects.get(0).start(p, match);
            } catch (ApplyEffectImmediatelyException e){
                shootTarget(p, null, null); //todo: check
            }
        }
    }

    private void setupForPaying(Player p){
        p.setState(PAYING);
        p.resetSelectables();
        if ((p.getPending().subtract(p.getCredit())).lessEqual(p.getWallet())){     //the player can complete the payment at the moment
            if (p.getPowerUpsOfColors(p.getPending().subtract(p.getCredit())).isEmpty()){
                completePayment(p);
            } else {
                p.setSelectableCommands(Command.OK);
                p.setSelectablePowerUps(p.getPowerUpsOfColors(p.getPending().subtract(p.getCredit())));
            }
        } else {
            p.setSelectablePowerUps(p.getPowerUpsOfColors(p.getPending().subtract(p.getCredit())));
        }
    }

    public void payWith(Player p, PowerUp po){
        p.setCredit(p.getCredit().sum(new Cash(po.getColor(), 1)));
        p.removePowerUp(po);
        match.getStackManager().discardPowerUp(po);
        setupForPaying(p);
    }

    public void completePayment(Player p){
        if ((p.getPending().subtract(p.getCredit())).lessEqual(p.getWallet())){
            p.getWallet().pay(p.getPending().subtract(p.getCredit()));
            p.setCredit(p.getPending());
        }
        switch (p.getNextState()){
            case RELOAD:
                reloadWeapon(p, match.getCurrentAction().getCurrWeapon());
                break;
            case GRAB_WEAPON:
                grabWeapon(p, match.getCurrentAction().getCurrWeapon());
                break;
            case CHOOSE_MODE:
                addMode(p, match.getCurrentAction().getCurrMode());
                break;
        }
    }

    public void reloadWeapon(Player p, Weapon w){
        p.setPending(w.getCost());
        if (p.getCredit().isEqual(p.getPending())){
            p.getPending().setZero();
            p.getCredit().setZero();
            p.setLoad(w, true);
            nextMicroAction();
        } else {
            p.setNextState(RELOAD);
            match.getCurrentAction().setCurrWeapon(w);
            setupForPaying(p);
        }
    }

    public void usePowerUp (Player p, PowerUp po){
        p.removePowerUp(po);
        match.getStackManager().discardPowerUp(po);
        match.getCurrentAction().setCurrPowerUp(po);
        switch (po.getType()){
            case TARGETING_SCOPE:
                p.setState(PAYING_ANY);
                p.resetSelectables();
                p.setSelectablePowerUps(p.getPowerUps());
                p.setSelectableColors(p.getWallet().getColors());
                break;
            case TAGBACK_GRENADE:
                match.getCurrentAction().incrWaitingFor(-1);
                p.setState(IDLE);
                p.resetSelectables();
                useCurrentPowerUp(p);
                break;
            case ACTION_POWERUP:
                p.setState(USE_POWERUP);
                p.resetSelectables();
                useCurrentPowerUp(p);
                break;
        }
    }

    public void payAny(Player p, PowerUp po){
        p.removePowerUp(po);
        match.getStackManager().discardPowerUp(po);
        p.setState(USE_POWERUP);
        p.resetSelectables();
        useCurrentPowerUp(p);
    }

    public void payAny(Player p, Color c){
        p.getWallet().pay(new Cash(c, 1));
        p.setState(USE_POWERUP);
        p.resetSelectables();
        useCurrentPowerUp(p);
    }

    public void useCurrentPowerUp(Player p){
        match.getCurrentAction().addEffects(match.getCurrentAction().getCurrPowerUp().getEffects());
        try {
            match.getCurrentAction().getCurrEffects().get(0).start(p, match);
        } catch (ApplyEffectImmediatelyException e){
            match.getCurrentAction().getCurrEffects().get(0).applyOn(p, match.getCurrentPlayer(), null, match);
        }
    }

    public void choosePowerUpTarget(Player p, Player targetP, Square targetS){
        List<Effect> effects = match.getCurrentAction().getCurrEffects();
        Effect temp = effects.get(0).applyOn(p, targetP, targetS, match);
        if (temp != null){
            effects.add(1, temp);
        }
        effects.remove(0);
        if (match.getCurrentAction().getWaitingFor() == 0) {
            if (effects.isEmpty()) {
                nextMicroAction();
            } else {
                p.setState(USE_POWERUP);
                p.resetSelectables();
                try {
                    effects.get(0).start(p, match);
                } catch (ApplyEffectImmediatelyException e){
                    shootTarget(p, null, null);
                }
            }
        } else {
            p.setState(USE_POWERUP);
            p.resetSelectables();
        }
    }

    public void dontUsePowerUp(Player p){
        p.setState(IDLE);
        p.resetSelectables();
        match.getCurrentAction().incrWaitingFor(-1);
        if (match.getCurrentAction().getWaitingFor()<0) match.getCurrentAction().setWaitingFor(0);
        if (match.getCurrentAction().getWaitingFor() == 0) {
            nextMicroAction();
        }
    }

    public void nextMicroAction(){
        List<MicroAction> temp = match.getCurrentAction().getMicroActions();
        temp.remove(0);
        if (temp.isEmpty()){
            actionCompleted();
        } else {
            try {
                temp.get(0).act(match, match.getCurrentPlayer());
            } catch (NextMicroActionException nmae){
                nextMicroAction();
            }
        }
    }

    public void actionCompleted(){
        Player p = match.getCurrentPlayer();
        List<Action> selectableActions = match.createSelectablesAction(match.getCurrentPlayer());
        if (selectableActions.isEmpty()){
            endTurn();
        } else {
            p.setState(CHOOSE_ACTION);
            p.resetSelectables();
            p.setSelectableActions(selectableActions);
            if (match.isTurnCompletable()){
                p.setSelectableCommands(Command.OK);
            }
            saveSnapshot(match);
        }
    }

    private void saveSnapshot(Match match){
        currBackup = new Backup(match);
        currBackup.saveOnFile(backupName);
    }

    public void restore(){
        currBackup.restore(match);
        actionCompleted();
    }

    public void endTurn(){
        match.getCurrentPlayer().setState(IDLE);
        match.getCurrentPlayer().resetSelectables();
        match.getLayout().refillAll(match.getStackManager());
        List<Player> deadPlayers = match.endTurnCheck();
        if (deadPlayers.isEmpty()){
            beginNextTurn();
        } else {
            for (Player p : deadPlayers) {
                prepareForSpawning(p, false);
            }
        }
    }

    private boolean nameTaken(String name){
        List<Player> allPlayers = allPlayers();
        for(Player p : allPlayers){
            if(p.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    private boolean colorTaken(PlayerColor color){
        List<Player> allPlayers = allPlayers();
        for(Player p : allPlayers){
            if(p.getColor() == color){
                return true;
            }
        }
        return false;
    }

    public int getNumberOfPlayers(){
        return activePlayers.size();
    }

    public void attach(Player player, Observer observer){
        if(observers.containsKey(player)){
            observers.replace(player, observer);
            activePlayers.add(player);
            inactivePlayers.remove(player);
        } else {
            observers.put(player, observer);
        }
    }

    public void detach(Observer observer){
        try {
            Player tempPlayer = getPlayerByObserver(observer);
            observers.remove(observer);
            inactivePlayers.add(tempPlayer);
            activePlayers.remove(tempPlayer);
        } catch (NoSuchObserverException e){

        }
    }

    public Player getPlayerByObserver(Observer observer) throws NoSuchObserverException {
        for (Map.Entry<Player, Observer> entry : observers.entrySet()){
            if (observer == entry.getValue()){
                return entry.getKey();
            }
        }
        throw new NoSuchObserverException();
    }

    public boolean alreadyActive(String name){
        for(Player p : activePlayers){
            if(p.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public List<Player> allPlayers(){
        List<Player> allPlayers = new ArrayList<>();
        allPlayers.addAll(activePlayers);
        allPlayers.addAll(inactivePlayers);
        return allPlayers;
    }

    public void relogin(String name) throws NameNotFoundException, AlreadyLoggedException{
        if(!nameTaken(name)){
            throw new NameNotFoundException();
        } else if(alreadyActive(name)){
            throw new AlreadyLoggedException();
        }
    }

    public Player getPlayerByName(String name){
        List<Player> allPlayers = allPlayers();
        for(Player p : allPlayers){
            if(p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }



    public List<String> getAllPlayerNames(){
        List<String> result = new ArrayList<>();
        for (Player p : allPlayers()){
            result.add(p.getName());
        }
        return result;
    }

    public void notify(MessageVisitable messageVisitable, Observer observer){
        observer.update(messageVisitable);
    }
}
