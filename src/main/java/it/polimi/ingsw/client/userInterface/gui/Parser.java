package it.polimi.ingsw.client.userInterface.gui;

import com.google.gson.Gson;

import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * It's represent the parser who loads PixelPosition and PixelWeapon
 */
public class Parser {

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
}
