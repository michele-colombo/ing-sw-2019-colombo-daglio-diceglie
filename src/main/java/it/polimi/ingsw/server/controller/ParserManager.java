package it.polimi.ingsw.server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.server.model.*;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.server.model.enums.Border.OPEN;

public class ParserManager {
    private static final String BACKUP_NAME = "currentBackup";
    private static final String BACKUP_EXTENSION = ".json";

    private Layout[] layouts;
    private StackManager stackManager;
    private Backup backup;

    private ServerConfig serverConfig;

    private Gson gson;


    public ParserManager(){
        gson= new Gson();
        layouts= new Layout[4];
        for(int i=0; i<4; i++){
            parseLayout(i);
        }
        parseStack();

        parseBackup();

        parseServerConfig();


    }

    public Backup getBackup(){
        return backup;
    }

    public StackManager getStackManager() {
        return stackManager;
    }

    public int getLayoutConfig(){
        return serverConfig.layoutConfig;
    }

    public int getPortConfig() {
        return serverConfig.port;
    }

    public int getLoginTimerConfig() {
        return serverConfig.loginTimer;
    }

    public int getInputTimerConfig() {
        return serverConfig.inputTimer;
    }

    public int getSkullNumberConfig() {
        return serverConfig.skullNumber;
    }

    public boolean saveOnSameDirectory(Backup backup, String fileName){

        String jarPath = "";
        System.out.println("Writing data...");
        try {
            jarPath = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            System.out.println(jarPath);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        // construct a File within the same folder of this jar, or of this class.
        String dirPath = jarPath.substring(0, jarPath.lastIndexOf("/")) + File.separator + "backups";
        File dir = new File(dirPath);
        if (!dir.exists()){
            dir.mkdir();
        }
        String completePath = dirPath + File.separator + fileName + BACKUP_EXTENSION;
        File file = new File(completePath);
        try {
            FileWriter fw = new FileWriter(file);
            gson.toJson(backup, fw);
            fw.close();
            return true;
        } catch (IOException e){
            System.out.println("Cannot write backup file");
        }
        System.out.println("backup file written [OK]");

        return false;
    }



    private void parseServerConfig(){

        try {
            InputStream url= ServerMain.class.getClassLoader().getResourceAsStream("serverConfig.json");
            Scanner sc= new Scanner(url);

            Gson gson= new Gson();
            serverConfig= gson.fromJson(sc.nextLine(), ServerConfig.class);
        }
        catch (NullPointerException e){
            System.out.println("Configuration file not found");
        }
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

    public Layout getLayout(){
        return layouts[getLayoutConfig()];
    }

    private void parseBackup(){
        try{
            String jarPath = URLDecoder.decode(Backup.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            String filePath = jarPath.substring(0, jarPath.lastIndexOf("/")) + File.separator + "backups" + File.separator + BACKUP_NAME + ".json";
            File file= new File(filePath);

            if(file.exists()) {

                InputStream url = new FileInputStream(filePath);
                Scanner sc = new Scanner(url);
                backup = gson.fromJson(sc.nextLine(), Backup.class);
                sc.close();
                url.close();
            } else {
                backup= null;
            }
        }
        catch (UnsupportedEncodingException e){
            backup= null;
            System.out.println("Unsupported encoding exception while parsing currBackup");
        }
        catch (IOException e){
            backup= null;
            System.out.println("File not found or error while closing stream");
        }
        catch (JsonSyntaxException e){
            backup= null;
            System.out.println("Backup file is non correctly written");
        }
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


    private static class ServerConfig{
        private int port;
        private int loginTimer;
        private int inputTimer;
        private int skullNumber;
        private int layoutConfig;
    }

}
