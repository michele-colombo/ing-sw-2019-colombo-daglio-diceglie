package it.polimi.ingsw.server.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.communication.message.ConnectionUpdateMessage;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.server.exceptions.*;
import it.polimi.ingsw.communication.message.MessageVisitable;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerColor;
import it.polimi.ingsw.server.observer.Observable;
import it.polimi.ingsw.server.observer.Observer;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;


import static it.polimi.ingsw.server.model.enums.PlayerState.*;

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
    private static final String BACKUP_NAME = "currentBackup";


    public GameModel(){
        activePlayers = new ArrayList<>();
        activePlayers.clear();
        inactivePlayers = new ArrayList<>();
        inactivePlayers.clear();
        spawningPlayers = new ArrayList<>();
        spawningPlayers.clear();
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

    public Player addPlayer (String name) throws NameAlreadyTakenException, GameFullException, NameNotFoundException, AlreadyLoggedException {
        if(!matchInProgress){
            return login(name);
        } else{
            return relogin(name);
        }
    }

    /**
     *
     * @return true if a new match begins, false if a saved one is restored
     */
    public boolean startMatch(){
        if (Backup.isBackupAvailable(BACKUP_NAME)){
            try {
                String jarPath = URLDecoder.decode(Backup.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
                String filePath = jarPath.substring(0, jarPath.lastIndexOf("/")) + File.separator + "backups"+ File.separator + BACKUP_NAME + ".json";
                InputStream url = new FileInputStream(filePath);
                Backup tempBackup = Backup.initFromFile(url);
                if (tempBackup.getPlayerNames().containsAll(getAllPlayerNames()) && getAllPlayerNames().containsAll(tempBackup.getPlayerNames())){
                    //all names match
                    int layoutConfig = tempBackup.getLayoutConfig();
                    match = new Match(layoutConfig);
                    for (String name : tempBackup.getPlayerNames()){
                        match.addPlayer(getPlayerByName(name));
                    }
                    tempBackup.restore(match);
                    match.setObservers(observers);
                    matchInProgress = true;
                    actionCompleted();
                    return false;
                } else {
                    //saved backup has at least one different name
                    startNewMatch();
                }
            } catch (IOException | NullPointerException e){
                System.out.println("[WARNING] Cannot open saved backup");
                startNewMatch();
                return true;
            }
        } else {
            //there is no backup available
            startNewMatch();
        }
        return true;
    }

    public void startNewMatch(){
        InputStream url = ServerMain.class.getClassLoader().getResourceAsStream("serverConfig.json");
        Scanner sc = new Scanner(url);
        JsonObject o = (JsonObject) new JsonParser().parse(sc.nextLine());
        JsonElement data =  o.get("layoutConfig");
        int layoutConfig = data.getAsInt();
        data =  o.get("skullNumber");
        int skulls = data.getAsInt();
        match = new Match(layoutConfig, skulls);
        for (Player p : allPlayers()){ //activePlayers only? or all players?
            match.addPlayer(p);
            p.setState(IDLE);
            p.resetSelectables();
        }
        match.setObservers(observers);
        match.getLayout().refillAll(match.getStackManager());
        for (Player p : allPlayers()){
            p.setColor(PlayerColor.values()[allPlayers().indexOf(p)]);
        }
        match.getFirstPlayer().setFirstPlayer(true);
        matchInProgress = true;
        beginNextTurn();
    }

    //test-only method!
    public void resumeMatchFromFile(InputStream url){


        Backup savedBackup = Backup.initFromFile(url);
        int layoutConfig = savedBackup.getLayoutConfig();
        match = new Match(layoutConfig);
        for (String playerName : savedBackup.getPlayerNames()){
            try {
                Player newPlayer = addPlayer(playerName);
                match.addPlayer(newPlayer);
            } catch(NameAlreadyTakenException | AlreadyLoggedException | GameFullException | NameNotFoundException e){
            }
        }
        savedBackup.restore(match);
        match.setObservers(observers);
        matchInProgress = true;
        actionCompleted();
    }

    private void prepareForSpawning(Player p, boolean firstSpawn){
        p.addPowerUp(match.getStackManager().drawPowerUp());    //p.getPowerUps.size() can be 4 in this moment
        if (firstSpawn){
            p.addPowerUp(match.getStackManager().drawPowerUp());
        }
        match.addWaitingFor(p);
        /*
        if (!activePlayers.contains(p)){
            match.addToFakeList(p);
        }
        */
        if (!spawningPlayers.contains(p)) spawningPlayers.add(p);
        p.setState(SPAWN);
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
        match.removeWaitingFor(p);
        spawningPlayers.remove(p);
        if (spawningPlayers.isEmpty()) {      //if there are no other player still (re)spawning
            beginNextTurn();
        }
    }

    private void beginNextTurn(){
        Player nextP;
        try {
            nextP = nextActivePlayer(match.getCurrentPlayer());
            if (!nextP.isBorn()) {
                prepareForSpawning(nextP, true);
            } else {
                match.setCurrentPlayer(nextP);
                match.addWaitingFor(nextP);
                //check not required, since nextP is certainly active
                nextP.setState(CHOOSE_ACTION);
                nextP.resetSelectables();
                nextP.setSelectableActions(match.initSelectableActions(nextP));
                saveSnapshot(match);
            }
        } catch (GameOverException e){
            e.printStackTrace();
            //todo: score killshotTrack points, end game, notify
            //todo: DELETE BACKUP
        }
    }

    /**
     * Returns the next active player.
     * The call with null player is used to get the first player (beginning of the match),
     * otherwise currP must be contained in activePlayers
     * @param currP If not null, must be contained in activePlayers
     * @return Next active player
     */
    public Player nextActivePlayer(Player currP) throws GameOverException{
        Player nextPlayer;
        nextPlayer = match.getNextPlayer(currP);
        while (!activePlayers.contains(nextPlayer)){
            nextPlayer = match.getNextPlayer(nextPlayer);
        }

        if (!nextPlayer.hasAnotherTurn()){
            throw new GameOverException();
        } else {
            if (match.isFrenzyOn()){
                nextPlayer.setAnotherTurn(false);
            }
        }
        return nextPlayer;
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
                p.setState(IDLE);
                p.resetSelectables();
                match.removeWaitingFor(p);
                try {
                    po.getEffects().get(0).start(p, match);
                } catch (ApplyEffectImmediatelyException e){
                    po.getEffects().get(0).applyOn(p, null, null, match);
                    //calls applyOn and not ChoosePowerUpTarget() because tagback always has just one effect
                    //or like that: po.getEffects().get(0).applyOn(p, match.getCurrentPlayer(), null, match);
                }
                if (match.getWaitingFor().isEmpty()){
                    match.addWaitingFor(match.getCurrentPlayer());
                    //in actionCompleted (called from nextMicroAction) is checked whether the currentPlayer is still active or not
                    //if not, is called endTurn()
                    nextMicroAction();
                }
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

    public void payAny(Player p, AmmoColor c){
        p.getWallet().pay(new Cash(c, 1));
        p.setState(USE_POWERUP);
        p.resetSelectables();
        useCurrentPowerUp(p);
    }

    public void useCurrentPowerUp(Player p){
        match.getCurrentAction().setCurrEffects(match.getCurrentAction().getCurrPowerUp().getEffects());
        try {
            match.getCurrentAction().getCurrEffects().get(0).start(p, match);
        } catch (ApplyEffectImmediatelyException e){
            choosePowerUpTarget(p, null, null);
        }
    }

    public void choosePowerUpTarget(Player p, Player targetP, Square targetS){
        List<Effect> effects = match.getCurrentAction().getCurrEffects();
        Effect temp = effects.get(0).applyOn(p, targetP, targetS, match);
        if (temp != null){
            effects.add(1, temp);
        }
        effects.remove(0);
        if (effects.isEmpty()) {
            nextMicroAction();
        } else {
            p.setState(USE_POWERUP);
            p.resetSelectables();
            try {
                effects.get(0).start(p, match);
            } catch (ApplyEffectImmediatelyException e){
                choosePowerUpTarget(p, null, null);
            }
        }
    }

    public void dontUsePowerUp(Player p){
        //if the current player  is (not) using the targeting scope, at this point he is certainly active (because otherwise the match would have been restored)
        //even if it were inactive, either the next microAction would have been a 'receive tagback grenade' (which is possibile even for an inactive current player)
        //or the action would have been completed (in actionCompleted the actual state of activity of current player is checked)
        //if it is another (not the current one) player (not) using the tagback grenade, current player could be inactive
        //in that case, the activity state of current player is chacked in ActionCompleted
        p.setState(IDLE);
        p.resetSelectables();
        match.removeWaitingFor(p);
        if (match.getWaitingFor().isEmpty()){
            match.addWaitingFor(match.getCurrentPlayer());
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
            } catch (NextMicroActionException e){
                nextMicroAction();
            }
        }
    }

    public void actionCompleted(){
        Player p = match.getCurrentPlayer();
        List<Action> selectableActions = match.createSelectableActions(match.getCurrentPlayer());
        if (selectableActions.isEmpty() || !activePlayers.contains(p)){
            //anche rimuovendo il controllo sui giocatori attivi
            //il turno verrebbe comunque terminato nel 'finalCleaning'
            endTurn();
        } else {
            match.addWaitingFor(match.getCurrentPlayer());
            saveSnapshot(match);
            p.setState(CHOOSE_ACTION);
            p.resetSelectables();
            p.setSelectableActions(selectableActions);
            if (match.isTurnCompletable()){
                p.setSelectableCommands(Command.OK);
            }
        }
    }

    private void saveSnapshot(Match match){
        currBackup = new Backup(match);
        currBackup.saveOnFile(BACKUP_NAME);
    }

    public Backup createMatchBackup(){
        return new Backup(match);
    }

    public void restore(){
        currBackup.restore(match);
        actionCompleted();
    }

    public void endTurn(){
        match.setAlreadyCompleted(true);
        saveSnapshot(match);
        match.removeWaitingFor(match.getCurrentPlayer());
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
            /*
            for (Player p : match.getToFakeList()){
                fakeAction(p);
            }
            */
        }
    }

    private boolean nameTaken(String name){
        List<Player> allPlayers = allPlayers();
        for(Player p : allPlayers){
            if(p.getName().equalsIgnoreCase(name)){
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
        observers.put(player, observer);
        /*
        if(observers.containsKey(player)){
            observers.replace(player, observer);
            activePlayers.add(player);
            inactivePlayers.remove(player);
        } else {
            observers.put(player, observer);
        }
        */
    }

    public void detach(Observer observer){
        try{
            Player tempPlayer = getPlayerByObserver(observer);
            observers.remove(tempPlayer);
        } catch(NoSuchObserverException e){
            System.out.println("Trying to remove an absent observer");
        }
        //todo: add check players.size and throw exception
    }

    public Player getPlayerByObserver(Observer observer) throws NoSuchObserverException {
        for (Map.Entry<Player, Observer> entry : observers.entrySet()){
            if (observer == entry.getValue()){
                return entry.getKey();
            }
        }
        throw new NoSuchObserverException();
    }

    public Observer getObserver(Player p){
        return observers.get(p);
    }

    public void deactivate(Player player){
        activePlayers.remove(player);
        if (isMatchInProgress()){
            inactivePlayers.add(player);
        }
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

    public Player login(String name) throws NameAlreadyTakenException, GameFullException{
        Player newPlayer;
        if((activePlayers.size() < 5)){
            if(!nameTaken(name)){
                newPlayer = new Player(name);
                activePlayers.add(newPlayer);
                return newPlayer;
            } else{
                throw new NameAlreadyTakenException();
            }
        } else {
            throw new GameFullException();
        }
    }

    public Player relogin(String name) throws NameNotFoundException, AlreadyLoggedException, GameFullException{
        Player oldPlayer;
        if(inactivePlayers.isEmpty()){
            throw new GameFullException();
        } else {
            if (!nameTaken(name)) {
                throw new NameNotFoundException();
            } else if (alreadyActive(name)) {
                throw new AlreadyLoggedException(); //todo tirare eccezione non puoi loggarti se active.size() == player in match
            } else {
                oldPlayer = getPlayerByName(name);
                inactivePlayers.remove(oldPlayer);
                activePlayers.add(oldPlayer);
            }
        }
        match.notifyStartMatchUpdate();
        return oldPlayer;
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

    public List<Player> getWaitingFor() {
        return match.getWaitingFor();
    }

    public List<Player> getActivePlayers() {
        return activePlayers;
    }

    public List<Player> getInactivePlayers() {
        return inactivePlayers;
    }

    public List<Player> getSpawningPlayers() {
        return spawningPlayers;
    }

    public void notify(MessageVisitable messageVisitable, Observer observer){
        observer.update(messageVisitable);
    }

    public void notifyAll(MessageVisitable messageVisitable){
        for(Player p : activePlayers){
            observers.get(p).update(messageVisitable);
        }
    }

    public boolean tooFewPlayers(){
        return activePlayers.size() < 3;
    }

    public List<Player> endGame(){
        //todo qualcosa che effettivament blocchi il game
        return match.getWinners();
    }

    public int howManyActivePlayers(){
        return activePlayers.size();
    }

    public void fakeAction(Player p){
        if (p == match.getCurrentPlayer()){
            if (match.getWaitingFor().contains(p)){
                restore();
                //if fakeAction is called after actually detaching and disconnecting current player,
                //endTurn() is implicit in ActionCompeted, since currentPlayer is no longer active (see comments below)
            } else {
                //current player is either receiving a tagback grenade or someone is spawning after his turn
                    //if someone is using tagback grenade, there's nothing to do.
                    //in ActionCompleted, check if currentPlayer is still active. If not -> endTurn()

                    //if someone is spawning, there's nothing to do, too.
                    //once they are all spawned, the next turn begins normally

                    //should never occur a third possibility
            }
        } else if (match.getWaitingFor().contains(p)){
            //p is not the current player
            if (p.getState() == USE_POWERUP){
                dontUsePowerUp(p);
                //p is removed from waitingFor
                //If there is someone else using power up, he is left there
                //otherwise, ActionCompeted is called (from nextMicroAction)
                //If the current player had disconnected, actionCompleted automatically ends his turn
            } else if (p.getState() == SPAWN) {
                spawn(p, p.getSelectablePowerUps().get(p.getSelectablePowerUps().size()-1));
                //he always respawns on the last powerup
                //if there is someone else respawning, he is left there
                //otherwise, beginNextTurn() is called and the next turn begins normally
            }
        }
    }

    public void notifyConnectionUpdate(){
        Map<String, Boolean> connectionStates = new HashMap<>();
        for (Player p : allPlayers()){
            connectionStates.put(p.getName(), activePlayers.contains(p));
        }

        //todo: check if o is null
        for (Observer o : observers.values()){
            ConnectionUpdateMessage message = new ConnectionUpdateMessage(connectionStates);
            o.update(message);
        }
    }
}
