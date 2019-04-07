package it.polimi.ingsw;

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
        blue += c.blue;
        if (blue > 3) blue = 3;
        red += c.red;
        if (red > 3) red = 3;
        yellow += c.yellow;
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

    public void setZero(){
        set(0, 0, 0);
    }
}
