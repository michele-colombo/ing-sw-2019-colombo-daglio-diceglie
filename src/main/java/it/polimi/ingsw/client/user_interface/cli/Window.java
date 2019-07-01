package it.polimi.ingsw.client.user_interface.cli;

import java.util.ArrayList;
import java.util.List;

public class Window {
    protected int width;
    protected int height;
    protected String[][] stringBox;
    protected List<MiniBox> miniBoxes;

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

    public void show(){
        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                System.out.print(stringBox[i][j]);
            }
            System.out.println();
        }
    }

    protected void build(){
        for (MiniBox mb : miniBoxes){
            insertSubBox(mb.getStringBox(), mb.getX(), mb.getY());
        }
    }

    private void insertSubBox(String[][] matrix, int x, int y){
        for(int i=0; i<matrix.length && i+y<height; i++){
            for (int j=0; j<matrix[0].length && j+x<width; j++){
                stringBox[i+y][j+x] = matrix[i][j];
            }
        }
    }

    public void addMiniBox(MiniBox mb){
        miniBoxes.add(mb);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
