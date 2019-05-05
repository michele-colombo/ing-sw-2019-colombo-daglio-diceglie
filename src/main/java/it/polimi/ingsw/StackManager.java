package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

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

    private List<Weapon> loadOriginalWeaponArsenal(){
        List<Weapon> result= new ArrayList<>();
        result.addAll(new WeaponBuilder().getWeapons());

        return result;
    }

    private List<PowerUp> loadOriginalPowerups(){
        List<PowerUp> result= new ArrayList<>();

        return result;
    }

    private List<AmmoTile> loadOriginalAmmoTiles(){
        List<AmmoTile> result= new ArrayList<>();

        return result;
    }

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

    public List<Weapon> getOriginalWeaponArsenal() {
        return originalWeaponArsenal;
    }

    public List<PowerUp> getOriginalPowerUps() {
        return originalPowerUps;
    }

    public List<AmmoTile> getOriginalAmmoTiles() {
        return originalAmmoTiles;
    }

    public StackManager(){
        originalWeaponArsenal= loadOriginalWeaponArsenal();
        originalPowerUps= loadOriginalPowerups();
        originalAmmoTiles= loadOriginalAmmoTiles();

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
        Collections.shuffle(weaponActiveStack);
    }

    public void initAmmoTilesStack(List<AmmoTile> list){

        ammoTilesActiveStack.clear();
        ammoTilesActiveStack.addAll(list);
        Collections.shuffle(ammoTilesActiveStack);
    }

    public void initPowerUpStack(List<PowerUp> list){

        powerUpActiveStack.clear();
        powerUpActiveStack.addAll(list);
        Collections.shuffle(powerUpActiveStack);
    }




    }
