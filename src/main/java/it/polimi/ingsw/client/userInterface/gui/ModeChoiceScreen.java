package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.ModeView;
import it.polimi.ingsw.client.WeaponView;
import it.polimi.ingsw.client.WrongSelectionException;
import it.polimi.ingsw.server.model.enums.Command;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;

/**
 * It represents the screen in which to choose the mode of a weapon
 */
public class ModeChoiceScreen {

    private WeaponView weapon;
    /**
     * Grid shown, in which there are buttons and card
     */
    private GridPane grid;

    /**
     * VBox containing the buttons with whom choose the preferred modes
     */
    private VBox buttons;
    private AnchorPane card;

    /**
     * Create the screen in which shows the weapon and it's selectable modes
     */
    public ModeChoiceScreen(){
        System.out.println("lo sto creando (mode selection)");

        grid = new GridPane();
        grid.setPadding(new Insets(5));
        grid.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        grid.setVgap(5);
        grid.setHgap(5);
        card= new AnchorPane();


        buttons= new VBox();
        buttons.setAlignment(Pos.CENTER);


        Text description= new Text(Gui.getClient().getMatch().getMyPlayer().getCurrWeapon().getDescription());
        description.setWrappingWidth(Gui.getScreenBounds().getWidth() * 0.4);
        description.setFont(new Font(20));
        description.setFill(Color.GHOSTWHITE);
        description.setTextAlignment(TextAlignment.JUSTIFY);

        grid.add(description, 1, 0);
        grid.add(buttons, 2, 0);



        weapon= Gui.getClient().getMatch().getMyPlayer().getCurrWeapon();

        Image weaponImage= new Image(getClass().getClassLoader().getResourceAsStream("weapon/" + weapon.getName() + ".png"));
        ImageView imageView = new ImageView(weaponImage);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight() * 0.90);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        card.getChildren().add(imageView);

        grid.add(card, 0, 0);
        update();
    }


    /**
     * Update the screen, showing new selectable modes
     */
    public void update() {
        Platform.runLater( () -> {
            List<ModeView> modeViews = Gui.getClient().getMatch().getMyPlayer().getSelectableModes();
            List<Command> commands = Gui.getClient().getMatch().getMyPlayer().getSelectableCommands();

            buttons.getChildren().clear();

            for (ModeView mode : modeViews) {
                Button button = new Button(mode.getTitle());
                button.setOnMouseClicked((MouseEvent me) -> {
                    try {
                        Gui.getClient().selected(mode.getTitle());
                    } catch (WrongSelectionException e) {
                    System.out.println("Wrong exception");
                    }
                });
                buttons.getChildren().add(button);
            }
            for (Command c : commands) {
                Button button = new Button(c.toString());
                button.setOnMouseClicked((MouseEvent me) -> {
                    try {
                        Gui.getClient().selected(c.toString());
                    } catch (WrongSelectionException e) {
                        System.out.println("Wrong exception");
                    }
                });
                buttons.getChildren().add(button);
            }
        });

    }

    /**
     *
     * @return grid
     */
    public Parent getParent(){
        return grid;
    }
}
