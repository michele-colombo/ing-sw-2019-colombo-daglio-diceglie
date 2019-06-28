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

/**
 * It's represent an ammo on the board
 */
public class AmmoButton extends Parent {
    private static final String AMMOTILES_FOLDER= "resources/ammo/";

    /**
     * It's the scale ratio of the card associated to this card
     */
    private static final double SCALE_RATIO = 25;

    /**
     * The image of this AmmoButton
     */
    private final ImageView ammoImageView;
    /**
     * It's the width of this AmmoButton
     */
    private double width;

    /**
     * Creates an AmmoButton. Entering the mouse, it will be zoomed and will have a dropshadow. Exiting, it will return
     * to its normal state
     * @param squareView The SquareView associated to this AmmoButton
     */
    public AmmoButton(SquareView squareView){
        InputStream ammoUrl = getClass().getClassLoader().getResourceAsStream(AMMOTILES_FOLDER + "ammo" + squareView.getAmmo().getAmmoTileID() + ".png");
        Image ammoImage = new Image(ammoUrl);
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
