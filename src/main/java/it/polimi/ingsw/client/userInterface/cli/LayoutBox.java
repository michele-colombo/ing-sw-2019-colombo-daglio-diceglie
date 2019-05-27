package it.polimi.ingsw.client.userInterface.cli;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;
import it.polimi.ingsw.client.SquareView;

import static it.polimi.ingsw.client.userInterface.cli.CliUtils.*;
import static it.polimi.ingsw.server.model.enums.Border.*;

public class LayoutBox extends MiniBox {
    private int squareHeight;
    private int squareWidth;

    public LayoutBox(int x, int y, int height, int width, int squareHeight, int squareWidth) {
        super(x, y, height, width);
        this.squareHeight = squareHeight;
        this.squareWidth = squareWidth;
    }

    @Override
    public void update(MatchView match) {
        for (int i=0; i<3; i++){
            this.insertText(String.valueOf(2-i), 0, squareHeight/2 + i*squareHeight+1);
            this.insertText(String.valueOf(2-i), 4*squareWidth+1,squareHeight/2 + i*squareHeight+1);
        }
        for (int j=0; j<4; j++){
            this.insertText(String.valueOf(j), 1+squareWidth/2 + j*squareWidth, 0);
            this.insertText(String.valueOf(j), 1+squareWidth/2 + j*squareWidth, 3*squareHeight+1);
        }
        for (SquareView square : match.getLayout().getSquares()){
            this.insertSubBox(createSquare(square, match), 1+(square.getX())*squareWidth, 1+(2-square.getY())*squareHeight);
        }
    }

