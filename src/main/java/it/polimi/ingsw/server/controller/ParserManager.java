package it.polimi.ingsw.server.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.server.model.enums.Border.OPEN;

public class ParserManager {

    Layout[] layouts;
    StackManager stackManager;

    private Gson gson;


    public ParserManager(){
        gson= new Gson();
        layouts= new Layout[4];
        for(int i=0; i<4; i++){
            parseLayout(i);
        }
        parseStack();
    }

    public StackManager getStackManager() {
        return stackManager;
    }

    /**
     * le configurazioni del tabellone riferite al manuale di gioco sono:
     * -0 == piccola "ottima per 4 o 5 giocatori"
     * -1 == quella grande che e' disegnata su entrambe le pagine
     * -2 == piccola "ottima per qualsiasi numero di giocatori
     * -3 == picoola "ottima per 3 o 4 giocatori"
     *
     * @param configNumber configuration code
     */
    public Layout getLayout(int configNumber){
        if(configNumber>=0 && configNumber<=3) {
            return layouts[configNumber];
        }
        return null;
    }

    private void parseLayout(int num){
        List<AmmoSquare> ammoSquares= new ArrayList<>();
        List<SpawnSquare> spawnSquares= new ArrayList<>();

        InputStream url = getClass().getClassLoader().getResourceAsStream("layoutConfig/layoutConfig"+num+".json");
        Scanner sc = new Scanner(url);

        AmmoSquare[] tempAmmo;
        tempAmmo = gson.fromJson(sc.nextLine(), AmmoSquare[].class);
        ammoSquares.clear();
        ammoSquares.addAll(Arrays.asList(tempAmmo));

        SpawnSquare[] tempSpawn;
        tempSpawn = gson.fromJson(sc.nextLine(), SpawnSquare[].class);
        spawnSquares.clear();
        spawnSquares.addAll(Arrays.asList(tempSpawn));

        Layout layout= new Layout(spawnSquares, ammoSquares, num);
        layout.instantiateRooms();

        layouts[num]= layout;
    }

    private void parseStack(){
        List<Weapon> weapons;
        List<PowerUp> powerUps;
        List<AmmoTile> ammoTiles;

        InputStream url;
        Scanner sc;

        try {

            url = getClass().getClassLoader().getResourceAsStream("weapons.json");
            sc = new Scanner(url);
            weapons = Arrays.asList(gson.fromJson(sc.nextLine(), Weapon[].class));
            sc.close();
            url.close();


            url = getClass().getClassLoader().getResourceAsStream("powerUps.json");
            sc = new Scanner(url);
            powerUps = Arrays.asList(gson.fromJson(sc.nextLine(), PowerUp[].class));
            sc.close();
            url.close();

            url= getClass().getClassLoader().getResourceAsStream("ammoTiles.json");
            sc= new Scanner(url);
            ammoTiles= Arrays.asList(gson.fromJson(sc.nextLine(), AmmoTile[].class));
            sc.close();
            url.close();

            stackManager= new StackManager(weapons, powerUps, ammoTiles);
        }
        catch (IOException e){
            System.out.println("Problems while closing inputStream");
        }
        catch (NullPointerException e){
            System.out.println("NO FILE DETECTED FOR STACKS");
        }


    }

}
