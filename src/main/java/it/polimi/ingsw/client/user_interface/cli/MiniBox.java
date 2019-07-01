package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.MatchView;

import static it.polimi.ingsw.client.user_interface.cli.CliUtils.*;

public class MiniBox {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected String[][] stringBox;

    public MiniBox(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        stringBox = new String[height][width];
        clear();
    }

    public void clear(){
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                stringBox[i][j] = " ";
            }
        }
    }

    public void update(MatchView match){

    }

    public void insertText(String str, int x, int y, String color){
        if (y<0 || y>=height || x<0) return;
        char[] tempArray =  str.toCharArray();
        for (int i=0; i<tempArray.length && i+x<width; i++){
            stringBox[y][x+i] = color + tempArray[i] +  DEFAULT_COLOR;
        }
    }

    public void insertText(String str, int x, int y){
        insertText(str, x, y, DEFAULT_COLOR);
    }

    public void insertCenteredText(String text, int width, int y, String color){
        insertText(text, (width-text.length())/2, y, color);
    }

    public void insertCenteredText(String text, int width, int y){
        insertText(text, (width-text.length())/2, y, DEFAULT_COLOR);
    }

    public void insertSubBox(String[][] matrix, int x, int y){
        if (y<0 || x<0) return;
        for(int i=0; i<matrix.length && i+y<height; i++){
            for (int j=0; j<matrix[0].length && j+x<width; j++){
                stringBox[i+y][j+x] = matrix[i][j];
            }
        }
    }

    public void insertLine(String[] line, int x, int y){
        if (y<0 || y>=height) return;
        if (x<0) return;
        for (int j=0; j<line.length && j+x<width; j++){
            stringBox[y][j+x] = line[j];
        }
    }

    public void insertTopRule(int y, String color){
        char[] rule = new char[width];
        rule[0] = CORNER_TL.toCharArray()[0];
        char dash = HOR.toCharArray()[0];
        for (int j=1; j<width - 1;  j++){
            rule[j] = dash;
        }
        rule[rule.length-1] = CORNER_TR.toCharArray()[0];
        insertText(new String(rule), 0, y, color);
    }

    public void insertMiddleRule(int y, String color){
        char[] rule = new char[width];
        rule[0] = "\u251c".toCharArray()[0];
        char dash = HOR.toCharArray()[0];
        for (int j=1; j<width - 1;  j++){
            rule[j] = dash;
        }
        rule[rule.length-1] = "\u2524".toCharArray()[0];
        insertText(new String(rule), 0, y, color);
    }

    public void insertBottomRule(int y, String color){
        char[] rule = new char[width];
        rule[0] = CORNER_BL.toCharArray()[0];
        char dash = HOR.toCharArray()[0];
        for (int j=1; j<width - 1;  j++){
            rule[j] = dash;
        }
        rule[rule.length-1] = CORNER_BR.toCharArray()[0];
        insertText(new String(rule), 0, y, color);
    }

    public int insertTextMultiline(String text, int x, int y, int width){
        if (x<0 || x>=this.width) return 0;
        if (y<0 || y>=this.height) return 0;
        if (x+width > this.width){
            width = this.width-x;
        }
        String[] words = text.split(" ");
        String line = "";
        int r=0;
        for (int i=0; i<words.length; i++){
            words[i] = words[i].trim();
            if (line.length()+words[i].length() <= width){
                line = line + words[i];
                if (line.length() < width){
                    line = line + " ";
                }
            } else {
                insertText(line, x, y+r);
                r++;
                line = words[i]+" ";
            }
        }
        insertText(line, x, y+r);
        return r+1;
    }

    //TEST-ONLY
    public void printStringBox(){
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                System.out.print(stringBox[i][j]);
            }
            System.out.println();
        }
    }

    public String[][] getStringBox() {
        return stringBox;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
