package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.server.model.AmmoTile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DecksView {
    private List<WeaponView> originalWeaponArsenal;
    private List<PowerUpView> originalPowerUps;
    private List<AmmoTile> originalAmmoTiles;


    public DecksView(List<WeaponView> originalWeaponArsenal, List<PowerUpView> originalPowerUps, List<AmmoTile> originalAmmoTiles) {
        this.originalWeaponArsenal = originalWeaponArsenal;
        this.originalPowerUps = originalPowerUps;
        this.originalAmmoTiles = originalAmmoTiles;
    }


    public PowerUpView getPowerUpFromString(String string){
        for (PowerUpView po : originalPowerUps){
            if (po.toString().equalsIgnoreCase(string)){
                return po;
            }
        }
        return null;
    }

    public AmmoTile getAmmoTileFromString(String string){
        if (string == null ) return null;
        if (string.equals("")) return null;
        for (AmmoTile at : originalAmmoTiles){
            if (at.toString().equalsIgnoreCase(string)){
                return at;
            }
        }
        return null;
    }

    public WeaponView getWeaponFromName(String name){
        for (WeaponView w : originalWeaponArsenal){
            if (w.getName().equalsIgnoreCase(name)){
                return w;
            }
        }
        return null;
    }
}
