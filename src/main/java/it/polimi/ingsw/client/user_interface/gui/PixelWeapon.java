package it.polimi.ingsw.client.user_interface.gui;

/**
 * It represent the translation and rotation ratio for a weapon
 */
public class PixelWeapon {
    /**
     * The ratio of translation on x axe
     */
    private double x;
    /**
     * The ratio of translation on y axe
     */
    private double y;
    /**
     * The number of degrees a weapon has to be rotated
     */
    private int rotate;

    /**
     * Creates a PixelWeapon with the specified ratios and degrees
     * @param x The ratio on x axe
     * @param y The ratio on y axe
     * @param rotate The degrees of rotation
     */
    public PixelWeapon(double x, double y, int rotate){
        this.x = x;
        this.y = y;
        this.rotate = rotate;
    }

    /**
     *
     * @return x ratio
     */
    public double getX(){
        return x;
    }

    /**
     *
     * @return y ratio
     */
    public double getY(){
        return y;
    }

    /**
     *
     * @return rotate
     */
    public int getRotate(){
        return rotate;
    }
}