    private String[][] createSquare(SquareView square, MatchView match){
        String[][] result = new String[squareHeight][squareWidth];
        String strColor = printColorOf(square.getColor());
        for (int i=0; i<squareHeight; i++){
            for (int j=0; j<squareWidth; j++){
                result[i][j] = " ";
            }
        }

        if (square.getNorth() == OPEN){
            if (square.getWest() == OPEN){
                result[0][0] = strColor+C_DOT+DEFAULT_COLOR;
            } else {
                result[0][0] = strColor+VERT+DEFAULT_COLOR;
            }
        } else {
            if (square.getWest() == OPEN){
                result[0][0] = strColor+HOR+DEFAULT_COLOR;
            } else {
                result[0][0] = strColor+DIAG_CORNER_TL+DEFAULT_COLOR;
            }
        }

        if (square.getNorth() == OPEN){
            if (square.getEast() == OPEN){
                result[0][squareWidth-1] = strColor+C_DOT+DEFAULT_COLOR;
            } else {
                result[0][squareWidth-1] = strColor+VERT+DEFAULT_COLOR;
            }
        } else {
            if (square.getEast() == OPEN){
                result[0][squareWidth-1] = strColor+HOR+DEFAULT_COLOR;
            } else {
                result[0][squareWidth-1] = strColor+DIAG_CORNER_TR+DEFAULT_COLOR;
            }
        }

        if (square.getSouth() == OPEN){
            if (square.getEast() == OPEN){
                result[squareHeight-1][squareWidth-1] = strColor+C_DOT+DEFAULT_COLOR;
            } else {
                result[squareHeight-1][squareWidth-1] = strColor+VERT+DEFAULT_COLOR;
            }
        } else {
            if (square.getEast() == OPEN){
                result[squareHeight-1][squareWidth-1] = strColor+HOR+DEFAULT_COLOR;
            } else {
                result[squareHeight-1][squareWidth-1] = strColor+DIAG_CORNER_BR+DEFAULT_COLOR;
            }
        }

        if (square.getSouth() == OPEN){
            if (square.getWest() == OPEN){
                result[squareHeight-1][0] = strColor+C_DOT+DEFAULT_COLOR;
            } else {
                result[squareHeight-1][0] = strColor+VERT+DEFAULT_COLOR;
            }
        } else {
            if (square.getWest() == OPEN){
                result[squareHeight-1][0] = strColor+HOR+DEFAULT_COLOR;
            } else {
                result[squareHeight-1][0] = strColor+DIAG_CORNER_BL+DEFAULT_COLOR;
            }
        }

        int doorVertSide = squareHeight/3;
        int doorVertDim = squareHeight - (2+2*doorVertSide);
        int doorHorSide = squareWidth/3;
        int doorHorDim = squareWidth - (2+2*doorHorSide);

        switch (square.getNorth()){
            case WALL:
                for (int i=1; i<squareWidth-1; i++){
                    result[0][i] = strColor+HOR+DEFAULT_COLOR;
                }
                break;
            case OPEN:
                for (int i=1; i<squareWidth-1; i++){
                    result[0][i] = strColor+C_DOT+DEFAULT_COLOR;
                }
                break;
            case DOOR:
                for (int i=1; i<doorHorSide; i++){
                    result[0][i] = strColor+HOR+DEFAULT_COLOR;
                }
                result[0][doorHorSide] = strColor+DIAG_CORNER_BR+DEFAULT_COLOR;
                for (int i=doorHorSide+1; i<doorHorSide+doorHorDim+1; i++){
                    result[0][i] = strColor+" "+DEFAULT_COLOR;
                }
                result[0][doorHorSide+doorHorDim+1] = strColor+DIAG_CORNER_BL+DEFAULT_COLOR;
                for (int i=doorHorSide+doorHorDim+2; i<squareWidth-1; i++){
                    result[0][i] = strColor+HOR+DEFAULT_COLOR;
                }
                break;
        }

        switch (square.getSouth()){
            case WALL:
                for (int i=1; i<squareWidth-1; i++){
                    result[squareHeight-1][i] = strColor+HOR+DEFAULT_COLOR;
                }
                break;
            case OPEN:
                for (int i=1; i<squareWidth-1; i++){
                    result[squareHeight-1][i] = strColor+C_DOT+DEFAULT_COLOR;
                }
                break;
            case DOOR:
                for (int i=1; i<doorHorSide; i++){
                    result[squareHeight-1][i] = strColor+HOR+DEFAULT_COLOR;
                }
                result[squareHeight-1][doorHorSide] = strColor+DIAG_CORNER_TR+DEFAULT_COLOR;
                for (int i=doorHorSide+1; i<doorHorSide+doorHorDim+1; i++){
                    result[squareHeight-1][i] = strColor+" "+DEFAULT_COLOR;
                }
                result[squareHeight-1][doorHorSide+doorHorDim+1] = strColor+DIAG_CORNER_TL+DEFAULT_COLOR;
                for (int i=doorHorSide+doorHorDim+2; i<squareWidth-1; i++){
                    result[squareHeight-1][i] = strColor+HOR+DEFAULT_COLOR;
                }
                break;
        }

        switch (square.getWest()){
            case WALL:
                for (int i=1; i<squareHeight-1; i++){
                    result[i][0] = strColor+VERT+DEFAULT_COLOR;
                }
                break;
            case OPEN:
                for (int i=1; i<squareHeight-1; i++){
                    result[i][0] = strColor+C_DOT+DEFAULT_COLOR;
                }
                break;
            case DOOR:
                for (int i=1; i<doorVertSide; i++){
                    result[i][0] = strColor+VERT+DEFAULT_COLOR;
                }
                result[doorVertSide][0] = strColor+DIAG_CORNER_BR+DEFAULT_COLOR;
                for (int i=doorVertSide+1; i<doorVertSide+doorVertDim+1; i++){
                    result[i][0] = strColor+" "+DEFAULT_COLOR;
                }
                result[doorVertSide+doorVertDim+1][0] = strColor+DIAG_CORNER_TR+DEFAULT_COLOR;
                for (int i=doorVertSide+doorVertDim+2; i<squareHeight-1; i++){
                    result[i][0] = strColor+VERT+DEFAULT_COLOR;
                }
                break;
        }

        switch (square.getEast()){
            case WALL:
                for (int i=1; i<squareHeight-1; i++){
                    result[i][squareWidth-1] = strColor+VERT+DEFAULT_COLOR;
                }
                break;
            case OPEN:
                for (int i=1; i<squareHeight-1; i++){
                    result[i][squareWidth-1] = strColor+C_DOT+DEFAULT_COLOR;
                }
                break;
            case DOOR:
                for (int i=1; i<doorVertSide; i++){
                    result[i][squareWidth-1] = strColor+VERT+DEFAULT_COLOR;
                }
                result[doorVertSide][squareWidth-1] = strColor+DIAG_CORNER_BL+DEFAULT_COLOR;
                for (int i=doorVertSide+1; i<doorVertSide+doorVertDim+1; i++){
                    result[i][squareWidth-1] = strColor+" "+DEFAULT_COLOR;
                }
                result[doorVertSide+doorVertDim+1][squareWidth-1] = strColor+DIAG_CORNER_TL+DEFAULT_COLOR;
                for (int i=doorVertSide+doorVertDim+2; i<squareHeight-1; i++){
                    result[i][squareWidth-1] = strColor+VERT+DEFAULT_COLOR;
                }
                break;
        }

        String[] temp = new String[3];
        for (int i=0; i<temp.length; i++) {
            temp[i] = " ";
        }
        if (!square.isAmmo()){
            temp = createLineFromString("SPW", printColorOf(square.getColor()));
        } else if (square.getAmmo() != null){
            temp = printAmmoTile(square.getAmmo());
        }
        for (int i=0; i<temp.length && i<squareWidth; i++){
            result[1][1+i] = temp[i];
        }

        String[] playersOn = new String[match.getPlayersOn(square).size()];
        for (int i=0; i<playersOn.length; i++){
            PlayerView player = match.getPlayersOn(square).get(i);
            playersOn[i] = printColorOf(player.getColor())+PLAYER_SYMBOL+DEFAULT_COLOR;
        }
        for (int i=0; i<playersOn.length && i<squareWidth; i++){
            result[squareHeight/2][i+(squareWidth-playersOn.length)/2] = playersOn[i];
        }

        return result;
    }

}
