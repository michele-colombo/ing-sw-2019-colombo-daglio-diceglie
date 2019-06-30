package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.PlayerColor;
import it.polimi.ingsw.server.model.enums.PlayerState;

import java.io.*;
import java.util.*;


/**
 * It contains information in order to make a rollback during a match or to resume a match from file
 */
public class Backup {

    /**
     * Contains the backup of a player
     */
    private class PlayerBackup{
        /**
         * name of the player (identifier)
         */
        private String name;

        /**
         * color of the player
         */
        private PlayerColor color;

        /**
         * has the player already spawned for the first time?
         */
        private boolean isBorn;

        /**
         * is the player before or after the first (significant in frenzy)
         */
        private boolean isBeforeFirst;

        /**
         * points of the player
         */
        private int points;

        /**
         * state of the player
         */
        private PlayerState state;

        /**
         * next state of the player
         */
        private PlayerState nextState;

        /**
         * is the player the first?
         */
        private boolean isFirstPlayer;

        /**
         * has the player another turn in the game?
         */
        private boolean hasAnotherTurn;

        /**
         * position of the player (saved as square)
         */
        private String squarePosition;

        /**
         * skull number on the palyer's damage track
         */
        private int skullsNumber;

        /**
         * has the damage track been turned (frenzy mode)?
         */
        private boolean isFrenzy;

        /**
         * list of damges of the player
         */
        private List<String> damageList;

        /**
         * list of marks of the player
         */
        private Map<String, Integer> markMap;

        /**
         * weapons of the player, with their charging status
         */
        private Map<String, Boolean> weapons;

        /**
         * list of powerUps of the player
         */
        private List<String> powerUps;

        /**
         * ammos currently owned by the player
         */
        private Cash wallet;

        /**
         * ammos the player has currently to pay
         */
        private Cash pending;

        /**
         * ammos the player has currently paid
         */
        private Cash credit;

        /**
         * Builds the backup of a player
         * @param p the player to backup
         */
        private PlayerBackup(Player p){
            name = p.getName();
            color = p.getColor();
            isBorn = p.isBorn();
            isBeforeFirst = p.isBeforeFirst();
            hasAnotherTurn = p.hasAnotherTurn();
            points = p.getPoints();
            state = p.getState();
            nextState = p.getNextState();
            isFirstPlayer = p.isFirstPlayer();
            if (isBorn && p.getSquarePosition()!=null) squarePosition = p.getSquarePosition().toString();
            skullsNumber = p.getDamageTrack().getSkullsNumber();
            isFrenzy = p.getDamageTrack().isFrenzy();

            damageList = new ArrayList<>();
            for (Player player : p.getDamageTrack().getDamageList()){
                damageList.add(player.getName());
            }

            markMap = new HashMap<>();
            for (Map.Entry<Player, Integer> entry : p.getDamageTrack().getMarkMap().entrySet()){
                markMap.put(entry.getKey().getName(), entry.getValue());
            }

            weapons = new HashMap<>();
            for (Weapon w : p.getWeapons()){
                weapons.put(w.getName(), p.isLoaded(w));
            }

            powerUps = new ArrayList<>();
            for (PowerUp po : p.getPowerUps()){
                powerUps.add(po.toString());
            }

            wallet = new Cash(p.getWallet());
            pending = new Cash(p.getPending());
            credit = new Cash(p.getCredit());
        }

