package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.PlayerView;
import it.polimi.ingsw.client.WeaponView;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * It represents a player's damageTrack
 */
public class DamageTrack extends Parent {
    /**
     * The path of the DamageTrack PNG
     */
    private static final String DAMAGE_TRACK_IMAGE_FOLDER= "resources/damageTracks/";

    /**
     * It's the ratio of translation on x axe of the ammoBox
     */
    private static final double TRANSLATE_AMMO_BOX_X = 0.09;

    /**
     * It's the ratio of translation on y axe of the ammoBox
     */
    private static final double TRANSLATE_AMMO_BOX_Y = 0.01;

    /**
     * It's the ratio of translation on x axe of the marksBox
     */
    private static final double TRANSLATE_MARKS_BOX_X = 0.48;

    /**
     * It's the ratio of translation on y axe of the marksBox
     */
    private static final double TRANSLATE_MARKS_BOX_Y = 0.01;

    /**
     * It's the playerView associated to this DamageTrack
     */
    private final PlayerView playerView;

    /**
     * The image of this DamageTrack
     */
    private final ImageView damageTrackImageView;

    /**
     * The ammoBox that shows how many ammos has this player's damageTrack
     */
    private HBox ammoBox;

    /**
     * The ammoBox that shows how many marks has this player's damageTrack
     */
    private HBox markBox;

    /**
     * It's the width of this DamageTrack
     */
    private final double width;

    /**
     * It's the height of this DamageTrack
     */
    private final double height;

    /**
     * It's the number of damage on thi DamageTrack
     */
    private int tears;

    /**
     * It's represents the number of ammo for each color
     */
    private Map<Color, Label> ammos;

    /**
     * It's represents the number of mark for each color
     */
    private Map<Color, Label> marks;

    /**
     * It contains the damage (shown as rectangle) of this DamageTrack
     */
    private List<Rectangle> damage;

    /**
     * It contains the skulls (shown as circle) of this DamageTrack
     */
    private List<Circle> skulls;

    /**
     * It contains the number of power up, weapons and images of unloaded weapons of this player's DamageTrack
     */
    private HBox playerInfo;


    /**
     * Creates a DamageTrack. Entering on it, it will shows the number of power up, weapons and images of unloaded weapons
     * of this player's DamageTrack
     * @param markColors The colors of the marks shown on this DamageTrack
     * @param playerView The playerView associated to this DamageTrack
     *
     */
    public DamageTrack(List<Color> markColors, PlayerView playerView){
        InputStream myDmgUrl = getClass().getClassLoader().getResourceAsStream(DAMAGE_TRACK_IMAGE_FOLDER + "dmg" + playerView.getColor().toString().toLowerCase() + ".png");
        Image image = new Image(myDmgUrl);
        this.damageTrackImageView = new ImageView(image);
        this.playerView = playerView;
        this.damageTrackImageView.setFitWidth(Gui.getScreenBounds().getWidth()/4);
        this.damageTrackImageView.setPreserveRatio(true);
        this.width = damageTrackImageView.boundsInParentProperty().get().getWidth();
        this.height = damageTrackImageView.boundsInParentProperty().get().getHeight();
        this.ammoBox = new HBox();
        this.markBox = new HBox();
        this.ammos = new HashMap<>();
        this.marks = new HashMap<>();
        this.damage = new LinkedList<>();
        this.skulls = new LinkedList<>();
        this.playerInfo = new HBox();
        this.playerInfo.setVisible(false);
        ammoBox.setTranslateX(width * TRANSLATE_AMMO_BOX_X);
        ammoBox.setTranslateY(height * TRANSLATE_AMMO_BOX_Y);
        Label ammoLabel = new Label("AMMO ");
        ammoLabel.setTextFill(Color.WHITE);
        ammoLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        ammoBox.getChildren().add(ammoLabel);
        markBox.setTranslateX(width * TRANSLATE_MARKS_BOX_X);
        markBox.setTranslateY(height * TRANSLATE_MARKS_BOX_Y);
        Label marksLabel = new Label("MARKS ");
        marksLabel.setTextFill(Color.WHITE);
        marksLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        markBox.getChildren().add(marksLabel);
        this.tears = 0;
        this.getChildren().addAll(damageTrackImageView, ammoBox, markBox, playerInfo);
        addAmmos();
        addMarkLabels(markColors);

        this.setOnMouseEntered((MouseEvent t) ->
            playerInfo.setVisible(true)
        );

        this.setOnMouseExited((MouseEvent t) ->
            playerInfo.setVisible(false)
        );
    }

    /**
     * Create and add the labels associated to player's ammo to this DamageTrack
     */
    private void addAmmos(){
        Label redAmmo = new Label("0");
        redAmmo.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        Label blueAmmo = new Label("0");
        blueAmmo.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        blueAmmo.setTextFill(Color.WHITE);
        Label yellowAmmo = new Label("0");
        yellowAmmo.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        ammos.put(Color.RED, redAmmo);
        ammos.put(Color.BLUE, blueAmmo);
        ammos.put(Color.YELLOW, yellowAmmo);
        ammoBox.getChildren().addAll(redAmmo, blueAmmo, yellowAmmo);
    }

