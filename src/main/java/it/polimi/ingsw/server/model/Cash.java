package it.polimi.ingsw.server.model;


import it.polimi.ingsw.server.model.enums.AmmoColor;

import java.util.ArrayList;
import java.util.List;

public class Cash {
    private int blue;
    private int red;
    private int yellow;

    public Cash(int blue, int red, int yellow) {
        this.blue = blue;
        this.red = red;
        this.yellow = yellow;
    }

    public Cash(Cash source){
        this.blue = source.getBlue();
        this.red = source.getRed();
        this.yellow = source.getYellow();
    }

    public Cash(){
        this(0, 0, 0);
    }

    public Cash(AmmoColor c, int quantity){
        this();
        switch (c){
            case BLUE:
                blue += quantity;
                break;
            case RED:
                red += quantity;
                break;
            case YELLOW:
                yellow += quantity;
                break;
        }
    }

    @Override
    public String toString(){
        return "b:"+blue+"|r:"+red+"|y:"+yellow;
    }

    public int getBlue() {
        return blue;
    }

    public int getRed() {
        return red;
    }

    public int getYellow() {
        return yellow;
    }

    public boolean greaterEqual(Cash c){
        return (blue >= c.blue && red >= c.red && yellow >= c.yellow);
    }

    public boolean lessEqual(Cash c){
        return (blue <= c.blue && red <= c.red && yellow <= c.yellow);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cash)) return false;
        Cash other = (Cash)obj;
        return this.isEqual(other);
    }

    @Override
    public int hashCode() {
        return 100*blue + 10*red + yellow;
    }

    public boolean isEqual(Cash c){
        return (blue == c.getBlue() && red == c.getRed() && yellow == c.getYellow());
    }

    public Cash subtract(Cash c){
        return new Cash(
            blue - c.blue,
            red - c.red,
            yellow - c.yellow
        );
    }

    public Cash sum(Cash c){
        return new Cash (
            blue + c.blue,
            red + c.red,
            yellow + c.yellow
        );
    }

    public boolean pay(Cash c){     //should we put a CantPayException instead of boolean?
        if (this.greaterEqual(c)){
            blue -= c.blue;
            red -= c.red;
            yellow -= c.yellow;
            return true;
        } else {
            return false;
        }
    }

    public void deposit(Cash c){        //should we put an AmmoLostException (just in order to inform the player)?
        blue = blue + c.getBlue();
        if (blue > 3) blue = 3;
        red = red + c.getRed();
        if (red > 3) red = 3;
        yellow = yellow + c.getYellow();
        if (yellow > 3) yellow = 3;
    }

    public int getTotal(){
        return blue + red + yellow;
    }

    public void set(int b, int r, int y){
        blue = b;
        red = r;
        yellow = y;
    }

    public void set(Cash c){
        blue = c.getBlue();
        red = c.getRed();
        yellow = c.getYellow();
    }

    public void setZero(){
        set(0, 0, 0);
    }

    public boolean containsColor(AmmoColor c){
        switch (c){
            case RED:
                if (red > 0) return true;
                break;
            case BLUE:
                if (blue > 0 ) return true;
                break;
            case YELLOW:
                if (yellow > 0) return true;
                break;
            default:
                return false;
        }
        return false;
    }

    public List<AmmoColor> getColors() {
        List<AmmoColor> result = new ArrayList<>();
        for (AmmoColor c : AmmoColor.getAmmoColors()){
            if (this.containsColor(c)) {
                result.add(c);
            }
        }
        return result;
    }
}
