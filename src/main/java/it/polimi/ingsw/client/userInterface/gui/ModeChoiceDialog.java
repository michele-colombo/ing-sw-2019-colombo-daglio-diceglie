package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ModeView;
import it.polimi.ingsw.client.WeaponView;
import it.polimi.ingsw.client.WrongSelectionException;
import it.polimi.ingsw.server.model.Weapon;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.observer.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ModeChoiceDialog {
    private static final float WIDTH_RATIO= 2;
    private static final float HEIGHT_RATIO= 2;


    private WeaponView weapon;
    private GridPane grid;
    private ComboBox selectables;
    private Stage stage;

    private double width;
    private double height;




    public ModeChoiceDialog(){
        System.out.println("lo sto creando (mode selection)");

        grid = new GridPane();
        selectables= new ComboBox();

        grid.add(selectables, 1, 0);


        Button ok= new Button("OK");

        ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(selectables.getValue() != null && !selectables.getValue().toString().isEmpty()){
                    try{
                        String daMandare= selectables.getValue().toString();
                        Gui.getClient().selected(daMandare);
                        if(daMandare.equalsIgnoreCase("OK") || daMandare.equalsIgnoreCase("BACK")){
                            killWindow();
                        }
                    } catch(WrongSelectionException e){
                        System.out.println("Action ComboBox error!");
                    }
                }
            }
        });

        grid.add(ok, 2, 0);


        width= Gui.getScreenBounds().getWidth()/WIDTH_RATIO;
        height= Gui.getScreenBounds().getHeight()/HEIGHT_RATIO;
        update();

        weapon= Gui.getClient().getMatch().getMyPlayer().getCurrWeapon();
        Image weaponImage= new Image(getClass().getClassLoader().getResourceAsStream("weapon/" + weapon.getName() + ".png"));
        ImageView imageView = new ImageView(weaponImage);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width/2);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        grid.add(imageView, 0, 0);

        Scene scene = new Scene(grid, width, height);
        stage= new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.show();
/*
        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(new GridPane(), 450, 450));
        stage.show();
        */
    }

    private void killWindow() {
        stage.close();
    }

    public void update() {
        List<ModeView> modeViews= Gui.getClient().getMatch().getMyPlayer().getSelectableModes();
        List<String> modes= new ArrayList<>();
        for(ModeView mode: modeViews){
            modes.add(mode.getTitle());
        }

        ObservableList<String> comboItemsModes = FXCollections.observableList(modes);
        ObservableList<Command> comboItemsCommand = FXCollections.observableList(Gui.getClient().getMatch().getMyPlayer().getSelectableCommands());

        selectables.getItems().clear();
        selectables.getItems().addAll(comboItemsModes);
        selectables.getItems().addAll(comboItemsCommand);

    }
}
