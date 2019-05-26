package it.polimi.ingsw.client.userInterface.cli;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;

import java.util.ArrayList;
import java.util.Map;

import static it.polimi.ingsw.client.userInterface.cli.CliUtils.*;

public class KillShotTrackBox extends MiniBox {

    public KillShotTrackBox(int x, int y, int height, int width) {
        super(x, y, height, width);
    }

    @Override
    public void update(MatchView match) {
        stringBox = prepareBorder(height, width);
        String title = "KILLSHOT TRACK:";
        insertText(title, (width-title.length())/2, 1);
        for (int i=0; i<match.getTrack().size(); i++){
            Map<PlayerView, Integer> map = match.getTrack().get(i);
            PlayerView player;
            int nDrops;
            player = new ArrayList<>(map.keySet()).get(0);
            nDrops = map.get(player);
            this.insertText(DROP+" ", 2+i*2, 3, printColorOf(player.getColor()));
            if (nDrops > 1) this.insertText(DROP+" ", 2+i*2, 2, printColorOf(player.getColor()));
        }
        for (int i=0; i<match.getSkulls(); i++){
            this.insertText(SKULL+" ", 2+i*2, 3);
        }
    }
}
