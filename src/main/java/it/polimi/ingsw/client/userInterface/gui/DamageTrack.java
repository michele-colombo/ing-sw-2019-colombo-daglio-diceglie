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

public class DamageTrack extends Parent {
    private static final double TRANSLATE_AMMO_BOX_X = 0.09;
    private static final double TRANSLATE_AMMO_BOX_Y = 0.01;
    private static final double TRANSLATE_MARKS_BOX_X = 0.48;
    private static final double TRANSLATE_MARKS_BOX_Y = 0.01;

    private final PlayerView playerView;
    private final ImageView damageTrackImageView;
    private HBox ammoBox;
    private HBox markBox;
    private final double width;
    private final double height;
    private int tears;
    private Map<Color, Label> ammos;
    private Map<Color, Label> marks;
    private List<Rectangle> damage;
    private List<Color> markColors;
    private List<Circle> skulls;
    private HBox playerInfo;

    public DamageTrack(List<Color> markColors, PlayerView playerView){
        InputStream myDmgUrl = getClass().getClassLoader().getResourceAsStream("damageTracks/dmg" + playerView.getColor().toString().toLowerCase() + ".png");
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
        this.markColors = markColors;
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

        this.setOnMouseEntered((MouseEvent t) -> {
            playerInfo.setVisible(true);
        });

        this.setOnMouseExited((MouseEvent t) ->{
            playerInfo.setVisible(false);
        });
    }

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
            tears++;
        }
    }

    private void addMarkLabels(List<Color> colors){
        for(Color color : colors){
            Label markLabel = new Label("0");
            markLabel.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
            marks.put(color, markLabel);
            markBox.getChildren().add(markLabel);
        }
    }

    public void addAmmo(Color color, Integer quantity){
        Label label = ammos.get(color);
        label.setText(quantity.toString());
    }

    public void addMark(Map<PlayerView, Integer> markMap){
        for(PlayerView pv : markMap.keySet()){
            Label markLabel = marks.get(pv.getColor());
            markLabel.setText(markMap.get(pv).toString());
        }
    }

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
        //infos.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Label numberPu = new Label("Number of PowerUps: " + playerView.getNumPowerUps());
        numberPu.setTextFill(Color.YELLOWGREEN);
        Label numberWeapons = new Label("Number of Weapons: " + (playerView.getUnloadedWeapons().size() + playerView.getNumLoadedWeapons()));
        numberWeapons.setTextFill(Color.YELLOWGREEN);
        infos.getChildren().addAll(numberPu, numberWeapons);
        playerInfo.getChildren().add(infos);
    }

    public void checkSwitchToFrenzy(){
        InputStream frenzyDmgUrl;
        Image image;
        if(Gui.getClient().getMatch().isFrenzyOn()){
            if(playerView.isFrenzy()){
                frenzyDmgUrl = getClass().getClassLoader().getResourceAsStream("damageTracks/dmgfullf" + playerView.getColor() + ".png");
            } else {
                frenzyDmgUrl = getClass().getClassLoader().getResourceAsStream("damageTracks/dmgf" + playerView.getColor() + ".png");
            }
            image = new Image(frenzyDmgUrl);
            damageTrackImageView.setImage(image);
        }
        //this.getChildren().remove(damage);
        //damage.clear(); //todo teoricamente viene già fatto
        //tears = 0;
    }

    public void addSkull(int skullNumber){
        Circle newSkull = new Circle(5);
        newSkull.setFill(Color.RED);
        newSkull.setTranslateX(width * (0.2335 + skullNumber * 0.053));
        newSkull.setTranslateY(height * 0.85);
        this.getChildren().add(newSkull);
        skulls.add(newSkull);
    }

    public void updateDamage(){ //todo da controllare
        this.getChildren().remove(damage);
        damage.clear();
        if(playerView.getDamageList().isEmpty()){
            tears = 0;
        } else {
            for(PlayerView pv : playerView.getDamageList()){
                this.addDamage(Color.valueOf(pv.getColor().toString()));
            }
        }
    }

    public void updateMarks(){
        this.addMark(playerView.getMarkMap());
    }

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
