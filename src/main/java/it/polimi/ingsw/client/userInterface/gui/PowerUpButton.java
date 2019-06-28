package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.PowerUpView;
import it.polimi.ingsw.client.WrongSelectionException;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.InputStream;

/**
 * It represents a power up card
 */
public class PowerUpButton extends Parent{
    /**
     * The image of this PowerUpButton
     */
    private final ImageView powerUpImageView;
    /**
     * It's the width of this power up
     */
    private double width;
    /**
     * It's the height of this power up
     */
    private double height;

    private static final String POWERUP_IMAGES_FOLDER= "resources/powerUp/";


    /**
     * Creates a PowerUpButton. Pressing on it, it will send this powerUpView's name to the server
     * @param powerUpView The powerUpView associated to this PowerUpButton
     */
    public PowerUpButton(PowerUpView powerUpView){
        InputStream powerUpUrl = getClass().getClassLoader().getResourceAsStream(POWERUP_IMAGES_FOLDER + powerUpView.getName() + " " + powerUpView.getColor().toString().toLowerCase() + ".png");
        Image powerUpImage = new Image(powerUpUrl);
        this.powerUpImageView = new ImageView(powerUpImage);
        this.width = powerUpImageView.boundsInParentProperty().get().getWidth() / 1.5;
        this.height = powerUpImageView.boundsInParentProperty().get().getHeight() / 1.5;
        this.powerUpImageView.setFitWidth(width);
        this.powerUpImageView.setFitHeight(height);
        this.getChildren().add(powerUpImageView);

        this.powerUpImageView.setOnMouseEntered((MouseEvent t) -> {
            powerUpImageView.setFitWidth(width * 1.2);
            powerUpImageView.setFitHeight(height * 1.2);
        });

        this.setOnMouseExited((MouseEvent t) -> {
            powerUpImageView.setFitWidth(width);
            powerUpImageView.setFitHeight(height);
        });

        this.setOnMouseClicked((MouseEvent t) -> {
            try{
                Gui.getClient().selected(powerUpView.toString());
            } catch(WrongSelectionException e){
                System.out.println("Wrong selection!");
            }
        });
    }
}
