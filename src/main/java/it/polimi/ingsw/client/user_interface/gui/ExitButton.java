package it.polimi.ingsw.client.user_interface.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ExitButton extends ImageView {
    private static final double WIDTH= Gui.getScreenBounds().getWidth() / 32;
    public ExitButton(){
        super();
        setImage(new Image(getClass().getClassLoader().getResourceAsStream("resources/exit_icon.png")));
        setPreserveRatio(true);
        setFitWidth(WIDTH);
        this.setOnMouseClicked(mouseEvent -> Gui.shutDown());
    }
}
