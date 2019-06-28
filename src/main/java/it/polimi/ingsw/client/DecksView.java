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

    private static final String WEAPONS_PATH = "resources/weapons.json";
    private static final String POWERUPS_PATH = "resources/powerUps.json";
    private static final String POWERUPS_DESCRIPTION_PATH = "resources/powerUpsDescription.json";
    private static final String AMMOTILES_PATH = "resources/ammoTiles.json";



    public DecksView() {
        originalWeaponArsenal= loadOriginalWeaponArsenal();
        originalPowerUps= loadOriginalPowerups();
        originalAmmoTiles= loadOriginalAmmoTiles();
    }

    private List<WeaponView> loadOriginalWeaponArsenal(){
        Gson gson= new Gson();
        List<WeaponView> result;

        InputStream url= getClass().getClassLoader().getResourceAsStream(WEAPONS_PATH);
        Scanner sc= new Scanner(url);
        result= Arrays.asList( gson.fromJson(sc.nextLine(), WeaponView[].class));

        return result;
    }

    private List<PowerUpView> loadOriginalPowerups(){
        Gson gson= new Gson();
        List<PowerUpView> result;

        InputStream url= getClass().getClassLoader().getResourceAsStream(POWERUPS_PATH);
        Scanner sc= new Scanner(url);
        result= Arrays.asList( gson.fromJson(sc.nextLine(), PowerUpView[].class));

        url = getClass().getClassLoader().getResourceAsStream(POWERUPS_DESCRIPTION_PATH);
        sc = new Scanner(url);
        JsonObject o = (JsonObject) new JsonParser().parse(sc.nextLine());
        String tagbackDescription =  o.get("Tagback grenade").getAsString();
        String targetingDescription =  o.get("Targeting scope").getAsString();
        String teleporterDescription =  o.get("Teleporter").getAsString();
        String newtonDescription =  o.get("Newton").getAsString();

        for (PowerUpView p : result){
            switch (p.getName()){
                case "Tagback grenade":
                    p.setDescription(tagbackDescription);
                    break;
                case "Targeting scope":
                    p.setDescription(targetingDescription);
                    break;
                case "Teleporter":
                    p.setDescription(teleporterDescription);
                    break;
                case "Newton":
                    p.setDescription(newtonDescription);
                    break;
            }
        }

        return result;
    }

    private List<AmmoTile> loadOriginalAmmoTiles(){
        Gson gson = new Gson();
        List<AmmoTile> result;

        InputStream url= getClass().getClassLoader().getResourceAsStream(AMMOTILES_PATH);
        Scanner sc= new Scanner(url);
        result= Arrays.asList( gson.fromJson(sc.nextLine(), AmmoTile[].class));

        return result;
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
