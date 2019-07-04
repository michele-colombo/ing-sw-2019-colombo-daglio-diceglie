package it.polimi.ingsw.client.user_interface.gui;

import it.polimi.ingsw.client.model_view.PlayerView;
import it.polimi.ingsw.client.model_view.WeaponView;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * It represents a player's damageTrack
 */
public class DamageTrack extends Parent {

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
     * It's the ratio to properly rescale damage rectangle
     */
    private static final double SCALE_RATIO_DAMAGE_RECTANGLE_WIDTH = 38.4;
    /**
     * It's the ratio to properly rescale damage rectangle
     */
    private static final double SCALE_RATIO_DAMAGE_RECTANGLE_HEIGHT = 9.385;
    /**
     * It's the ratio to properly rescale damage rectangle gap
     */
    private static final double GAP_2_DAMAGE = 0.051;
    /**
     * It's the ratio to properly rescale damage rectangle
     */
    private static final double GAP_5_DAMAGE = 0.055;
    /**
     * It's the ratio to properly rescale damage rectangle
     */
    private static final double GAP_12_DAMAGE = 0.0558;
    /**
     * It's the ratio to properly translate damage rectangle on normal mode on x axe
     */
    private static final double X_DAMAGE_TRANSLATE_NORMAL = 0.107;
    /**
     * It's the ratio to properly translate damage rectangle on frenzy mode on x axe
     */
    private static final double X_DAMAGE_TRANSLATE_FRENZY_1 = 0.118;
    /**
     * It's the ratio to properly translate damage rectangle on normal mode on x axe
     */
    private static final double X_DAMAGE_TRANSLATE_FRENZY_2 = 0.115;
    /**
     * It's the ratio to properly translate damage rectangle on y axe
     */
    private static final double Y_DAMAGE_TRANSLATE = 0.48;
    /**
     * It's the ratio to properly translate skull on x axe
     */
    private static final double X_SKULL_TRANSLATE = 0.2335;
    /**
     * It's the ratio to properly translate skull on y axe
     */
    private static final double Y_SKULL_TRANSLATE = 0.85;
    /**
     * It's the ratio to properly rescale gap skull
     */
    private static final double GAP_SKULL = 0.053;
    /**
     * It's the ratio to properly rescale skull
     */
    private static final double SCALE_RATIO_SKULL = 76.8;
    /**
     * Max tear 1
     */
    private static final double MAX_TEAR_1 = 12;
    /**
     * Max tear 2
     */
    private static final double MAX_TEAR_2 = 2;
    /**
     * Max tear 3
     */
    private static final double MAX_TEAR_3 = 5;
    /**
     * Starting ammos value
     */
    private static final String STARTING_AMMOS_MARKS = "0";
    /**
     * String of powerup label
     */
    private static final String POWERUP_LABEL = "Number of PowerUps: ";
    /**
     * String of weapon label
     */
    private static final String WEAPON_LABEL = "Number of Weapons: ";
    /**
     * String of ammo label
     */
    private static final String AMMO_LABEL = "AMMO ";
    /**
     * String of marks label
     */
    private static final String MARKS_LABEL = "MARKS ";
    /**
     * Name of the image for damage track full frenzy
     */
    private static final String DAMAGE_TRACK_FULL_FRENZY = "dmgfullf";
    /**
     * Name of the image for damage track half frenzy
     */
    private static final String DAMAGE_TRACK_HALF_FRENZY = "dmgf";

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

    private List<Color> colors;


    /**
     * Creates a DamageTrack. Entering on it, it will shows the number of power up, weapons and images of unloaded weapons
     * of this player's DamageTrack
     * @param markColors The colors of the marks shown on this DamageTrack
     * @param playerView The playerView associated to this DamageTrack
     *
     */
    public DamageTrack(List<Color> markColors, PlayerView playerView){
        this.damageTrackImageView = new ImageView(BoardGui.getParser().getDamageTrackImage("dmg" + playerView.getColor().toString().toLowerCase()));
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
        colors = markColors;
        ammoBox.setTranslateX(width * TRANSLATE_AMMO_BOX_X);
        ammoBox.setTranslateY(height * TRANSLATE_AMMO_BOX_Y);
        Label ammoLabel = new Label(AMMO_LABEL);
        ammoLabel.setTextFill(Color.WHITE);
        ammoLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        ammoBox.getChildren().add(ammoLabel);
        markBox.setTranslateX(width * TRANSLATE_MARKS_BOX_X);
        markBox.setTranslateY(height * TRANSLATE_MARKS_BOX_Y);
        Label marksLabel = new Label(MARKS_LABEL);
        marksLabel.setTextFill(Color.WHITE);
        marksLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        markBox.getChildren().add(marksLabel);
        this.tears = 0;
        this.getChildren().addAll(damageTrackImageView, ammoBox, markBox, playerInfo);
        addAmmos();
        addMarkLabels(markColors);

        this.setOnMouseEntered((MouseEvent t) ->{
            playerInfo.setVisible(true);
            for (Rectangle damage : damage){
                damage.setVisible(false);
            }
            for (Circle skull : skulls){
                skull.setVisible(false);
            }
                }

        );

        this.setOnMouseExited((MouseEvent t) ->{
            playerInfo.setVisible(false);
            for (Rectangle damage : damage){
                damage.setVisible(true);
            }
            for (Circle skull : skulls){
                skull.setVisible(true);
            }
                }

        );
    }

