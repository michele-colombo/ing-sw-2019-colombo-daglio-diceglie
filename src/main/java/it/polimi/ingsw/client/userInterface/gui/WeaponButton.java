package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.PowerUpView;
import it.polimi.ingsw.client.WeaponView;
import it.polimi.ingsw.client.WrongSelectionException;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.InputStream;

public class WeaponButton extends Parent {
    private final Image weaponImage;
    private final ImageView weaponImageView;
    private final WeaponView weaponView;
    private double width;
    private double height;


    public WeaponButton(WeaponView weaponView){
        InputStream weaponUrl = getClass().getClassLoader().getResourceAsStream("weapon/" + weaponView.getName() + ".png");
        this.weaponImage = new Image(weaponUrl);
        this.weaponImageView = new ImageView(weaponImage);
        this.weaponView = weaponView;
        this.width = weaponImageView.boundsInParentProperty().get().getWidth() / 3.5;
        //this.height = weaponImageView.boundsInParentProperty().get().getHeight() / 1.5;
        this.weaponImageView.setFitWidth(width);
        //this.weaponImageView.setFitHeight(height);
        this.weaponImageView.setPreserveRatio(true);
        this.getChildren().add(weaponImageView);

        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //weaponImageView.setFitWidth(width * 1.2);
                //weaponImageView.setFitHeight(height * 1.2);
                weaponImageView.setEffect(new DropShadow(20, Color.WHITE));
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //weaponImageView.setFitWidth(width);
                //weaponImageView.setFitHeight(height);
                weaponImageView.setEffect(null);
            }
        });

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    Gui.getClient().selected(weaponView.toString());
                } catch(WrongSelectionException e){
                    System.out.println("Wrong selection!");
                }
            }
        });
    }

    public void disable(){
        this.setDisable(true);
    }
}
