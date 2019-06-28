package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.SquareView;
import it.polimi.ingsw.client.WrongSelectionException;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * It represents a square of the board as a rectangle
 */
public class SquareRectangle extends Rectangle{
    /**
     *It's the squareView associated to this SquareRectangle
     */
    private SquareView squareView;

    /**
     * Creates a new SquareButton. Pressing on it, it will send this square's coordinates to the server and
     * set to invisible each SquareButton
     * @param width The width of this square
     * @param height the height if this square
     * @param squareView The squareView associated to this SquareRectangle
     */
    public SquareRectangle(double width, double height, SquareView squareView ){
        super(width, height, Color.TRANSPARENT);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStroke(Color.RED);
        this.setVisible(false);
        this.squareView = squareView;

        this.setOnMouseClicked((MouseEvent t) -> {
            try{
                Gui.getClient().selected(squareView.toString());
                BoardGui.setUnvisibleSquareRectangle();
            } catch(WrongSelectionException e){
                System.out.println("Wrong selection!");
            }
        });
    }

    /**
     * If the squareview is the same istance of this squareview, return true, otherwise false
     * @param squareView The squareView to compare
     * @return boolean
     */
    public boolean equalsSquareView(SquareView squareView){
        if(this.squareView == squareView){
            return true;
        }
        return false;
    }
}
