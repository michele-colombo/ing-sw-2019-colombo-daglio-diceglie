package it.polimi.ingsw.server.model;



import it.polimi.ingsw.communication.message.*;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerColor;
import it.polimi.ingsw.server.model.enums.PlayerState;
import it.polimi.ingsw.server.observer.Observable;
import it.polimi.ingsw.server.observer.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.server.model.enums.PowerUpType.*;

/**
 * It represents the current match
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
     * @param layoutConfig It's the number of the selected layout
     * @param skulls It's the number of skull of the KillShotTrack
     */
    public Match(int layoutConfig, int skulls){
        layout = new Layout();
        layout.initLayout(layoutConfig);
        players = new ArrayList<>();
        waitingFor = new ArrayList<>();
        killShotTrack = new KillShotTrack(skulls);
        stackManager = new StackManager();
        currentAction = null;
        currentPlayer = null;
    }

    public Match(int layoutConfig){
        this(layoutConfig, 8);
    }

    public void setObservers(Map<Player, Observer> observers) {
        this.observers = observers;
    }

    public StackManager getStackManager() {
        return stackManager;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public KillShotTrack getKillShotTrack() {
        return killShotTrack;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Returns all the players on squares
     * @param squares The squares to be analyzed
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

    public void addPlayer(Player p){
        this.players.add(p);
    }

    public void activateFrenzy(){
        this.frenzyOn = true;
    }

    /**
     * Return the winner or the winners of the match
     * @return An ArrayList containing the winning player/players (in case of tie)
     */
    public List<Player> getWinners(){
        List<Player> almostWinners = getMaxPoints();
        if(almostWinners.size() == 1){
            return almostWinners;
        }
        else{
            int max = -1;
            List<Player> winners = null;
            for(Player p : killShotTrack.score().keySet()){
                if(almostWinners.contains(p)){
                    if(killShotTrack.score().get(p) > max){
                        winners = new ArrayList<>();
                        winners.add(p);
                        max = killShotTrack.score().get(p);
                    }
                    else if(killShotTrack.score().get(p) == max){
                        winners.add(p);
                    }
                }
            }
            if(winners == null){
                return  almostWinners;
            }
            return winners;
        }
    }

    /**
     * Return all the players with highest number of points (considering both player's point and KillShotTrack)
     * @return An ArrayList containing players
     */
    private List<Player> getMaxPoints(){
        List<Player> maxPoints = null;
        Map<Player, Integer> finalPoints = getFinalPoints();
        int max = -1;
        for(Player p : finalPoints.keySet()){
            if(finalPoints.get(p) > max){
                maxPoints = new ArrayList<>();
                maxPoints.add(p);
                max = finalPoints.get(p);
            }
            else if(finalPoints.get(p) == max){
                maxPoints.add(p);
            }
        }
        return maxPoints;
    }

    public String getRank(){
        int rankIndex = 1;
        StringBuilder rank = new StringBuilder();
        List<Player> winners = getWinners();
        for(Player p : winners){
            rank.append(rankIndex + " " + p.getName() + " has won the game with " + p.getPoints() + " points");
        }
        rankIndex = winners.size() + 1;
        for(Player p : players){
            rank.append(rankIndex + " " + p.getName() + " has lost the game with " + p.getPoints() + " points");
            rankIndex++;
        }
        return rank.toString();
    }

    /**
     * Returns a map containing each player and his own points
     * @return An HashMap
     */
    private Map<Player, Integer> getAllPoints(){
        Map<Player, Integer> points = new HashMap<>();
        for(Player p : players){
            points.put(p, p.getPoints());
        }
        return points;
    }

    /**
     * Returns a map containing all the players who has killed anyone and how many points they get from the killShotTrack
     * @return Ah HashMap
     */
    private Map<Player, Integer> getFinalPoints(){
        Map<Player, Integer> finalPoints = getAllPoints();
        Map<Player, Integer> killShotTrackPoints = killShotTrack.score();
        for(Player p : killShotTrackPoints.keySet()){
            finalPoints.replace(p, finalPoints.get(p), finalPoints.get(p) + killShotTrackPoints.get(p));
        }
        return finalPoints;
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
            }
        }

        if(deadPlayers.size() > 2){
            currentPlayer.addPoints(1);
        }
        if(!frenzyOn && killShotTrack.getSkulls() <= 0){
            activateFrenzy();
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
     * @param p The player sho is in CHOOSE_ACTION state
     * @return
     */
    public List<Action> createSelectableActions(Player p) {
        //could be loaded from a json file in the future?
        List<Action> result = new ArrayList<>();
        Action temp;
        if (!alreadyCompleted) {
            if (numberOfActions - actionsCompleted > 0) {
                if (!frenzyOn) {
                    switch (p.getDamageTrack().getAdrenaline()) {
                        case 0:
                            result.add(new Action(true, false, new Move(3)));
                            result.add(new Action(true, false, new Grab(1)));
                            temp = new Action(true, false, new Shoot());
                            if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UsePowerUp(TARGETING_SCOPE));
                            temp.add(new UsePowerUp(TAGBACK_GRENADE));
                            result.add(temp);
                            break;
                        case 1:
                            result.add(new Action(true, false, new Move(3)));
                            result.add(new Action(true, false, new Grab(2)));
                            temp = new Action(true, false, new Shoot());
                            if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UsePowerUp(TARGETING_SCOPE));
                            temp.add(new UsePowerUp(TAGBACK_GRENADE));
                            result.add(temp);
                            break;
                        case 2:
                            result.add(new Action(true, false, new Move(3)));
                            result.add(new Action(true, false, new Grab(2)));
                            temp = new Action(true, false, new Move(1));
                            temp.add(new Shoot());
                            if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UsePowerUp(TARGETING_SCOPE));
                            temp.add(new UsePowerUp(TAGBACK_GRENADE));
                            result.add(temp);
                            break;
                    }
                } else {
                    if (p.isBeforeFirst()) {
                        temp = new Action(true, false, new Move(1));
                        temp.add(new Reload());
                        temp.add(new Shoot());
                        if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UsePowerUp(TARGETING_SCOPE));
                        temp.add(new UsePowerUp(TAGBACK_GRENADE));
                        result.add(temp);
                        result.add(new Action(true, false, new Move(4)));
                        result.add(new Action(true, false, new Grab(2)));
                    } else {
                        temp = new Action(true, false, new Move(2));
                        temp.add(new Reload());
                        temp.add(new Shoot());
                        if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UsePowerUp(TARGETING_SCOPE));
                        temp.add(new UsePowerUp(TAGBACK_GRENADE));
                        result.add(temp);
                        result.add(new Action(true, false, new Grab(3)));
                    }
                }
            } else {
                turnCompletable = true;
                if (!p.getUnloadedWeapons().isEmpty()) {
                    result.add(new Action(false, true, new Reload()));
                }
            }
            if (!onlyReload && p.howManyPowerUps(ACTION_POWERUP) > 0) {
                result.add(new Action(false, false, new UsePowerUp(ACTION_POWERUP)));
            }
        }
        return result;
    }

    /**
     * Creates the list of action a player can take. Must be called at the beginning of his turn.
     * It also initializes support variables to keep track of the turn status.
     * @param p A player in the beginning of his turn, while is in CHOOSE_ACTION state.
     * @return
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
     * @return
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
    private void scoreDamageTrack(Map<Player, Integer> points){
        for(Player p : players){
            if(points.containsKey(p)){
                p.addPoints(points.get(p));
            }
        }
    }

    public void createBackup(){
        ;
    }
    /**
     * checks if the player can complete his turn in the current moment
     * @return
     */
    public boolean isTurnCompletable() {
        return turnCompletable;
    }

    /**
     * Checks if final frenzy is currently active
     * @return frenzyOn
     */
    public boolean isFrenzyOn() {
        return frenzyOn;
    }

    public int getNumberOfActions() {
        return numberOfActions;
    }

    public int getActionsCompleted() {
        return actionsCompleted;
    }

    public boolean isOnlyReload() {
        return onlyReload;
    }

    public Player getPlayerFromName(String name){
        for(Player p : players){
            if (p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

    public void setFrenzyOn(boolean frenzyOn) {
        this.frenzyOn = frenzyOn;
    }

    public void setNumberOfActions(int numberOfActions) {
        this.numberOfActions = numberOfActions;
    }

    public void setActionsCompleted(int actionsCompleted) {
        this.actionsCompleted = actionsCompleted;
    }

    public void setOnlyReload(boolean onlyReload) {
        this.onlyReload = onlyReload;
    }

    public void setTurnCompletable(boolean turnCompletable) {
        this.turnCompletable = turnCompletable;
    }

    public boolean isAlreadyCompleted() {
        return alreadyCompleted;
    }

    public void setAlreadyCompleted(boolean alreadyCompleted) {
        this.alreadyCompleted = alreadyCompleted;
    }

    public List<Player> getWaitingFor() {
        return waitingFor;
    }

    public void clearWaitingFor() {
        waitingFor.clear();
    }

    public void addWaitingFor(Player p){
        if (!waitingFor.contains(p)){
            waitingFor.add(p);
        }
    }

    public boolean removeWaitingFor(Player p){
        return waitingFor.remove(p);
    }

    public Player getNextPlayer(Player currP){
        int index;
        Player nextPlayer;
        if (currP == null){
            nextPlayer = players.get(0);    //if current player is null, returns the first player
        } else {
            index = players.indexOf(currP);
            assert (index != -1);
            if (index == players.size()-1){
                nextPlayer = players.get(0);
            } else {
                nextPlayer = players.get(index + 1);
            }
        }
        return nextPlayer;
    }

    public Player getFirstPlayer(){
        return players.get(0);
    }

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

    public void notifyKillShotTrackUpdate(){
        int skulls = killShotTrack.getSkulls();
        boolean frenzyOn = isFrenzyOn();

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
            KillshotTrackUpdateMessage message = new KillshotTrackUpdateMessage(skulls, track, frenzyOn, yourPoints);
            o.getValue().update(message);
        }
    }

    public void notifyCurrentPlayerUpdate(){
        if (currentPlayer != null) {
            String currentPlayer = getCurrentPlayer().getName();
            for (Observer o : observers.values()) {
                CurrentPlayerUpdateMessage message = new CurrentPlayerUpdateMessage(currentPlayer);
                o.update(message);
            }
        }
    }

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

    public void notifyPaymentUpdate(Player receiver){
        Cash pending = receiver.getPending();
        Cash credit = receiver.getCredit();

        PaymentUpdateMessage message = new PaymentUpdateMessage(pending, credit);
        observers.get(receiver).update(message);
    }

    public void notifyWeaponsUpdate(Player player){
        String name = player.getName();
        List<String> loadedWeapons = new ArrayList<>();
        List<String> unloadedWeapons = new ArrayList<>();
        int numLoadedWeapons;
        for (Weapon w : player.getLoadedWeapons()){
            loadedWeapons.add(w.getName());
        }
        numLoadedWeapons = loadedWeapons.size();

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
        observers.get(player).update(message);
    }

    public void notifyAllSelectablesUpdate(){
        for (Player p : players){
            notifySelectableUpdate(p);
        }
    }

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
        for (Player p : players){
            notifySelectableUpdate(p);
        }
    }
}

