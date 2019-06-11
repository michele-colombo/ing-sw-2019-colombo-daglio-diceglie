package it.polimi.ingsw.client.userInterface.gui;

import javafx.scene.shape.Rectangle;

public class SquareButton extends Rectangle {
    int x_coordinate;
    int y_coordinate;

    public SquareButton(double x, double y, double width, double height, int x_coordinate, int y_coordinate){
        super(x, y, width, height);
        this.x_coordinate = x_coordinate;
        this.y_coordinate = y_coordinate;
    }
}
