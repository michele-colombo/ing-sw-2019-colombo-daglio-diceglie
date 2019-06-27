package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.PlayerColor;
import it.polimi.ingsw.server.model.enums.PlayerState;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.*;


/**
 * It contains information in order to make a rollback during a match or to resume a match from file
 */
public class Backup {

    private class PlayerBackup{
        private String name;
        private PlayerColor color;
        private boolean isBorn;
        private boolean isBeforeFirst;
        private int points;
        private PlayerState state;
        private PlayerState nextState;
        private boolean isFirstPlayer;
        private boolean hasAnotherTurn;
        private String squarePosition;
        private int skullsNumber;
        private boolean isFrenzy;
        private List<String> damageList;
        private Map<String, Integer> markMap;
        private Map<String, Boolean> weapons;
        private List<String> powerUps;
        private Cash wallet;
        private Cash pending;
        private Cash credit;

        public PlayerBackup(Player p){
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

        public void restore(Player p, Match match){
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
            //if (nextState != other.nextState) return false;
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

    private class KillShotTrackBackup{
        private int skulls;
        private List<Map<String, Integer>> track;

        public KillShotTrackBackup(KillShotTrack k){
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

        public void restore(KillShotTrack k, Match match){
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

    private class StackManagerBackup{
        private List<String> weaponActiveStack;
        private List<String> powerUpActiveStack;
        private List<String> powerUpWasteStack;
        private List<String> ammoTilesActiveStack;
        private List<String> ammoTilesWasteStack;

        public StackManagerBackup(StackManager sm){
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

        public void restore(StackManager stackManager){
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

    private class LayoutBackup{
        private int layoutConfiguration;
        private List<String> blueWeapons;
        private List<String> redWeapons;
        private List<String> yellowWeapons;
        private Map<String, String> ammosTilesInSquares;

        public LayoutBackup(Layout layout){
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

        public void restore(Layout layout, Match match){
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
            int blueHash, redHash, yellowHash, ammoHash;
            blueHash = new HashSet<String>(blueWeapons).hashCode();
            redHash = new HashSet<String>(redWeapons).hashCode();
            yellowHash = new HashSet<String>(yellowWeapons).hashCode();
            ammoHash = ammosTilesInSquares.hashCode();
            return layoutConfiguration +
                    blueHash * 63 +
                    redHash * 31 +
                    yellowHash * 15 +
                    ammoHash * 7;
        }
    }

    private class MatchBackup{
        private boolean frenzyOn;
        private int numberOfActions;
        private int actionsCompleted;
        private boolean onlyReload;
        private boolean turnCompletable;
        private boolean alreadyCompleted;
        private String currentPlayer;
        private List<String> waitingFor = new ArrayList<>();

        public MatchBackup(Match match){
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

        public void restore (Match match){
            match.setFrenzyOn(frenzyOn);
            match.setNumberOfActions(numberOfActions);
            match.setActionsCompleted(actionsCompleted);
            match.setOnlyReload(onlyReload);
            match.setTurnCompletable(turnCompletable);
            match.setAlreadyCompleted(alreadyCompleted);
            match.setCurrentPlayer(match.getPlayerFromName(currentPlayer));
            //todo: restore waitingFor or not (it is recreated when resuming game)
        }

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

    private List<PlayerBackup> playerBackups;
    private KillShotTrackBackup killShotTrackBackup;
    private StackManagerBackup stackManagerBackup;
    private LayoutBackup layoutBackup;
    private MatchBackup matchBackup;
    private static final String FILE_PATH = "backups/";
    private static final String EXTENSION = ".json";

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

    public static Backup initFromFile(String name){
        InputStream url= Backup.class.getClassLoader().getResourceAsStream(FILE_PATH + name + EXTENSION);
        return initFromFile(url);
    }

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
            System.out.println("Error while processing file");
            return null;
        }

        return temp;
    }

    public static boolean isBackupAvailable(String name){
        //return Backup.class.getClassLoader().getResource("backup/" + name + EXTENSION) != null;
        try {
            String jarPath = URLDecoder.decode(Backup.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            String filePath = jarPath.substring(0, jarPath.lastIndexOf("/")) + File.separator + "backups"+ File.separator + name + EXTENSION;
            File file = new File(filePath);
            return file.exists();
        } catch (UnsupportedEncodingException e){
            System.out.println("[WARNING] Cannot check if there's a backup");
            return false;
        }
    }

    public Backup (){}

    public boolean saveOnFile(String name){
        return saveOnFile(FILE_PATH, name);
    }



    public boolean saveOnFile(String path, String name) {
        Gson gson = new Gson();

        String jarPath = "";
        System.out.println("Writing data...");
        try {
            jarPath = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            System.out.println(jarPath);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        // construct a File within the same folder of this jar, or of this class.
        String dirPath = jarPath.substring(0, jarPath.lastIndexOf("/")) + File.separator + "backups";
        File dir = new File(dirPath);
        if (!dir.exists()){
            dir.mkdir();
        }
        String completePath = dirPath + File.separator + name + EXTENSION;
        File file = new File(completePath);
        try {
            FileWriter fw = new FileWriter(file);
            gson.toJson(this, fw);
            fw.close();
            return true;
        } catch (IOException e){
            System.out.println("Cannot write backup file");
        }
        System.out.println("backup file writte [OK]");

        /*
        //File file = new File(path+name+EXTENSION);
        System.out.println("Sto per scrivere su file");
        try {
            File file = new File(getClass().getClassLoader().getResource("backups/" + name + EXTENSION).toURI());
            FileWriter fw;
            try {
                fw = new FileWriter(file);
                gson.toJson(this, fw);
                fw.close();
                return true;
            } catch (IOException e){
                e.printStackTrace();
            }
            System.out.println("Ho finito di scrivere su file");
        }
        catch (URISyntaxException e){
            e.printStackTrace();
        }
        */
        return false;
    }

    public void restore(Match match){
        restoreMatch(match);
    }

    private void restoreMatch(Match match){
        stackManagerBackup.restore(match.getStackManager());
        layoutBackup.restore(match.getLayout(), match);
        for (PlayerBackup pb : playerBackups) {
            Player tempPlayer = match.getPlayerFromName(pb.name);
            pb.restore(tempPlayer, match);
        }
        killShotTrackBackup.restore(match.getKillShotTrack(), match);
        matchBackup.restore(match);
    }

    public int getLayoutConfig(){
        return layoutBackup.layoutConfiguration;
    }

    public List<String> getPlayerNames(){
        List<String> result = new ArrayList<>();
        for (PlayerBackup pb : playerBackups){
            result.add(pb.name);
        }
        return result;
    }

    public int getPlayerIndex(String name){
        return getPlayerNames().indexOf(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Backup)) return false;
        Backup other = (Backup)obj;

        boolean result = playerBackups.equals(other.playerBackups) &&
                killShotTrackBackup.equals((other.killShotTrackBackup)) &&
                layoutBackup.equals(other.layoutBackup) &&
                stackManagerBackup.equals(other.stackManagerBackup) &&
                matchBackup.equals(other.matchBackup);
        return result;
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
