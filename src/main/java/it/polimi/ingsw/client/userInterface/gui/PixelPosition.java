package it.polimi.ingsw.client.userInterface.gui;


import com.google.gson.Gson;
import it.polimi.ingsw.client.SquareView;

import java.util.LinkedList;
import java.util.List;

public class PixelPosition {

    private int xSquare;
    private int ySquare;
    private Double xAmmo;
    private Double yAmmo;
    private Double xPlayer;
    private Double yPlayer;
    private Double xSelectable;
    private Double ySelectable;

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

    public int getxSquare(){
        return xSquare;
    }

    public int getySquare(){
        return ySquare;
    }

    public Double getxAmmo(){
        return xAmmo;
    }

    public Double getyAmmo(){
        return yAmmo;
    }

    public Double getxPlayer(){
        return xPlayer;
    }

    public Double getyPlayer(){
        return yPlayer;
    }

    public Double getxSelectable(){
        return xSelectable;
    }

    public Double getySelectable(){
        return ySelectable;
    }

    public boolean equalsSquare(SquareView squareView){
        if(squareView.getX() == xSquare && squareView.getY() == ySquare){
            return true;
        }
        return false;
    }
}

