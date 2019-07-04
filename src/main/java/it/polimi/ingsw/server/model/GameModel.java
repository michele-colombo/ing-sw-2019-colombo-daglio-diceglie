package it.polimi.ingsw.server.model;

import it.polimi.ingsw.communication.message.ConnectionUpdateMessage;
import it.polimi.ingsw.server.ParserManager;
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
import java.util.logging.Logger;


import static it.polimi.ingsw.server.model.enums.PlayerState.*;

/**
 * Is the center of the game, which embeds most of the game rules.
 * It is updated by the controller by quite general methods that represents events of the game,
 * then it knows how to transform these calls in actual modifications on data.
 * It is not aware of concepts like network and timers, but it does make distinction between two possible players' states: active and inactive.
 */
public class GameModel implements Observable {
    public static final String PREVIOUS_MATCH_STARTED = "match from a previous backup started";
    public static final String CANNOT_OPEN_SAVED_BACKUP = "cannot open saved backup";
    public static final String NEW_MATCH_STARTED = "new match started";
    public static final String BACKUP_CANNOT_ADD_PLAYER = "error in loading backup: can't add a player";
    public static final String REMOVING_ABSENT_OBSERVER = "trying to remove an absent observer";
    public static final String BACKUP_DELETED_SUCCESSFULLY = "backup deleted successfully";
    public static final String COULD_NOT_DELETE_BACKUP_FILE = "could not delete backup file";
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

    /**
     * keeps the status of the match
     */
    private boolean matchInProgress;

    /**
     * Stores the current backup
     */
    private Backup currBackup;

    /**
     * Keeps track of observer (associated to the corresponding player)
     */
    private Map<Player, Observer> observers;

    /**
     * support flag to keep track of gameOver
     */
    private boolean gameOver;

    /**
     * the name of the file to save the backup
     */
    private static final String BACKUP_NAME = "currentBackup";

    /**
     * default number of skulls
     */
    private static final int DEFAULT_SKULLS= 8;

    /**
     * default layout configuration
     */
    private static final int DEFAULT_LAYOUT= 0;

    /**
     * reference to the parserManager, used to get resources from file
     */
    private ParserManager pm;

    private int layoutConfig;
    private int skullsNumber;

    private final static Logger logger = Logger.getLogger(GameModel.class.getName());


    /**
     * Builds the gameModel, initializing all its attributes to default values
     */
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
        gameOver = false;

        pm= new ParserManager();

