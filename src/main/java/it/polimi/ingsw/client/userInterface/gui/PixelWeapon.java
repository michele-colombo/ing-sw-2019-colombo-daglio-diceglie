package it.polimi.ingsw.client.userInterface.gui;

public class PixelWeapon {
    private double x;
    private double y;
    private int rotate;

    public PixelWeapon(double x, double y, int rotate){
        this.x = x;
        this.y = y;
        this.rotate = rotate;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public int getRotate(){
        return rotate;
    }
}
