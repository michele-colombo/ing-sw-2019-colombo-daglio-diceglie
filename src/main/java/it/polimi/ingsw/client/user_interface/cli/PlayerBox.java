package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.model_view.MatchView;
import it.polimi.ingsw.client.model_view.PlayerView;
import it.polimi.ingsw.client.model_view.WeaponView;

import java.util.Map;

import static it.polimi.ingsw.client.user_interface.cli.CliUtils.*;
import static it.polimi.ingsw.server.model.enums.PlayerState.IDLE;

/**
 * Contains information, damages and marks of a player
 */
public class PlayerBox extends MiniBox {
    /**
     * the corresponding player
     */
    private PlayerView player;

    /**
     * Builds the box specifying all its parameters (except its content)
     * @param x the x position int the window (from the left side)
     * @param y the y position in the window (from the top)
     * @param height the height of this box
     * @param width the width of this box
     * @param player the involved player
     */
    public PlayerBox(int x, int y, int height, int width, PlayerView player) {
        super(x, y, height, width);
        this.player = player;
    }

    /**
     * Updates info, damages and marks of the player
     * @param match the match from which retrieve information
     */
    @Override
    public void update(MatchView match) {
        insertSubBox(prepareBorder(height, width-1, printColorOf(player.getColor())), 1, 0 );
        if (player.getState() != IDLE){
            for (int i=0; i<height; i++){
                stringBox[i][0] = printColorOf(player.getColor())+FULL_BLOCK+DEFAULT_COLOR;
            }
        } else {
            for (int i=0; i<height; i++){
                stringBox[i][0] = printColorOf(player.getColor())+" "+DEFAULT_COLOR;
            }
        }
        String firstLine = player.getName();
        if (match.readConnections().get(player.getName()) != null){
            firstLine = firstLine + ((match.readConnections().get(player.getName())) ? " (ONLINE)" : " (OFFLINE)");
        }
        firstLine = firstLine + " pUps: "+player.getNumPowerUps()+" loaded wpn: "+player.getNumLoadedWeapons();
        insertText(firstLine, 2, 1, printColorOf(player.getColor()));
        insertLine(printCash(player.getWallet()), firstLine.length()+4, 1);
        String unloadedWeapons = "unloaded wpn: ";
        for (WeaponView w : player.getUnloadedWeapons()){
            unloadedWeapons = unloadedWeapons + w.getName().toUpperCase()+", ";
        }
        insertText(unloadedWeapons, 2, 2);
        insertSubBox(createDamageTrack(), 2, 3);
    }

    /**
     * Builds the representation of the damage track of the player
     * @return
     */
    private String[][] createDamageTrack(){
        int myWidth = 54;
        String[][] result = new String[2][myWidth];
        for (int i=0; i<2; i++){
            for (int j=0; j<myWidth; j++){
                result[i][j] = " ";
            }
        }
        char[] damagesArray =  "damages: ".toCharArray();
        for (int i=0; i<damagesArray.length && i<myWidth; i++){
            result[1][i] = DEFAULT_COLOR + damagesArray[i] +  DEFAULT_COLOR;
        }
        int j=0;
        for (PlayerView damager : player.getDamageList()){
            if (damagesArray.length+j+1<myWidth) {
                result[1][damagesArray.length + j] = printColorOf(damager.getColor()) + DROP + DEFAULT_COLOR;
                result[1][damagesArray.length + j + 1] = printColorOf(damager.getColor()) + " " + DEFAULT_COLOR;
                j += 2;
            }
        }
        char[] marksArray =  "marks: ".toCharArray();
        for (int i=0; i<marksArray.length && damagesArray.length+24+i<myWidth; i++){
            result[1][damagesArray.length+24+i] = DEFAULT_COLOR + marksArray[i] +  DEFAULT_COLOR;
        }
        int k=0;
        for (Map.Entry<PlayerView, Integer> entry : player.getMarkMap().entrySet()){
            for (int i=0; i<entry.getValue(); i++){
                if (damagesArray.length+24+marksArray.length+k < myWidth){
                    result[1][damagesArray.length+24+marksArray.length+k] = printColorOf(entry.getKey().getColor())+DROP+DEFAULT_COLOR;
                }
                k++;
            }
        }
        if (!player.isFrenzy()){
            char[] firstLine = "    >>G   >S        @ o ".toCharArray();
            for (int i=0; i<firstLine.length && damagesArray.length+i<myWidth; i++){
                result[0][damagesArray.length+i] = DEFAULT_COLOR + firstLine[i] +  DEFAULT_COLOR;
            }
        }
        return result;
    }
}
