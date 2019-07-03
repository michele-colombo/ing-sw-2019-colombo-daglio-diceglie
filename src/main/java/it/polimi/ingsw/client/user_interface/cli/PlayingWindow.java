package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;
import it.polimi.ingsw.server.model.enums.AmmoColor;

import java.util.ArrayList;
import java.util.List;
/**
 * Is the (virtual) window of the game
 */
public class PlayingWindow extends Window{
    //protected MatchView match;
    /**
     * the reference to the cli
     */
    protected Cli cli;
    /**
     * the width of the first column
     */
    private int firstColumnWidth;
    /**
     * the width of the second column
     */
    private int secondColumnWidth;
    /**
     * the width of the third column
     */
    private int thirdColumnWidth;
    /**
     * the height of the first row
     */
    private int firstRowHeight;
    /**
     * the height of the weapon box (of the layout)
     */
    private int weaponsBoxHeight;

    /**
     * the height of a single square
     */
    private int squareHeight;

    /**
     * the width of a single square
     */
    private int squareWidth;

    /**
     * the height of each player box
     */
    private int playerBoxHeight;

    /**
     * the width of the powerups box
     */
    private int myPowerUpBoxWidth;
    /**
     * the width of the weapons box
     */
    private int myWeaponBoxWidth;
    /**
     * the height of the last row
     */
    private int lastRowHeight;
    /**
     * the y position of the last row
     */
    private int lastRowY;
    /**
     * the reference to the killshot track box
     */
    KillShotTrackBox killShotTrackBox;
    /**
     * the reference to blue weapons box
     */
    WeaponsInLayoutBox blueWeapons;
    /**
     * the reference to red weapons box
     */
    WeaponsInLayoutBox redWeapons;
    /**
     * the reference to yellow weapons box
     */
    WeaponsInLayoutBox yellowWeapons;
    /**
     * the reference to the general info box
     */
    GeneralInfoBox generalInfoBox;
    /**
     * the reference to the layout box with the map
     */
    LayoutBox layoutBox;
    /**
     * the list of all players' box
     */
    List<PlayerBox> allPlayers;
    /**
     * the reference to the box of my weapons
     */
    MyWeaponBox myWeaponBox;
    /**
     * the reference to the box of my powerups
     */
    MyPowerUpBox myPowerUpBox;
    /**
     * teh reference to the box with my info
     */
    MyInfoBox myInfoBox;
    /**
     * the reference to the box of my selectables
     */
    SelectablesBox selectablesBox;

    /**
     * Builds the playing window
     * @param width the width of the playing window
     * @param height the height of the playing window
     * @param match the current match
     * @param cli the reference to cli
     */
    public PlayingWindow(int width, int height, MatchView match, Cli cli) {
        super(width, height);
        //this.match = match;
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

    /**
     * Updates all contained miniBoxes
     * @param match the current match
     */
    public void fullUpdate(MatchView match){
        for (MiniBox mb : miniBoxes){
            mb.update(match);
        }
        build();
    }

    /**
     * Gets the killshot track box
     * @return reference to killshot track box
     */
    public KillShotTrackBox getKillShotTrackBox() {
        return killShotTrackBox;
    }

    /**
     * Gets the blue weapons box
     * @return reference to the MiniBox
     */
    public WeaponsInLayoutBox getBlueWeapons() {
        return blueWeapons;
    }

    /**
     * Gets the red weapons box
     * @return reference to the MiniBox
     */
    public WeaponsInLayoutBox getRedWeapons() {
        return redWeapons;
    }

    /**
     * Gets the yellow weapons box
     * @return reference to the MiniBox
     */
    public WeaponsInLayoutBox getYellowWeapons() {
        return yellowWeapons;
    }

    /**
     * Gets the general info box
     * @return reference to the MiniBox
     */
    public GeneralInfoBox getGeneralInfoBox() {
        return generalInfoBox;
    }

    /**
     * Gets the layout box
     * @return reference to the MiniBox
     */
    public LayoutBox getLayoutBox() {
        return layoutBox;
    }

    /**
     * Gets the list with each player's box
     * @return reference to the MiniBox
     */
    public List<PlayerBox> getAllPlayers() {
        return allPlayers;
    }

    /**
     * Gets the box of my weapons
     * @return reference to the MiniBox
     */
    public MyWeaponBox getMyWeaponBox() {
        return myWeaponBox;
    }

    /**
     * Gets the box of my powerups
     * @return reference to the MiniBox
     */
    public MyPowerUpBox getMyPowerUpBox() {
        return myPowerUpBox;
    }

    /**
     * Gets the box of my info
     * @return reference to the MiniBox
     */
    public MyInfoBox getMyInfoBox() {
        return myInfoBox;
    }

    /**
     * Gets the box of my selectables
     * @return reference to the MiniBox
     */
    public SelectablesBox getSelectablesBox() {
        return selectablesBox;
    }
}
