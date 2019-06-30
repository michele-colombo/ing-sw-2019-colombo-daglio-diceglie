package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.WeaponView;
import it.polimi.ingsw.client.ClientExceptions.WrongSelectionException;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * It represent a weapon
 */
public class WeaponButton extends Parent {

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
     * Rescale ratio of shadow effect
     */
    private static final double SCALE_RATIO_EFFECT = 3;
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

        this.weaponImageView = new ImageView(Gui.getCacheImage().getWeaponButtonImage(weaponView.getName()));
        this.weaponView = weaponView;
        this.width = weaponImageView.boundsInParentProperty().get().getWidth();
        this.weaponImageView.setPreserveRatio(true);
        this.getChildren().add(weaponImageView);

        this.setOnMouseEntered((MouseEvent t) -> {
            if(!inHand){
                weaponImageView.setEffect(new DropShadow(width / SCALE_RATIO_EFFECT, Color.WHITE));
            }
        });

        this.setOnMouseExited((MouseEvent t) ->
            weaponImageView.setEffect(null)
        );

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