        /**
         * Sets all attributes in the player as in the backup. Objects are taken from the current match
         * @param p player to restore to backup status
         * @param match current match (used to take current objects for weapons, powerUps and squares)
         */
        private void restore(Player p, Match match){
            p.setColor(color);
            p.setBorn(isBorn);
            p.setBeforeFirst(isBeforeFirst);
            p.setAnotherTurn(hasAnotherTurn);
            p.setPoints(points);
            p.setState(state);
            p.setNextState(nextState);
            p.setFirstPlayer(isFirstPlayer);
            p.setSquarePosition(match.getLayout().getSquareFromString(squarePosition));
            if (isFrenzy){
                p.setDamageTrack(new FrenzyDamageTrack());
            } else {
                p.setDamageTrack(new NormalDamageTrack());
            }
            p.getDamageTrack().setSkullsNumber(skullsNumber);

            List<Player> tempDamageList = new ArrayList<>();
            for (String damagerName : damageList){
                tempDamageList.add(match.getPlayerFromName(damagerName));
            }
            p.getDamageTrack().setDamageList(tempDamageList);

            Map<Player, Integer> tempMarkMap = new HashMap<>();
            for (Map.Entry<String, Integer> entry : markMap.entrySet()){
                tempMarkMap.put(match.getPlayerFromName(entry.getKey()), entry.getValue());
            }
            p.getDamageTrack().setMarkMap(tempMarkMap);

            Map<Weapon, Boolean> tempWeapons = new HashMap<>();
            for (Map.Entry<String, Boolean> entry : weapons.entrySet()){
                tempWeapons.put(match.getStackManager().getWeaponFromName(entry.getKey()), entry.getValue());
            }
            p.setWeapons(tempWeapons);

            List<PowerUp> tempPowerUps = new ArrayList<>();
            for (String s : powerUps){
                tempPowerUps.add(match.getStackManager().getPowerUpFromString(s));
            }
            p.setPowerUps(tempPowerUps);

            p.getWallet().set(wallet);
            p.getCredit().set(credit);
            p.getPending().set(pending);
        }

        /**
         * checks equality between the backups of two players
         * @param obj the object to compare
         * @return true if it is equal
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof PlayerBackup)) return false;
            PlayerBackup other = (PlayerBackup)obj;
            if (!(name.equals(other.name))) return false;
            if (!(color.equals(other.color))) return false;
            if (isBorn != other.isBorn) return false;
            if (isBeforeFirst != other.isBeforeFirst) return false;
            if (hasAnotherTurn != other.hasAnotherTurn) return false;
            if (points != other.points) return false;
            if (state != other.state) return false;
            if (isFirstPlayer != other.isFirstPlayer) return false;
            if (squarePosition == null){
                if (other.squarePosition != null) return false;
            } else if (!(squarePosition.equals(other.squarePosition))) return false;
            if (skullsNumber != other.skullsNumber) return false;
            if (isFrenzy != other.isFrenzy) return false;
            if (!(damageList.equals(((PlayerBackup) obj).damageList))) return false;
            if (!(markMap.equals(other.markMap))) return false;
            if (!(weapons.equals(other.weapons))) return false;     //weapons is a map, so order doesn't count in the comparison
            for (String s : powerUps){      //frequency should always be 1 for every power up string
                for (String sOther: other.powerUps) {
                    if (s.equals(sOther) && Collections.frequency(powerUps, s) != Collections.frequency(other.powerUps, sOther)) {
                        return false;
                    }
                }
            }
            Set<String> tempPowerUpsSet = new HashSet<>(powerUps);
            Set<String> tempOtherPowerUpsSet = new HashSet<>(other.powerUps);
            if (!(tempPowerUpsSet.equals(tempOtherPowerUpsSet))) return false;  //powerUps are compared as sets, so order doesn't count in the comparison
            if (!(wallet.equals(other.wallet))) return false;
            if (!(pending.equals(other.pending))) return false;
            if (!(credit.equals(other.credit))) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return name.hashCode() +
                    color.toString().hashCode() +
                    points * 127 +
                    state.toString().hashCode() +
                    skullsNumber * 63 +
                    wallet.hashCode();
        }
    }

    /**
     * Contains the backup of the kill shot track of the game
     */
    private class KillShotTrackBackup{
        /**
         * number of skulls currently present
         */
        private int skulls;

        /**
         * the killshot track with killings already performed
         */
        private List<Map<String, Integer>> track;

        /**
         * Builds the backup of the killshot track
         * @param k the killshot track to backup
         */
        private KillShotTrackBackup(KillShotTrack k){
            skulls = k.getSkulls();

            track = new ArrayList<>();
            for (Map<Player, Integer> map : k.getTrack()){
                Map<String, Integer> temp = new HashMap<>();
                for (Map.Entry<Player, Integer> entry : map.entrySet()){
                    temp.put(entry.getKey().getName(), entry.getValue());
                }
                track.add(temp);
            }
        }

