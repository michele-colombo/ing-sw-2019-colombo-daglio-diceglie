package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.model_view.*;
import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;

import static it.polimi.ingsw.client.user_interface.cli.CliUtils.*;

/**
 * The MiniBox containing the choices available for the user
 */
public class SelectablesBox extends MiniBox {
    /**
     * the height of the choice boxes
     */
    private static final int boxHeight = 3;
    /**
     * the reference to cli
     */
    Cli cli;
    /**
     * Support variable to store the x coordinate of the next choice box
     */
    int nextBoxX;
    /**
     * Support variable to store the y coordinate of the next choice box
     */
    int nextBoxY;

    /**
     * Builds the box specifying all its parameters (except its content)
     * @param x the x position int the window (from the left side)
     * @param y the y position in the window (from the top)
     * @param height the height of this box
     * @param width the width of this box
     * @param cli teh reference to the cli
     */
    public SelectablesBox(int x, int y, int height, int width, Cli cli) {
        super(x, y, height, width);
        this.cli = cli;
        reset();
    }

    /**
     * Updates thebox adding all the possible choices for the user (sent from server)
     * @param match the match from which retrieve information
     */
    @Override
    public void update(MatchView match) {
        reset();
        MyPlayer me = match.getMyPlayer();
        for (WeaponView w : me.getSelectableWeapons()){
            insertSelectableBox(w.getName(), w.getName().toUpperCase(), DEFAULT_COLOR, null);
        }
        for (SquareView s : me.getSelectableSquares()){
            insertSelectableBox(s.toString(), s.getX()+", "+s.getY(), DEFAULT_COLOR, null);
        }
        for (ModeView m : me.getSelectableModes()){
            insertSelectableBox(m.getTitle(), m.getTitle(), DEFAULT_COLOR, printCash(m.getCost()));
        }
        for (Command c : me.getSelectableCommands()){
            insertSelectableBox(c.toString(), c.toString(), (c==Command.OK)? OK_COLOR:WRONG_COLOR, null);
        }
        for (String action : me.getSelectableActions()){
            insertSelectableBox(action, action, DEFAULT_COLOR, null);
        }
        for (AmmoColor color : me.getSelectableColors()){
            insertSelectableBox(color.toString(), null, DEFAULT_COLOR, printCash(new Cash(color, 1)));
        }
        for (PlayerView p : me.getSelectablePlayers()){
            insertSelectableBox(p.getName(), p.getName(), printColorOf(p.getColor()), null);
        }
        for (PowerUpView p : me.getSelectablePowerUps()){
            insertSelectableBox(p.toString(), p.getName().toUpperCase(), printColorOf(p.getColor()), null);
        }
    }

    /**
     * Builds and add the choice box to the list of selectables
     * @param id the ID (unique string identifier) of the selectable
     * @param prettyName the text to dislay to the user
     * @param color the color of the text
     * @param line the Array of Strings to append at the end of text
     */
    private void insertSelectableBox(String id, String prettyName, String color, String[] line){
        cli.addSelectableId(id);
        int index = cli.indexOf(id);
        String[][] sBox = createSelectableBox(index, prettyName, color, line);
        if (nextBoxX+sBox[0].length <= width){
            insertSubBox(sBox, nextBoxX, nextBoxY *boxHeight);
            nextBoxX += sBox[0].length;
        } else {
            nextBoxY++;
            nextBoxX = 0;
            insertSubBox(sBox, nextBoxX, nextBoxY *boxHeight);
        }

    }

    /**
     * Builds the graphical representation of the choice box
     * @param index the number corresponding to the selection
     * @param str the text to display to the user
     * @param color the color of the text
     * @param line the Array of Strings to append at the end of text
     * @return A matrix of Strings (escape color - char - escape color)
     */
    private String[][] createSelectableBox(int index, String str, String color, String[] line){
        int boxWidth;
        char[] indexArray = String.valueOf(index).toCharArray();
        boxWidth = 3+indexArray.length;
        if (str != null){
            boxWidth += str.length();
        }
        if (line != null){
            boxWidth += line.length;
        }
        String[][] result = prepareBorder(boxHeight, boxWidth);
        for (int i=0; i<indexArray.length; i++){
            result[1][1+i] = DEFAULT_COLOR+indexArray[i]+DEFAULT_COLOR;
        }
        result[0][1+indexArray.length] = DEFAULT_COLOR+"\u252c"+DEFAULT_COLOR;
        result[1][1+indexArray.length] = DEFAULT_COLOR+VERT+DEFAULT_COLOR;
        result[2][1+indexArray.length] = DEFAULT_COLOR+"\u2534"+DEFAULT_COLOR;
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                result[1][2+indexArray.length+ i] = color + str.charAt(i) + DEFAULT_COLOR;
            }
            if (line != null){
                for (int i = 0; i < line.length; i++) {
                    result[1][2+indexArray.length + str.length() + i] = line[i];
                }
            }
        } else if (line != null){
            for (int i = 0; i < line.length; i++) {
                result[1][2+indexArray.length + i] = line[i];
            }
        }
        return result;
    }

    /**
     * Resets the box
     */
    private void reset(){
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                stringBox[i][j] = " ";
            }
        }
        nextBoxX = 0;
        nextBoxY = 0;
    }
}
