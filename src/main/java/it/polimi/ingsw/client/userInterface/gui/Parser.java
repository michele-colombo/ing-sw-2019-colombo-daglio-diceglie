package it.polimi.ingsw.client.userInterface.gui;

import com.google.gson.Gson;

import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    private Gson gson;
    private int layout;

    public Parser(int layout){
        gson = new Gson();
        this.layout = layout;
    }

    public List<PixelPosition> loadAmmoResource(){
        InputStream resourceUrl = getClass().getClassLoader().getResourceAsStream("coord" + layout + ".json");
        List<PixelPosition> pixelPositions = new LinkedList<>();
        PixelPosition[] pixelPositionArray;
        Scanner sc = new Scanner(resourceUrl);
        pixelPositionArray = gson.fromJson(sc.nextLine(), PixelPosition[].class);
        pixelPositions.addAll(Arrays.asList(pixelPositionArray));

        return pixelPositions;
    }

    public List<PixelWeapon> loadWeaponResource(String color){
        InputStream resourceUrl = getClass().getClassLoader().getResourceAsStream(color + "Weapons.json");
        List<PixelWeapon> pixelWeapons = new LinkedList<>();
        PixelWeapon[] pixelWeaponsArray;
        Scanner sc = new Scanner(resourceUrl);
        pixelWeaponsArray = gson.fromJson(sc.nextLine(), PixelWeapon[].class);
        pixelWeapons.addAll(Arrays.asList(pixelWeaponsArray));

        return pixelWeapons;
    }
}
