package it.polimi.ingsw.client.userInterface.cli;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;
import it.polimi.ingsw.server.model.enums.AmmoColor;

import java.util.ArrayList;
import java.util.List;

public class PlayingWindow extends Window{
    protected MatchView match;
    protected Cli cli;
    private int firstColumnWidth;
    private int secondColumnWidth;
    private int thirdColumnWidth;
    private int firstRowHeight;
    private int weaponsBoxHeight;
    private int squareHeight;
    private int squareWidth;
    private int playerBoxHeight;
    private int myPowerUpBoxWidth;
    private int myWeaponBoxWidth;
    private int lastRowHeight;
    private int lastRowY;
    KillShotTrackBox killShotTrackBox;
    WeaponsInLayoutBox blueWeapons;
    WeaponsInLayoutBox redWeapons;
    WeaponsInLayoutBox yellowWeapons;
    GeneralInfoBox generalInfoBox;
    LayoutBox layoutBox;
    List<PlayerBox> allPlayers;
    MyWeaponBox myWeaponBox;
    MyPowerUpBox myPowerUpBox;
    MyInfoBox myInfoBox;
    SelectablesBox selectablesBox;


    public PlayingWindow(int width, int height, MatchView match, Cli cli) {
        super(width, height);
        this.match = match;
        this.cli = cli;
        this.firstColumnWidth = width/6;
        this.secondColumnWidth = (width*3)/7;
        this.thirdColumnWidth = width - (firstColumnWidth + secondColumnWidth);
        this.firstRowHeight = 5;
        this.weaponsBoxHeight = 7;
        this.squareHeight = 7;
        this.squareWidth = 13;
        this.playerBoxHeight = 6;
        this.myPowerUpBoxWidth = firstColumnWidth;
        this.myWeaponBoxWidth = firstColumnWidth + 4;
        this.lastRowHeight = 6;
        killShotTrackBox = new KillShotTrackBox(0,0, firstRowHeight, firstColumnWidth);
        miniBoxes.add(killShotTrackBox);
        int layoutBoxHeight = 2+squareHeight*3;
        int layoutBoxWidth = 2+squareWidth*4;
        layoutBox = new LayoutBox(firstColumnWidth+(secondColumnWidth-layoutBoxWidth)/2, firstRowHeight, layoutBoxHeight, layoutBoxWidth, squareHeight, squareWidth);
        miniBoxes.add(layoutBox);
        blueWeapons = new WeaponsInLayoutBox(0, layoutBox.getY()+1, weaponsBoxHeight, firstColumnWidth, AmmoColor.BLUE);
        redWeapons = new WeaponsInLayoutBox(0, blueWeapons.getY()+blueWeapons.getHeight(), weaponsBoxHeight, firstColumnWidth, AmmoColor.RED);
        yellowWeapons = new WeaponsInLayoutBox(0, redWeapons.getY()+redWeapons.getHeight(), weaponsBoxHeight, firstColumnWidth, AmmoColor.YELLOW);
        miniBoxes.add(blueWeapons);
        miniBoxes.add(redWeapons);
        miniBoxes.add(yellowWeapons);
        generalInfoBox = new GeneralInfoBox(firstColumnWidth, 0, firstRowHeight, secondColumnWidth);
        miniBoxes.add(generalInfoBox);
        allPlayers = new ArrayList<>();
        for (PlayerView p : match.getAllPlayers()){
            PlayerBox temp = new PlayerBox(firstColumnWidth+secondColumnWidth, allPlayers.size()*playerBoxHeight, playerBoxHeight, thirdColumnWidth, p);
            allPlayers.add(temp);
            miniBoxes.add(temp);
        }
        this.lastRowY = Math.max(layoutBox.getHeight()+layoutBox.getY(), allPlayers.get(allPlayers.size()-1).getY()+playerBoxHeight);
        myWeaponBox = new MyWeaponBox(0, lastRowY, lastRowHeight, myWeaponBoxWidth);
        miniBoxes.add(myWeaponBox);
        myPowerUpBox = new MyPowerUpBox(myWeaponBox.getWidth(), lastRowY, lastRowHeight, myPowerUpBoxWidth);
        miniBoxes.add(myPowerUpBox);
        myInfoBox = new MyInfoBox(myPowerUpBox.getWidth()+myPowerUpBox.getX(), lastRowY, lastRowHeight, width-myWeaponBox.getWidth()-myPowerUpBox.getWidth());
        miniBoxes.add(myInfoBox);
        selectablesBox = new SelectablesBox(0,lastRowHeight+lastRowY, 6, width, cli);
        miniBoxes.add(selectablesBox);
    }

    public void fullUpdate(MatchView match){
        for (MiniBox mb : miniBoxes){
            mb.update(match);
        }
        build();
    }

    public KillShotTrackBox getKillShotTrackBox() {
        return killShotTrackBox;
    }

    public WeaponsInLayoutBox getBlueWeapons() {
        return blueWeapons;
    }

    public WeaponsInLayoutBox getRedWeapons() {
        return redWeapons;
    }

    public WeaponsInLayoutBox getYellowWeapons() {
        return yellowWeapons;
    }

    public GeneralInfoBox getGeneralInfoBox() {
        return generalInfoBox;
    }

    public LayoutBox getLayoutBox() {
        return layoutBox;
    }

    public List<PlayerBox> getAllPlayers() {
        return allPlayers;
    }

    public MyWeaponBox getMyWeaponBox() {
        return myWeaponBox;
    }

    public MyPowerUpBox getMyPowerUpBox() {
        return myPowerUpBox;
    }

    public MyInfoBox getMyInfoBox() {
        return myInfoBox;
    }

    public SelectablesBox getSelectablesBox() {
        return selectablesBox;
    }
}
