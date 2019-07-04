package it.polimi.ingsw.client.model_view;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents Layout on client, in order to properly load it in user interface
 */
public class LayoutView {
    /**
     * Represent the width of layouts
     */
    private static final int WIDTH= 4;
    /**
     * Represent the height of layouts
     */
    private static final int HEIGHT= 3;
    /**
     * Represents if a SquareView doesn't exist in the chosen layout
     */
    private static final int NOT_EXISTING_SQUARE = 0;
    /**
     * Represent if a squareView exists in the chosen layout
     */
    private static final int EXISTING_SQUARE = 1;
    /**
     * Contains all squares of the selected layout; max size is 12
     */
    private List<SquareView> squares;

    /**
     * A matrix: if the square at coordinates x and y is in squares, then existingSquares[x][y] is 1, otherwise it's 0
     */
    private int[][] existingSquare;

    /**
     * Chosen configuration of layout
     */
    private int layoutConfiguration;
    /**
     * List of WeaponView on blue spaces of layout
     */
    private List<WeaponView> blueWeapons;
    /**
     * List of WeaponView on red spaces of layout
     */
    private List<WeaponView> redWeapons;
    /**
     * List of WeaponView on yellow spaces of layout
     */
    private List<WeaponView> yellowWeapons;


    /**
     * Creates layoutView from squareViews, according to config and initialize existingSquareView to 0 value
     * @param squares List of squares to be loaded from file
     * @param config Chosen layouts
     */
    public LayoutView(List<SquareView> squares, int config){
        this.squares= new ArrayList<>();
        this.squares.addAll(squares);

        layoutConfiguration= config;

        existingSquare= new int[WIDTH][HEIGHT];
        for(int row = 0; row < WIDTH; row++){
            for(int column = 0; column < HEIGHT; column++){
                existingSquare[row][column] = NOT_EXISTING_SQUARE;
            }
        }

        blueWeapons = new ArrayList<>();
        redWeapons = new ArrayList<>();
        yellowWeapons = new ArrayList<>();


        for(SquareView sv: squares){
            existingSquare[sv.getX()][sv.getY()] = EXISTING_SQUARE;
        }

    }


    /**
     * Returns a reference to the squareView with x and y coordinates
     * @param x Selected coordinate x
     * @param y Selected coordinate y
     * @return The selected squareView: if the squareView doesn't exist, it's null;
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
     * Looks for SquareView corresponding to square
     * @param square Name of squareView searched
     * @return The SquareView corresponding to square, if exists, else null
     */
    public SquareView getSquareFromString(String square){
        for (SquareView s : squares){
            if (s.toString().equals(square)){
                return s;
            }
        }
        return null;
    }

    /**
     *
     * @return layoutConfiguration
     */
    public int getLayoutConfiguration() {
        return layoutConfiguration;
    }

    /**
     *
     * @return blueWeapons
     */
    public List<WeaponView> getBlueWeapons() {
        return blueWeapons;
    }

    /**
     *
     * @return redWeapons
     */
    public List<WeaponView> getRedWeapons() {
        return redWeapons;
    }

    /**
     *
     * @return yellowWeapons
     */
    public List<WeaponView> getYellowWeapons() {
        return yellowWeapons;
    }

    /**
     *
     * @param blueWeapons Sets blueWeapons
     */
    public void setBlueWeapons(List<WeaponView> blueWeapons) {
        this.blueWeapons = blueWeapons;
    }

    /**
     *
     * @param redWeapons Sets redWeapons
     */
    public void setRedWeapons(List<WeaponView> redWeapons) {
        this.redWeapons = redWeapons;
    }

    /**
     *
     * @param yellowWeapons set yellowWeapons
     */
    public void setYellowWeapons(List<WeaponView> yellowWeapons) {
        this.yellowWeapons = yellowWeapons;
    }

    /**
     *
     * @return A new List, containing all elements of squares
     */
    public List<SquareView> getSquares() {
        return new ArrayList<>(squares);
    }
}
