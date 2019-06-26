package it.polimi.ingsw.client.userInterface.gui;

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
    private static final double SCALE_RATIO_ON_BOARD = 11.09;
    private static final double SCALE_RATIO_UNLOADED = 11.09;
    private static final double SCALE_RATIO_IN_HAND = 11.09;

    private final Image weaponImage;
    private final ImageView weaponImageView;
    private final WeaponView weaponView;
    private double width;
    private boolean inHand;


    public WeaponButton(WeaponView weaponView, boolean inHand){
        InputStream weaponUrl = getClass().getClassLoader().getResourceAsStream("weapon/" + weaponView.getName() + ".png");
        this.weaponImage = new Image(weaponUrl);
        this.weaponImageView = new ImageView(weaponImage);
        this.weaponView = weaponView;
        this.inHand = inHand;
        this.width = weaponImageView.boundsInParentProperty().get().getWidth();
        this.weaponImageView.setPreserveRatio(true);
        this.getChildren().add(weaponImageView);

        this.setOnMouseEntered((MouseEvent) -> {
            if(!inHand){
                weaponImageView.setEffect(new DropShadow(20, Color.WHITE));
            }
        });

        this.setOnMouseExited((MouseEvent t) -> {
            weaponImageView.setEffect(null);
        });

        this.setOnMouseClicked((MouseEvent t) -> {
            try{
                Gui.getClient().selected(weaponView.getName());
            } catch(WrongSelectionException e){
                System.out.println("Wrong selection!");
            }
        });
    }

    public void disable(){
        this.setDisable(true);
    }

    public void reScale(double scaleRatio){
        weaponImageView.setFitWidth(width / 2);
        weaponImageView.setFitWidth(BoardGui.getWidth() / SCALE_RATIO_ON_BOARD);
        weaponImageView.setFitWidth(scaleRatio);
    }

    public void rescaleOnBoard(){
        weaponImageView.setFitWidth(BoardGui.getWidth() / SCALE_RATIO_ON_BOARD);
    }

    public void rescaleOnHand(){
        weaponImageView.setFitWidth(width / 2.2);
    }

    public void rescaleUnloaded(double width){
        weaponImageView.setFitWidth(width / 7);
    }
}
