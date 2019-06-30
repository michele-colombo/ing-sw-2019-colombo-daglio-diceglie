package it.polimi.ingsw.client;

import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LayoutView {
    private static final String LAYPUT_IMAGES_FOLDER= "resources/layoutConfig/";
    private static final int WIDTH= 4;
    private static final int HEIGHT= 3;

    /**
     * Contains all squares of the selected layout; max size is 12
     */
    private List<SquareView> squares;

    /**
     * A matrix: if the square at coordinates x and y is in squares, then existingSquares[x][y] is 1, otherwise it's 0
     */
    private int[][] existingSquare;

    /**
     * an integer representing the chosen layoutConfiguration
     */
    private int layoutConfiguration;

    private List<WeaponView> blueWeapons;
    private List<WeaponView> redWeapons;
    private List<WeaponView> yellowWeapons;

    /**
     * Create sa layout from squares
     */
    public LayoutView(List<SquareView> squares, int config){
        this.squares= new ArrayList<>();
        this.squares.addAll(squares);

        layoutConfiguration= config;

        existingSquare= new int[4][3];
        for(int row = 0; row < WIDTH; row++){
            for(int column = 0; column < HEIGHT; column++){
                existingSquare[row][column] = 0;
            }
        }

        blueWeapons = new ArrayList<>();
        redWeapons = new ArrayList<>();
        yellowWeapons = new ArrayList<>();


        for(SquareView sv: squares){
            existingSquare[sv.getX()][sv.getY()] = 1;
        }

    }


    /**
     * Returns a reference to the square with x and y coordinates
     * @param x Selected coordinate x
     * @param y Selected coordinate y
     * @return The selected square: if the square doesn't exist, it's null;
     */
    public SquareView getSquare(int x, int y){
        SquareView found = null;
        for(SquareView s : squares){
            if(s.getX() == x && s.getY() == y){
                return s;
            }
        }
        return found;
    }

    /**
     * Return true if the square is added, otherwise false
     * @param s It's the square to be added
     * @return A boolean
     */
    public boolean addSquare(SquareView s){
        if(!existSquare(s.getX(), s.getY()) && squares.size() < 12){
            squares.add(s);
            existingSquare[s.getX()][s.getY()] = 1;
            return true;
        }
        return false;
    }

    /**
     * Returns true if existingSquare[x][y] is 1, otherwise it returns false
     * @param x Coordinate x of the square
     * @param y Coordinate y of the square
     * @return A boolean
     */
    public boolean existSquare(int x, int y){
        if(existingSquare[x][y] == 1){
            return true;
        }
        return false;
    }



    public SquareView getSquareFromString(String square){
        for (SquareView s : squares){
            if (s.toString().equals(square)){
                return s;
            }
        }
        return null;
    }

    public int getLayoutConfiguration() {
        return layoutConfiguration;
    }

    public List<WeaponView> getBlueWeapons() {
        return blueWeapons;
    }

    public List<WeaponView> getRedWeapons() {
        return redWeapons;
    }

    public List<WeaponView> getYellowWeapons() {
        return yellowWeapons;
    }

    public void setBlueWeapons(List<WeaponView> blueWeapons) {
        this.blueWeapons = blueWeapons;
    }

    public void setRedWeapons(List<WeaponView> redWeapons) {
        this.redWeapons = redWeapons;
    }

    public void setYellowWeapons(List<WeaponView> yellowWeapons) {
        this.yellowWeapons = yellowWeapons;
    }

    public List<SquareView> getSquares() {
        return new ArrayList<>(squares);
    }
}
