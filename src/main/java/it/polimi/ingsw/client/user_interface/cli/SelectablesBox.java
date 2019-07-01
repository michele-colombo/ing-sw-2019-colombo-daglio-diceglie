package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;

import static it.polimi.ingsw.client.user_interface.cli.CliUtils.*;

public class SelectablesBox extends MiniBox {
    private static final int boxHeight = 3;
    Cli cli;
    int nextBoxX;
    int nextBoxY;

    public SelectablesBox(int x, int y, int height, int width, Cli cli) {
        super(x, y, height, width);
        this.cli = cli;
        reset();
    }

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
