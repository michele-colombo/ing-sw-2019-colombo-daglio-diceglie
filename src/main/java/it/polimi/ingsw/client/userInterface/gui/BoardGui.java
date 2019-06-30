package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.ClientExceptions.WrongSelectionException;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.util.*;

public class BoardGui {
    /**
     * The path of layout PNG
     */
    private static final String LAYOUT_PNG_FOLDER = "resources/layoutPNG/layout";
    /**
     * File extension of the images
     */
    private static final String LAYOUT_PNG_EXTENSION = ".png";
    /**
     * String to properly load PixelWeapon from file
     */
    private static final String BLUE_WEAPONS_PARSER_NAME = "blue";
    /**
     * String to properly load PixelWeapon from file
     */
    private static final String YELLOW_WEAPONS_PARSER_NAME = "yellow";
    /**
     * String to properly load PixelWeapon from file
     */
    private static final String RED_WEAPONS_PARSER_NAME = "red";
    /**
     * String used in connection label
     */
    private static final String CONNECTION_LABEL = "PLAYERS";
    /**
     * String used to show current player
     */
    private static final String CURRENT_PLAYER_LABEL = "Current: ";
    /**
     * String used to show online player
     */
    private static final String ONLINE_PLAYER_LABEL = "online";
    /**
     * String used to show offline player
     */
    private static final String OFFLINE_PLAYER_LABEL = "offline";
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
    private static final int GRID_FOURTH_ROW = 4;
    /**
     * Column of grid in which put elements
     */
    private static final int GRID_FIRST_COLUMN = 1;
    /**
     * Column of grid in which put elements
     */
    private static final int GRID_ZERO_COLUMN = 0;
    /**
     * Column of damage track grid in which put elements
     */
    private static final int DMG_TRACK_ZERO_COLUMN = 0;
    /**
     * Column of damage track grid in which put elements
     */
    private static final int DMG_TRACK_ZERO_ROW = 0;
    /**
     * Row of connection grid in which put elements
     */
    private static final int CONNECTION_ZERO_ROW = 0;
    /**
     * Row of connection grid in which put elements
     */
    private static final int CONNECTION_FIRST_ROW = 1;
    /**
     * Column of connection grid in which put elements
     */
    private static final int CONNECTION_ZERO_COLUMN = 0;
    /**
     * Column of connection grid in which put elements
     */
    private static final int CONNECTION_FIRST_COLUMN = 1;
    /**
     * Number to properly calculate selectables row and column
     */
    private static final int SELECTABLES_INDEX = 3;
    /**
     * Max number of kills on killshotTrack
     */
    private static final int MAX_KILLS = 8;
    /**
     * Number to calculate double kill on killshotTrack
     */
    private static final int DOUBLE_KILL = 1;
    /**
     * Index of first player
     */
    private static final int FIRST_PLAYER = 0;
    /**
     * Ratio used to properly place a skull/kill on killshot track, on x axe
     */
    private static final double X_KILLSHOT_TRACK = 0.0909;
    /**
     * Ratio used to properly place a skull/kill on killshot track, on y axe
     */
    private static final double Y_KILLSHOT_TRACK = 0.0921;
    /**
     * Ratio used to properly place an overkill on killshotTrack, on y axe
     */
    private static final double Y_KILLSHOT_TRACK_OVERKILL = 0.025;
    /**
     * Ratio used to properly separate skulls/kills on killshotTrack, on x axe
     */
    private static final double GAP_KILLSHOT_TRACK = 0.0421;
    /**
     * Ratio used to properly create the width of SquareRectangles on layout
     */
    private static final double SCALE_RATIO_SQUARE_RECTANGLE_WIDTH = 7.68;
    /**
     * Ratio used to properly create height of the SquareRectangles on layout
     */
    private static final double SCALE_RATIO_SQUARE_RECTANGLE_HEIGHT = 5.25;
    /**
     * Ratio to properly rescale layout image
     */
    private static final double SCALE_RATIO_LAYOUT_IMAGE = 1.5;
    /**
     * Ratio to properly rescale hgap grid
     */
    private static final double SCALE_RATIO_HGAP_GRID = 307.2;
    /**
     * Ratio to properly rescale vgap grid
     */
    private static final double SCALE_RATIO_VGAP_GRID = 102.4;
    /**
     * Ratio to properly rescale hgap of damage track grid
     */
    private static final double SCALE_RATIO_HGAP_DMG_TRACKS = 307.2;
    /**
     * Ratio to properly rescale gap of connection grid
     */
    private static final double SCALE_RATIO_GAP_CONNECTION = 153.6;
    /**
     * Ratio to properly rescale labels of connection grid
     */
    private static final double SCALE_RATIO_CONNECTION_LABEL = 20;
    /**
     * Opacity of unloaded weapon
     */
    private static final double WEAPON_OPACITY = 0.5;
    /**
     * Ratio to properly rescale kill rectangle
     */
    private static final double SCALE_RATIO_KILL_RECTANGLE = 153.6;
    /**
     * Ratio to properly rescale player rectangle width
     */
    private static final double SCALE_RATIO_PLAYER_RECTANGLE_WIDTH = 307.2;
    /**
     * Ratio to properly rescale player rectangle height
     */
    private static final double SCALE_RATIO_PLAYER_RECTANGLE_HEIGHT = 76.8;
    /**
     * Ratio used to properly create skulls on layout
     */
    private static final double SKULL_CIRCLE = 76.08;
    /**
     * Ratio used to properly create color button on board
     */
    private static final double COLOR_BUTTON_SIZE = Gui.getScreenBounds().getWidth() / 68;

