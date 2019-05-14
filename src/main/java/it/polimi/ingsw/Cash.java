package it.polimi.ingsw;


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

    public Cash(){
        this(0, 0, 0);
    }

    public Cash(Color c, int quantity){
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

    public boolean isEqual(Cash c){
        return (blue == c.blue && red == c.red && yellow == c.yellow);
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
        this.blue += c.getBlue();
        if (this.blue > 3){
            this.blue = 3;
        }
        this.red += c.getRed();
        if (this.red > 3){
            this.red = 3;
        }
        this.yellow += c.getYellow();
        if (this.yellow > 3){
            this.yellow = 3;
        }
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

    public boolean containsColor(Color c){
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

    public List<Color> getColors() {
        List<Color> result = new ArrayList<>();
        for (Color c : Color.getAmmoColors()){
            if (this.containsColor(c)) {
                result.add(c);
            }
        }
        return result;
    }
}
