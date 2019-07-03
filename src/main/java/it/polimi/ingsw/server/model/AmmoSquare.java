package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Border;

/**
 * Extends square and represents those square which contain ammoTiles (opposite to spawn points)
 */
public class AmmoSquare extends Square{
    /**
     * Contains the ammoTile of the square. Is null when no ammoTile is present
     */
    private AmmoTile ammo;

    /**
     * Constructor with all parameters.
     * @param x x coordinate of square
     * @param y y coordinate of square
     * @param north type of border at north
     * @param east type of border at east
     * @param south type of border at south
     * @param west type of border at west
     * @param color color of the square
     */
    public AmmoSquare(int x, int y, Border north, Border east, Border south, Border west, AmmoColor color){
        super(x, y, north, east, south, west, color);
        ammo = null;
    }

    public AmmoSquare(){
        super();
    }

    /**
     * Gets a description of the content of the square
     * @return a string with description (contains \n)
     */
    @Override
    public String getFullDescription(){
        StringBuilder result= new StringBuilder();
        result.append("Ammo");
        result.append(super.getFullDescription());
        if (!isEmpty()) {
            result.append("Ammos available: "+ammo.toString()+"\n");
        } else {
            result.append("is empty\n");
        }
        return result.toString();
    }

    /**
     * Gives the player p the content of this square
     * @param p player who collects
     * @param m reference to the current match
     * @return true, because microaction is concluded
     */
    @Override
    public boolean collect(Player p, Match m){
        p.getWallet().deposit(ammo.getAmmos());
        if (ammo.hasPowerUp()){
            if (p.getPowerUps().size() < 3){
                p.addPowerUp(m.getStackManager().drawPowerUp());
                m.notifyPowerUpUpdate(p);
            }
        }
        m.getStackManager().trashAmmoTile(ammo);
        assert(ammo.getAmmos().getTotal()<4);
        ammo = null;
        m.notifyLayotUpdate();
        return true;
    }

    /**
     * Tells if the square is empty (has currently no ammoTile)
     * @return false if there is no ammoTile
     */
    @Override
    public boolean isEmpty(){
        return (ammo == null);
    }

    /**
     * Refills the square eith a new ammoTile
     * @param s the stack manager to draw from
     */
    @Override
    public void refill(StackManager s){
        if (isEmpty()){
            ammo = s.drawAmmoTile();
        }
    }

    /**
     * Getter for the ammoTile
     * @return the actual reference to the ammoTile
     */
    public AmmoTile getAmmo() {
        return ammo;
    }

    /**
     * Setter for the ammoTile
     * @param ammo
     */
    public void setAmmo(AmmoTile ammo){
        this.ammo = ammo;
    }
}
