package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.PowerUpView;
import it.polimi.ingsw.client.WrongSelectionException;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.InputStream;

public class PowerUpButton extends Parent {
    private final Image powerUpImage;
    private final ImageView powerUpImageView;
    private final PowerUpView powerUpView;
    private double width;
    private double height;

    private static final String POWERUP_IMAGES_FOLDER= "resources/powerUp/";


    public PowerUpButton(PowerUpView powerUpView){
        InputStream powerUpUrl = getClass().getClassLoader().getResourceAsStream(POWERUP_IMAGES_FOLDER + powerUpView.getName() + " " + powerUpView.getColor().toString().toLowerCase() + ".png");
        this.powerUpImage = new Image(powerUpUrl);
        this.powerUpImageView = new ImageView(powerUpImage);
        this.powerUpView = powerUpView;
        this.width = powerUpImageView.boundsInParentProperty().get().getWidth() / 1.5;
        this.height = powerUpImageView.boundsInParentProperty().get().getHeight() / 1.5;
        this.powerUpImageView.setFitWidth(width);
        this.powerUpImageView.setFitHeight(height);
        this.getChildren().add(powerUpImageView);

        this.powerUpImageView.setOnMouseEntered((MouseEvent t) -> {
            powerUpImageView.setFitWidth(width * 1.2);
            powerUpImageView.setFitHeight(height * 1.2);
            //powerUpImageView.setEffect(new DropShadow(20, Color.BLUE));
        });

        this.setOnMouseExited((MouseEvent t) -> {
            powerUpImageView.setFitWidth(width);
            powerUpImageView.setFitHeight(height);
            //powerUpImageView.setEffect(null);
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
