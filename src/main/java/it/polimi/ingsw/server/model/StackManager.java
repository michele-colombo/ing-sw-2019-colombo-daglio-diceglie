package it.polimi.ingsw.server.model;

import java.util.*;

/**
 * Represents a common point of access to all the decks of the game, that allows drawing and discarding of all the cards.
 * The "original" complete decks (i.e. the full set of cards) are passed as constructor parameter.
 */
public class StackManager {

    // STIAMO USANDO LE ARRAYLIST. LA "TESTA" DEL MAZZO E' L'INDICE ZERO
    //WE ARE USING ARRAYLISTS. THE HEAD OF THE STACK IS INDEX ZERO

    /**
     * stack from which pick weapons
     */
    private List<Weapon> weaponActiveStack;

    /**
     * stack from which pick power ups
     */
    private List<PowerUp> powerUpActiveStack;

    /**
     * stack where to discard powerups
     */
    private List<PowerUp> powerUpWasteStack;

    /**
     * stack from which pick ammo tiles
     */
    private List<AmmoTile> ammoTilesActiveStack;

    /**
     * stack where to discard ammo tiles
     */
    private List<AmmoTile> ammoTilesWasteStack;


    /**
     * original list of all 21  weapons
     */
    private List<Weapon> originalWeaponArsenal;

    /**
     * original list of all 24 powerups
     */
    private List<PowerUp> originalPowerUps;

    /**
     * original list of all ammo tiles
     */
    private List<AmmoTile> originalAmmoTiles;

    /**
     *
     * @return weapon active stack
     */
    public List<Weapon> getWeaponActiveStack() {
        return weaponActiveStack;
    }

    /**
     *
     * @return powerup active stack
     */
    public List<PowerUp> getPowerUpActiveStack() {
        return powerUpActiveStack;
    }

    /**
     *
     * @return powerup waste stack
     */
    public List<PowerUp> getPowerUpWasteStack() {
        return powerUpWasteStack;
    }

    /**
     *
     * @return ammo tiles active stack
     */
    public List<AmmoTile> getAmmoTilesActiveStack() {
        return ammoTilesActiveStack;
    }

    /**
     *
     * @return ammo tiles waste stack
     */
    public List<AmmoTile> getAmmoTilesWasteStack() {
        return ammoTilesWasteStack;
    }

    //Should we return a clone of the original lists? yes it does

    /**
     *
     * @return a clone of the original weapon list
     */

    public List<Weapon> getOriginalWeaponArsenal() {
        List<Weapon> clone= new ArrayList<>();
        clone.addAll(originalWeaponArsenal);
        return clone;
    }

    /**
     *
     * @return the original power ups list
     */
    public List<PowerUp> getOriginalPowerUps() {
        return originalPowerUps;
    }

    /**
     *
     * @return the original ammo tiles list
     */
    public List<AmmoTile> getOriginalAmmoTiles() {
        return originalAmmoTiles;
    }

    /**
     * build the stack manager with the three original stacks. It creates the active stacks cloning the originals and shuffling them
     * @param originalWeaponArsenal original list of weapons
     * @param originalPowerUps original list of powerups
     * @param originalAmmoTiles original list of ammo tiles
     */
    public StackManager(List<Weapon> originalWeaponArsenal, List<PowerUp> originalPowerUps, List<AmmoTile> originalAmmoTiles) {
        this.originalWeaponArsenal = originalWeaponArsenal;
        this.originalPowerUps = originalPowerUps;
        this.originalAmmoTiles = originalAmmoTiles;

        weaponActiveStack= new ArrayList<>();
        weaponActiveStack.addAll(originalWeaponArsenal);
        Collections.shuffle(weaponActiveStack);

        powerUpActiveStack= new ArrayList<>();
        powerUpActiveStack.addAll(originalPowerUps);
        Collections.shuffle(powerUpActiveStack);

        ammoTilesActiveStack= new ArrayList<>();
        ammoTilesActiveStack.addAll(originalAmmoTiles);
        Collections.shuffle(ammoTilesActiveStack);

        powerUpWasteStack= new ArrayList<>();
        ammoTilesWasteStack= new ArrayList<>();
    }