        /**
         * Restores the passed killshot track to the state saved in the backup, using objects from the current match
         * @param k the killshot track to restore
         * @param match the current match (used to take the actual objects of players)
         */
        private void restore(KillShotTrack k, Match match){
            k.setSkulls(skulls);
            k.clearKillingCounter();
            k.clearKillingOrder();
            k.clearTrack();

            for (Map<String, Integer> map : track){
                Map<Player, Integer> temp = new HashMap<>();
                for (Map.Entry<String, Integer> entry : map.entrySet()){
                    temp.put(match.getPlayerFromName(entry.getKey()), entry.getValue());
                }
                k.addKilled(temp);
            }
        }

        /**
         * checks equality between two killshot track backups
         * @param obj the object to compare
         * @return treu if it is equal
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof KillShotTrackBackup)) return false;
            KillShotTrackBackup other = (KillShotTrackBackup)obj;
            if (skulls != other.skulls) return false;
            if (!(track.equals(other.track))) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return skulls;
        }
    }

    /**
     * Contains the backup of the stack manager (with cards' objects of the match)
     */
    private class StackManagerBackup{
        /**
         * the stack of the weapons to draw
         */
        private List<String> weaponActiveStack;

        /**
         * the stack of the powerUps to draw
         */
        private List<String> powerUpActiveStack;

        /**
         * the stack of the powerUps already drawed and discarded
         */
        private List<String> powerUpWasteStack;

        /**
         * the stack of the ammoTiles to draw
         */
        private List<String> ammoTilesActiveStack;

        /**
         * the stack of the ammoTiles already drawed and discarded
         */
        private List<String> ammoTilesWasteStack;

        /**
         * Builds the backup of the current stack manager
         * @param sm the stack manager to backup
         */
        private StackManagerBackup(StackManager sm){
            weaponActiveStack = new ArrayList<>();
            for (Weapon w : sm.getWeaponActiveStack()){
                weaponActiveStack.add(w.getName());
            }

            powerUpActiveStack = new ArrayList<>();
            for (PowerUp po : sm.getPowerUpActiveStack()){
                powerUpActiveStack.add(po.toString());
            }

            powerUpWasteStack = new ArrayList<>();
            for (PowerUp po : sm.getPowerUpWasteStack()){
                powerUpWasteStack.add(po.toString());
            }

            ammoTilesActiveStack = new ArrayList<>();
            for (AmmoTile at : sm.getAmmoTilesActiveStack()){
                ammoTilesActiveStack.add(at.toString());
            }

            ammoTilesWasteStack = new ArrayList<>();
            for (AmmoTile at : sm.getAmmoTilesWasteStack()){
                ammoTilesWasteStack.add(at.toString());
            }
        }

        /**
         * Restores the stack manager to the state saved in the backup (building new objects for cards)
         * @param stackManager the stack manager to restore
         */
        private void restore(StackManager stackManager){
            List<Weapon> tempWeapons = new ArrayList<>();
            for (String name : weaponActiveStack){
                tempWeapons.add(stackManager.getWeaponFromName(name));
            }
            stackManager.initWeaponStack(tempWeapons);

            List<PowerUp> tempPowerUpsActive = new ArrayList<>();
            for (String str : powerUpActiveStack){
                tempPowerUpsActive.add(stackManager.getPowerUpFromString(str));
            }
            stackManager.initPowerUpStack(tempPowerUpsActive);

            List<PowerUp> tempPowerUpsWaste = new ArrayList<>();
            for (String str : powerUpWasteStack){
                tempPowerUpsWaste.add(stackManager.getPowerUpFromString(str));
            }
            stackManager.initPowerUpWasteStack(tempPowerUpsWaste);

            List<AmmoTile> tempAmmoTiles = new ArrayList<>();
            for (String str : ammoTilesActiveStack){
                tempAmmoTiles.add(stackManager.getAmmoTileFromString(str));
            }
            stackManager.initAmmoTilesStack(tempAmmoTiles);

            List<AmmoTile> tempAmmoTilesWaste = new ArrayList<>();
            for (String str : ammoTilesWasteStack){
                tempAmmoTilesWaste.add(stackManager.getAmmoTileFromString(str));
            }
            stackManager.initAmmoTilesWasteStack(tempAmmoTilesWaste);
        }

