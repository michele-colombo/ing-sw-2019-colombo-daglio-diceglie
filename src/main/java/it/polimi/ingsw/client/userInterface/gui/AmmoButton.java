package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.SquareView;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * It's represent an ammo on the board
 */
public class AmmoButton extends Parent {
    /**
     * It's the scale ratio of the card associated to this card
     */
    private static final double SCALE_RATIO = 25;
    /**
     * It's the scale ratio of the shadow effect
     */
    private static final double SCALE_RATIO_SHADOW = 30;
    /**
     * The image of this AmmoButton
     */
    private final ImageView ammoImageView;


    /**
     * Creates an AmmoButton. Entering the mouse, it will be zoomed and will have a dropshadow. Exiting, it will return
     * to its normal state
     * @param squareView The SquareView associated to this AmmoButton
     */
    public AmmoButton(SquareView squareView){
        this.ammoImageView = new ImageView(Gui.getCacheImage().getAmmoButtonImage(squareView.getAmmo().getAmmoTileID()));
        this.ammoImageView.setFitWidth(BoardGui.getWidth() / SCALE_RATIO);
        this.ammoImageView.setPreserveRatio(true);
        this.getChildren().add(ammoImageView);

        this.setOnMouseEntered((MouseEvent t) ->
            ammoImageView.setEffect(new DropShadow(BoardGui.getWidth() / SCALE_RATIO_SHADOW, Color.RED))
        );

        this.setOnMouseExited((MouseEvent t) ->
            ammoImageView.setEffect(null)
        );
    }
}
