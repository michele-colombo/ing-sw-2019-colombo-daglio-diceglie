package it.polimi.ingsw.client.userInterface;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardGui {
    private final GridPane view;
    private GridPane connectionState;
    private GridPane damageTracks;
    private Map<PlayerView, Boolean> connectionText;

    public BoardGui(MatchView match){
        view = new GridPane();
        view.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //view.setAlignment(Pos.CENTER);
        view.setHgap(15);
        view.setVgap(15);

        GridPane board = new GridPane();
        board.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        board.setAlignment(Pos.TOP_LEFT);
        board.setPadding(new Insets(0,0,0,0));

        final ImageView ivTarget = new ImageView();
        //board.add(ivTarget, 0,0);
        Image image = new Image("layoutPNG/layout" + match.getLayout().getLayoutConfiguration() + ".png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight());
        imageView.setFitWidth(Gui.getScreenBounds().getHeight());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        /*imageView.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("deh");
                event.consume();
            }
        });
        imageView.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("yes");
                int x = (int) mouseEvent.getX();
                int y = (int) mouseEvent.getY();
                PixelReader reader = image.getPixelReader();
                Image currentFrame = new WritableImage(reader, x, y, 200, 200);
                ivTarget.setImage(image);
            }
        });*/
        board.getChildren().add(imageView);
        GridPane.setHalignment(imageView, HPos.LEFT);
        view.add(board, 0,0);
        view.add(ivTarget,0,0);
        addDamageTrack(match); //todo utilizzare il messaggio di startMatchUpdate
        addConnectionState(match.getAllPlayers());
    }

    public Parent getView(){
        return view;
    }

    //todo la prima damagetrack è quella del player corrente
    public void addDamageTrack(MatchView match){
        damageTracks = new GridPane();
        damageTracks.setVgap(5);
        damageTracks.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        damageTracks.setAlignment(Pos.TOP_CENTER);

        Image image = new Image("damageTracks/dmg" + match.getMyPlayer().getColor().toString().toLowerCase() + ".png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(Gui.getScreenBounds().getHeight());
        imageView.setFitWidth(Gui.getScreenBounds().getWidth()/4);
        imageView.setPreserveRatio(true);
        damageTracks.add(imageView, 0, 0);

        int i = 1;
        for(PlayerView pv : match.getAllPlayers()){
            if(pv.getColor() != match.getMyPlayer().getColor()){
                image = new Image("damageTracks/dmg" + pv.getColor().toString().toLowerCase() + ".png");
                imageView = new ImageView(image);
                imageView.setFitHeight(Gui.getScreenBounds().getHeight());
                imageView.setFitWidth(Gui.getScreenBounds().getWidth()/4);
                imageView.setPreserveRatio(true);
                damageTracks.add(imageView, 0, i);
            }
            i++;
        }
        view.add(damageTracks, 1,0);
    }

    public void addConnectionState(List<PlayerView> players ){
        this.connectionText = new HashMap<>();
        connectionState = new GridPane();
        Label connectionLabel = new Label("PLAYERS CONNECTION");
        connectionLabel.setTextFill(Color.GHOSTWHITE);
        connectionState.add(connectionLabel, 0, 0);
        connectionState.setVgap(10);
        connectionState.setHgap(10);
        connectionState.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        view.add(connectionState, 4,0);
        int i = 1;
        for(PlayerView pv : players){
            Label playerLabel = new Label(pv.getName());
            playerLabel.setTextFill(Color.WHITE);
            Label onlineLabel = new Label("online");
            onlineLabel.setTextFill(Color.GREEN);
            playerLabel.setWrapText(true);
            playerLabel.setMaxWidth(Gui.getScreenBounds().getWidth()/20);
            //playerLabel.textProperty().bind()
            onlineLabel.setWrapText(true);
            onlineLabel.setMaxWidth(Gui.getScreenBounds().getWidth()/20);
            connectionState.add(playerLabel, 0, i);
            connectionState.add(onlineLabel, 1, i);
            this.connectionText.put(pv, true);
            i++;
        }
    }

    public void updateConnection(Map<String, Boolean> connections){

    }

}
