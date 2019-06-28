package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.SquareView;

/**
 * It represent the translation for: AmmoButton, PlayerRectangle, SquareRectangle, associated to a square, on the board
 */
public class PixelPosition {

    /**
     * The coordinate x of the associated square
     */
    private int xSquare;
    /**
     * The coordinate y of the associated square
     */
    private int ySquare;
    /**
     * The ratio of translation on x axe of AmmoButton
     */
    private Double xAmmo;
    /**
     * The ratio of translation on y axe of AmmoButton
     */
    private Double yAmmo;
    /**
     * The ratio of translation on x axe of PlayerRectangle
     */
    private Double xPlayer;
    /**
     * The ratio of translation on y axe of PlayerRectangle
     */
    private Double yPlayer;
    /**
     * The ratio of translation on x axe of SquareRectangle
     */
    private Double xSelectable;
    /**
     * The ratio of translation on y axe of SquareRectangle
     */
    private Double ySelectable;

    /**
     * Create a PixelPosition, with chosen parameters
     * @param xSquare x coordinates of the associated square
     * @param ySquare y coordinates of the associated square
     * @param xAmmo The ratio on x axe of AmmoButton
     * @param yAmmo The ratio on y axe of AmmoButton
     * @param xPlayer The ratio on x axe of PlayerRectangle
     * @param yPlayer The ratio on y axe of PlayerRectangle
     * @param xSelectable The ratio on x axe of SquareRectangle
     * @param ySelectable The ratio on y axe of SquareRectangle
     */
    public PixelPosition(int xSquare, int ySquare, Double xAmmo, Double yAmmo, Double xPlayer, Double yPlayer, Double xSelectable, Double ySelectable){
        this.xSquare = xSquare;
        this.ySquare = ySquare;
        this.xAmmo = xAmmo;
        this.yAmmo = yAmmo;
        this.xPlayer = xPlayer;
        this.yPlayer = yPlayer;
        this.xSelectable = xSelectable;
        this.ySelectable = ySelectable;
    }

    /**
     *
     * @return xSquare
     */
    public int getxSquare(){
        return xSquare;
    }

    /**
     *
     * @return ySquare
     */
    public int getySquare(){
        return ySquare;
    }

    /**
     *
     * @return xAmmo
     */
    public Double getxAmmo(){
        return xAmmo;
    }

    /**
     *
     * @return yAmmo
     */
    public Double getyAmmo(){
        return yAmmo;
    }

    /**
     *
     * @return xPlayer
     */
    public Double getxPlayer(){
        return xPlayer;
    }

    /**
     *
     * @return yPlayer
     */
    public Double getyPlayer(){
        return yPlayer;
    }

    /**
     *
     * @return xSelectable
     */
    public Double getxSelectable(){
        return xSelectable;
    }

    /**
     *
     * @return ySelectable
     */
    public Double getySelectable(){
        return ySelectable;
    }

    /**
     * If the squareview is has same x and y coordinates, otherwise false
     * @param squareView The squareView to compare
     * @return boolean
     */
    public boolean equalsSquare(SquareView squareView){
        if(squareView.getX() == xSquare && squareView.getY() == ySquare){
            return true;
        }
        return false;
    }
}

