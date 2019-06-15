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
    private boolean inHand;


    public WeaponButton(WeaponView weaponView, boolean inHand){
        InputStream weaponUrl = getClass().getClassLoader().getResourceAsStream("weapon/" + weaponView.getName() + ".png");
        this.weaponImage = new Image(weaponUrl);
        this.weaponImageView = new ImageView(weaponImage);
        this.weaponView = weaponView;
        this.inHand = inHand;
        this.width = weaponImageView.boundsInParentProperty().get().getWidth();
        //this.height = weaponImageView.boundsInParentProperty().get().getHeight() / 1.5;
        //this.weaponImageView.setFitHeight(height);
        this.weaponImageView.setPreserveRatio(true);
        this.getChildren().add(weaponImageView);

        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!inHand){
                    weaponImageView.setEffect(new DropShadow(20, Color.WHITE));
                }
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                weaponImageView.setEffect(null);
            }
        });

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    Gui.getClient().selected(weaponView.getName());
                    System.out.println("Sto acquistando " + weaponView.getName());
                } catch(WrongSelectionException e){
                    System.out.println("Wrong selection!");
                }
            }
        });
    }

    public void disable(){
        this.setDisable(true);
    }

    public void reScale(){
        if(inHand){
            weaponImageView.setFitWidth(width / 2);
        } else {
            weaponImageView.setFitWidth(width / 3.5);
        }
    }
}
