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

    public PixelPosition(int xSquare, int ySquare, Double xAmmo, Double yAmmo, Double xPlayer, Double yPlayer){
        this.xSquare = xSquare;
        this.ySquare = ySquare;
        this.xAmmo = xAmmo;
        this.yAmmo = yAmmo;
        this.xPlayer = xPlayer;
        this.yPlayer = yPlayer;
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

    public boolean equalsSquare(SquareView squareView){
        if(squareView.getX() == xSquare && squareView.getY() == ySquare){
            return true;
        }
        return false;
    }


    public static void main(String[] args){

        List<PixelPosition> pixel = new LinkedList<>();
        pixel.add(new PixelPosition(0,2,0.2042, 0.3568, 0.22, 0.31));
        pixel.add(new PixelPosition(0, 0,0.2051, 0.7784, 0.22, 0.725));
        pixel.add(new PixelPosition(1,0,0.3441, 0.7784, 0.4, 0.725));
        pixel.add(new PixelPosition(1,2,0.3540, 0.2452, 0.4, 0.31));
        pixel.add(new PixelPosition(2,0,0.5435, 0.7729, 0.6, 0.725));
        pixel.add(new PixelPosition(3,2,0.7618, 0.3475, 0.72, 0.31));
        pixel.add(new PixelPosition(1,1,0.3396, 0.5238, 0.4, 0.53));
        pixel.add(new PixelPosition(2,1,0.5339, 0.5860, 0.6, 0.53));
        pixel.add(new PixelPosition(3,1,0.6830, 0.5852, 0.72, 0.53));
        pixel.add(new PixelPosition(3,0,0.0, 0.0, 0.72, 0.725));
        pixel.add(new PixelPosition(0,1,0.0, 0.0, 0.22, 0.53));
        pixel.add(new PixelPosition(2,2,0.0, 0.0, 0.57, 0.31));

        Gson gson = new Gson();
        System.out.println(gson.toJson(pixel));
    }
}