    /**
     * pick a weapon card
     * @return weapon drawn
     */
    public Weapon drawWeapon(){
        if(! weaponActiveStack.isEmpty()){
            Weapon result=  weaponActiveStack.get(0);
            weaponActiveStack.remove(result);

            return result;
        }
        return null;
    }

    /**
     * pick a ammo tile
     * @return ammo tile drawn
     */
    public AmmoTile drawAmmoTile(){
        if(ammoTilesActiveStack.isEmpty()){
            ammoTilesActiveStack.addAll(ammoTilesWasteStack);
            Collections.shuffle(ammoTilesActiveStack);
            ammoTilesWasteStack.clear();
        }

        AmmoTile result= ammoTilesActiveStack.get(0);
        ammoTilesActiveStack.remove(result);
        return result;

    }

    /**
     * pick a power up card
     * @return power up drawn
     */
    public PowerUp drawPowerUp(){
        if(powerUpActiveStack.isEmpty()){
            powerUpActiveStack.addAll(powerUpWasteStack);
            Collections.shuffle(powerUpActiveStack);
            powerUpWasteStack.clear();
        }
        PowerUp result= powerUpActiveStack.get(0);
        powerUpActiveStack.remove(result);

        return result;
    }

    /**
     * throw power up in the waste stack
     * @param pu power up to discard
     */
    public void discardPowerUp(PowerUp pu){
        powerUpWasteStack.add(pu);
    }

    /**
     * throw ammo tile in the waste stack
     * @param ammo ammo tile to discard
     */
    public void trashAmmoTile(AmmoTile ammo){
        ammoTilesWasteStack.add(ammo);
    }

    /**
     * init the weapon active stack with a particular order
     * @param list ordered list of weapons
     */
    public void initWeaponStack(List<Weapon> list){

        weaponActiveStack.clear();
        weaponActiveStack.addAll(list);
    }

    /**
     * init the ammo tiles active stack with a particular order
     * @param list ordered list of ammo tiles in active stack
     */
    public void initAmmoTilesStack(List<AmmoTile> list){

        ammoTilesActiveStack.clear();
        ammoTilesActiveStack.addAll(list);
    }

    /**
     * init init the ammo tiles waste stack with a particular order
     * @param list ordered list of ammotiles in waste stack
     */
    public void initAmmoTilesWasteStack(List<AmmoTile> list){

        ammoTilesWasteStack.clear();
        ammoTilesWasteStack.addAll(list);
    }

    /**
     * init the power up active stack with a particular order
     * @param list ordered liist of powerups in active stack
     */
    public void initPowerUpStack(List<PowerUp> list){

        powerUpActiveStack.clear();
        powerUpActiveStack.addAll(list);
    }

    /**
     * init the power up waste stack with a particular order
     * @param list ordered list of powerups in waste stack
     */
    public void initPowerUpWasteStack(List<PowerUp> list){

        powerUpWasteStack.clear();
        powerUpWasteStack.addAll(list);
    }

    /**
     * get a power up
     * @param string the unique name of wanted powerup
     * @return wanted powerup or null if it does not exist
     */
    public PowerUp getPowerUpFromString(String string){
        for (PowerUp po : originalPowerUps){
            if (po.toString().equals(string)){
                return po;
            }
        }
        return null;
    }

    /**
     * get a ammo tile
     * @param string unique name of the ammotile
     * @return the ammotile or null if it does not exist
     */
    public AmmoTile getAmmoTileFromString(String string){
        if (string == null ) return null;
        if (string.equals("")) return null;
        for (AmmoTile at : originalAmmoTiles){
            if (at.toString().equals(string)){
                return at;
            }
        }
        return null;
    }

    /**
     * get a weapon
     * @param name name of the wanted weapon
     * @return the weapon or null if it does not exists
     */
    public Weapon getWeaponFromName(String name){
        for (Weapon w : originalWeaponArsenal){
            if (w.getName().equals(name)){
                return w;
            }
        }
        return null;
    }

}
