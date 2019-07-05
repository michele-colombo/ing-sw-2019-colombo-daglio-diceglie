package it.polimi.ingsw.client.user_interface.cli;

import java.util.ArrayList;
import java.util.List;

/**
 * Virtual window containing some content to display on the terminal
 */
public class Window {
    /**
     * the width of the window
     */
    protected int width;
    /**
     * the height of the window
     */
    protected int height;
    /**
     * the actual content (matrix of Strings) of the window
     */
    protected String[][] stringBox;
    /**
     * the list of miniBoxes contained in the window
     */
    protected List<MiniBox> miniBoxes;

    /**
     * Builds a window
     * @param width the width of the playing window
     * @param height the height of the playing window
     */
    public Window(int width, int height) {
        this.width = width;
        this.height = height;
        stringBox = new String[height][width];
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                stringBox[i][j] = " ";
            }
        }
        this.miniBoxes = new ArrayList<>();
    }

    /**
     * Prints the content of the window on the terminal
     */
    public void show(){
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                System.out.print(stringBox[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Inserts all the boxes in the content of the window
     */
    protected void build(){
        for (MiniBox mb : miniBoxes){
            insertSubBox(mb.getStringBox(), mb.getX(), mb.getY());
        }
    }

    /**
     * Inserts a sub-box (matrix of Strings) in the window
     * @param matrix the sub-box
     * @param x the desired x position of the sub-box
     * @param y the desired y position of the sub-box
     */
    private void insertSubBox(String[][] matrix, int x, int y){
        for(int i=0; i<matrix.length && i+y<height; i++){
            for (int j=0; j<matrix[0].length && j+x<width; j++){
                stringBox[i+y][j+x] = matrix[i][j];
            }
        }
    }

    /**
     * Adds a MiniBox to the list of MiniBoxes
     * @param mb minibox to add
     */
    public void addMiniBox(MiniBox mb){
        miniBoxes.add(mb);
    }

    /**
     * Gets the width of the window
     * @return the integer width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the window
     * @return the integer height
     */
    public int getHeight() {
        return height;
    }
}
