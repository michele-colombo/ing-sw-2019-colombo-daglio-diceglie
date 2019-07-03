package it.polimi.ingsw.server.model;



import it.polimi.ingsw.communication.message.*;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerColor;
import it.polimi.ingsw.server.model.enums.PlayerState;
import it.polimi.ingsw.server.observer.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.server.model.enums.PowerUpType.*;

/**
 * Represents a specific match.
 * It contains all the players, the map, the killshot track and the stacks of the current match.
 * It also contains turn-related information.
 * It embeds the logic for point calculation after killing and creation of possible actions during a turn.
 */
public class Match {
    /**
     * It's the selected layout of the match
     */
    private Layout layout;
    /**
     * It's the stackManager
     */
    private StackManager stackManager;
    /**
     * It contains the list of the players
     */
    private List<Player> players;
    /**
     * It's the KillShotTrack of the match
     */
    private KillShotTrack killShotTrack;
    /**
     * It's true if frenzy mode is on, otherwise it's false
     */
    private boolean frenzyOn;
    /**
     * It's the number of action that the current player can do
     */
    private int numberOfActions;
    /**
     * It's the number of action completed by the current player
     */
    private int actionsCompleted;
    /**
     * It's true if the current player can only setLoad his weapons, otherwise it's false
     */
    private boolean onlyReload;
    /**
     * It's true if the turn is completable, otherwise it's false
     */
    private boolean turnCompletable;

    /**
     * True if the player has already completed its turn (used for restoring match)
     */
    private boolean alreadyCompleted;

    /**
     * List containing players from which an action is required (used for timers)
     */
    private List<Player> waitingFor;

    /**
     * It's the reference to the current action
     */
    private Action currentAction;
    /**
     * It's the reference to the current player
     */
    private Player currentPlayer;

    /**
     * It's the reference to the map of observers in gameModel
     */
    private Map<Player, Observer> observers;

    /**
     * It creates the match with the selected layout configuration and number of skulls
     * @param layout It's the layout of the game
     * @param skulls It's the number of skull of the KillShotTrack
     * @param stackManager They are the stacks
     */
    public Match(Layout layout, int skulls, StackManager stackManager){
        this.layout = layout;
        players = new ArrayList<>();
        waitingFor = new ArrayList<>();
        killShotTrack = new KillShotTrack(skulls);

        this.stackManager = stackManager;

        currentAction = null;
        currentPlayer = null;
        observers = new HashMap<>();
    }

    /**
     * Set the reference to the observer map
     * @param observers the map to set
     */
    public void setObservers(Map<Player, Observer> observers) {
        this.observers = observers;
    }

    /**
     * Gets the stack manager which contains the stack of cards for the current match
     * @return the reference to the stack manager
     */
    public StackManager getStackManager() {
        return stackManager;
    }

    /**
     * Gets the list of all players of the match
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the reference to the killshot track
     * @return the reference to killshot track
     */
    public KillShotTrack getKillShotTrack() {
        return killShotTrack;
    }

    /**
     * Gets the current player. A player remains current until the next has EFFECTIVELY started its turn.
     * While someone is spawning, the current palyer is the player before him.
     * @return a reference to a player or null (if the first player has not already spawned)
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player
     * @param currentPlayer the player to set
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Returns all the players on squares
     * @param squares The squares to analyze
     * @return An ArrayList containing found players; it could be empty
     */
    public List<Player> getPlayersOn(List<Square> squares){
        List<Player> squaresOccupied = new ArrayList<>();
        for(Player p : players){
            if(squares.contains(p.getSquarePosition())){
                squaresOccupied.add(p);
            }
        }
        return squaresOccupied;
    }

    /**
     * Adds a player to the match. Validity of the new player is a precondition.
     * @param p the player to add
     */
    public void addPlayer(Player p){
        this.players.add(p);
    }

    /**
     * Returns a map containing each player and his own points
     * @return An HashMap
     */
    public Map<Player, Integer> getAllPoints(){
        Map<Player, Integer> points = new HashMap<>();
        for(Player p : players){
            points.put(p, p.getPoints());
        }
        return points;
    }

