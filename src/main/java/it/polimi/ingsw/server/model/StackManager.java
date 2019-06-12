package it.polimi.ingsw.server.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class StackManager {

    // STIAMO USANDO LE ARRAYLIST. LA "TESTA" DEL MAZZO E' L'INDICE ZERO
    private List<Weapon> weaponActiveStack;
    private List<PowerUp> powerUpActiveStack;
    private List<PowerUp> powerUpWasteStack;
    private List<AmmoTile> ammoTilesActiveStack;
    private List<AmmoTile> ammoTilesWasteStack;

    private List<Weapon> originalWeaponArsenal;
    private List<PowerUp> originalPowerUps;
    private List<AmmoTile> originalAmmoTiles;

    public List<Weapon> getWeaponActiveStack() {
        return weaponActiveStack;
    }

    public List<PowerUp> getPowerUpActiveStack() {
        return powerUpActiveStack;
    }

    public List<PowerUp> getPowerUpWasteStack() {
        return powerUpWasteStack;
    }

    public List<AmmoTile> getAmmoTilesActiveStack() {
        return ammoTilesActiveStack;
    }

    public List<AmmoTile> getAmmoTilesWasteStack() {
        return ammoTilesWasteStack;
    }

    //Should we return a clone of the original lists?
    public List<Weapon> getOriginalWeaponArsenal() {
        return originalWeaponArsenal;
    }

    public List<PowerUp> getOriginalPowerUps() {
        return originalPowerUps;
    }

    public List<AmmoTile> getOriginalAmmoTiles() {
        return originalAmmoTiles;
    }

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

    public Weapon drawWeapon(){
        if(! weaponActiveStack.isEmpty()){
            Weapon result=  weaponActiveStack.get(0);
            weaponActiveStack.remove(result);

            return result;
        }
        return null;
    }

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

    public void discardPowerUp(PowerUp pu){
        powerUpWasteStack.add(pu);
    }

    public void trashAmmoTile(AmmoTile ammo){
        ammoTilesWasteStack.add(ammo);
    }

    public void initWeaponStack(List<Weapon> list){

        weaponActiveStack.clear();
        weaponActiveStack.addAll(list);
    }

    public void initAmmoTilesStack(List<AmmoTile> list){

        ammoTilesActiveStack.clear();
        ammoTilesActiveStack.addAll(list);
    }

    public void initAmmoTilesWasteStack(List<AmmoTile> list){

        ammoTilesWasteStack.clear();
        ammoTilesWasteStack.addAll(list);
    }

    public void initPowerUpStack(List<PowerUp> list){

        powerUpActiveStack.clear();
        powerUpActiveStack.addAll(list);
    }

    public void initPowerUpWasteStack(List<PowerUp> list){

        powerUpWasteStack.clear();
        powerUpWasteStack.addAll(list);
    }

    public PowerUp getPowerUpFromString(String string){
        for (PowerUp po : originalPowerUps){
            if (po.toString().equals(string)){
                return po;
            }
        }
        return null;
    }

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

    public Weapon getWeaponFromName(String name){
        for (Weapon w : originalWeaponArsenal){
            if (w.getName().equals(name)){
                return w;
            }
        }
        return null;
    }

}
