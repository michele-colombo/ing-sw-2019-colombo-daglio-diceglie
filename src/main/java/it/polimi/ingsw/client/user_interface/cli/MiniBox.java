package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.MatchView;

import static it.polimi.ingsw.client.user_interface.cli.CliUtils.*;

/**
 * Is the elementary component of a window, a rectangular matrix of Strings
 */
public class MiniBox {
    /**
     * the x position int the window (from the left side)
     */
    protected int x;

    /**
     * the y position in the window (from the top)
     */
    protected int y;

    /**
     * the width of this box
     */
    protected int width;

    /**
     * the height of this box
     */
    protected int height;

    /**
     * the array of Strings (escape color - char - escape color) with the visual content of the box
     */
    protected String[][] stringBox;

    /**
     * Builds the box specifying all its parameters (except its content)
     * @param x the x position int the window (from the left side)
     * @param y the y position in the window (from the top)
     * @param height the height of this box
     * @param width the width of this box
     */
    public MiniBox(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        stringBox = new String[height][width];
        clear();
    }

    /**
     * Prepares the empty matrix of strings
     */
    public void clear(){
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                stringBox[i][j] = " ";
            }
        }
    }

    /**
     * Automatically updates its content.
     * in this 'basic' MiniBox it is empty since has only static content
     * @param match the match from which retrieve information
     */
    public void update(MatchView match){
        //has only static content, no update required
    }

    /**
     * Inserts a text in this MiniBox
     * @param str the text to insert
     * @param x the x position of text from the left side of this MiniBox
     * @param y the y position of text from the top fo this MiniBox
     * @param color the desired color of the text to insert
     */
    public void insertText(String str, int x, int y, String color){
        if (y<0 || y>=height || x<0) return;
        char[] tempArray =  str.toCharArray();
        for (int i=0; i<tempArray.length && i+x<width; i++){
            stringBox[y][x+i] = color + tempArray[i] +  DEFAULT_COLOR;
        }
    }

    /**
     * Inserts a text (with default color) in this MiniBox
     * @param str the text to insert
     * @param x the x position of text from the left side of this MiniBox
     * @param y the y position of text from the top fo this MiniBox
     */
    public void insertText(String str, int x, int y){
        insertText(str, x, y, DEFAULT_COLOR);
    }

    /**
     * Inserts a text horizontally centered in this MiniBox
     * @param text the text to insert
     * @param y the y position of text from the top fo this MiniBox
     * @param width the width of the test to insert
     * @param color the desired color of the text to insert
     */
    public void insertCenteredText(String text, int width, int y, String color){
        insertText(text, (width-text.length())/2, y, color);
    }

    /**
     * Inserts a text horizontally centered (with default color) in this MiniBox
     * @param text the text to insert
     * @param y the y position of text from the top fo this MiniBox
     * @param width the width of the test to insert
     */
    public void insertCenteredText(String text, int width, int y){
        insertText(text, (width-text.length())/2, y, DEFAULT_COLOR);
    }

    /**
     * Inserts a sub-box (matrix of Strings) in this MiniBox
     * @param matrix the matrix of Strings (escape color - char - escape color) to insert
     * @param x the x position of sub-box from the left side of this MiniBox
     * @param y the y position of sub-box from the top of this MiniBox
     */
    public void insertSubBox(String[][] matrix, int x, int y){
        if (y<0 || x<0) return;
        for(int i=0; i<matrix.length && i+y<height; i++){
            for (int j=0; j<matrix[0].length && j+x<width; j++){
                stringBox[i+y][j+x] = matrix[i][j];
            }
        }
    }

    /**
     * Inserts a line (array of Strings) in this MiniBox
     * @param line the array of Strings (escape color - char - escape color) to insert
     * @param x the x position of sub-box from the left side of this MiniBox
     * @param y the y position of sub-box from the top of this MiniBox
     */
    public void insertLine(String[] line, int x, int y){
        if (y<0 || y>=height) return;
        if (x<0) return;
        for (int j=0; j<line.length && j+x<width; j++){
            stringBox[y][j+x] = line[j];
        }
    }

    /**
     * Inserts the top rule of a table
     * @param y the y position from the top
     * @param color the color of the rule
     */
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

    /**
     * Inserts a middle rule of a table
     * @param y the y position from the top
     * @param color the color of the rule
     */
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

    /**
     * Inserts the bottom rule of a table
     * @param y the y position from the top fo this MiniBox
     * @param color the color of the rule
     */
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

    /**
     * Insertes multiline text on this MiniBox
     * @param text the text to insert
     * @param x the x position of the text, from the left side of this MiniBox
     * @param y the y position of sub-box from the top of this MiniBox
     * @param width the width of the multiline text
     * @return the number of resulting line of the text inserted
     */
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

    /**
     * Gets the content of this MiniBox
     * @return the matrix of Strings (escape color - char - escape color)
     */
    public String[][] getStringBox() {
        return stringBox;
    }

    /**
     * Gets the x position of this MiniBox
     * @return the x integer coordinate from the left side of the window
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y position of this MiniBox
     * @return the y integer coordinate from the top of the window
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the width of thi MiniBox
     * @return the integer width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of this MiniBox
     * @return the integer height
     */
    public int getHeight() {
        return height;
    }
}