        /**
         * chacks equality between two stack managers
         * @param obj the object to compare
         * @return true if it is equal
         */
        @Override
        public boolean equals(Object obj) {
            if (!( obj instanceof StackManagerBackup)) return false;
            StackManagerBackup other = (StackManagerBackup)obj;
            if (!(weaponActiveStack.equals(other.weaponActiveStack))) return false;
            if (!(powerUpActiveStack.equals(other.powerUpActiveStack))) return false;
            Set<String> set = new HashSet<>(powerUpWasteStack);
            Set<String> otherSet = new HashSet<>(other.powerUpWasteStack);
            if (!(set.equals(otherSet))) return false;
            if (!(ammoTilesActiveStack.equals(other.ammoTilesActiveStack))) return false;
            set = new HashSet<>(ammoTilesWasteStack);
            otherSet = new HashSet<>(other.ammoTilesWasteStack);
            if (!(set.equals(otherSet))) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return weaponActiveStack.hashCode() +
                    powerUpActiveStack.hashCode() +
                    ammoTilesActiveStack.hashCode();
        }
    }

    /**
     * Contains the backup of the current layout
     */
    private class LayoutBackup{
        /**
         * number of configuration
         */
        private int layoutConfiguration;

        /**
         * weapons in the blue spawn point
         */
        private List<String> blueWeapons;

        /**
         * weapons in the red spawn point
         */
        private List<String> redWeapons;

        /**
         * weapons in the yellow spawn point
         */
        private List<String> yellowWeapons;

        /**
         * ammoTiles in the squares of the map
         */
        private Map<String, String> ammosTilesInSquares;

        /**
         * Builds the backup of the current layout
         * @param layout the layout to backup
         */
        private LayoutBackup(Layout layout){
            layoutConfiguration = layout.getLayoutConfiguration();

            blueWeapons = new ArrayList<>();
            for (Weapon w : layout.getSpawnPoint(AmmoColor.BLUE).getWeapons()){
                blueWeapons.add(w.getName());
            }

            redWeapons = new ArrayList<>();
            for (Weapon w : layout.getSpawnPoint(AmmoColor.RED).getWeapons()){
                redWeapons.add(w.getName());
            }

            yellowWeapons = new ArrayList<>();
            for (Weapon w : layout.getSpawnPoint(AmmoColor.YELLOW).getWeapons()){
                yellowWeapons.add(w.getName());
            }

            ammosTilesInSquares = new HashMap<>();
            for (AmmoSquare as : layout.getAmmoSquares()){
                if (as.getAmmo()!=null){
                    ammosTilesInSquares.put(as.toString(), as.getAmmo().toString());
                } else {
                    ammosTilesInSquares.put(as.toString(), "");
                }
            }
        }

        /**
         * Restores the layout to the state saved in the backup (using objects from current match)
         * @param layout the layout to restore
         * @param match the current match (used to take the actual objects of the cards)
         */
        private void restore(Layout layout, Match match){
            SpawnSquare tempSpawnSquare = layout.getSpawnPoint(AmmoColor.BLUE);
            tempSpawnSquare.clearWeapons();
            for (String name : blueWeapons){
                tempSpawnSquare.addWeapon(match.getStackManager().getWeaponFromName(name));
            }

            tempSpawnSquare = layout.getSpawnPoint(AmmoColor.RED);
            tempSpawnSquare.clearWeapons();
            for (String name : redWeapons){
                tempSpawnSquare.addWeapon(match.getStackManager().getWeaponFromName(name));
            }

            tempSpawnSquare = layout.getSpawnPoint(AmmoColor.YELLOW);
            tempSpawnSquare.clearWeapons();
            for (String name : yellowWeapons){
                tempSpawnSquare.addWeapon(match.getStackManager().getWeaponFromName(name));
            }

            AmmoSquare tempAmmoSquare;
            for (Map.Entry<String, String> entry : ammosTilesInSquares.entrySet()){
                tempAmmoSquare = (AmmoSquare)layout.getSquareFromString(entry.getKey());
                tempAmmoSquare.setAmmo(match.getStackManager().getAmmoTileFromString(entry.getValue()));
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof LayoutBackup)) return false;
            LayoutBackup other = (LayoutBackup)obj;
            if (layoutConfiguration != other.layoutConfiguration) return false;
            Set<String> tempSet1 = new HashSet<>(blueWeapons);
            Set<String> tempSet2 = new HashSet<>(other.blueWeapons);
            if (!(tempSet1.equals(tempSet2))) return false;
            tempSet1 = new HashSet<>(redWeapons);
            tempSet2 = new HashSet<>(other.redWeapons);
            if (!(tempSet1.equals(tempSet2))) return false;
            tempSet1 = new HashSet<>(yellowWeapons);
            tempSet2 = new HashSet<>(other.yellowWeapons);
            if (!(tempSet1.equals(tempSet2))) return false;
            if (!(ammosTilesInSquares.equals(other.ammosTilesInSquares))) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int blueHash;
            int redHash;
            int yellowHash;
            int ammoHash;
            blueHash = new HashSet<>(blueWeapons).hashCode();
            redHash = new HashSet<>(redWeapons).hashCode();
            yellowHash = new HashSet<>(yellowWeapons).hashCode();
            ammoHash = ammosTilesInSquares.hashCode();
            return layoutConfiguration +
                    blueHash * 63 +
                    redHash * 31 +
                    yellowHash * 15 +
                    ammoHash * 7;
        }
    }

