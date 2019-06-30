package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.PowerUpView;
import it.polimi.ingsw.client.ClientExceptions.WrongSelectionException;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * It represents a power up card
 */
public class PowerUpButton extends Parent{
    /**
     * String used to properly load image
     */
    private static final String FILE_IMAGE_SPACE = " ";
    /**
     * Ratio to properly rescale image
     */
    private static final double SCALE_IMAGE = 6;


    /**
     * Creates a PowerUpButton. Pressing on it, it will send this powerUpView's name to the server
     * @param powerUpView The powerUpView associated to this PowerUpButton
     */
    public PowerUpButton(PowerUpView powerUpView){
        ImageView powerUpImageView = new ImageView(Gui.getCacheImage().getPowerUpButtonImage(powerUpView.getName() + FILE_IMAGE_SPACE + powerUpView.getColor().toString().toLowerCase()));
        powerUpImageView.setFitWidth(BoardGui.getWidth() / SCALE_IMAGE);
        powerUpImageView.setPreserveRatio(true);
        this.getChildren().add(powerUpImageView);

        /*this.powerUpImageView.setOnMouseEntered((MouseEvent t) -> {
            powerUpImageView.setFitWidth(width * 1.2);
            powerUpImageView.setFitHeight(height * 1.2);
        });

        this.setOnMouseExited((MouseEvent t) -> {
            powerUpImageView.setFitWidth(width);
            powerUpImageView.setFitHeight(height);
        });*/

        this.setOnMouseClicked((MouseEvent t) -> {
            try{
                Gui.getClient().selected(powerUpView.toString());
            } catch(WrongSelectionException e){
                System.out.println("Wrong selection!");
            }
        });
    }
}