    /**
     * Create and add the labels associated to player's ammo to this DamageTrack
     */
    private void addAmmos(){
        Label redAmmo = new Label(STARTING_AMMOS_MARKS);
        redAmmo.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        Label blueAmmo = new Label(STARTING_AMMOS_MARKS);
        blueAmmo.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        blueAmmo.setTextFill(Color.WHITE);
        Label yellowAmmo = new Label(STARTING_AMMOS_MARKS);
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
        if(tears < MAX_TEAR_1){
            Rectangle newDamage = new Rectangle(width / SCALE_RATIO_DAMAGE_RECTANGLE_WIDTH, height / SCALE_RATIO_DAMAGE_RECTANGLE_HEIGHT, color);
            newDamage.setStrokeType(StrokeType.OUTSIDE);
            newDamage.setStroke(Color.BLACK);
            damage.add(newDamage);
            this.getChildren().add(newDamage);
            if(tears < MAX_TEAR_2){
                if(playerView.isFrenzy()){
                    newDamage.setTranslateX(width * (X_DAMAGE_TRANSLATE_FRENZY_1 + tears * GAP_2_DAMAGE));
                } else {
                    newDamage.setTranslateX(width * (X_DAMAGE_TRANSLATE_NORMAL + tears * GAP_2_DAMAGE));
                }
                newDamage.setTranslateY(height * Y_DAMAGE_TRANSLATE);
            } else if(tears < MAX_TEAR_3){
                if(playerView.isFrenzy()){
                    newDamage.setTranslateX(width * (X_DAMAGE_TRANSLATE_FRENZY_2 + tears * GAP_5_DAMAGE));
                } else {
                    newDamage.setTranslateX(width * (X_DAMAGE_TRANSLATE_NORMAL + tears * GAP_5_DAMAGE));
                }
                newDamage.setTranslateY(height * Y_DAMAGE_TRANSLATE);
            } else {
                newDamage.setTranslateX(width * (X_DAMAGE_TRANSLATE_NORMAL + tears * GAP_12_DAMAGE));
                newDamage.setTranslateY(height * Y_DAMAGE_TRANSLATE);
            }
        }
    }

    /**
     * Create and add the labels associated to player's marks to this DamageTrack
     */
    private void addMarkLabels(List<Color> colors){
        for(Color color : colors){
            Label markLabel = new Label(STARTING_AMMOS_MARKS);
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
        for(Color color : colors){
            Label mark = marks.get(color);
            mark.setText("0");
        }
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
        Label numberPu = new Label(POWERUP_LABEL + playerView.getNumPowerUps());
        numberPu.setTextFill(Color.YELLOWGREEN);
        Label numberWeapons = new Label(WEAPON_LABEL + (playerView.getUnloadedWeapons().size() + playerView.getNumLoadedWeapons()));
        numberWeapons.setTextFill(Color.YELLOWGREEN);
        infos.getChildren().addAll(numberPu, numberWeapons);
        playerInfo.getChildren().add(infos);
    }

    /**
     * Check: if the game is in frenzy mode, then: if the owner of this DamageTrack is frenzy, change image to
     * full frenzy DamageTrack, else to frenzy DamageTrack
     */
    public void checkSwitchToFrenzy(){
        Image image;
        if(Gui.getClient().getMatch().isFrenzyOn()){
            if(playerView.isFrenzy()){
                image = BoardGui.getParser().getDamageTrackImage(DAMAGE_TRACK_FULL_FRENZY + playerView.getColor().toString().toLowerCase());

            } else {
                image = BoardGui.getParser().getDamageTrackImage(DAMAGE_TRACK_HALF_FRENZY + playerView.getColor().toString().toLowerCase());
            }
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
        Circle newSkull = new Circle(width / SCALE_RATIO_SKULL);
        newSkull.setFill(Color.RED);
        newSkull.setTranslateX(width * (X_SKULL_TRANSLATE + skullNumber * GAP_SKULL));
        newSkull.setTranslateY(height * Y_SKULL_TRANSLATE);
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