    /**
     * The main part of the stage
     */
    private final GridPane view;
    /**
     * Contains own WeaponButton
     */
    private HBox weaponBox;
    /**
     * Contains own PowerUpButton
     */
    private HBox powerUpBox;
    /**
     * Contains connection state anc current player labels
     */
    private GridPane connectionState;
    /**
     * Contains the image of the layout and the other playing images
     */
    private AnchorPane board;
    /**
     * Contains label of connectionState
     */
    private List<Label> connectionLabels;
    /**
     * It's the label that show the current player
     */
    private Label currentPlayer;
    /**
     * Contains selectables button
     */
    private GridPane selectables;
    /**
     * Contains PixelPosition, in order to properly place the buttons on layout
     */
    private List<PixelPosition> pixelPositions;
    /**
     * Contains AmmoButton of layout
     */
    private List<AmmoButton> ammoButtonsList;
    /**
     * Contains WeaponButton of layout
     */
    private List<WeaponButton> weaponButtonList;
    /**
     * Contains PixelWeapon for yellowWeapons, in order to properly the buttons on layout
     */
    private List<PixelWeapon> yellowWeapons;
    /**
     * Contains PixelWeapon for blueWeapons, in order to properly the buttons on layout
     */
    private List<PixelWeapon> blueWeapons;
    /**
     * Contains PixelWeapon for redWeapons, in order to properly the buttons on layout
     */
    private List<PixelWeapon> redWeapons;
    /**
     * Contains SquareRectangles, placed on layout
     */
    private static List<SquareRectangle> squareRectangles;
    /**
     * Contains each player (if he's spawned) and his own position
     */
    private Map<PlayerRectangle, HBox> playerPositions;
    /**
     * Contains each SquareView and its own HBox, used to move players
     */
    private Map<SquareView, HBox> playerPositionHBox;
    /**
     * Contains each PlayerView and his own DamageTrack
     */
    private Map<PlayerView, DamageTrack> playerDamageTracks;
    /**
     * Contains kills on killshoTrack, represented as Rectangles
     */
    private List<Rectangle> killOnKillshotTrack;
    /**
     * Contains skulls on killshotTrack, represented as Circles
     */
    private List<Circle> skulls;
    private Text stateText; //todo add javadoc
    /**
     * Width of the image used as background of layout
     */
    private static double boardWidth;
    /**
     * Height of the image used as background of layout
     */
    private static double boardHeight;

