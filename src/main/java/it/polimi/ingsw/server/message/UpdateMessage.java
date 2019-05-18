package it.polimi.ingsw.server.message;

import it.polimi.ingsw.*;
import it.polimi.ingsw.client.VisitorClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateMessage extends MessageVisitable {

    public class MyPlayerInfo {
        private String name;
        private PlayerColor color;
        private int points;
        private PlayerState state;
        private boolean isFirstPlayer;
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

        public MyPlayerInfo(){

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PlayerColor getColor() {
            return color;
        }

        public void setColor(PlayerColor color) {
            this.color = color;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public PlayerState getState() {
            return state;
        }

        public void setState(PlayerState state) {
            this.state = state;
        }

        public boolean isFirstPlayer() {
            return isFirstPlayer;
        }

        public void setFirstPlayer(boolean firstPlayer) {
            isFirstPlayer = firstPlayer;
        }

        public String getSquarePosition() {
            return squarePosition;
        }

        public void setSquarePosition(String squarePosition) {
            this.squarePosition = squarePosition;
        }

        public int getSkullsNumber() {
            return skullsNumber;
        }

        public void setSkullsNumber(int skullsNumber) {
            this.skullsNumber = skullsNumber;
        }

        public boolean isFrenzy() {
            return isFrenzy;
        }

        public void setFrenzy(boolean frenzy) {
            isFrenzy = frenzy;
        }

        public List<String> getDamageList() {
            return damageList;
        }

        public void setDamageList(List<String> damageList) {
            this.damageList = damageList;
        }

        public Map<String, Integer> getMarkMap() {
            return markMap;
        }

        public void setMarkMap(Map<String, Integer> markMap) {
            this.markMap = markMap;
        }

        public Map<String, Boolean> getWeapons() {
            return weapons;
        }

        public void setWeapons(Map<String, Boolean> weapons) {
            this.weapons = weapons;
        }

        public List<String> getPowerUps() {
            return powerUps;
        }

        public void setPowerUps(List<String> powerUps) {
            this.powerUps = powerUps;
        }

        public Cash getWallet() {
            return wallet;
        }

        public void setWallet(Cash wallet) {
            this.wallet = new Cash(wallet);
        }

        public Cash getPending() {
            return pending;
        }

        public void setPending(Cash pending) {
            this.pending = new Cash(pending);
        }

        public Cash getCredit() {
            return credit;
        }

        public void setCredit(Cash credit) {
            this.credit = new Cash(credit);
        }
    }

    public class OtherPlayerInfo {
        private String name;
        private PlayerColor color;
        private PlayerState state;
        private boolean isFirstPlayer;
        private String squarePosition;
        private int skullsNumber;
        private boolean isFrenzy;
        private List<String> damageList;
        private Map<String, Integer> markMap;
        private int numLoadedWeapons;
        private List<String> unloadedWeapons;
        private int numPowerUps;
        private Cash wallet;

        public OtherPlayerInfo(){

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PlayerColor getColor() {
            return color;
        }

        public void setColor(PlayerColor color) {
            this.color = color;
        }

        public PlayerState getState() {
            return state;
        }

        public void setState(PlayerState state) {
            this.state = state;
        }

        public boolean isFirstPlayer() {
            return isFirstPlayer;
        }

        public void setFirstPlayer(boolean firstPlayer) {
            isFirstPlayer = firstPlayer;
        }

        public String getSquarePosition() {
            return squarePosition;
        }

        public void setSquarePosition(String squarePosition) {
            this.squarePosition = squarePosition;
        }

        public int getSkullsNumber() {
            return skullsNumber;
        }

        public void setSkullsNumber(int skullsNumber) {
            this.skullsNumber = skullsNumber;
        }

        public boolean isFrenzy() {
            return isFrenzy;
        }

        public void setFrenzy(boolean frenzy) {
            isFrenzy = frenzy;
        }

        public List<String> getDamageList() {
            return damageList;
        }

        public void setDamageList(List<String> damageList) {
            this.damageList = damageList;
        }

        public Map<String, Integer> getMarkMap() {
            return markMap;
        }

        public void setMarkMap(Map<String, Integer> markMap) {
            this.markMap = markMap;
        }

        public int getNumLoadedWeapons() {
            return numLoadedWeapons;
        }

        public void setNumLoadedWeapons(int numLoadedWeapons) {
            this.numLoadedWeapons = numLoadedWeapons;
        }

        public List<String> getUnloadedWeapons() {
            return unloadedWeapons;
        }

        public void setUnloadedWeapons(List<String> unloadedWeapons) {
            this.unloadedWeapons = unloadedWeapons;
        }

        public int getNumPowerUps() {
            return numPowerUps;
        }

        public void setNumPowerUps(int numPowerUps) {
            this.numPowerUps = numPowerUps;
        }

        public Cash getWallet() {
            return wallet;
        }

        public void setWallet(Cash wallet) {
            this.wallet = new Cash(wallet);
        }
    }

    private int layoutConfiguration;
    private List<String> blueWeapons;
    private List<String> redWeapons;
    private List<String> yellowWeapons;
    private Map<String, String> ammosTilesInSquares;
    private int skulls;
    private List<Map<String, Integer>> track;
    private boolean frenzyOn;
    private String currentPlayer;

    private MyPlayerInfo myPlayerInfo;
    private List<OtherPlayerInfo> otherPlayers;

    public UpdateMessage (){
        myPlayerInfo = new MyPlayerInfo();
        otherPlayers = new ArrayList<>();
    }

    public void accept(VisitorClient visitorClient){
        visitorClient.visit(this);
    }

    public MyPlayerInfo getMyPlayerInfo() {
        return myPlayerInfo;
    }

    public List<OtherPlayerInfo> getOtherPlayers() {
        return otherPlayers;
    }

    public OtherPlayerInfo createOtherPlayerInfo(){
        OtherPlayerInfo temp = new OtherPlayerInfo();
        otherPlayers.add(temp);
        return temp;
    }

    //------------------------------------
    //        getters and setters
    //------------------------------------

    public int getLayoutConfiguration() {
        return layoutConfiguration;
    }

    public void setLayoutConfiguration(int layoutConfiguration) {
        this.layoutConfiguration = layoutConfiguration;
    }

    public List<String> getBlueWeapons() {
        return blueWeapons;
    }

    public void setBlueWeapons(List<String> blueWeapons) {
        this.blueWeapons = blueWeapons;
    }

    public List<String> getRedWeapons() {
        return redWeapons;
    }

    public void setRedWeapons(List<String> redWeapons) {
        this.redWeapons = redWeapons;
    }

    public List<String> getYellowWeapons() {
        return yellowWeapons;
    }

    public void setYellowWeapons(List<String> yellowWeapons) {
        this.yellowWeapons = yellowWeapons;
    }

    public Map<String, String> getAmmosTilesInSquares() {
        return ammosTilesInSquares;
    }

    public void setAmmosTilesInSquares(Map<String, String> ammosTilesInSquares) {
        this.ammosTilesInSquares = ammosTilesInSquares;
    }

    public int getSkulls() {
        return skulls;
    }

    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    public List<Map<String, Integer>> getTrack() {
        return track;
    }

    public void setTrack(List<Map<String, Integer>> track) {
        this.track = track;
    }

    public boolean isFrenzyOn() {
        return frenzyOn;
    }

    public void setFrenzyOn(boolean frenzyOn) {
        this.frenzyOn = frenzyOn;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