    /**
     * Add a damage, on this DamageTrack, of the specified color and at the right position
     * @param color The color of the damage to add
     */
    public void addDamage(Color color){
        if(tears < 12){
            Rectangle newDamage = new Rectangle(10, 10, color);
            newDamage.setStrokeType(StrokeType.OUTSIDE);
            newDamage.setStroke(Color.BLACK);
            damage.add(newDamage);
            this.getChildren().add(newDamage);
            if(tears < 2){
                if(playerView.isFrenzy()){
                    newDamage.setTranslateX(width * (0.118 + tears * 0.051));
                } else {
                    newDamage.setTranslateX(width * (0.107 + tears * 0.051));
                }
                newDamage.setTranslateY(height * 0.48);
            } else if(tears < 5){
                if(playerView.isFrenzy()){
                    newDamage.setTranslateX(width * (0.115 + tears * 0.055));
                } else {
                    newDamage.setTranslateX(width * (0.107 + tears * 0.055));
                }
                newDamage.setTranslateY(height * 0.48);
            } else {
                newDamage.setTranslateX(width * (0.107 + tears * 0.0558));
                newDamage.setTranslateY(height * 0.48);
            }
        }
    }

    /**
     * Create and add the labels associated to player's marks to this DamageTrack
     */
    private void addMarkLabels(List<Color> colors){
        for(Color color : colors){
            Label markLabel = new Label("0");
            markLabel.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
            marks.put(color, markLabel);
            markBox.getChildren().add(markLabel);
        }
    }

    /**
     * Set the text of the label, associated to the color, to the specified quantity
     * @param color The color of the ammo to add
     * @param quantity The number of ammo to add
     */
    public void addAmmo(Color color, Integer quantity){
        Label label = ammos.get(color);
        label.setText(quantity.toString());
    }

    /**
     * Set all the marks label to color and quantity specified by the markMap
     * @param markMap A map containing other PlayerView and each own number of marks on this DamageTrack
     */
    public void addMark(Map<PlayerView, Integer> markMap){
        for(PlayerView pv : markMap.keySet()){
            Label markLabel = marks.get(Color.valueOf(pv.getColor().toString().toLowerCase()));
            markLabel.setText(markMap.get(pv).toString());
        }
    }

    /**
     * Update the number of power up, weapon and images unloaded associated to playerView
     */
    public void updateInfo(){
        playerInfo.getChildren().clear();
        VBox infos = new VBox();
        playerInfo.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        for(WeaponView wv : playerView.getUnloadedWeapons()){
            WeaponButton unloadedWeapon = new WeaponButton(wv, false);
            unloadedWeapon.setDisable(true);
            unloadedWeapon.rescaleUnloaded(width);
            playerInfo.getChildren().add(unloadedWeapon);
        }
        Label numberPu = new Label("Number of PowerUps: " + playerView.getNumPowerUps());
        numberPu.setTextFill(Color.YELLOWGREEN);
        Label numberWeapons = new Label("Number of Weapons: " + (playerView.getUnloadedWeapons().size() + playerView.getNumLoadedWeapons()));
        numberWeapons.setTextFill(Color.YELLOWGREEN);
        infos.getChildren().addAll(numberPu, numberWeapons);
        playerInfo.getChildren().add(infos);
    }

    /**
     * Check: if the game is in frenzy mode, then: if the owner of this DamageTrack is frenzy, change image to
     * full frenzy DamageTrack, else to frenzy DamageTrack
     */
    public void checkSwitchToFrenzy(){
        InputStream frenzyDmgUrl;
        Image image;
        if(Gui.getClient().getMatch().isFrenzyOn()){
            if(playerView.isFrenzy()){
                frenzyDmgUrl = getClass().getClassLoader().getResourceAsStream(DAMAGE_TRACK_IMAGE_FOLDER + "dmgfullf" + playerView.getColor().toString().toLowerCase() + ".png");
                //this.getChildren().removeAll(skulls);
            } else {
                frenzyDmgUrl = getClass().getClassLoader().getResourceAsStream(DAMAGE_TRACK_IMAGE_FOLDER + "dmgf" + playerView.getColor().toString().toLowerCase() + ".png");
            }
            image = new Image(frenzyDmgUrl);
            damageTrackImageView.setImage(image);
        }
        //this.getChildren().remove(damage);
        //damage.clear(); //todo teoricamente viene già fatto
        //tears = 0;
    }

    /**
     * Add a skull on this DamageTrack, and positions it on the right place, based on skullNumber
     * @param skullNumber It's represent the number of skulls on this board, in order to place properly the next one
     */
    public void addSkull(int skullNumber){
        Circle newSkull = new Circle(5);
        newSkull.setFill(Color.RED);
        newSkull.setTranslateX(width * (0.2335 + skullNumber * 0.053));
        newSkull.setTranslateY(height * 0.85);
        this.getChildren().add(newSkull);
        skulls.add(newSkull);
    }

    /**
     * Update the damage shown on this DamageTrack
     */
    public void updateDamage(){
        this.getChildren().removeAll(damage);
        damage.clear();
        tears = 0;
        for(PlayerView pv : playerView.getDamageList()){
            this.addDamage(Color.valueOf(pv.getColor().toString()));
            tears++;
        }
    }

    /**
     * Update the marks shown on this DamageTrack
     */
    public void updateMarks(){
        this.addMark(playerView.getMarkMap());
    }

    /**
     * Update the skulls shown on this DamageTrack
     */
    public void updateSkulls(){
        this.getChildren().removeAll(skulls); //todo controllare meglio
        skulls.clear();
        if(!Gui.getClient().getMatch().isFrenzyOn()){
            for(int i = 0; i < playerView.getSkulls(); i++){
                addSkull(i);
            }
        }
    }
}
