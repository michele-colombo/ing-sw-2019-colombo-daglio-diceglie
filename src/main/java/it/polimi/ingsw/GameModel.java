package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.PlayerState.*;

public class GameModel {
    private List<Player> activePlayers;
    private List<Player> spawningPlayers;
    private Match match;
    private Match backupMatch;

    public GameModel(){
        activePlayers = new ArrayList<>();
        match = null;
        backupMatch = null;
    }

    public boolean initMatch(){
        if (match == null){
            match = new Match();
            return true;
        } else {
            return false;
        }
    }

    //TODO check name uniqueness and throw exceptions
    public boolean addPlayer (Player p){
        activePlayers.add(p);
        match.addPlayer(p);
        p.setState(IDLE);
        p.resetSelectables();
        return true;    //will be changed
    }

    //TODO check number of players, etc.
    public void startMatch(){
        beginNextTurn();
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
        p.discardPowerUp(po);
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
        match.getCurrentAction().getMicroActions().get(0).act(match, p);    //the microAction sets player state

    }

}
