package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.WeaponView;
import it.polimi.ingsw.client.WrongSelectionException;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.InputStream;

/**
 * It represent a weapon
 */
public class WeaponButton extends Parent {
    private static final String WEAPON_IMAGE_FOLDER= "resources/weapon/";

    /**
     * Rescale ratio of the image associated to this card when is shown on board
     */
    private static final double SCALE_RATIO_ON_BOARD = 11.09;
    /**
     * Rescale ratio of the image associated to this card when is shown unloaded
     */
    private static final double SCALE_RATIO_UNLOADED = 7;
    /**
     * Rescale ratio of the image associated to this card when is shown in hand
     */
    private static final double SCALE_RATIO_IN_HAND = 2.2;
    /**
     * The image of this WeaponButton
     */
    private final ImageView weaponImageView;
    /**
     * The weaponView associated to this WeaponButton
     */
    private final WeaponView weaponView;
    /**
     * It's the width of this WeaponButton
     */
    private double width;

    /**
     * Creates a new WeaponButton. Pressing on it, it will send this weapon's name to the server.
     * @param weaponView The weaponView associated to this WeaponButton
     * @param inHand If true, entering the mouse will create a DropShadow effect, removed when mouse is exited
     */
    public WeaponButton(WeaponView weaponView, boolean inHand){
        InputStream weaponUrl = getClass().getClassLoader().getResourceAsStream(WEAPON_IMAGE_FOLDER + weaponView.getName() + ".png");
        Image weaponImage = new Image(weaponUrl);
        this.weaponImageView = new ImageView(weaponImage);
        this.weaponView = weaponView;
        this.width = weaponImageView.boundsInParentProperty().get().getWidth();
        this.weaponImageView.setPreserveRatio(true);
        this.getChildren().add(weaponImageView);

        this.setOnMouseEntered((MouseEvent t) -> {
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

    /**
     * Rescale this WeaponButton with SCALE_RATION_ON_BOARD and Screen width
     */
    public void rescaleOnBoard(){
        weaponImageView.setFitWidth(BoardGui.getWidth() / SCALE_RATIO_ON_BOARD);
    }

    /**
     * Rescale this WeaponButton with SCALE_RATION_IN_HAND and the width of the associated image
     */
    public void rescaleOnHand(){
        weaponImageView.setFitWidth(width / SCALE_RATIO_IN_HAND);
    }

    /**
     * Rescale this WeaponButton with SCALE_RATION_UNLOADED and the chosen width
     */
    public void rescaleUnloaded(double width){
        weaponImageView.setFitWidth(width / SCALE_RATIO_UNLOADED);
    }

    /**
     *
     * @return weaponView
     */
    public WeaponView getWeaponView(){
        return weaponView;
    }
}
