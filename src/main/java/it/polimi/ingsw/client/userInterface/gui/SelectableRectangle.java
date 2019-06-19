package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.SquareView;
import it.polimi.ingsw.client.WrongSelectionException;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class SelectableRectangle extends Rectangle {
    private SquareView squareView;

    public SelectableRectangle(double v, double v1, SquareView squareView ){
        super(v, v1, Color.TRANSPARENT);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStroke(Color.RED);
        this.setVisible(false);
        this.squareView = squareView;

        this.setOnMouseClicked((MouseEvent t) -> {
            try{
                Gui.getClient().selected(squareView.toString());
                BoardGui.setUnvisibleSelectableRectangle();
            } catch(WrongSelectionException e){
                System.out.println("Wrong selection!");
            }
        });
    }

    public boolean equals(SquareView squareView){
        if(this.squareView == squareView){
            return true;
        }
        return false;
    }
}
