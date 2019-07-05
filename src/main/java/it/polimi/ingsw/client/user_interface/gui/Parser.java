package it.polimi.ingsw.client.user_interface.gui;

import com.google.gson.Gson;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.*;

/**
 * It's represent the parser who loads graphics resources
 */
public class Parser {
    /**
     * The path of the DamageTrack PNG
     */
    private static final String DAMAGE_TRACK_IMAGE_FOLDER= "resources/damageTracks/";
    /**
     * The path of layout PNG
     */
    private static final String LAYOUT_PNG_FOLDER = "resources/layoutPNG/layout";
    /**
     * The path of the AmmoButton PNG
     */
    private static final String AMMOTILES_FOLDER = "resources/ammo/ammo";
    /**
     * The path of the WeaponButton PNG
     */
    private static final String WEAPON_IMAGE_FOLDER= "resources/weapon/";

    /**
     * The path of the PowerUpButton PNG
     */
    private static final String POWERUP_IMAGES_FOLDER= "resources/powerUp/";
    /**
     * File extension of the images
     */
    private static final String IMAGE_EXTENSION = ".png";

    /**
     * Map ammoTiles ids with their images
     */
    private Map<Integer, Image> ammoButtonImages;
    /**
     * Map weapons ids with their images
     */
    private Map<String, Image> weaponButtonImages;
    /**
     * Map powerUps names with their images
     */
    private Map<String, Image> powerUpButtonImages;

    /**
     * The path of the JSON file
     */
    private static final String FILE_SOURCE = "resources/";
    private static final String FILE_NAME_AMMO = "coord";
    private static final String FILE_NAME_WEAPONS = "Weapons";
    private static final String FILE_EXTENSION = ".json";
    /**
     * The gson parser
     */
    private Gson gson;
    /**
     * The layout of the board
     */
    private int layout;

    /**
     * Create a parser with the selected layout
     * @param layout The selected layout
     */
    public Parser(int layout){
        gson = new Gson();
        ammoButtonImages = new HashMap<>();
        weaponButtonImages = new HashMap<>();
        powerUpButtonImages = new HashMap<>();
        this.layout = layout;
    }


    /**
     * Load PixelPosition from file
     * @return The List of PixelPosition loaded from file
     */
    public List<PixelPosition> loadAmmoResource(){
        InputStream resourceUrl = getClass().getClassLoader().getResourceAsStream(FILE_SOURCE + FILE_NAME_AMMO + layout + FILE_EXTENSION);
        List<PixelPosition> pixelPositions = new LinkedList<>();
        PixelPosition[] pixelPositionArray;
        Scanner sc = new Scanner(resourceUrl);
        pixelPositionArray = gson.fromJson(sc.nextLine(), PixelPosition[].class);
        pixelPositions.addAll(Arrays.asList(pixelPositionArray));

        return pixelPositions;
    }

    /**
     * Load PixelWeapon from file
     * @param color weapon's color
     * @return The List of PixelWeapon loaded from file
     */
    public List<PixelWeapon> loadWeaponResource(String color){
        InputStream resourceUrl = getClass().getClassLoader().getResourceAsStream(FILE_SOURCE + color + FILE_NAME_WEAPONS + FILE_EXTENSION);
        List<PixelWeapon> pixelWeapons = new LinkedList<>();
        PixelWeapon[] pixelWeaponsArray;
        Scanner sc = new Scanner(resourceUrl);
        pixelWeaponsArray = gson.fromJson(sc.nextLine(), PixelWeapon[].class);
        pixelWeapons.addAll(Arrays.asList(pixelWeaponsArray));

        return pixelWeapons;
    }

    /**
     * If the ammotile associated to id hasn't been already loaded, load it
     * @param id id of ammoTile requested
     * @return image of the respective ammoTile
     */
    public Image getAmmoButtonImage(int id){
        if(ammoButtonImages.get(id) == null){
            ammoButtonImages.put(id, loadImage(AMMOTILES_FOLDER + id));

        }
        return ammoButtonImages.get(id);
    }

    /**
     * If the weapon associated to name hasn't been already loaded, load it
     * @param name name of weapon requested
     * @return image of the respective ammoTile
     */
    public Image getWeaponButtonImage(String name){
        if(weaponButtonImages.get(name) == null){
            weaponButtonImages.put(name, loadImage(WEAPON_IMAGE_FOLDER + name));
        }
        return weaponButtonImages.get(name);
    }

    /**
     * If the powerup associated to name hasn't been already loaded, load it
     * @param name name of powerUp requested
     * @return image of the respective ammoTile
     */
    public Image getPowerUpButtonImage(String name){
        if(powerUpButtonImages.get(name) == null){
            powerUpButtonImages.put(name, loadImage(POWERUP_IMAGES_FOLDER + name));
        }
        return powerUpButtonImages.get(name);
    }

    /**
     * Load chosen layout image for BoardGui
     * @param configuration chosen layout configuration
     * @return layout image
     */
    public Image getLayoutImage(int configuration){
        return loadImage(LAYOUT_PNG_FOLDER + configuration);
    }

    /**
     * Load chosen damage track image for BoardGui
     * @param name damage track name
     * @return damage track image
     */
    public Image getDamageTrackImage(String name){
        return loadImage(DAMAGE_TRACK_IMAGE_FOLDER + name);
    }

    /**
     * Load image from path
     * @param path path of the image to be loaded
     * @return image associated to path
     */
    private Image loadImage(String path){
        InputStream url = getClass().getClassLoader().getResourceAsStream(path + IMAGE_EXTENSION);
        return new Image(url);
    }
}