    /**
     * Gets the rank of each player
     * @return a map with each player and its rank
     */
    public Map<Player, Integer> getRank(){
        Map<Player, Integer> result = new HashMap<>();
        int rank = 1;
        Map<Player, Integer> tempPoints = getAllPoints();
        while (tempPoints.size()>0) {
            int max = -1;
            List<Player> maxPlayers = new ArrayList<>();
            for (Map.Entry<Player, Integer> playerPoints : tempPoints.entrySet()){
                if (playerPoints.getValue() > max){
                    max = playerPoints.getValue();
                    maxPlayers.clear();
                    maxPlayers.add(playerPoints.getKey());
                } else if (playerPoints.getValue() == max){
                    if (compare(playerPoints.getKey(), maxPlayers.get(0)) > 0){
                        maxPlayers.clear();
                        maxPlayers.add(playerPoints.getKey());
                    } else if (compare(playerPoints.getKey(), maxPlayers.get(0)) == 0){
                        maxPlayers.add(playerPoints.getKey());
                    }
                }
            }
            for (Player p : maxPlayers){
                result.put(p, rank);
                tempPoints.remove(p);
            }
            rank += maxPlayers.size();
        }
        return result;
    }

    /**
     * Compare the number of killings between two players
     * @param p1 a player
     * @param p2 the player to compare
     * @return an integer < 0 if p2 has killed more than p1
     */
    private int compare(Player p1, Player p2){
        int p1Points;
        int p2Points;
        if (killShotTrack.score().get(p1) != null){
            p1Points = killShotTrack.score().get(p1);
        } else {
            p1Points = 0;
        }
        if (killShotTrack.score().get(p2) != null){
            p2Points = killShotTrack.score().get(p2);
        } else {
            p2Points = 0;
        }
        return p1Points - p2Points;
    }

    /**
     * Adds the points from killshot track to each player
     */
    public void scoreFinalPoints(){
        for (Map.Entry<Player, Integer> entry : killShotTrack.score().entrySet()){
            entry.getKey().addPoints(entry.getValue());
        }
    }

    /**
     * Set the position of a player to the proper SpawnPoint
     * @param p The player to be spawned
     * @param c The color of the SpawnPoint
     */
    public void spawn(Player p, AmmoColor c){
        p.setSquarePosition(layout.getSpawnPoint(c));
    }

    /**
     *  At the end of the turn, check if some players is dead: in this case, their DamageTrack will be scored.
     *  Then, if there are at least two deaths, the active player will get an additional point.
     *  If frenzy has to be activated (last skull removed), it activates frenzy and set the flag 'isBeforeFirst' in each player.
     *  If frenzy mode is true, switches to frenzy all the dead players and, in the end, calls resetAfterDeathAll on them
     * @return An ArrayList containing all the dead players; it could be empty
     */
    public List<Player> endTurnCheck(){
        List<Player> deadPlayers = getDeadPlayers();
        if(!deadPlayers.isEmpty()){
            for(Player p : deadPlayers){
                scoreDamageTrack(p.getDamageTrack().score());
                killShotTrack.removeSkull();
                killShotTrack.addKilled(p.getDamageTrack().howDoTheyKilledYou());
                for (Map.Entry<Player, Integer> entry : p.getDamageTrack().howDoTheyKilledYou().entrySet()){
                    if (entry.getValue() == 2){
                        entry.getKey().getDamageTrack().addMark(p, 1);
                        notifyDamageUpdate(entry.getKey());
                    }
                }
            }
        }

        if(deadPlayers.size() > 2){
            currentPlayer.addPoints(1);
        }
        if(!frenzyOn && killShotTrack.getSkulls() <= 0){
            setFrenzyOn(true);
            for(Player p : players){
                if(players.indexOf(p) > players.indexOf(currentPlayer)){
                    p.setBeforeFirst(true);
                }
            }
        }
        if(frenzyOn){
            switchToFrenzyAll(deadPlayers);
        }
        resetAfterDeathAll(deadPlayers);    //if is a NormalDamageTrack, it also increases the number of skullsk
        return deadPlayers;
    }

