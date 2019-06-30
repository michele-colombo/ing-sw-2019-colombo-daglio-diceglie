package it.polimi.ingsw.server.model;


import it.polimi.ingsw.server.model.enums.AmmoColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cash implements Serializable {
    /**
     * amount of blue ammos
     */
    private int blue;

    /**
     * amount of red ammos
     */
    private int red;

    /**
     * amount of yellow ammos
     */
    private int yellow;

    /**
     * Builds a cash from the number of ammos of each color
     * @param blue number of blue ammos
     * @param red numebr of red ammos
     * @param yellow number of yellow amos
     */
    public Cash(int blue, int red, int yellow) {
        this.blue = blue;
        this.red = red;
        this.yellow = yellow;
    }

    /**
     * Builds a cash from an existing cash, cloning the number of ammos
     * @param source existing cash to clone
     */
    public Cash(Cash source){
        this.blue = source.getBlue();
        this.red = source.getRed();
        this.yellow = source.getYellow();
    }

    /**
     * Builds the default cash (0 amoms for each color)
     */
    public Cash(){
        this(0, 0, 0);
    }


    /**
     * Builds a cash with the specified number of ammos of the specified color
     * @param c color of ammos
     * @param quantity amount of ammos
     */
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

    /**
     * Gets a string describing the content of the cash
     * @return a string in the form: b:x|r:y|y:z, where x, y, z are integers
     */
    @Override
    public String toString(){
        return "b:"+blue+"|r:"+red+"|y:"+yellow;
    }

    /**
     * Gets blue ammos
     * @return the number of blue ammos
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Gets red ammos
     * @return the number of red amoms
     */
    public int getRed() {
        return red;
    }

    /**
     * gets yellow ammos
     * @return the number of yellow ammos
     */
    public int getYellow() {
        return yellow;
    }

    /**
     * Gets the amount of ammos of the specified color
     * @param color the color of ammos
     * @return integer of the corresponding amount
     */
    public int getAmountOf(AmmoColor color){
        switch (color){
            case BLUE:
                return getBlue();
            case RED:
                return getRed();
            case YELLOW:
                return getYellow();
            default:
                return 0;
        }
    }

    /**
     * Tells if this cash contains a greater or equal amount of ammos of each color
     * @param c cash to compare
     * @return true if this cash contains at least everything which is contained in the passed cash
     */
    public boolean greaterEqual(Cash c){
        return (blue >= c.blue && red >= c.red && yellow >= c.yellow);
    }

    /**
     * Tells if this cash contains a less or equal amount of ammos of each color than the passed cash
     * @param c cash to compare
     * @return true if the passed cash contains everything which is contained in this cash
     */
    public boolean lessEqual(Cash c){
        return (blue <= c.blue && red <= c.red && yellow <= c.yellow);
    }

    /**
     * Checks if two cash contain the same amount of ammos of each color
     * @param obj object to compare
     * @return true if they contain exactly the same ammos
     */
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

    /**
     * Checks if two cash contain the same amount of ammos of each color
     * @param c cash to compare
     * @return true if they contain exactly the same ammos
     */
    public boolean isEqual(Cash c){
        return (blue == c.getBlue() && red == c.getRed() && yellow == c.getYellow());
    }

    /**
     * Creates a new cash with the result of the subtraction between this cash and the passed one.
     * By subtraction between cash is intended the subtraction between each amount of ammos.
     * Amounts of ammos can be negative. It does not modify this cash.
     * @param c cash to subtract
     * @return new cash with the subtraction result
     */
    public Cash subtract(Cash c){
        return new Cash(
            blue - c.blue,
            red - c.red,
            yellow - c.yellow
        );
    }

    /**
     * Creates a new cash with the result of the sum between this cash and the passed one.
     * By addition between cash is intended the addition between each amount of ammos.
     * It does not modify this cash.
     * @param c cash to summ
     * @return new cash with the addition result
     */
    public Cash sum(Cash c){
        return new Cash (
            blue + c.blue,
            red + c.red,
            yellow + c.yellow
        );
    }

    /**
     * Checks if this cash can pay for the specified ammo cost, the it pays for it. Modifies this cash.
     * @param c the ammo cost to pay.
     * @return true if the payment is possible (no ammo is negative after payment)
     */
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

    /**
     * Deposits ammos in this cash, embedding the rule of maximum 3 ammos for each color.
     * Exceeding ammos are lost. It modifies this cash.
     * @param c ammos to deposit.
     */
    public void deposit(Cash c){
        blue = blue + c.getBlue();
        if (blue > 3) blue = 3;
        red = red + c.getRed();
        if (red > 3) red = 3;
        yellow = yellow + c.getYellow();
        if (yellow > 3) yellow = 3;
    }

    /**
     * Gets the total amount of ammos, of all colors
     * @return the total amount of ammos
     */
    public int getTotal(){
        return blue + red + yellow;
    }

    /**
     * Sets the specified ammos in the cash
     * @param b blue ammos
     * @param r red ammos
     * @param y yellow ammos
     */
    public void set(int b, int r, int y){
        blue = b;
        red = r;
        yellow = y;
    }

    /**
     * Sets this cash with the content of the specified cash
     * @param c source cash to match
     */
    public void set(Cash c){
        blue = c.getBlue();
        red = c.getRed();
        yellow = c.getYellow();
    }

    /**
     * Sets all ammos to zero
     */
    public void setZero(){
        set(0, 0, 0);
    }

    /**
     * Checks if this cash contains at least on ammo of the specified color
     * @param c color to check
     * @return true if it contains at least one ammo of the color
     */
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

    /**
     * Gets a list of colors contained (of which there is at least one ammo) in this cash
     * @return a list of contained colors
     */
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
