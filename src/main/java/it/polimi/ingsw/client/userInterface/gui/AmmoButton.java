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
    private static final String AMMOTILES_FOLDER= "resources/ammo/";

    private static final double SCALE_RATIO = 25;

    private final Image ammoImage;
    private final ImageView ammoImageView;
    private double width;

    public AmmoButton(SquareView squareView){
        InputStream ammoUrl = getClass().getClassLoader().getResourceAsStream(AMMOTILES_FOLDER + "ammo" + squareView.getAmmo().getAmmoTileID() + ".png");
        this.ammoImage = new Image(ammoUrl);
        this.ammoImageView = new ImageView(ammoImage);
        this.width = BoardGui.getWidth() / SCALE_RATIO;
        this.ammoImageView.setFitWidth(width);
        this.ammoImageView.setPreserveRatio(true);
        this.getChildren().add(ammoImageView);

        this.setOnMouseEntered((MouseEvent t) -> {
            ammoImageView.setFitWidth(width * 1.8);
            ammoImageView.setEffect(new DropShadow(20, Color.RED));
        });

        this.setOnMouseExited((MouseEvent t) -> {
            ammoImageView.setFitWidth(width);
            ammoImageView.setEffect(null);
        });
    }
}