    /**
     * Creates a list of possible action a player can take, according to his adrenaline and turn status
     * Must be called after the first action has been taken.
     * Also sets turnCompletable true if the player can choose to end his turn
     * @param p The player which is in CHOOSE_ACTION state
     * @return the list with the possible action
     */
    public List<Action> createSelectableActions(Player p) {
        List<Action> result = new ArrayList<>();
        if (!alreadyCompleted) {
            if (numberOfActions - actionsCompleted > 0) {
                if (!frenzyOn) {
                    result.addAll(createAdrenalinicAction(p));
                } else {
                    result.addAll(createFrenzyActions(p));
                }
            } else {
                turnCompletable = true;
                if (!p.getUnloadedWeapons().isEmpty()) {
                    result.add(new Action(false, true, new Reload()));
                }
            }
            if (!onlyReload && p.howManyPowerUps(ACTION_POWERUP) > 0) {
                result.add(new Action(false, false, new UseActionPowerUp()));
            }
        }
        return result;
    }

    /**
     * Creates the adrenalinic actions (for normal mode) according to the adrenaline level of the player
     * @param p the player in CHOOSE_ACTION state
     * @return a list of actions
     */
    private List<Action> createAdrenalinicAction(Player p){
        List<Action> result = new ArrayList<>();
        Action temp;
        switch (p.getDamageTrack().getAdrenaline()) {
            case 0:
                result.add(new Action(true, false, new Move(3)));
                result.add(new Action(true, false, new Grab(1)));
                if (!p.getLoadedWeapons().isEmpty()){
                    temp = new Action(true, false, new Shoot());
                    if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UseTargeting());
                    temp.add(new ReceiveTagback());
                    result.add(temp);
                }
                break;
            case 1:
                result.add(new Action(true, false, new Move(3)));
                result.add(new Action(true, false, new Grab(2)));
                if (!p.getLoadedWeapons().isEmpty()){
                    temp = new Action(true, false, new Shoot());
                    if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UseTargeting());
                    temp.add(new ReceiveTagback());
                    result.add(temp);
                }
                break;
            case 2:
                result.add(new Action(true, false, new Move(3)));
                result.add(new Action(true, false, new Grab(2)));
                if (!p.getLoadedWeapons().isEmpty()) {
                    temp = new Action(true, false, new Move(1));
                    temp.add(new Shoot());
                    if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UseTargeting());
                    temp.add(new ReceiveTagback());
                    result.add(temp);
                }
                break;
        }
        return result;
    }

    /**
     * Creates the frenzy actions (for frenzy mode) according to the game order of the player
     * @param p the player in CHOOSE_ACTION state
     * @return a list of actions
     */
    private List<Action> createFrenzyActions(Player p){
        List<Action> result = new ArrayList<>();
        Action temp;
        if (p.isBeforeFirst()) {
            if (!p.getWeapons().isEmpty()) {
                temp = new Action(true, false, new Move(1));
                temp.add(new Reload());
                temp.add(new Shoot());
                if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UseTargeting());
                temp.add(new ReceiveTagback());
                result.add(temp);
            }
            result.add(new Action(true, false, new Move(4)));
            result.add(new Action(true, false, new Grab(2)));
        } else {
            if (!p.getWeapons().isEmpty()) {
                temp = new Action(true, false, new Move(2));
                temp.add(new Reload());
                temp.add(new Shoot());
                if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UseTargeting());
                temp.add(new ReceiveTagback());
                result.add(temp);
            }
            result.add(new Action(true, false, new Grab(3)));
        }
        return result;
    }

    /**
     * Creates the list of action a player can take. Must be called at the beginning of his turn.
     * It also initializes support variables to keep track of the turn status.
     * @param p A player in the beginning of his turn, while is in CHOOSE_ACTION state.
     * @return the list of actions he can take
     */
    public List<Action> initSelectableActions(Player p){
        if (frenzyOn){
            if (p.isBeforeFirst()){
                numberOfActions = 2;
            } else {
                numberOfActions = 1;
            }
        } else {
            numberOfActions = 2;
        }
        turnCompletable = false;
        alreadyCompleted = false;
        actionsCompleted = 0;
        onlyReload = false;
        return createSelectableActions(p);
    }

    /**
     * Updates the turn status (number of remaining actions, the player can only setLoad)
     * @param a The action chosen by the player, the status is updated according to it
     */
    public void updateTurnStatus(Action a){
        if (a.isIncrementActionCounter()) actionsCompleted++;
        if (a.isActivateOnlyReloads()) onlyReload = true;
    }

    /**
     * Gets the action currently in progress.
     * currentAction also stores support variables related to it
     * (the weapon and the modes currently selected, the effects to take, etc)
     * @return the reference to the current action
     */
    public Action getCurrentAction() {
        return currentAction;
    }

    /**
     * Sets the action to be processed
     * @param currentAction The action just chosen by the player
     */
    public void setCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
    }

    /**
     * Gets the match layout
     * @return A reference to the actual layout
     */
    public Layout getLayout(){
        return layout;
    }

    /**
     * Substitutes player's damageTrack with a FrenzyDamageTrack
     * @param deadPlayers Only dead players can switch to frenzy
     */
    private void switchToFrenzyAll(List<Player> deadPlayers){
        for(Player p : deadPlayers){
            p.switchToFrenzy();
        }
    }

    /**
     * Resets all dead players' damage track (same marks, same skulls, no damages)
     * @param deadPlayers Only dead players can reset own damage track
     */
    private void resetAfterDeathAll(List<Player> deadPlayers){
        for(Player p : deadPlayers){
            p.getDamageTrack().resetAfterDeath();
            notifyPlayerUpdate(p);
            notifyDamageUpdate(p);
        }
    }

    /**
     * Returns all the dead players
     * @return An ArrayList containing all the dead players
     */
    private List<Player> getDeadPlayers(){
        List<Player> deadPlayers = new ArrayList<>();
        for(Player p : players){
            if(!p.isAlive()){
                deadPlayers.add(p);
            }
        }
        return deadPlayers;
    }

    /**
     * At the end of the turn, if a player is dead, each other player gets his own points
     * @param points A map containing how many points will be added to each other player
     */
    public void scoreDamageTrack(Map<Player, Integer> points){
        for(Player p : players){
            if(points.containsKey(p)){
                p.addPoints(points.get(p));
            }
        }
    }

    /**
     * checks if the current player can complete his turn in the current moment
     * @return true if he can complete the turn
     */
    public boolean isTurnCompletable() {
        return turnCompletable;
    }

    /**
     * Checks if final frenzy is currently active
     * @return true if frenzy mode is on
     */
    public boolean isFrenzyOn() {
        return frenzyOn;
    }

    /**
     * Gets the number of total actions in the turn available for the current player
     * @return the number of total actions
     */
    public int getNumberOfActions() {
        return numberOfActions;
    }

    /**
     * Gets the number of action completed by the current player in the current turn
     * @return the number of actions completed
     */
    public int getActionsCompleted() {
        return actionsCompleted;
    }

    /**
     * Checks if the current player can only reload at the moment in his turn
     * @return tru if he can only reload
     */
    public boolean isOnlyReload() {
        return onlyReload;
    }

    /**
     * Retrieves the reference to a player object from its name
     * @param name the name of the player to retrieve
     * @return the reference to the player object
     */
    public Player getPlayerFromName(String name){
        for(Player p : players){
            if (p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

    /**
     * Sets the flag for the frenzy mod
     * @param frenzyOn value to set
     */
    public void setFrenzyOn(boolean frenzyOn) {
        this.frenzyOn = frenzyOn;
    }

    /**
     * Sets the number of action
     * @param numberOfActions value to set
     */
    public void setNumberOfActions(int numberOfActions) {
        this.numberOfActions = numberOfActions;
    }

    /**
     * Sets the action completed flag
     * @param actionsCompleted value to set
     */
    public void setActionsCompleted(int actionsCompleted) {
        this.actionsCompleted = actionsCompleted;
    }

    /**
     * Sets the flag onlyReload
     * @param onlyReload value to set
     */
    public void setOnlyReload(boolean onlyReload) {
        this.onlyReload = onlyReload;
    }

    /**
     * Sets the flag turnCompletable
     * @param turnCompletable value to set
     */
    public void setTurnCompletable(boolean turnCompletable) {
        this.turnCompletable = turnCompletable;
    }

    /**
     * Gets the value of the flag alreadyCompleted
     * @return true if player has already completed its turn
     */
    public boolean isAlreadyCompleted() {
        return alreadyCompleted;
    }

    /**
     * Sets the flag alreadyCompleted
     * @param alreadyCompleted value to set
     */
    public void setAlreadyCompleted(boolean alreadyCompleted) {
        this.alreadyCompleted = alreadyCompleted;
    }

    /**
     * Gets the list of player from which an interaction is expected
     * @return the list of players
     */
    public List<Player> getWaitingFor() {
        return waitingFor;
    }

    /**
     * Clears the list of player from which and interaction is expected
     */
    public void clearWaitingFor() {
        waitingFor.clear();
    }

    /**
     * Adds a player to the list of players from which and interaction is expected. Uniqueness is preserved.
     * @param p the player to add
     */
    public void addWaitingFor(Player p){
        if (!waitingFor.contains(p)){
            waitingFor.add(p);
        }
    }

    /**
     * Removes a player from the list of player from which and interaction is expected
     * @param p the player to remove
     * @return true if the player was found
     */
    public boolean removeWaitingFor(Player p){
        return waitingFor.remove(p);
    }

    /**
     * Gets the next player in order of game, without checking his activity state
     * @param currP the player which is current now
     * @return the next player
     */
    public Player getNextPlayer(Player currP){
        int index;
        Player nextPlayer;
        if (currP == null){
            nextPlayer = players.get(0);    //if current player is null, returns the first player
        } else {
            index = players.indexOf(currP);
            if (index == players.size()-1){
                nextPlayer = players.get(0);
            } else {
                nextPlayer = players.get(index + 1);
            }
        }
        return nextPlayer;
    }

    /**
     * Gets the first player of the game
     * @return the reference to the first player
     */
    public Player getFirstPlayer(){
        return players.get(0);
    }

    /**
     * Updates all the observer about the weapons and ammoTiles on the map
     */
    public void notifyLayotUpdate(){
        List<String> blueWeapons = new ArrayList<>();
        List<String> redWeapons = new ArrayList<>();
        List<String> yellowWeapons = new ArrayList<>();
        Map<String, String> ammosTilesInSquares = new HashMap<>();
        for (Weapon w : layout.getSpawnPoint(AmmoColor.BLUE).getWeapons()){
            blueWeapons.add(w.getName());
        }

        for (Weapon w : layout.getSpawnPoint(AmmoColor.RED).getWeapons()){
            redWeapons.add(w.getName());
        }

        for (Weapon w : layout.getSpawnPoint(AmmoColor.YELLOW).getWeapons()){
            yellowWeapons.add(w.getName());
        }

        for (AmmoSquare as : layout.getAmmoSquares()){
            if (as.getAmmo()!=null){
                ammosTilesInSquares.put(as.toString(), as.getAmmo().toString());
            } else {
                ammosTilesInSquares.put(as.toString(), "");
            }
        }

        for (Observer o : observers.values()){
            LayoutUpdateMessage message = new LayoutUpdateMessage(blueWeapons, redWeapons, yellowWeapons, ammosTilesInSquares);
            o.update(message);
        }
    }

    /**
     * Updates all the observer about the killings in the killshottrack and the frenzy status. Updates each observer with points of the corresponding player.
     */
    public void notifyKillShotTrackUpdate(){
        int skulls = killShotTrack.getSkulls();

        List<Map<String, Integer>> track = new ArrayList<>();
        for (Map<Player, Integer> map : killShotTrack.getTrack()){
            Map<String, Integer> temp = new HashMap<>();
            for (Map.Entry<Player, Integer> entry : map.entrySet()){
                temp.put(entry.getKey().getName(), entry.getValue());
            }
            track.add(temp);
        }

        for (Map.Entry<Player, Observer> o : observers.entrySet()){
            int yourPoints = o.getKey().getPoints();
            KillshotTrackUpdateMessage message = new KillshotTrackUpdateMessage(skulls, track, isFrenzyOn(), yourPoints);
            o.getValue().update(message);
        }
    }

    /**
     * Updates all the observer with the new current player
     */
    public void notifyCurrentPlayerUpdate(){
        if (currentPlayer != null) {
            String currentPlayerName = getCurrentPlayer().getName();
            for (Observer o : observers.values()) {
                CurrentPlayerUpdateMessage message = new CurrentPlayerUpdateMessage(currentPlayerName);
                o.update(message);
            }
        }
    }

    /**
     * Communicates to all the observers that a match is started (or is in progress, in case of relogin)
     * Updates them with the number of layout configuration, the names of all the players in the match and their colors
     */
    public void notifyStartMatchUpdate(){
        int layoutConfiguration = layout.getLayoutConfiguration();
        List<String> names = new ArrayList<>();
        List<PlayerColor> colors = new ArrayList<>();
        for (Player p : players){
            names.add(p.getName());
            colors.add(p.getColor());
        }

        for (Observer o : observers.values()){
            StartMatchUpdateMessage message = new StartMatchUpdateMessage(layoutConfiguration, names, colors);
            o.update(message);
        }
    }

    /**
     * Updates all the observers with information about the spedified player: state, position and wallet.
     * Information is sent as strings (unique identifiers)
     * @param player the player subject of information
     */
    public void notifyPlayerUpdate(Player player){
        String name = player.getName();
        PlayerState state = player.getState();
        String position = "";
        if (player.getSquarePosition() != null){
            position = player.getSquarePosition().toString();
        }
        Cash wallet = player.getWallet();

        for (Observer o : observers.values()){
            PlayerUpdateMessage message = new PlayerUpdateMessage(name, state, position, wallet);
            o.update(message);
        }
    }

    /**
     * Updates the observer of a player (only it) about the amount of ammos he has to pay and he has already paid
     * @param receiver the subject of the payment, which corresponds also the receiving observer
     */
    public void notifyPaymentUpdate(Player receiver){
        Cash pending = receiver.getPending();
        Cash credit = receiver.getCredit();

        PaymentUpdateMessage message = new PaymentUpdateMessage(pending, credit);


        Observer o = observers.get(receiver);
        if (o != null){
            o.update(message);
        }
    }

    /**
     * Updates all observers about the weapons of the specified player.
     * Unload weapons are send to everyone, while the loaded ones are sent explicitly only to the observer of the involved player,
     * other observers only know the number. Weapons are sent as string of the name.
     * @param player the player subject of the update
     */
    public void notifyWeaponsUpdate(Player player){
        String name = player.getName();
        List<String> loadedWeapons = new ArrayList<>();
        List<String> unloadedWeapons = new ArrayList<>();
        int numLoadedWeapons;
        for (Weapon w : player.getLoadedWeapons()){
            loadedWeapons.add(w.getName());
        }
        numLoadedWeapons = loadedWeapons.size();
        for (Weapon w : player.getUnloadedWeapons()){
            unloadedWeapons.add(w.getName());
        }

        WeaponsUpdateMessage message;
        for (Map.Entry<Player, Observer> o : observers.entrySet()){
            if (o.getKey() == player){
                message = new WeaponsUpdateMessage(name, loadedWeapons, unloadedWeapons, numLoadedWeapons);
            } else {
                message = new WeaponsUpdateMessage(name, unloadedWeapons, numLoadedWeapons);
            }
            o.getValue().update(message);
        }
    }

    /**
     * Updates all observers with the powerUps of the specified player.
     * Only his observer receives the list with the names of powerUps, other only know the number.
     * @param player the player subject of the update
     */
    public void notifyPowerUpUpdate(Player player){
        String name = player.getName();
        List<String> powerUps = new ArrayList<>();
        int numPowerUps;
        for (PowerUp p : player.getPowerUps()){
            powerUps.add(p.toString());
        }
        numPowerUps = powerUps.size();

        for (Map.Entry<Player, Observer> o : observers.entrySet()){
            PowerUpUpdateMessage message;
            if (o.getKey() == player){
                message = new PowerUpUpdateMessage(name, powerUps, numPowerUps);
            } else {
                message = new PowerUpUpdateMessage(name, numPowerUps);
            }
            o.getValue().update(message);
        }
    }

    /**
     * Updates all observers with damages and marks regarding the specified player.
     * Update message is equal for every observer.
     * @param damaged the player subject of the update
     */
    public void notifyDamageUpdate(Player damaged){
        String name = damaged.getName();
        List<String> damageList = new ArrayList<>();
        Map<String, Integer> markMap = new HashMap<>();
        int skulls = damaged.getDamageTrack().getSkullsNumber();
        boolean isFrenzy = damaged.getDamageTrack().isFrenzy();

        for (Player p : damaged.getDamageTrack().getDamageList()){
            damageList.add(p.getName());
        }

        for (Map.Entry<Player, Integer> entry : damaged.getDamageTrack().getMarkMap().entrySet()){
            markMap.put(entry.getKey().getName(), entry.getValue());
        }

        for (Observer o : observers.values()){
            DamageUpdateMessage message = new DamageUpdateMessage(name, damageList, markMap, skulls, isFrenzy);
            o.update(message);
        }
    }

    /**
     * Updates the observer with the selectable items of the corresponding player.
     * The update message is specific of the receiver.
     * @param player the receiving player
     */
    public void notifySelectableUpdate(Player player){
        List<String> selWeapons = new ArrayList<>();
        for (Weapon w : player.getSelectableWeapons()){
            selWeapons.add(w.getName());
        }

        List<String> selSquares = new ArrayList<>();
        for (Square s : player.getSelectableSquares()){
            selSquares.add(s.toString());
        }

        List<String> selPlayers = new ArrayList<>();
        for (Player p : player.getSelectablePlayers()){
            selPlayers.add(p.getName());
        }

        List<String> selModes = new ArrayList<>();
        for (Mode m : player.getSelectableModes()){
            selModes.add(m.getTitle());
        }

        List<String> selActions = new ArrayList<>();
        for (Action a : player.getSelectableActions()){
            selActions.add(a.toString());
        }

        List<String> selColors = new ArrayList<>();
        for (AmmoColor c : player.getSelectableColors()){
            selColors.add(c.toString());
        }

        List<String> selPowerUps = new ArrayList<>();
        for (PowerUp p : player.getSelectablePowerUps()){
            selPowerUps.add(p.toString());
        }

        List<String> selCommands = new ArrayList<>();
        for (Command c : player.getSelectableCommands()){
            selCommands.add(c.toString());
        }

        String currWeapon;
        if (player.getState() == PlayerState.CHOOSE_MODE){
            currWeapon = getCurrentAction().getCurrWeapon().getName();
        } else {
            currWeapon = "";
        }

        SelectablesUpdateMessage message = new SelectablesUpdateMessage(selWeapons, selSquares, selModes, selCommands, selActions, selColors, selPlayers, selPowerUps, currWeapon);
        Observer o = observers.get(player);
        if (o != null){
            o.update(message);
        }
    }

    /**
     * Updates all players with their specific updates of selectable items
      */
    public void notifySelectablesUpdateAllPlayers(){
        for (Player p : players){
            notifySelectableUpdate(p);
        }
    }

    /**
     * Updates all observers with the complete set of information regarding the match in progress
     */
    public void notifyFullUpdateAllPlayers(){
        notifyLayotUpdate();
        notifyKillShotTrackUpdate();
        notifyCurrentPlayerUpdate();
        for (Player p : players){
            notifyPlayerUpdate(p);
            notifyPaymentUpdate(p);
            notifyWeaponsUpdate(p);
            notifyPowerUpUpdate(p);
            notifyDamageUpdate(p);
        }
    }

    /**
     * Sends to all observers the rank and final scoring of each player, and states the end of the game.
     */
    public void notifyGameOver(){
        Map<String, Integer> rank = new HashMap<>();
        for (Map.Entry<Player, Integer> entry : getRank().entrySet()){
            rank.put(entry.getKey().getName(), entry.getValue());
        }
        Map<String, Integer> points = new HashMap<>();
        for (Map.Entry<Player, Integer> entry : getAllPoints().entrySet()){
            points.put(entry.getKey().getName(), entry.getValue());
        }

        for (Observer o : observers.values()){
            GameOverMessage message = new GameOverMessage(rank, points);
            o.update(message);
        }

    }
}

