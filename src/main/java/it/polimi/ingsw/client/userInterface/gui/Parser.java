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

    public List<PixelAmmo> loadAmmoResource(){
        InputStream resourceUrl = getClass().getClassLoader().getResourceAsStream("coord" + layout + ".json");
        List<PixelAmmo> pixelAmmos = new LinkedList<>();
        PixelAmmo[] pixelAmmoArray;
        Scanner sc = new Scanner(resourceUrl);
        pixelAmmoArray = gson.fromJson(sc.nextLine(), PixelAmmo[].class);
        pixelAmmos.addAll(Arrays.asList(pixelAmmoArray));

        return pixelAmmos;
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

    public static void main(String[] args){
        Parser parser = new Parser(0);
        List<PixelAmmo> pixelAmmos = parser.loadAmmoResource();
        List<PixelWeapon> a = parser.loadWeaponResource("yellow");
        if(pixelAmmos.isEmpty()){
            System.out.println("ehi");
        }
        System.out.println("fine");
    }
}
