package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.server.model.AmmoTile;
import it.polimi.ingsw.server.model.Weapon;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientParser {
    private static final String LAYOUT_FOLDER= "resources/layoutConfig/";
    private static final String LAYOUT_PREFIX = "layoutConfig";
    private static final String EXTENSION = ".json";

    private static final String WEAPONS_PATH = "resources/weapons.json";
    private static final String POWERUPS_PATH = "resources/powerUps.json";
    private static final String POWERUPS_DESCRIPTION_PATH = "resources/powerUpsDescription.json";
    private static final String AMMOTILES_PATH = "resources/ammoTiles.json";


    private List<WeaponView> weapons;
    private List<PowerUpView> powerUps;
    private List<AmmoTile> ammoTiles;
    private LayoutView[] layouts;

    private Gson gson;


    public ClientParser(){
        gson= new Gson();
        layouts= new LayoutView[4];
        parseLayouts();
        parseWeapons();
        parsePowerUps();
        parseAmmoTiles();
    }

    private void parseLayouts(){
        for(int i=0; i<4; i++) {
            List<SquareView> tempSquares;
            try ( InputStream url = getClass().getClassLoader().getResourceAsStream(LAYOUT_FOLDER + LAYOUT_PREFIX + i + EXTENSION);
                Scanner sc = new Scanner(url); ) {
                tempSquares = new ArrayList<>(Arrays.asList(gson.fromJson(sc.nextLine(), SquareView[].class)));
                tempSquares.addAll(Arrays.asList(gson.fromJson(sc.nextLine(), SquareView[].class)));

                LayoutView layout= new LayoutView(tempSquares, i);
                layouts[i]= layout;
            }
            catch (IOException e){
                printError();
            }
        }

    }

    private void parseWeapons(){
        try(Scanner sc= new Scanner(getClass().getClassLoader().getResourceAsStream(WEAPONS_PATH))){
            weapons= Arrays.asList(gson.fromJson(sc.nextLine(), WeaponView[].class));
        }
    }

    private void parsePowerUps() {
        try (Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream(POWERUPS_PATH))) {
            powerUps = Arrays.asList(gson.fromJson(sc.nextLine(), PowerUpView[].class));
        }

        String tagbackDescription;
        String targetingDescription;
        String teleporterDescription;
        String newtonDescription;
        try (
                InputStream url = getClass().getClassLoader().getResourceAsStream(POWERUPS_DESCRIPTION_PATH);
                Scanner sc = new Scanner(url);) {

            JsonObject o = (JsonObject) new JsonParser().parse(sc.nextLine());
            tagbackDescription = o.get("Tagback grenade").getAsString();
            targetingDescription = o.get("Targeting scope").getAsString();
            teleporterDescription = o.get("Teleporter").getAsString();
            newtonDescription = o.get("Newton").getAsString();

            for (PowerUpView p : powerUps) {
                switch (p.getName()) {
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
        } catch (IOException e) {
            printError();
        }
    }

    private void parseAmmoTiles(){
        List<AmmoTile> result;
        try(Scanner sc= new Scanner(getClass().getClassLoader().getResourceAsStream(AMMOTILES_PATH))){
            result= Arrays.asList( gson.fromJson(sc.nextLine(), AmmoTile[].class));
        }

        ammoTiles= result;
    }


    private void printError() {
        System.out.println("ERROR WHILE PARSING CLIENT");
    }

    public List<WeaponView> getWeapons() {
        return weapons;
    }

    public List<PowerUpView> getPowerUps() {
        return powerUps;
    }

    public List<AmmoTile> getAmmoTiles() {
        List<AmmoTile> result= new ArrayList<>();
        result.addAll(ammoTiles);
        return result;
    }

    public LayoutView getLayout(int config){
        return layouts[config];
    }
}
