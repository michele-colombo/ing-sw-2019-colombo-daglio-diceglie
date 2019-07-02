package it.polimi.ingsw.client.user_interface.gui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DisconnectButton extends ImageView {
    private static final double WIDTH= Gui.getScreenBounds().getWidth() / 32;

    public DisconnectButton(){
        super();
        Image image= new Image(getClass().getClassLoader().getResourceAsStream("resources/disconnect_icon.png"));
        setImage(image);
        setPreserveRatio(true);
        setFitWidth(WIDTH);
        this.setOnMouseClicked(mouseEvent -> Gui.restart());
    }
}
