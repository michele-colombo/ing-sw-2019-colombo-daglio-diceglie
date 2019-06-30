package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.ModeView;
import it.polimi.ingsw.client.WeaponView;
import it.polimi.ingsw.client.WrongSelectionException;
import it.polimi.ingsw.server.model.enums.Command;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
    /**
     * Row of grid in which put elements
     */
    private static final int GRID_ZERO_ROW = 0;
    /**
     * Row of grid in which put elements
     */
    private static final int GRID_FIRST_ROW = 1;
    /**
     * Row of grid in which put elements
     */
    private static final int GRID_SECOND_ROW = 2;
    /**
     * Column of grid in which put elements
     */
    private static final int GRID_ZERO_COLUMN = 0;
    /**
     * Ratio to properly rescale padding grid
     */
    private static final double SCALE_RATIO_GRID_PADDING = 307.2;
    /**
     * Ratio to properly rescale gap grid
     */
    private static final double SCALE_RATIO_GAP_GRID = 307.2;
    /**
     * Ratio to properly rescale wrapping width
     */
    private static final double SCALE_RATIO_WRAP = 0.4;
    /**
     * Ratio to properly rescale image
     */
    private static final double SCALE_RATIO_IMAGE = 0.9;
    /**
     * Ratio to properly rescale font size
     */
    private static final double SCALE_RATIO_FONT_SIZE = 76.8;

    /**
     * Weapon of which the player has to choose the modes
     */
    private WeaponView weapon;

    /**
     * Grid to collocate nodes
     */
    private GridPane grid;

    /**
     * VBox containing the buttons with whom choose the preferred modes
     */
    private VBox buttons;

    /**
     * card image in an anchorPane
     */
    private AnchorPane card;

    /**
     * build the screen: set the image on the left, text in the center and button on the right
     */
    public ModeChoiceScreen(){
        grid = new GridPane();
        grid.setPadding(new Insets(Gui.getScreenBounds().getWidth() / SCALE_RATIO_GRID_PADDING));
        grid.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        grid.setVgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_GAP_GRID);
        grid.setHgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_GAP_GRID);
        card= new AnchorPane();


        buttons= new VBox();
        buttons.setAlignment(Pos.CENTER);


        Text description= new Text(Gui.getClient().getMatch().getMyPlayer().getCurrWeapon().getDescription());
        description.setWrappingWidth(Gui.getScreenBounds().getWidth() * SCALE_RATIO_WRAP);
        description.setFont(new Font(Gui.getScreenBounds().getWidth() / SCALE_RATIO_FONT_SIZE));
        description.setFill(Color.GHOSTWHITE);
        description.setTextAlignment(TextAlignment.JUSTIFY);

        grid.add(description, GRID_FIRST_ROW, GRID_ZERO_COLUMN);
        grid.add(buttons, GRID_SECOND_ROW, GRID_ZERO_COLUMN);



        weapon = Gui.getClient().getMatch().getMyPlayer().getCurrWeapon();
        ImageView imageView = new ImageView(Gui.getCacheImage().getWeaponButtonImage(weapon.getName()));
        imageView.setFitHeight(Gui.getScreenBounds().getHeight() * SCALE_RATIO_IMAGE);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        card.getChildren().add(imageView);

        grid.add(card, GRID_ZERO_ROW, GRID_ZERO_COLUMN);
        update();
    }


    /**
     * Updates the screen, showing new selectable modes
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
