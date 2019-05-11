package it.polimi.ingsw;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
            if (isBorn) squarePosition = p.getSquarePosition().toString();
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

            wallet = p.getWallet();
            pending = p.getPending();
            credit = p.getCredit();
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
            for (String name : damageList){
                tempDamageList.add(match.getPlayerFromName(name));
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
    }

    private class KillShotTrackBackup{
        private int skulls;
        private List<Map<String, Integer>> track;
        private Map<String, Integer> killingCounter;
        private List<String> killingOrder;

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

            killingCounter = new HashMap<>();
            for (Map.Entry<Player, Integer> entry : k.getKillingCounter().entrySet()){
                killingCounter.put(entry.getKey().getName(), entry.getValue());
            }

            killingOrder = new ArrayList<>();
            for (Player p : k.getKillingOrder()){
                killingOrder.add(p.getName());
            }
        }

        public void restore(KillShotTrack k, Match match){
            k.setSkulls(skulls);

            List<Map<Player, Integer>> tempTrack = new ArrayList<>();
            for (Map<String, Integer> map : track){
                Map<Player, Integer> temp = new HashMap<>();
                for (Map.Entry<String, Integer> entry : map.entrySet()){
                    temp.put(match.getPlayerFromName(entry.getKey()), entry.getValue());
                }
                tempTrack.add(temp);
            }
            k.setTrack(tempTrack);

            Map<Player, Integer> tempKillingCounter = new HashMap<>();
            for (Map.Entry<String, Integer> entry : killingCounter.entrySet()){
                tempKillingCounter.put(match.getPlayerFromName(entry.getKey()), entry.getValue());
            }
            k.setKillingCounter(tempKillingCounter);

            List<Player> tempKillingOrder = new ArrayList<>();
            for (String name : killingOrder){
                tempKillingOrder.add(match.getPlayerFromName(name));
            }
            k.setKillingOrder(tempKillingOrder);
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

        public void restore(StackManager stackManager, Match match){
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
            for (Weapon w : layout.getSpawnPoint(Color.BLUE).getWeapons()){
                blueWeapons.add(w.getName());
            }

            redWeapons = new ArrayList<>();
            for (Weapon w : layout.getSpawnPoint(Color.RED).getWeapons()){
                redWeapons.add(w.getName());
            }

            yellowWeapons = new ArrayList<>();
            for (Weapon w : layout.getSpawnPoint(Color.YELLOW).getWeapons()){
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
            SpawnSquare tempSpawnSquare = layout.getSpawnPoint(Color.BLUE);
            tempSpawnSquare.clearWeapons();
            for (String name : blueWeapons){
                tempSpawnSquare.addWeapon(match.getStackManager().getWeaponFromName(name));
            }

            tempSpawnSquare = layout.getSpawnPoint(Color.RED);
            tempSpawnSquare.clearWeapons();
            for (String name : redWeapons){
                tempSpawnSquare.addWeapon(match.getStackManager().getWeaponFromName(name));
            }

            tempSpawnSquare = layout.getSpawnPoint(Color.YELLOW);
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
    }

    private class MatchBackup{
        private boolean frenzyOn;
        private int numberOfActions;
        private int actionsCompleted;
        private boolean onlyReload;
        private boolean turnCompletable;
        private boolean alreadyCompleted;
        private String currentPlayer;

        public MatchBackup(Match match){
            frenzyOn = match.isFrenzyOn();
            numberOfActions = match.getNumberOfActions();
            actionsCompleted = match.getActionsCompleted();
            onlyReload = match.isOnlyReload();
            turnCompletable = match.isTurnCompletable();
            alreadyCompleted = match.isAlreadyCompleted();
            currentPlayer = match.getCurrentPlayer().getName();
        }

        public void restore (Match match){
            match.setFrenzyOn(frenzyOn);
            match.setNumberOfActions(numberOfActions);
            match.setActionsCompleted(actionsCompleted);
            match.setOnlyReload(onlyReload);
            match.setTurnCompletable(turnCompletable);
            match.setAlreadyCompleted(alreadyCompleted);
            match.setCurrentPlayer(match.getPlayerFromName(currentPlayer));
        }
    }

    private List<PlayerBackup> playerBackups;
    private KillShotTrackBackup killShotTrackBackup;
    private StackManagerBackup stackManagerBackup;
    private LayoutBackup layoutBackup;
    private MatchBackup matchBackup;
    private static final String filePath = "./src/main/resources/backups/";

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
        return initFromFile(filePath, name);
    }

    public static Backup initFromFile(String path, String name){
        Gson gson = new Gson();
        File file = new File(path+name+".json");
        Backup temp = new Backup();

        try (Scanner sc = new Scanner(file)){

            temp = gson.fromJson(sc.nextLine(), Backup.class);
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();

        }
        return temp;
    }

    public static boolean isBackupAvailable(String name){
        File file = new File(filePath+name+".json");
        return file.exists();
    }

    public Backup (){}

    public boolean saveOnFile(String name) {
        Gson gson = new Gson();
        File file = new File(filePath+name+".json");
        //File file= new File(getClass().getClassLoader().getResource("backup" + name + ".json").getFile());
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            gson.toJson(this, fw);
            fw.close();
            return true;
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public void restore(Match match){
        restoreMatch(match, false);
    }

    public void resumeMatch(Match match){
        restoreMatch(match, true);
    }

    private void restoreMatch(Match match, boolean fromFile){
        stackManagerBackup.restore(match.getStackManager(), match);
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

}
