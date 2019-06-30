package it.polimi.ingsw.client.userInterface.gui;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * It loads image from file and properly cache them
 */
public class CacheImage {
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
     * Create CacheImage, initializing attributes
     */
    public CacheImage(){
        ammoButtonImages = new HashMap<>();
        weaponButtonImages = new HashMap<>();
        powerUpButtonImages = new HashMap<>();
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
     * Load image from path
     * @param path path of the image to be loaded
     * @return image associated to path
     */
    private Image loadImage(String path){
        InputStream url = getClass().getClassLoader().getResourceAsStream(path + IMAGE_EXTENSION);
        return new Image(url);
    }
}
