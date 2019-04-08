package it.polimi.ingsw;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class StackManager {
    private Stack<Weapon> weaponActiveStack;
    private Stack<PowerUp> powerUpActiveStack;
    private Stack<PowerUp> powerUpWasteStack;
    private Stack<AmmoTile> ammoTilesActiveStack;
    private Stack<AmmoTile> ammoTilesWasteStack;

    public StackManager(){
        weaponActiveStack= new Stack<>();
        powerUpActiveStack= new Stack<>();
        powerUpWasteStack= new Stack<>();
        ammoTilesActiveStack= new Stack<>();
        ammoTilesWasteStack= new Stack<>();
    }

    public Weapon drawWeapon(){
        if(! weaponActiveStack.isEmpty()){
            return (Weapon)weaponActiveStack.pop();
        }
        return null;
    }

    public AmmoTile drawAmmoTile(){
        if(ammoTilesActiveStack.isEmpty()){
            ammoTilesActiveStack = ammoTilesWasteStack;
            Collections.shuffle(ammoTilesActiveStack);
            ammoTilesWasteStack.clear();
        }
        return ammoTilesActiveStack.pop();
    }

    public PowerUp drawPowerUp(){
        if(powerUpActiveStack.isEmpty()){
            powerUpActiveStack = powerUpWasteStack;
            Collections.shuffle(powerUpActiveStack);
            powerUpWasteStack.clear();
        }
        return powerUpActiveStack.pop();
    }

    public void discardPowerUp(PowerUp pu){
        powerUpWasteStack.push(pu);
    }

    public void trashAmmoTile(AmmoTile ammo){
        ammoTilesWasteStack.push(ammo);
    }

    public void initWeaponStack(List<Weapon> list){
        for(Weapon w : list){
            weaponActiveStack.push(w);
        }
        Collections.shuffle(weaponActiveStack);
    }

    public void initAmmoTilesStack(List<AmmoTile> list){
        for(AmmoTile at : list){
            ammoTilesActiveStack.push(at);
        }
        Collections.shuffle(ammoTilesActiveStack);
    }

    public void initPowerUpStack(List<PowerUp> list){
        for(PowerUp pu : list){
            powerUpActiveStack.push(pu);
        }
        Collections.shuffle(powerUpActiveStack);
    }




    }
