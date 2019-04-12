package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.PowerUpType.*;

public class Match {
    private Layout layout;
    private StackManager stackManager;
    private List<Player> players;
    private KillShotTrack killShotTrack;
    private boolean frenzyOn;
    private int numberOfActions;
    private int actionsCompleted;
    private boolean onlyReload;
    private boolean turnCompleatable;
    private Action currentAction;
    private Player currentPlayer;

    public Match(int skulls){
        layout = new Layout();
        //layout.initLayout(1);   //TODO ask for layout configuration
        stackManager = new StackManager();
        players = new ArrayList<>();
        killShotTrack = new KillShotTrack(skulls);
        currentAction = null;
    }

    public Match(){
        this(8);
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

    public void activateFrenezy(){
        this.frenzyOn = true;
    }

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

    private Map<Player, Integer> getAllPoints(){
        Map<Player, Integer> points = new HashMap<>();
        for(Player p : players){
            points.put(p, p.getPoints());
        }
        return points;
    }

    private Map<Player, Integer> getFinalPoints(){
        Map<Player, Integer> finalPoints = getAllPoints();
        Map<Player, Integer> killShotTrackPoints = killShotTrack.score();
        for(Player p : killShotTrackPoints.keySet()){
            finalPoints.replace(p, finalPoints.get(p), finalPoints.get(p) + killShotTrackPoints.get(p));
        }
        return finalPoints;
    }

    public void spawn(Player p, Color c){
        p.setSquarePosition(layout.getSpawnPoint(c));
    }

    public List<Player> endTurnCheck(){
        List<Player> deadPlayers = getDeadPlayers();
        if(deadPlayers.size() > 0){
            for(Player p : deadPlayers){
                scoreDamageTrack(p.getDamageTrack().score());
            }
        }
        if(deadPlayers.size() > 2){
            currentPlayer.addPoints(1);
        }
        if(frenzyOn){
            switchToFrenzyAll(deadPlayers);
        }
        resetAfterDeathAll(deadPlayers);
        return deadPlayers;
    }

    public List<Action> createSelectablesAction(Player p){  //todo rename in createSelectableActions
        //could be loaded from a json file in the future?
        List<Action> result = new ArrayList<>();
        Action temp;
        if (numberOfActions - actionsCompleted > 0){
            if (!frenzyOn){
                switch(p.getDamageTrack().getAdrenaline()){
                    case 0:
                        result.add(new Action(true, false, new Move(3)));
                        result.add(new Action(true, false, new Grab(1)));
                        temp = new Action (true, false, new Shoot());
                        if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UsePowerUp(TARGETING_SCOPE));
                        result.add(temp);
                        break;
                    case 1:
                        result.add(new Action(true, false, new Move(3)));
                        result.add(new Action(true, false, new Grab(2)));
                        temp = new Action(true, false, new Shoot());
                        if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UsePowerUp(TARGETING_SCOPE));
                        result.add(temp);
                        break;
                    case 2:
                        result.add(new Action(true, false, new Move(3)));
                        result.add(new Action(true, false, new Grab(2)));
                        temp = new Action(true, false, new Move(1));
                        temp.add(new Shoot());
                        if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UsePowerUp(TARGETING_SCOPE));
                        result.add(temp);
                        break;
                }
            } else {
                if (p.isBeforeFirst()){
                    temp = new Action(true, false, new Move(1));
                    temp.add(new Reload());
                    temp.add(new Shoot());
                    if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UsePowerUp(TARGETING_SCOPE));
                    result.add(temp);
                    result.add(new Action(true, false, new Move(4)));
                    result.add(new Action(true, false, new Grab(2)));
                } else {
                    temp = new Action(true, false, new Move(2));
                    temp.add(new Reload());
                    temp.add(new Shoot());
                    if (p.howManyPowerUps(TARGETING_SCOPE) > 0) temp.add(new UsePowerUp(TARGETING_SCOPE));
                    result.add(temp);
                    result.add(new Action (true, false, new Grab(3)));
                }
            }
        } else {
            if (p.getUnloadedWeapons().size() > 0){
                result.add(new Action(false, true, new Reload()));
            }
        }
        if (!onlyReload && p.howManyPowerUps(YOUR_TURN_POWERUP) > 0){
            result.add(new Action(false, false, new UsePowerUp(YOUR_TURN_POWERUP)));
        }
        return result;
    }

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
        actionsCompleted = 0;
        onlyReload = false;
        return createSelectablesAction(p);
    }

    public void updateTurnStatus(Action a){
        if (a.isIncrementActionCounter()) actionsCompleted++;
        if (a.isActivateOnlyReloads()) onlyReload = true;
    }

    public Action getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
    }

    public Layout getLayout(){
        return layout;
    }

    public boolean getFrenzy(){
        return frenzyOn;
    }

    private void switchToFrenzyAll(List<Player> deadPlayers){
        for(Player p : deadPlayers){
            p.switchToFrenzy();
        }
    }

    private void resetAfterDeathAll(List<Player> deadPlayers){
        for(Player p : deadPlayers){
            p.getDamageTrack().resetAfterDeath();
        }
    }

    private List<Player> getDeadPlayers(){
        List<Player> deadPlayers = new ArrayList<>();
        for(Player p : players){
            if(!p.isAlive()){
                deadPlayers.add(p);
            }
        }
        return deadPlayers;
    }

    private void scoreDamageTrack(Map<Player, Integer> points){
        for(Player p : players){
            if(points.containsKey(p)){
                p.addPoints(points.get(p));
            }
        }
    }

    public Match(int config, int skulls){
        layout = new Layout();
        layout.initLayout(config);
        players = new ArrayList<>();
        killShotTrack = new KillShotTrack(skulls);
    }


}
