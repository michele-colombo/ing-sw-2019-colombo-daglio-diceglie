package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.SquareView;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.InputStream;

public class AmmoButton extends Parent {
    private final Image ammoImage;
    private final ImageView ammoImageView;
    private double width;
    private double height;


    public AmmoButton(SquareView squareView){
        InputStream ammoUrl = getClass().getClassLoader().getResourceAsStream("ammo/ammo" + squareView.getAmmo().getAmmoTileID() + ".png");
        this.ammoImage = new Image(ammoUrl);
        this.ammoImageView = new ImageView(ammoImage);
        this.width = ammoImageView.boundsInParentProperty().get().getWidth() / 4.5;
        this.height = ammoImageView.boundsInParentProperty().get().getHeight() / 4.5;
        this.ammoImageView.setFitWidth(width);
        this.ammoImageView.setFitHeight(height);
        this.getChildren().add(ammoImageView);

        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ammoImageView.setFitWidth(width * 1.8);
                ammoImageView.setFitHeight(height * 1.8);
                ammoImageView.setEffect(new DropShadow(20, Color.RED));
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ammoImageView.setFitWidth(width);
                ammoImageView.setFitHeight(height);
                ammoImageView.setEffect(null);
            }
        });
    }
}