    /**
     * Create a new BoardGui from match. It adds the board, damageTracks, connection state of the player,
     * PlayerRectangle, playerPosition, SquareButtons
     * @param match The MatchView from which get information
     */
    public BoardGui(MatchView match){
        view = new GridPane();
        weaponBox = new HBox();
        powerUpBox = new HBox();
        selectables = new GridPane();
        ammoButtonsList = new LinkedList<>();
        weaponButtonList = new LinkedList<>();
        Parser parser = new Parser(match.getLayout().getLayoutConfiguration());
        pixelPositions = parser.loadAmmoResource();
        yellowWeapons = parser.loadWeaponResource(YELLOW_WEAPONS_PARSER_NAME);
        blueWeapons = parser.loadWeaponResource(BLUE_WEAPONS_PARSER_NAME);
        redWeapons = parser.loadWeaponResource(RED_WEAPONS_PARSER_NAME);
        playerPositions = new HashMap<>();
        playerPositionHBox = new HashMap<>();
        playerDamageTracks = new HashMap<>();
        killOnKillshotTrack = new LinkedList<>();
        skulls = new LinkedList<>();
        createPlayerRectangle(match);

        view.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        view.setHgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_HGAP_GRID);
        view.setVgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_VGAP_GRID);

        board = new AnchorPane();
        board.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //board.setPadding(new Insets(0,0,0,0));

        InputStream boardUrl = getClass().getClassLoader().getResourceAsStream(LAYOUT_PNG_FOLDER + match.getLayout().getLayoutConfiguration() + LAYOUT_PNG_EXTENSION);
        Image image = new Image(boardUrl);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight() / SCALE_RATIO_LAYOUT_IMAGE);
        imageView.setFitWidth(Gui.getScreenBounds().getWidth() / SCALE_RATIO_LAYOUT_IMAGE);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        boardWidth = imageView.boundsInParentProperty().get().getWidth();
        boardHeight = imageView.boundsInParentProperty().get().getHeight();

        board.getChildren().addAll(imageView);
        GridPane.setHalignment(imageView, HPos.LEFT);
        stateText = new Text();
        stateText.setFill(Color.WHITE);
        view.add(board, GRID_ZERO_ROW,GRID_ZERO_COLUMN);
        view.add(weaponBox, GRID_ZERO_ROW,GRID_FIRST_COLUMN);
        view.add(powerUpBox, GRID_FIRST_ROW, GRID_FIRST_COLUMN);
        addDamageTrack(match);
        addConnectionState(match.getAllPlayers());
        addSelectables();
        updateConnection(match.readConnections());
    }

    /**
     *
     * @return view
     */
    public Parent getView(){
        return view;
    }

    /**
     * Creat DamageTrack and add them view; the higher is the one referred to this client; the color is based on player's color
     * @param match MatchView from which get all players' color
     */
    private void addDamageTrack(MatchView match){
        List<Color> colors = getAllColors(match);
        GridPane damageTracks;
        damageTracks = new GridPane();
        damageTracks.setVgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_HGAP_DMG_TRACKS);
        damageTracks.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        damageTracks.setAlignment(Pos.TOP_CENTER);

        DamageTrack damageTrack = new DamageTrack(colors, match.getMyPlayer());
        damageTrack.updateInfo();
        playerDamageTracks.put(match.getMyPlayer(), damageTrack);
        damageTracks.add(damageTrack, DMG_TRACK_ZERO_ROW, DMG_TRACK_ZERO_COLUMN);

        int i = 1;
        for(PlayerView pv : match.getAllPlayers()){
            if(pv.getColor() != match.getMyPlayer().getColor()){
                DamageTrack otherDamageTrack = new DamageTrack(colors, pv);
                otherDamageTrack.updateInfo();
                playerDamageTracks.put(pv, otherDamageTrack);
                damageTracks.add(otherDamageTrack, DMG_TRACK_ZERO_ROW, i);
            }
            i++;
        }
        view.add(damageTracks, GRID_FIRST_ROW,GRID_ZERO_COLUMN);
    }

    /**
     * Create connection state GridPane and add it to view; it contains a label as title of the GridPane, and another one
     * showing the current player (in white)
     * @param players List of Player, from whom get the current one
     */
    private void addConnectionState(List<PlayerView> players ){
        connectionLabels = new ArrayList<>();
        connectionState = new GridPane();
        currentPlayer = new Label(CURRENT_PLAYER_LABEL + players.get(0).getName()); //todo forse da fixare
        currentPlayer.setTextFill(Color.WHITE);
        currentPlayer.setWrapText(true);
        Label connectionLabel = new Label(CONNECTION_LABEL);
        connectionLabel.setTextFill(Color.GHOSTWHITE);
        connectionState.add(connectionLabel, CONNECTION_ZERO_ROW, CONNECTION_ZERO_COLUMN);
        connectionState.setVgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_GAP_CONNECTION);
        connectionState.setHgap(Gui.getScreenBounds().getWidth() / SCALE_RATIO_GAP_CONNECTION);
        connectionState.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        connectionState.add(currentPlayer, CONNECTION_ZERO_ROW, CONNECTION_FIRST_COLUMN);
        view.add(connectionState, GRID_FOURTH_ROW, GRID_ZERO_COLUMN);
    }

    /**
     * Add selectables GridPane in view
     */
    private void addSelectables(){
        view.add(selectables,GRID_FOURTH_ROW,GRID_FIRST_COLUMN);
    }

    /**
     * Update connection state, adding two label for each player (and clearing old ones)
     * : the former contains own name, the latter their
     * connection state: this one is green if the respective player is online, otherwise is red
     * @param connections Map containing each player and their own connection state
     */
    public void updateConnection(Map<String, Boolean> connections){
        Platform.runLater(() -> {
            connectionState.getChildren().removeAll(connectionLabels);
            connectionLabels.clear();
            int i = 2;
            for(PlayerView pv : Gui.getClient().getMatch().getAllPlayers()){
                Label playerLabel = new Label(pv.getName());
                playerLabel.setWrapText(true);
                playerLabel.setTextFill(Color.valueOf(pv.getColor().toString()));
                playerLabel.setMaxWidth(Gui.getScreenBounds().getWidth() / SCALE_RATIO_CONNECTION_LABEL);
                Label onlineLabel;
                if(connections.get(pv.getName())){
                    onlineLabel = new Label(ONLINE_PLAYER_LABEL);
                    onlineLabel.setTextFill(Color.GREEN);
                    onlineLabel.setWrapText(true);
                } else {
                    onlineLabel = new Label(OFFLINE_PLAYER_LABEL);
                    onlineLabel.setTextFill(Color.RED);
                    onlineLabel.setWrapText(true);
                }
                onlineLabel.setMaxWidth(Gui.getScreenBounds().getWidth() / SCALE_RATIO_CONNECTION_LABEL);
                connectionState.add(playerLabel, CONNECTION_ZERO_ROW, i);
                connectionState.add(onlineLabel, CONNECTION_FIRST_ROW, i);
                connectionLabels.add(playerLabel);
                connectionLabels.add(onlineLabel);
                i++;
            }
            if(Gui.getClient().getMatch().getCurrentPlayer() != null){
                currentPlayer.setText(CURRENT_PLAYER_LABEL + Gui.getClient().getMatch().getCurrentPlayer());
            }
            updateStateText();
        });
    }

    /**
     * Update currentPlayer with the name of the current one
     */
    public void updateCurrentPlayer(){
        Platform.runLater(() -> {
            currentPlayer.setText(CURRENT_PLAYER_LABEL + Gui.getClient().getMatch().getCurrentPlayer());
            updateStateText();
        });
    }

    /**
     * Updates player's damageTrack info and your own power up, clearing old ones and recreating them
     * @param player The player to be updated
     */
    public void updatePowerUp(PlayerView player){
        Platform.runLater(() -> {
            MyPlayer me = Gui.getClient().getMatch().getMyPlayer();
                if(player.getName().equals(me.getName())){
                    powerUpBox.getChildren().clear();
                    for(PowerUpView powerUpView : me.getPowerUps()){
                        PowerUpButton powerUpButton = new PowerUpButton(powerUpView);
                        powerUpBox.getChildren().add(powerUpButton);
                        powerUpBox.setHgrow(powerUpButton, Priority.ALWAYS); //forse si può togliere
                }
            }
                playerDamageTracks.get(player).updateInfo();
            updateStateText();
        });
    }

    /**
     * Updates the selectables for this client, creating and adding them to the window, and clearing old ones.
     * They includes: action, command as button and selectableSquares as SquareButton set to visible.
     * Clicking on action or command button, the choice will be sent to the server
     */
    public void updateSelectables(){
        Platform.runLater(() -> {
            selectables.getChildren().clear();


            MyPlayer me = Gui.getClient().getMatch().getMyPlayer();
            ObservableList<String> comboItemsActions = FXCollections.observableList(me.getSelectableActions());
            ObservableList<Command> comboItemsCommand = FXCollections.observableList(me.getSelectableCommands());
            int index= 0;
            for(String action : comboItemsActions){
                Button button= new Button(action);
                button.setOnMouseClicked((MouseEvent t) ->
                {
                    try {
                        Gui.getClient().selected(action);
                    }
                    catch (WrongSelectionException e){
                        System.out.println("Wrong selection");
                    }
                });

                selectables.add(button, index%SELECTABLES_INDEX, index/SELECTABLES_INDEX);
                index++;
            }
            for(Command command : comboItemsCommand){
                Button button= new Button(command.toString());
                button.setOnMouseClicked((MouseEvent t) ->
                {
                    try {
                        Gui.getClient().selected(command.toString());
                    }
                    catch (WrongSelectionException e){
                        System.out.println("Wrong selection");
                    }
                });

                selectables.add(button, index%SELECTABLES_INDEX, index/SELECTABLES_INDEX);
                index++;
            }
            for(AmmoColor colors: me.getSelectableColors()){
                Button button= new Button();

                button.setGraphic(new Rectangle(COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE, Color.valueOf(colors.toString().toUpperCase())));

                button.setOnMouseClicked((MouseEvent t) ->
                {
                    try {
                        Gui.getClient().selected(colors.toString());
                    }
                    catch (WrongSelectionException e){
                        System.out.println("Wrong selection");
                    }
                });
                selectables.add(button, index%SELECTABLES_INDEX, index/SELECTABLES_INDEX);
                index++;
            }

            for(SquareView sv : me.getSelectableSquares()){
                for(SquareRectangle sr : squareRectangles){
                    if(sr.equalsSquareView(sv)){
                        sr.setVisible(true);
                    }
                }
            }
            updateStateText();
        });
    }


    /**
     * player's damageTrack info and creates and add WeaponButtons of your weapon (clearing old ones).
     * If they're unloaded, they have low opacity.
     * @param player
     */
    public void updateWeapons(PlayerView player){
        Platform.runLater(() -> {
            if(player.getName().equals(Gui.getClient().getMatch().getMyPlayer().getName())){
                weaponBox.getChildren().clear();
                for(WeaponView weaponView : Gui.getClient().getMatch().getMyPlayer().getWeapons().keySet()){
                    WeaponButton newWeapon = new WeaponButton(weaponView, true);
                    if(Gui.getClient().getMatch().getMyPlayer().getUnloadedWeapons().contains(newWeapon.getWeaponView())){
                        newWeapon.setOpacity(WEAPON_OPACITY);
                    }
                    newWeapon.rescaleOnHand();
                    weaponBox.getChildren().add(newWeapon);
                }
            }
            playerDamageTracks.get(player).updateInfo(); //todo forse è inutile
            updateStateText();
        });
    }

    /**
     * Updates KillshotTrack, creating and adding a kill for each skull removed (max eight), then remaining skulls
     * are added (old kills and skulls are removed)
     */
    public void updateKillshotTrack(){
        Platform.runLater(() -> {
            MatchView matchView = Gui.getClient().getMatch();
            board.getChildren().removeAll(killOnKillshotTrack);
            killOnKillshotTrack.clear();

            int i;
            for(i = 0; i < matchView.getTrack().size() && i < MAX_KILLS; i++){
                Map<PlayerView, Integer> map = matchView.getTrack().get(i);
                PlayerView player = new ArrayList<>(map.keySet()).get(FIRST_PLAYER);
                Rectangle kill = new Rectangle(Gui.getScreenBounds().getWidth() / SCALE_RATIO_KILL_RECTANGLE, Gui.getScreenBounds().getWidth() / SCALE_RATIO_KILL_RECTANGLE, Color.valueOf(player.getColor().toString()));
                kill.setStrokeType(StrokeType.OUTSIDE);
                kill.setStroke(Color.BLACK);
                kill.setTranslateX(boardWidth * (BoardGui.X_KILLSHOT_TRACK + i * BoardGui.GAP_KILLSHOT_TRACK));
                kill.setTranslateY(boardHeight * BoardGui.Y_KILLSHOT_TRACK);
                killOnKillshotTrack.add(kill);
                board.getChildren().add(kill);

                if(map.get(player) > DOUBLE_KILL){
                    kill = new Rectangle(Gui.getScreenBounds().getWidth() / SCALE_RATIO_KILL_RECTANGLE, Gui.getScreenBounds().getWidth() / SCALE_RATIO_KILL_RECTANGLE, Color.valueOf(player.getColor().toString()));
                    kill.setStrokeType(StrokeType.OUTSIDE);
                    kill.setStroke(Color.BLACK);
                    kill.setTranslateX(boardWidth * (BoardGui.X_KILLSHOT_TRACK + i * BoardGui.GAP_KILLSHOT_TRACK));
                    kill.setTranslateY(boardHeight * BoardGui.Y_KILLSHOT_TRACK_OVERKILL);
                    killOnKillshotTrack.add(kill);
                    board.getChildren().add(kill);
                }
            }

            board.getChildren().removeAll(skulls);
            skulls.clear();
            while(i < matchView.getSkulls()){
                Circle skull = new Circle(boardWidth / SKULL_CIRCLE);
                skull.setFill(Color.RED);
                skull.setTranslateX(boardWidth * (BoardGui.X_KILLSHOT_TRACK + i * BoardGui.GAP_KILLSHOT_TRACK));
                skull.setTranslateY(boardHeight * BoardGui.Y_KILLSHOT_TRACK);
                board.getChildren().add(skull);
                skulls.add(skull);
                i++;
            }
            updateStateText();
        });
    }

    /**
     * Updates position and wallet of the specified player
     * @param player The player to be updated
     */
    public void updatePlayer(PlayerView player){
        Platform.runLater(() -> {
            updatePosition(player);
            updateWallet(player);
            updateStateText();
            //updateDamageTrack(player);
        });
    }

    /**
     * Updates the position of the specified player, removing him from the old one and adding to the new one
     * @param player The player to be updated
     */
    private void updatePosition(PlayerView player){
        if(player.getSquarePosition() != null){
            PlayerRectangle playerRectangle = getPlayerRectangle(player);
            HBox newPosition = playerPositionHBox.get(player.getSquarePosition());

            if(playerPositions.get(player) == null){

                if(newPosition != playerPositions.get(playerRectangle)){ //messo per evitare problemi con i doppi update
                    playerPositions.put(playerRectangle, newPosition);
                    newPosition.getChildren().add(playerRectangle);
                }
            } else {
                HBox oldPosition = playerPositions.get(playerRectangle);
                oldPosition.getChildren().remove(playerRectangle);
                newPosition.getChildren().add(playerRectangle);
                playerPositions.put(playerRectangle, newPosition);
                //elimino dall'hbox precedente e lo sposto in quello nuovo
            }
        }
    }

    /**
     * Updates the damageTrack of the specified player (damage, marks, skulls and check of frenzy mode are done)
     * @param player The player to Update
     */
    public void updateDamageTrack(PlayerView player){
        Platform.runLater(() -> {
            DamageTrack toUpdate = playerDamageTracks.get(player);
            toUpdate.updateDamage();
            toUpdate.updateMarks();
            toUpdate.updateSkulls();
            toUpdate.checkSwitchToFrenzy();
            updateStateText();
            //toUpdate.updateInfo();
        });

    }

    /**
     * Updates the wallet of the specified player
     * @param player The player to be updated
     */
    private void updateWallet(PlayerView player){
        DamageTrack damageTrackToUpdate = playerDamageTracks.get(player);
        damageTrackToUpdate.addAmmo(Color.BLUE, player.getWallet().getBlue());
        damageTrackToUpdate.addAmmo(Color.RED,player.getWallet().getRed());
        damageTrackToUpdate.addAmmo(Color.YELLOW, player.getWallet().getYellow());
        updateStateText();
    }

    /**
     * Updates layout, replacing all the AmmoButton and WeaponButton
     * @param layoutView The layout to be updated
     */
    public void updateLayout(LayoutView layoutView){
        Platform.runLater(() -> {
            board.getChildren().removeAll(ammoButtonsList);
            updateLayoutAmmo(layoutView.getSquares());

            board.getChildren().removeAll(weaponButtonList);
            updateLayoutWeapon(yellowWeapons, layoutView.getYellowWeapons());
            updateLayoutWeapon(blueWeapons, layoutView.getBlueWeapons());
            updateLayoutWeapon(redWeapons, layoutView.getRedWeapons());
        });
    }

    /**
     * Updates the WeaponButton of the specified WeaponView and color on layout, creating and adding new ones,
     * clearing the older ones, translating them with pixelWeapons
     * @param weapons The PixelWeapon, from which get the position of the weapon
     * @param weaponViews The WeaponViews from which create new WeaponButton
     */
    private void updateLayoutWeapon(List<PixelWeapon> weapons, List<WeaponView> weaponViews){
        int i = 0;
        for(WeaponView wp : weaponViews){
            WeaponButton weaponButton = new WeaponButton(wp, false);
            weaponButton.rescaleOnBoard();
            weaponButtonList.add(weaponButton);
            board.getChildren().add(weaponButton);
            weaponButton.setTranslateX(boardWidth * weapons.get(i).getX());
            weaponButton.setTranslateY(boardHeight * weapons.get(i).getY());
            weaponButton.setRotate(weapons.get(i).getRotate());
            i++;
        }
    }

    /**
     * Updates all AmmoButton, creating and adding them to layout, clearing old ones, translating them
     * with pixelPositions
     * @param squareViews SquareViews from which create new AmmoButton
     */
    private void updateLayoutAmmo(List<SquareView> squareViews){
        for(SquareView sv : squareViews){
            for(PixelPosition pixelPosition : pixelPositions){
                if(sv.getAmmo() != null && pixelPosition.equalsSquare(sv)){
                    AmmoButton ammoButton = new AmmoButton(sv);
                    ammoButtonsList.add(ammoButton);
                    board.getChildren().add(ammoButton);
                    ammoButton.setTranslateX(boardWidth * pixelPosition.getxAmmo());
                    ammoButton.setTranslateY(boardHeight * pixelPosition.getyAmmo());
                }
            }
        }
    }

    /**
     * Creates and adds playerPositionsHBox to layout, translating them with pixelPositions
     * @param matchView MatchView from wich creates PlayerPositionHBox
     */
    public void createPlayerPositionHBox(MatchView matchView){
        for(PixelPosition pp : pixelPositions){
            HBox newPlayerPositionHBox = new HBox();
            newPlayerPositionHBox.setTranslateX(boardWidth * pp.getxPlayer());
            newPlayerPositionHBox.setTranslateY(boardHeight * pp.getyPlayer());
            playerPositionHBox.put(matchView.getLayout().getSquare(pp.getxSquare(), pp.getySquare()), newPlayerPositionHBox);
            board.getChildren().add(newPlayerPositionHBox);
        }
    }

    /**
     * Creates and adds playerRectangles to layout, translating them with pixelPositions
     * @param matchView MatchView from which creates PlayerRectangle
     */
    private void createPlayerRectangle(MatchView matchView){
        for(PlayerView pv : matchView.getAllPlayers()){
            PlayerRectangle position = new PlayerRectangle(Gui.getScreenBounds().getWidth() / SCALE_RATIO_PLAYER_RECTANGLE_WIDTH, Gui.getScreenBounds().getWidth() / SCALE_RATIO_PLAYER_RECTANGLE_HEIGHT, Color.valueOf(pv.getColor().toString()), pv);
            playerPositions.put(position, null);
        }
    }

    /**
     * Creates the SquareButton, adding them, invisible, to layout and translating them with pixelPositions
     * @param matchView The MatchView from which get the coordinates of the square
     */
    public void createSquareRectangle(MatchView matchView){
        BoardGui.squareRectangles = new LinkedList<>();
        for(PixelPosition pp : pixelPositions){
            SquareRectangle newSquareRectangle = new SquareRectangle(boardWidth / SCALE_RATIO_SQUARE_RECTANGLE_WIDTH, boardHeight / SCALE_RATIO_SQUARE_RECTANGLE_HEIGHT, matchView.getLayout().getSquare(pp.getxSquare(), pp.getySquare()));
            newSquareRectangle.setTranslateX(boardWidth * pp.getxSelectable());
            newSquareRectangle.setTranslateY(boardHeight * pp.getySelectable());
            squareRectangles.add(newSquareRectangle);
            board.getChildren().add(newSquareRectangle);
        }
    }

    /**
     *
     * @param playerView The playerView from whom get the PlayerRectangle
     * @return The PlayerRectangle with the same playerView
     */
    private PlayerRectangle getPlayerRectangle(PlayerView playerView){
        for(PlayerRectangle pr : playerPositions.keySet()){
            if(pr.getPlayerView() == playerView){
                return pr;
            }
        }
        return null;
    }

    /**
     * Sets to invisible all squareRectangles
     */
    public static synchronized void setUnvisibleSquareRectangle(){
        for(SquareRectangle sr : squareRectangles){
            sr.setVisible(false);
        }
    }

    /**
     *
     * @return boardWidth
     */
    public static double getWidth(){
        return boardWidth;
    }
    /**
     *
     * @return boardHeight
     */
    public static double getHeight(){
        return boardHeight;
    }

    /**
     * Returns the color of each player
     * @param match The MatchView from which get colors
     * @return A List of Color
     */
    private List<Color> getAllColors(MatchView match){
        List<Color> colors = new LinkedList<>();
        for(PlayerView pv : match.getAllPlayers()){
            colors.add(Color.valueOf(pv.getColor().toString().toLowerCase()));
        }
        return colors;
    }

    private void updateStateText(){
        return;
    }
}