    /**
     * Contains the backup of the status of the global match and turn
     */
    private class MatchBackup{
        /**
         * is the final frenzy started?
         */
        private boolean frenzyOn;

        /**
         * number of actions allowed in the current turn
         */
        private int numberOfActions;

        /**
         * number of actions already completed
         */
        private int actionsCompleted;

        /**
         * true if the current player can only reload
         */
        private boolean onlyReload;

        /**
         * true if the current player can complete the turn
         */
        private boolean turnCompletable;

        /**
         * true if the player has already completed his turn
         */
        private boolean alreadyCompleted;

        /**
         * the current player of the match
         */
        private String currentPlayer;

        /**
         * players which are requested to do something
         */
        private List<String> waitingFor = new ArrayList<>();

        /**
         * Builds a backup of the global status of the match
         * @param match the match to backup
         */
        private MatchBackup(Match match){
            frenzyOn = match.isFrenzyOn();
            numberOfActions = match.getNumberOfActions();
            actionsCompleted = match.getActionsCompleted();
            onlyReload = match.isOnlyReload();
            turnCompletable = match.isTurnCompletable();
            alreadyCompleted = match.isAlreadyCompleted();
            currentPlayer = match.getCurrentPlayer().getName();
            for (Player p : match.getWaitingFor()){
                waitingFor.add(p.getName());
            }
        }

        /**
         * Restores the match to the state saved in the backup
         * @param match the match to restore
         */
        private void restore (Match match){
            match.setFrenzyOn(frenzyOn);
            match.setNumberOfActions(numberOfActions);
            match.setActionsCompleted(actionsCompleted);
            match.setOnlyReload(onlyReload);
            match.setTurnCompletable(turnCompletable);
            match.setAlreadyCompleted(alreadyCompleted);
            match.setCurrentPlayer(match.getPlayerFromName(currentPlayer));
        }

        /**
         * checks equality between two match backups
         * @param obj teh object to compare
         * @return true if it is equal
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof MatchBackup)) return false;
            MatchBackup other = (MatchBackup)obj;
            if (frenzyOn != other.frenzyOn) return false;
            if (numberOfActions != other.numberOfActions) return false;
            if (actionsCompleted != other.actionsCompleted) return false;
            if (onlyReload != other.onlyReload) return false;
            if (turnCompletable != other.turnCompletable) return false;
            if (alreadyCompleted != other.alreadyCompleted) return false;
            if (!(currentPlayer.equals(other.currentPlayer))) return false;

            Set<String> waitingForSet = new HashSet<>(waitingFor);
            Set<String> otherWaitingForSet = new HashSet<>(other.waitingFor);
            if (!(waitingForSet.equals(otherWaitingForSet))) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return numberOfActions * 31 +
                    actionsCompleted * 15 +
                    currentPlayer.hashCode();
        }
    }

    /**
     * list with backups of the players of the match
     */
    private List<PlayerBackup> playerBackups;

