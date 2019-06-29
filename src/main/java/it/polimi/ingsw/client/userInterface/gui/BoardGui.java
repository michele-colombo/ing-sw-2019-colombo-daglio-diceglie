package it.polimi.ingsw.client.userInterface.gui;

import it.polimi.ingsw.client.*;
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
    private static final String LAYOUT_PNG_FOLDER = "resources/layoutPNG/";


    private final static double X_KILLSHOT_TRACK = 0.0909;
    private final static double Y_KILLSHOT_TRACK = 0.0921;
    private final static double Y_KILLSHOT_TRACK_OVERKILL = 0.025;
    private final static double GAP_KILLSHOT_TRACK = 0.0421;
    private final static double SCALE_RATIO_SELECTABLE_RECTANGLE_X = 7.68;
    private final static double SCALE_RATIO_SELECTABLE_RECTANGLE_Y = 5.25;
    private final static double SKULL_CIRCLE = 76.08;
    private static final double COLOR_BUTTON_SIZE = Gui.getScreenBounds().getWidth() / 68;

    private final GridPane view;
    private HBox weaponBox;
    private HBox powerUpBox;
    private GridPane connectionState;
    private AnchorPane board;
    private List<Label> connectionLabels;
    private Label currentPlayer;
    private GridPane selectables;
    private List<PixelPosition> pixelPositions;
    private List<AmmoButton> ammoButtonsList;
    private List<WeaponButton> weaponButtonList;
    private List<PixelWeapon> yellowWeapons;
    private List<PixelWeapon> blueWeapons;
    private List<PixelWeapon> redWeapons;
    private static List<SquareRectangle> squareRectangles;
    private Map<PlayerRectangle, HBox> playerPositions;
    private Map<SquareView, HBox> playerPositionHBox;
    private Map<PlayerView, DamageTrack> playerDamageTracks;
    private List<Rectangle> killOnKillshotTrack;
    private List<Circle> skulls;
    private Text stateText;
    private static double boardWidth;
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
        yellowWeapons = parser.loadWeaponResource("yellow");
        blueWeapons = parser.loadWeaponResource("blue");
        redWeapons = parser.loadWeaponResource("red");
        playerPositions = new HashMap<>();
        playerPositionHBox = new HashMap<>();
        squareRectangles = new LinkedList<>();
        playerDamageTracks = new HashMap<>();
        killOnKillshotTrack = new LinkedList<>();
        skulls = new LinkedList<>();
        //createPlayerPositionHBox(match);
        createPlayerRectangle(match);
        //selectables.setDisable(true);

        view.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //view.setAlignment(Pos.CENTER);
        view.setHgap(5);
        view.setVgap(15);

        board = new AnchorPane();
        board.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //board.setAlignment(Pos.TOP_LEFT);
        board.setPadding(new Insets(0,0,0,0));

        InputStream boardUrl = getClass().getClassLoader().getResourceAsStream(LAYOUT_PNG_FOLDER + "layout" + match.getLayout().getLayoutConfiguration() + ".png");
        Image image = new Image(boardUrl);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight()/1.5);
        imageView.setFitWidth(Gui.getScreenBounds().getWidth()/1.5);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        boardWidth = imageView.boundsInParentProperty().get().getWidth();
        boardHeight = imageView.boundsInParentProperty().get().getHeight();

        board.getChildren().addAll(imageView);
        GridPane.setHalignment(imageView, HPos.LEFT);
        stateText = new Text();
        stateText.setFill(Color.WHITE);
        view.add(board, 0,0);
        view.add(weaponBox, 0,1);
        view.add(powerUpBox, 1, 1);
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
        damageTracks.setVgap(5);
        damageTracks.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        damageTracks.setAlignment(Pos.TOP_CENTER);

        DamageTrack damageTrack = new DamageTrack(colors, match.getMyPlayer());
        damageTrack.updateInfo();
        playerDamageTracks.put(match.getMyPlayer(), damageTrack);
        damageTracks.add(damageTrack, 0, 0);

        int i = 1;
        for(PlayerView pv : match.getAllPlayers()){
            if(pv.getColor() != match.getMyPlayer().getColor()){
                DamageTrack otherDamageTrack = new DamageTrack(colors, pv);
                otherDamageTrack.updateInfo();
                playerDamageTracks.put(pv, otherDamageTrack);
                damageTracks.add(otherDamageTrack, 0, i);
            }
            i++;
        }
        view.add(damageTracks, 1,0);
    }

    /**
     * Create connection state GridPane and add it to view; it contains a label as title of the GridPane, and another one
     * showing the current player (in white)
     * @param players List of Player, from whom get the current one
     */
    private void addConnectionState(List<PlayerView> players ){
        connectionLabels = new ArrayList<>();
        connectionState = new GridPane();
        currentPlayer = new Label("Current: " + players.get(0).getName()); //todo forse da fixare
        currentPlayer.setTextFill(Color.WHITE);
        currentPlayer.setWrapText(true);
        Label connectionLabel = new Label("PLAYERS");
        connectionLabel.setTextFill(Color.GHOSTWHITE);
        connectionState.add(connectionLabel, 0, 0);
        connectionState.setVgap(10);
        connectionState.setHgap(10);
        connectionState.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        connectionState.add(currentPlayer, 0, 1);
        view.add(connectionState, 4,0);
    }

    /**
     * Add selectables GridPane in view
     */
    private void addSelectables(){
        view.add(selectables,4,1);
    }

    /**
     * Update connection state, adding two label for each player (and clearing old ones)
     * : the former contains own name, the latter their
     * connection state: this one is green if the respective player is online, otherwise is red
     * @param connections
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
                playerLabel.setMaxWidth(Gui.getScreenBounds().getWidth()/20);
                Label onlineLabel;
                if(connections.get(pv.getName())){
                    onlineLabel = new Label("online");
                    onlineLabel.setTextFill(Color.GREEN);
                    onlineLabel.setWrapText(true);
                } else {
                    onlineLabel = new Label("offline");
                    onlineLabel.setTextFill(Color.RED);
                    onlineLabel.setWrapText(true);
                }
                onlineLabel.setMaxWidth(Gui.getScreenBounds().getWidth()/20);
                connectionState.add(playerLabel, 0, i);
                connectionState.add(onlineLabel, 1, i);
                connectionLabels.add(playerLabel);
                connectionLabels.add(onlineLabel);
                i++;
            }
            if(Gui.getClient().getMatch().getCurrentPlayer() != null){
                currentPlayer.setText("Current: " + Gui.getClient().getMatch().getCurrentPlayer());
            }
            updateStateText();
        });
    }

    /**
     * Update currentPlayer with the name of the current one
     */
    public void updateCurrentPlayer(){
        Platform.runLater(() -> {
            currentPlayer.setText("Current: " + Gui.getClient().getMatch().getCurrentPlayer());
            updateStateText();
        });
    }

    /**
     * Updates your own power up, clearing old ones and recreating them
     * @param player
     */
    public void updatePowerUp(PlayerView player){
        Platform.runLater(() -> {
            MyPlayer me = Gui.getClient().getMatch().getMyPlayer();
                if(player.getName().equals(me.getName())){
                    powerUpBox.getChildren().clear();
                    for(PowerUpView powerUpView : me.getPowerUps()){
                        PowerUpButton powerUpButton = new PowerUpButton(powerUpView);
                        powerUpBox.getChildren().add(powerUpButton);
                        powerUpBox.setHgrow(powerUpButton, Priority.ALWAYS);
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
                button.setOnMouseClicked((MouseEvent) ->
                {
                    try {
                        Gui.getClient().selected(action);
                    }
                    catch (WrongSelectionException e){
                        System.out.println("Wrong selection");
                    }
                });

                selectables.add(button, index%3, index/3);
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

                selectables.add(button, index%3, index/3);
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
                selectables.add(button, index%3, index/3);
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
     * Create and add WeaponButtons of your weapon (clearing old ones). If they're unloaded, they have low opacity.
     * @param player
     */
    public void updateWeapons(PlayerView player){
        Platform.runLater(() -> {
            if(player.getName().equals(Gui.getClient().getMatch().getMyPlayer().getName())){
                weaponBox.getChildren().clear();
                for(WeaponView weaponView : Gui.getClient().getMatch().getMyPlayer().getWeapons().keySet()){
                    WeaponButton newWeapon = new WeaponButton(weaponView, true);
                    if(Gui.getClient().getMatch().getMyPlayer().getUnloadedWeapons().contains(newWeapon.getWeaponView())){
                        newWeapon.setOpacity(0.1);
                    } else{
                        newWeapon.setOpacity(1);
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
     * Update KillshotTrack, creating and adding a kill for each skull removed (max eight), then remaining skulls
     * are added (old kills and skulls are removed)
     */
    public void updateKillshotTrack(){
        Platform.runLater(() -> {
            MatchView matchView = Gui.getClient().getMatch();
            board.getChildren().removeAll(killOnKillshotTrack);
            killOnKillshotTrack.clear();

            int i;
            for(i = 0; i < matchView.getTrack().size() && i < 8; i++){
                Map<PlayerView, Integer> map = matchView.getTrack().get(i);
                PlayerView player = new ArrayList<>(map.keySet()).get(0);
                Rectangle kill = new Rectangle(10, 10, Color.valueOf(player.getColor().toString()));
                kill.setStrokeType(StrokeType.OUTSIDE);
                kill.setStroke(Color.BLACK);
                kill.setTranslateX(boardWidth * (BoardGui.X_KILLSHOT_TRACK + i * BoardGui.GAP_KILLSHOT_TRACK));
                kill.setTranslateY(boardHeight * BoardGui.Y_KILLSHOT_TRACK);
                killOnKillshotTrack.add(kill);
                board.getChildren().add(kill);

                if(map.get(player) > 1){
                    kill = new Rectangle(10, 10, Color.valueOf(player.getColor().toString()));
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
     * Update position, wallet of the specified player
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
     * Update the position of the specified player, removing him from the old one and adding to the new one
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
     * Update the damageTrack of the specified player (damage, marks, skulls and check of frenzy mode are done)
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
     * Updates the board, replacing all the AmmoButton and WeaponButton
     * @param layoutView
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
     * Updates the WeaponButton of the specified WeaponView and color on the board, creating and adding new ones,
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
     * Update all AmmoButton, creating and adding them to the board, clearing old ones, translating them
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
     * Creates and adds playerPositionsHBox to the board, translating them with pixelPositions
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
     * Creates and adds playerRectangles to the board, translating them with pixelPositions
     * @param matchView MatchView from which creates PlayerRectangle
     */
    private void createPlayerRectangle(MatchView matchView){
        for(PlayerView pv : matchView.getAllPlayers()){
            PlayerRectangle position = new PlayerRectangle(5, 20, Color.valueOf(pv.getColor().toString()), pv);
            playerPositions.put(position, null);
        }
    }

    /**
     * Creates the SquareButton, adding them to the board and translating them with pixelPositions
     * @param matchView The MatchView from which get the coordinates of the square
     */
    public void createSelectableRectangle(MatchView matchView){
        for(PixelPosition pp : pixelPositions){
            SquareRectangle newSquareRectangle = new SquareRectangle(boardWidth / SCALE_RATIO_SELECTABLE_RECTANGLE_X, boardHeight / SCALE_RATIO_SELECTABLE_RECTANGLE_Y, matchView.getLayout().getSquare(pp.getxSquare(), pp.getySquare()));
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
    public synchronized static void setUnvisibleSquareRectangle(){
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