        layoutConfig= DEFAULT_LAYOUT;
        skullsNumber= DEFAULT_SKULLS;
    }

    /**
     * builds the game model. set attributes to default values and layoutConfig and skullsConfig to chosen values
     * @param layoutConfig number config of layout [-1, 3]
     * @param skullsNumber number of skulls [5,8]
     */
    public GameModel(int layoutConfig, int skullsNumber){
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
        gameOver = false;
        pm= new ParserManager();

        this.layoutConfig= layoutConfig;
        this.skullsNumber= skullsNumber;
    }

    /**
     * Gets the current match in progress
     * @return the actual reference to match
     */
    public Match getMatch() {
        return match;
    }

    /**
     * Tells if the match is in progress
     * @return true if the match is in progress
     */
    public boolean isMatchInProgress() {
        return matchInProgress;
    }

    /**
     * Adds a player of the given name to the game.
     * It creates a new one if the match is not in progress, otherwise it retrieves the xorresponding Player
     * @param name the name of the player (either new or returning)
     * @return the Player object corresponding to the name
     * @throws NameAlreadyTakenException if the name is already taken from another player
     * @throws GameFullException if the game is already full, and no other player can join
     * @throws NameNotFoundException if a match is in progress, but the name is not present
     * @throws AlreadyLoggedException if a match is in progress and the corresponding players is already logged
     * @throws NameEmptyException if name is empty
     */
    public Player addPlayer (String name) throws NameAlreadyTakenException, GameFullException, NameNotFoundException, AlreadyLoggedException, NameEmptyException {
        if(!matchInProgress){
            return login(name);
        } else{
            return relogin(name);
        }
    }

    /**
     * Starts a match. If there is a valid backup with the same players, this is resumed, otherwise it creates a new one.
     * @return true if a new match begins, false if a saved one is restored
     */
    public boolean startMatch(){
            try {
                Backup tempBackup = pm.getBackup();
                if (tempBackup.getPlayerNames().containsAll(getAllPlayerNames()) && getAllPlayerNames().containsAll(tempBackup.getPlayerNames())){
                    //all names match
                    int layoutConfig = tempBackup.getLayoutConfig();
                    match = new Match(pm.getLayout(layoutConfig), DEFAULT_SKULLS, pm.getStackManager());
                    for (String name : tempBackup.getPlayerNames()){
                        match.addPlayer(getPlayerByName(name));
                    }
                    match.setObservers(observers);
                    tempBackup.restore(match);
                    logger.info(PREVIOUS_MATCH_STARTED);
                    match.notifyStartMatchUpdate();
                    match.notifyFullUpdateAllPlayers();
                    matchInProgress = true;
                    actionCompleted();
                    return false;
                } else {
                    //saved backup has at least one different name
                    startNewMatch();
                }
            } catch (NullPointerException | NoSuchElementException e){
                logger.warning(CANNOT_OPEN_SAVED_BACKUP);
                startNewMatch();
                return true;
            }
        return true;
    }

    /**
     * Force to start a new match, even if a valid backup is present
     */
    public void startNewMatch(){
        int lc;
        if(layoutConfig != -1){
            lc= layoutConfig;
        }
        else {
            Random rand= new Random();
            lc= Math.abs( rand.nextInt()%4 );
        }

        match = new Match(pm.getLayout(lc), skullsNumber, pm.getStackManager());


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
        logger.info(NEW_MATCH_STARTED);
        match.notifyStartMatchUpdate();
        match.notifyFullUpdateAllPlayers();
        beginNextTurn();
    }

    /**
     * Resume a saved match from the URL of a backup file. Used to easily set up a match for tests.
     * @param url URL of the saved match
     */
    public void resumeMatchFromFile(InputStream url){
        Backup savedBackup = Backup.initFromFile(url);
        int layoutConfig = savedBackup.getLayoutConfig();
        match = new Match(pm.getLayout(layoutConfig), DEFAULT_SKULLS, pm.getStackManager());
        for (String playerName : savedBackup.getPlayerNames()){
            try {
                Player newPlayer = addPlayer(playerName);
                match.addPlayer(newPlayer);
            } catch(NameAlreadyTakenException | AlreadyLoggedException | GameFullException | NameNotFoundException | NameEmptyException e){
                logger.warning(BACKUP_CANNOT_ADD_PLAYER);
            }
        }
        savedBackup.restore(match);
        match.setObservers(observers);
        matchInProgress = true;
        actionCompleted();
    }

    /**
     * Prepare a player for spawning (or respawning) by drawing one or two powerups anf making them selectable
     * @param p the player to spawn
     * @param firstSpawn true if is first spawn
     */
    private void prepareForSpawning(Player p, boolean firstSpawn){
        p.addPowerUp(match.getStackManager().drawPowerUp());    //p.getPowerUps.size() can be 4 in this moment
        if (firstSpawn){
            p.addPowerUp(match.getStackManager().drawPowerUp());
        }
        match.addWaitingFor(p);
        if (!spawningPlayers.contains(p)) spawningPlayers.add(p);
        p.setState(SPAWN);
        p.resetSelectables();
        p.setSelectablePowerUps(p.getPowerUps());

        match.notifyPlayerUpdate(p);
        match.notifyPowerUpUpdate(p);
    }

    /**
     * Spawns a player on the spawnPoint of the color corresponding to the powerUp (and discards it)
     * @param p the player to spawn
     * @param po the powerUp corresponding to the spawnPoint
     */
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

        match.notifyPlayerUpdate(p);
        match.notifyPowerUpUpdate(p);
    }

    /**
     * Checks if the next player is born. If this is the case, it starts the nest turn, otherwise prepares it for spawning
     */
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

                match.notifyCurrentPlayerUpdate();
                match.notifyPlayerUpdate(nextP);
            }
        } catch (GameOverException e){
            endGame();
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

    /**
     * Performs an action for a player.
     * Performing means that starts the action and sets up the possible selections necessary to the prosecution of the action.
     * @param p player that is taking the action
     * @param a the action to start
     */
    public void performAction(Player p, Action a){
        match.setCurrentAction(a);
        match.updateTurnStatus(a);
        try {
            match.getCurrentAction().getMicroActions().get(0).act(match, p);    //the microAction sets player state
        } catch (NextMicroActionException nmae){
            nextMicroAction();
        }

        match.notifyPlayerUpdate(p);
    }

    /**
     * Makes a player grab in a square. If it is a spawnSquare, it sets up the choice of the weapon to grab.
     * @param p player which is grabbing
     * @param s square where to grab
     */
    public void grabThere (Player p, Square s){
        p.setSquarePosition(s);
        if (s.collect(p, match)){     //collect sets player state, if necessary
            nextMicroAction();
        }

        match.notifyPlayerUpdate(p);
    }

    /**
     * Adds the selcted weapon to a player
     * @param p the player which is collecting the weapon
     * @param w the weapon to add
     */
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
            match.notifyPlayerUpdate(p);
            match.notifyWeaponsUpdate(p);
            match.notifyLayotUpdate();
        } else {
            p.setNextState(GRAB_WEAPON);
            setupForPaying(p);
        }
    }

    /**
     * Removes and discards the selected weapon from a player
     * @param p the player which is discarding
     * @param w the weapon to discard
     */
    public void discardWeapon(Player p, Weapon w){
        match.getCurrentAction().getCurrSpawnSquare().addWeapon(w);
        p.removeWeapon(w);
        //unload
        nextMicroAction();

        match.notifyLayotUpdate();
        match.notifyWeaponsUpdate(p);

    }

    /**
     * Moves a player in the selected square
     * @param p the player to move
     * @param s the destination square
     */
    public void moveMeThere(Player p, Square s){
        p.setSquarePosition(s);
        nextMicroAction();

        match.notifyPlayerUpdate(p);
    }

    /**
     * Starts the shooting process of a the player with the selected weapon
     * @param p the player which i sgoing to shoot
     * @param w the weapon to shoot with
     */
    public void shootWeapon(Player p, Weapon w){
        match.getCurrentAction().setCurrWeapon(w);
        p.setState(CHOOSE_MODE);
        p.resetSelectables();
        List<Mode> tempModes = new ArrayList<>();
        for (Mode m : w.getSelectableModes(match.getCurrentAction().getSelectedModes())){
            if (p.canAfford(m.getCost())){
                tempModes.add(m);
            }
        }
        p.setSelectableModes(tempModes);

        match.notifyPlayerUpdate(p);
    }

    /**
     * Builds a shoot for the player by adding the selected mode.
     * @param p the player which is shooting
     * @param m the mode to add
     */
    public void addMode(Player p, Mode m){
        p.setPending(m.getCost());
        if (p.getCredit().isEqual(p.getPending())){
            p.getPending().setZero();
            p.getCredit().setZero();
            match.getCurrentAction().getSelectedModes().add(m);
            match.getCurrentAction().getCurrEffects().addAll(m.getEffects());
            p.setState(CHOOSE_MODE);
            p.resetSelectables();
            List<Mode> tempModes = new ArrayList<>();
            for (Mode mode : match.getCurrentAction().getCurrWeapon().getSelectableModes(match.getCurrentAction().getSelectedModes())){
                if (p.canAfford(mode.getCost())){
                    tempModes.add(mode);
                }
            }
            boolean hasMandatoryToSelect = false;
            for (Mode mode : tempModes) {
                if (mode.isMandatory) hasMandatoryToSelect = true;
            }
            p.setSelectableModes(tempModes);
            if (!hasMandatoryToSelect){
                p.setSelectableCommands(Command.OK);
            }
            match.notifyPlayerUpdate(p);
        } else {
            p.setNextState(CHOOSE_MODE);
            match.getCurrentAction().setCurrMode(m);
            setupForPaying(p);
        }
    }

    /**
     * Confirm and ends the selection of modes and starts the selection of target in a shoot
     * @param p the player which is shooting
     */
    public void confirmModes(Player p){
        p.setState(SHOOT_TARGET);
        p.resetSelectables();
        p.setLoad(match.getCurrentAction().getCurrWeapon(), false);
        try {
            match.getCurrentAction().getCurrEffects().get(0).start(p, match);
        } catch (ApplyEffectImmediatelyException e){
            shootTarget(p, null, null);
        }

        match.notifyWeaponsUpdate(p);
        match.notifyPlayerUpdate(p);
    }

    /**
     * Shoot to the selected target during a shoot. Either targetP or targetS is null.
     * @param p the player which is shooting
     * @param targetP the target which is receiving the shoot
     * @param targetS the square which is receiving the shoot
     */
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
                shootTarget(p, null, null);
            }
        }
    }

    /**
     * Checks if the player can complete the payment with what he has already paid (with powerups) and prepare selectables.
     * If a payment can be completed OK is selectable, otherwise he can choose only powerUps.
     * Total affordability (including powerUps) is a precondition.
     * @param p the player who is paying
     */
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

        match.notifyPlayerUpdate(p);
        match.notifyPaymentUpdate(p);
    }

    /**
     * Adds the color of the powerUp to the credit of the player
     * @param p the player who is paying
     * @param po the powerUp to discard for paying
     */
    public void payWith(Player p, PowerUp po){
        p.setCredit(p.getCredit().sum(new Cash(po.getColor(), 1)));
        p.removePowerUp(po);
        match.getStackManager().discardPowerUp(po);
        setupForPaying(p);

        match.notifyPowerUpUpdate(p);
    }

    /**
     * Complete a payment by paying what is lefting with ammos
     * @param p the player which is paying
     */
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
        match.notifyPaymentUpdate(p);
        match.notifyPlayerUpdate(p);

    }

    /**
     * Reloads the selected weapon, after checking for payment
     * @param p the player which is reloading
     * @param w the weapon to reload
     */
    public void reloadWeapon(Player p, Weapon w){
        p.setPending(w.getCost());
        if (p.getCredit().isEqual(p.getPending())){
            p.getPending().setZero();
            p.getCredit().setZero();
            p.setLoad(w, true);
            nextMicroAction();
            match.notifyWeaponsUpdate(p);
            match.notifyPlayerUpdate(p);
        } else {
            p.setNextState(RELOAD);
            match.getCurrentAction().setCurrWeapon(w);
            setupForPaying(p);
        }
    }

    /**
     * Starts the use of a powerUp for a player by setting up selectables.
     * @param p the player which is using the powerUp
     * @param po the powerUp used
     */
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

        match.notifyPlayerUpdate(p);
        match.notifyPowerUpUpdate(p);
    }

    /**
     * Pays a powerUp for the targeting scope
     * @param p the player using the targeting scope
     * @param po the powerup to pay
     */
    public void payAny(Player p, PowerUp po){
        p.removePowerUp(po);
        match.getStackManager().discardPowerUp(po);
        p.setState(USE_POWERUP);
        p.resetSelectables();
        useCurrentPowerUp(p);

        match.notifyPlayerUpdate(p);
        match.notifyPowerUpUpdate(p);
    }

    /**
     * Pays a color for the targeting scope
     * @param p the player using the targeting scope
     * @param c the ammo color to pay
     */
    public void payAny(Player p, AmmoColor c){
        p.getWallet().pay(new Cash(c, 1));
        p.setState(USE_POWERUP);
        p.resetSelectables();
        useCurrentPowerUp(p);

        match.notifyPlayerUpdate(p);
    }

    /**
     * Uses the current powerup (by starting its effects)
     * @param p the player using the powerup
     */
    private void useCurrentPowerUp(Player p){
        match.getCurrentAction().setCurrEffects(match.getCurrentAction().getCurrPowerUp().getEffects());
        try {
            match.getCurrentAction().getCurrEffects().get(0).start(p, match);
        } catch (ApplyEffectImmediatelyException e){
            choosePowerUpTarget(p, null, null);
        }
    }

    /**
     * Applies the effect of a powerup on a terget (either a player or a square)
     * @param p the player using the powerup
     * @param targetP the player receiving the effect
     * @param targetS the square receiving the effect
     */
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

    /**
     * Skips the use of powerup for the player. It is used also when player is disconnected.
     * @param p the player which does not want to use the powerup
     */
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

        match.notifyPlayerUpdate(p);
    }

    /**
     * Removes the current micro action and starts the next one, if present. Otherwise it completes the action
     */
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

    /**
     * Checks if the player is still active and has other actions to perform in the turn. If that is the case, it prepares its selectable actions.
     */
    public void actionCompleted(){
        Player p = match.getCurrentPlayer();
        for (Player player : allPlayers()){
            player.getDamageTrack().setMarkToDamages(true);
        }
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

            match.notifyPlayerUpdate(p);
        }
    }

    /**
     * Saves a snapshot of the specified match in a file
     * @param match the match to save
     */
    private void saveSnapshot(Match match){
        currBackup = new Backup(match);
        pm.saveOnSameDirectory(currBackup, BACKUP_NAME);
    }

    /**
     * Restores the match to the state saved in the backup.
     */
    public void restore(){
        currBackup.restore(match);
        actionCompleted();

        match.notifyFullUpdateAllPlayers();
    }

    /**
     * Ends the turn of the current player and prepares dead players for respawning
     */
    public void endTurn(){
        match.setAlreadyCompleted(true);
        saveSnapshot(match);
        match.removeWaitingFor(match.getCurrentPlayer());
        match.getCurrentPlayer().setState(IDLE);
        match.notifyPlayerUpdate(match.getCurrentPlayer());
        match.getCurrentPlayer().resetSelectables();
        match.getLayout().refillAll(match.getStackManager());
        match.notifyLayotUpdate();
        List<Player> deadPlayers = match.endTurnCheck();
        match.notifyKillShotTrackUpdate();
        if (deadPlayers.isEmpty()){
            beginNextTurn();
        } else {
            for (Player p : deadPlayers) {
                prepareForSpawning(p, false);
            }
        }
    }

    /**
     * Checks if the specified name already exists in the current game
     * @param name the name to check
     * @return true if it already exists
     */
    private boolean nameTaken(String name){
        List<Player> allPlayers = allPlayers();
        for(Player p : allPlayers){
            if(p.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the specified color has been already taken in the current game
     * @param color the color to check
     * @return true if it has been already taken
     */
    private boolean colorTaken(PlayerColor color){
        List<Player> allPlayers = allPlayers();
        for(Player p : allPlayers){
            if(p.getColor() == color){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the number of all players of the match (also if the match has not already started)
     * @return the number of all players
     */
    public int getNumberOfPlayers(){
        return allPlayers().size();
    }

    /**
     * Adds a new observer, associating it to the corresponding player
     * @param player the corresponding player
     * @param observer the observer associated to player
     */
    public void attach(Player player, Observer observer){
        observers.put(player, observer);
    }

    /**
     * Removes the observer of a player
     * @param observer the observer to remove
     */
    public void detach(Observer observer){
        try{
            Player tempPlayer = getPlayerByObserver(observer);
            observers.remove(tempPlayer);
        } catch(NoSuchObserverException e){
            logger.warning(REMOVING_ABSENT_OBSERVER);
        }
    }

    /**
     * Gets a player from the corresponding observer
     * @param observer the observer of the player
     * @return the reference to the actual player
     * @throws NoSuchObserverException if no player is associated to the observer
     */
    public Player getPlayerByObserver(Observer observer) throws NoSuchObserverException {
        for (Map.Entry<Player, Observer> entry : observers.entrySet()){
            if (observer == entry.getValue()){
                return entry.getKey();
            }
        }
        throw new NoSuchObserverException();
    }

    /**
     * Gets the observer of a player
     * @param p the involved player
     * @return the observer or null if the player has no observer
     */
    public Observer getObserver(Player p){
        return observers.get(p);
    }

    /**
     * Sets a player as inactive
     * @param player the player to deactivate
     */
    public void deactivate(Player player){
        activePlayers.remove(player);
        if (isMatchInProgress()){
            inactivePlayers.add(player);
        }
    }

    /**
     * Checks if a player is active
     * @param name the player to check
     * @return true if the player is active
     */
    public boolean alreadyActive(String name){
        for(Player p : activePlayers){
            if(p.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a list of all players, both active and inactive
     * @return a clone list fo all players
     */
    public List<Player> allPlayers(){
        List<Player> allPlayers = new ArrayList<>();
        allPlayers.addAll(activePlayers);
        allPlayers.addAll(inactivePlayers);
        return allPlayers;
    }

    /**
     * Used if a match has still to start. Checks the validity of name and then creates a new player.
     * @param name the chosen name for the player
     * @return the reference to the new player created
     * @throws NameAlreadyTakenException if the name is already taken by another player
     * @throws GameFullException if the game is full and no other player can join
     * @throws NameEmptyException if the name is empty
     */
    private Player login(String name) throws NameAlreadyTakenException, GameFullException, NameEmptyException{
        Player newPlayer;
        if((activePlayers.size() < 5)){
            if(name.length() == 0){
                throw  new NameEmptyException();
            } else if(!nameTaken(name)){
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


    /**
     * Used if a match is already in progress. Checks the validity of name and then retrieves the corresponding player.
     * @param name the name of player which wants to relogin
     * @return the reference to the corresponding player
     * @throws NameNotFoundException if there is no player with such name in the match
     * @throws AlreadyLoggedException if someone is already logged with this player
     * @throws GameFullException if all the players are currently logged
     */
    private Player relogin(String name) throws NameNotFoundException, AlreadyLoggedException, GameFullException{
        Player oldPlayer;
        if(inactivePlayers.isEmpty()){
            throw new GameFullException();
        } else {
            if (!nameTaken(name)) {
                throw new NameNotFoundException();
            } else if (alreadyActive(name)) {
                throw new AlreadyLoggedException();
            } else {
                oldPlayer = getPlayerByName(name);
                inactivePlayers.remove(oldPlayer);
                activePlayers.add(oldPlayer);
            }
        }
        return oldPlayer;
    }

    /**
     * Retrieves the player with the specified name, if exists
     * @param name the name of the player
     * @return the reference to the player or null, if no player has that name
     */
    public Player getPlayerByName(String name){
        List<Player> allPlayers = allPlayers();
        for(Player p : allPlayers){
            if(p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

    /**
     * Returns the names of all the players
     * @return a list with all names
     */
    private List<String> getAllPlayerNames(){
        List<String> result = new ArrayList<>();
        for (Player p : allPlayers()){
            result.add(p.getName());
        }
        return result;
    }

    /**
     * Gets the list of player from which an interaction is expected
     * @return the reference to the actual list (can be modified)
     */
    public List<Player> getWaitingFor() {
        return match.getWaitingFor();
    }

    /**
     * Gets the list of active players
     * @return the reference to the actual list (can be modified)
     */
    public List<Player> getActivePlayers() {
        return activePlayers;
    }

    /**
     * Gets the list of inactive players
     * @return the reference to the actual list (can be modified)
     */
    public List<Player> getInactivePlayers() {
        return inactivePlayers;
    }


    /**
     * Gets the list of spawning players (both never born or killed)
     * @return the reference to the actual list (can be modified)
     */
    public List<Player> getSpawningPlayers() {
        return spawningPlayers;
    }

    /**
     * Notifies an observer with the passed message.
     * @param messageVisitable the message to send
     * @param observer the receiving observer
     */
    public void notify(MessageVisitable messageVisitable, Observer observer){
        observer.update(messageVisitable);
    }

    /**
     * Notifies all the observers with the passed message
     * @param messageVisitable the message to send
     */
    public void notifyAll(MessageVisitable messageVisitable){
        for(Player p : activePlayers){
            observers.get(p).update(messageVisitable);
        }
    }

    /**
     * Checks if the number of player is lower than the minimum allowed for the match to continue
     * @return true if the players are too few
     */
    public boolean tooFewPlayers(){
        return activePlayers.size() < 3;
    }

    /**
     * Ends tha game, manages the final point assignment and notifies observers.
     */
    public void endGame(){
        gameOver = true;
        matchInProgress = false;
        for (Player p: allPlayers()){
            match.scoreDamageTrack(p.getDamageTrack().score());
        }
        match.scoreFinalPoints();
        match.notifyGameOver();

        try{
            String jarPath = URLDecoder.decode(Backup.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            String filePath = jarPath.substring(0, jarPath.lastIndexOf('/')) + File.separator + "backups"+ File.separator + BACKUP_NAME + ".json";
            File backupFile = new File(filePath);
            if (backupFile.delete()){
                logger.info(BACKUP_DELETED_SUCCESSFULLY);
            } else {
                logger.warning(COULD_NOT_DELETE_BACKUP_FILE);
            }
        } catch (IOException e) {
            logger.warning(COULD_NOT_DELETE_BACKUP_FILE);
        }
    }

    /**
     * Checks if the match is ended
     * @return true if the match is ended
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Returns the number of active players
     * @return the number of players currently active
     */
    public int howManyActivePlayers(){
        return activePlayers.size();
    }

    /**
     * It brings back the state of match to a consistent one (if necessary) and fakes the action of a certain player.
     * It acts like the player is acting, while he is actually inactive.
     * Default choices are: not use tagback and spawn on the last drawn powerup.
     * @param p the player of which fake the action
     */
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

    /**
     * Notifies all players of the state of connection of all the players
     */
    public void notifyConnectionUpdate(){
        Map<String, Boolean> connectionStates = new HashMap<>();
        for (Player p : allPlayers()){
            connectionStates.put(p.getName(), activePlayers.contains(p));
        }

        for (Observer o : observers.values()){
            if (o != null) {
                ConnectionUpdateMessage message = new ConnectionUpdateMessage(connectionStates);
                o.update(message);
            }
        }
    }
}