    /**
     * the backup of the killshot track
     */
    private KillShotTrackBackup killShotTrackBackup;

    /**
     * the backup of the stack manager
     */
    private StackManagerBackup stackManagerBackup;

    /**
     * the backup of the layout
     */
    private LayoutBackup layoutBackup;

    /**
     * the backup of the layout
     */
    private MatchBackup matchBackup;

    /**
     * the folder containg backup(s) related to the current game
     */
    private static final String FILE_PATH = "backups/";

    /**
     * extension of the backups
     */
    private static final String EXTENSION = ".json";

    /**
     * Builds a backup of the whole match, saving each subpart (players, killshot, layout, stacks)
     * @param match the match to backup
     */
    public Backup(Match match){
        playerBackups = new ArrayList<>();
        for (Player p : match.getPlayers()){
            playerBackups.add(new PlayerBackup(p));
        }
        killShotTrackBackup = new KillShotTrackBackup(match.getKillShotTrack());
        stackManagerBackup = new StackManagerBackup(match.getStackManager());
        layoutBackup = new LayoutBackup(match.getLayout());
        matchBackup = new MatchBackup(match);
    }

    /**
     * Creates a backup from its name. Test-only method.
     * @param name name of the file with the saved backup
     * @return the backup created
     */
    public static Backup initFromFile(String name){
        InputStream url= Backup.class.getClassLoader().getResourceAsStream(FILE_PATH + name + EXTENSION);
        return initFromFile(url);
    }

    /**
     * Creates a backup from a input stream.
     * It is used to set up a proper match for weapon tests.
     * @param url the url of the file with the backup
     * @return the backup created
     */
    public static Backup initFromFile(InputStream url){
        Gson gson = new Gson();
        Backup temp;

        try {
        Scanner sc= new Scanner(url);
        temp = gson.fromJson(sc.nextLine(), Backup.class);
        sc.close();
        url.close();
        }
        catch (IOException e){
            System.out.println("[WARNING] could not create the backup from file");
            return null;
        }

        return temp;
    }

    public Backup (){}

    /**
     * Restores the whole match to the state saved in the backup.
     * After restoring all the objects in the match are coherent.
     * @param match the match to restore
     */
    public void restore(Match match){
        stackManagerBackup.restore(match.getStackManager());
        layoutBackup.restore(match.getLayout(), match);
        for (PlayerBackup pb : playerBackups) {
            Player tempPlayer = match.getPlayerFromName(pb.name);
            pb.restore(tempPlayer, match);
        }
        killShotTrackBackup.restore(match.getKillShotTrack(), match);
        matchBackup.restore(match);
    }

    /**
     * Gets the layout config from the backup
     * @return integer corresponding to layout config
     */
    public int getLayoutConfig(){
        return layoutBackup.layoutConfiguration;
    }


    /**
     * Gets all the names of the players in this backup.
     * @return list of the names, in the order of playing
     */
    public List<String> getPlayerNames(){
        List<String> result = new ArrayList<>();
        for (PlayerBackup pb : playerBackups){
            result.add(pb.name);
        }
        return result;
    }

    /**
     * Gets the order of a player in the match
     * @param name name of the player
     * @return integer corresponding to the order of playing. -1 if the player is not present
     */
    public int getPlayerIndex(String name){
        return getPlayerNames().indexOf(name);
    }

    /**
     * Checks equality between two backups (by recursively checking equality of subparts)
     * It is used in tests, to compare the actual and the expected situation
     * @param obj the object to compare
     * @return ture if it is equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Backup)) return false;
        Backup other = (Backup)obj;

        return playerBackups.equals(other.playerBackups) &&
                killShotTrackBackup.equals((other.killShotTrackBackup)) &&
                layoutBackup.equals(other.layoutBackup) &&
                stackManagerBackup.equals(other.stackManagerBackup) &&
                matchBackup.equals(other.matchBackup);
    }

    @Override
    public int hashCode() {
        return playerBackups.hashCode() +
                killShotTrackBackup.hashCode() +
                layoutBackup.hashCode() +
                stackManagerBackup.hashCode() +
                matchBackup.hashCode();
    }
}
