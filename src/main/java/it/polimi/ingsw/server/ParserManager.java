package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.server.model.*;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ParserManager {
    private static final String SAVING_BACKUP = "Saving backup to file";
    private static final String CANNOT_WRITE_BACKUP_FILE = "Cannot write backup file";
    private static final String BACKUP_FILE_WRITTEN = "backup file written";
    private static final String UNSUPPORTED_ENCODING_PARSING_BACKUP = "Unsupported encoding exception while parsing currBackup";
    private static final String IO_EXCEPTION_FILE = "File not found or error while closing stream";
    private static final String BACKUP_SYNTAX_ERROR = "Backup file is not correctly written";
    private static final String PROBLEM_CLOSING_INPUT_STREAM = "problem while closing inputStream";
    private static final String NO_FILE_DETECTED_FOR_STACKS = "no file detected for stacks";
    private static final String SERVER_CONFIG_FILE= "resources/serverConfig.json";
    private static final String LAYOUT_CONFIG_FOLDER= "resources/layoutConfig/";
    private static final String WEAPONS_FILE= "resources/weapons.json";
    private static final String POWERUPS_FILE= "resources/powerUps.json";
    private static final String AMMOTILES_FILE= "resources/ammoTiles.json";

    private static final String BACKUP_NAME = "currentBackup";
    private static final String BACKUP_EXTENSION = ".json";

    private static final Logger logger = Logger.getLogger(ParserManager.class.getName());
    public static final String BACKUPS_FOLDER = "backups";

    private Layout[] layouts;
    private StackManager stackManager;
    private Backup backup;


    private Gson gson;


    /**
     * builds the parser manager and parse all
     */
    public ParserManager(){
        gson= new Gson();
        layouts= new Layout[4];
        for(int i=0; i<4; i++){
            parseLayout(i);
        }
        parseStack();

        parseBackup();
    }

    /**
     * get te backup
     * @return the backup or null if it does not exists
     */
    public Backup getBackup(){
        return backup;
    }

    /**
     *
     * @return stack manager (powerups, weapons and ammotiles)
     */
    public StackManager getStackManager() {
        return stackManager;
    }

    /**
     * save a backup file in the same directory of the generated jar
     * @param backup backup to json-fy
     * @param fileName nome for the file
     * @return true if everything goes right
     */
    public boolean saveOnSameDirectory(Backup backup, String fileName){

        String jarPath = "";
        logger.info(SAVING_BACKUP);
        try {
            jarPath = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            logger.info("backup path:" + jarPath);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        // construct a File within the same folder of this jar, or of this class.
        String dirPath = jarPath.substring(0, jarPath.lastIndexOf("/")) + File.separator + BACKUPS_FOLDER;
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
            logger.info(BACKUP_FILE_WRITTEN);
            return true;
        } catch (IOException e){
            logger.warning(CANNOT_WRITE_BACKUP_FILE);
        }

        return false;
    }

    /**
     * layout configuration ffrom the manual are:
     * -0 == small "good for 4 or 5 players"
     * -1 == big one drawn on 2 whole pages
     * -2 == small "good for every number of players"
     * -3 == small "good for 3 or 4 players
     *
     * @param configNumber configuration code
     */
    public Layout getLayout(int configNumber){
        if(configNumber>=0 && configNumber<=3) {
            return layouts[configNumber];
        }
        return null;
    }

    /*
    generate a backup fron the json
     */
    private void parseBackup(){
        try{
            String jarPath = URLDecoder.decode(Backup.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            String filePath = jarPath.substring(0, jarPath.lastIndexOf("/")) + File.separator + BACKUPS_FOLDER + File.separator + BACKUP_NAME + ".json";
            File file= new File(filePath);

            if(file.exists()) {

                try(InputStream url = new FileInputStream(filePath);
                    Scanner sc = new Scanner(url)) {
                    backup = gson.fromJson(sc.nextLine(), Backup.class);
                }
            } else {
                backup= null;
            }
        }
        catch (UnsupportedEncodingException e){
            backup= null;
            logger.warning(UNSUPPORTED_ENCODING_PARSING_BACKUP);
        }
        catch (IOException e){
            backup= null;
            logger.warning(IO_EXCEPTION_FILE);
        }
        catch (JsonSyntaxException e){
            backup= null;
            logger.warning(BACKUP_SYNTAX_ERROR);
        }
    }

    /**
     * generate a layaout from json file
     * @param num configuration code of the layout
     */
    private void parseLayout(int num){
        List<AmmoSquare> ammoSquares= new ArrayList<>();
        List<SpawnSquare> spawnSquares= new ArrayList<>();

        InputStream url = getClass().getClassLoader().getResourceAsStream(LAYOUT_CONFIG_FOLDER + "layoutConfig"+num+".json");
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

    /**
     * generate a stackmanager from json files
     */
    private void parseStack(){
        List<Weapon> weapons;
        List<PowerUp> powerUps;
        List<AmmoTile> ammoTiles;

        InputStream url;
        Scanner sc;

        try {

            url = getClass().getClassLoader().getResourceAsStream(WEAPONS_FILE);
            sc = new Scanner(url);
            weapons = Arrays.asList(gson.fromJson(sc.nextLine(), Weapon[].class));
            sc.close();
            url.close();


            url = getClass().getClassLoader().getResourceAsStream(POWERUPS_FILE);
            sc = new Scanner(url);
            powerUps = Arrays.asList(gson.fromJson(sc.nextLine(), PowerUp[].class));
            sc.close();
            url.close();

            url= getClass().getClassLoader().getResourceAsStream(AMMOTILES_FILE);
            sc= new Scanner(url);
            ammoTiles= Arrays.asList(gson.fromJson(sc.nextLine(), AmmoTile[].class));
            sc.close();
            url.close();

            stackManager= new StackManager(weapons, powerUps, ammoTiles);
        }
        catch (IOException e){
            logger.warning(PROBLEM_CLOSING_INPUT_STREAM);
        }
        catch (NullPointerException e){
            logger.warning(NO_FILE_DETECTED_FOR_STACKS);
        }


    }

}
