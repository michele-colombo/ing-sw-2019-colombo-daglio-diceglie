package it.polimi.ingsw.client.userInterface.cli;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;
import it.polimi.ingsw.server.model.enums.AmmoColor;

import java.util.ArrayList;
import java.util.List;

public class PlayingWindow {
    private int width;
    private int height;
    private MatchView match;
    private Cli cli;
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
    private String[][] stringBox;
    private List<MiniBox> miniBoxes;
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
        this.width = width;
        this.height = height;
        this.match = match;
        this.cli = cli;
        stringBox = new String[height][width];
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                stringBox[i][j] = " ";
            }
        }
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
        this.miniBoxes = new ArrayList<>();
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

    public void show(){
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                System.out.print(stringBox[i][j]);
            }
            System.out.println();
        }
    }

    private void renderPlayingWindow(){
        for (MiniBox mb : miniBoxes){
            insertSubBox(mb.getStringBox(), mb.getX(), mb.getY());
        }
    }

    public void fullUpdate(MatchView match){
        for (MiniBox mb : miniBoxes){
            mb.update(match);
        }
        renderPlayingWindow();
    }

    public void insertSubBox(String[][] matrix, int x, int y){
        for(int i=0; i<matrix.length && i+y<height; i++){
            for (int j=0; j<matrix[0].length && j+x<width; j++){
                stringBox[i+y][j+x] = matrix[i][j];
            }
        }
    }

}
