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
        spawningPlayers = new ArrayList<>();
        match = null;
        backupMatch = null;
    }

    public Match getMatch() {
        return match;
    }

    public boolean initMatch(){
        if (match == null){
            match = new Match();
            return true;
        } else {
            return false;
        }
    }

    public boolean initMatch(int skull, int config){
        if (match == null){
            match = new Match(skull, config);
            return true;
        } else {
            return false;
        }
    }

    //TODO check name and color uniqueness and throw exceptions
    public synchronized boolean addPlayer (Player p){
        if(!nameTaken(p.getName()) && !colorTaken(p.getColor())){
            activePlayers.add(p);
            notifyAll();
            return true;
        }
        notifyAll();
        return false;
    }

    //TODO check number of players, etc.
    public void startMatch(){
        for (Player p : activePlayers){
            match.addPlayer(p);
            p.setState(IDLE);
            p.resetSelectables();
        }
        match.getLayout().refillAll(match.getStackManager());
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
        p.removePowerUp(po);
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
        match.getCurrentAction().getCurrEffects().get(0).start(p, match);
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
            effects.get(0).start(p, match);
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
                realoadWeapon(p, match.getCurrentAction().getCurrWeapon());
                break;
            case GRAB_WEAPON:
                grabWeapon(p, match.getCurrentAction().getCurrWeapon());
                break;
            case CHOOSE_MODE:
                addMode(p, match.getCurrentAction().getCurrMode());
                break;
        }
    }

    public void realoadWeapon (Player p, Weapon w){
        p.setPending(w.getCost());
        if (p.getCredit().isEqual(p.getPending())){
            p.getPending().setZero();
            p.getCredit().setZero();
            p.reload(w);
            nextMicroAction();
        } else {
            p.setNextState(RELOAD);
            match.getCurrentAction().setCurrWeapon(w);
            setupForPaying(p);
        }
    }

    private void nextMicroAction(){
        List<MicroAction> temp = match.getCurrentAction().getMicroActions();
        temp.remove(0);
        if (temp.isEmpty()){
            actionCompleted();
        } else {
            temp.get(0).act(match, match.getCurrentPlayer());
        }
    }

    private void actionCompleted(){
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
            //createMatchBackup();
        }
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
        for(Player p : activePlayers){
            if(p.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    private boolean colorTaken(PlayerColor color){
        for(Player p : activePlayers){
            if(p.getColor() == color){
                return true;
            }
        }
        return false;
    }

    public int getNumberOfPlayers(){
        return activePlayers.size();
    }





}
