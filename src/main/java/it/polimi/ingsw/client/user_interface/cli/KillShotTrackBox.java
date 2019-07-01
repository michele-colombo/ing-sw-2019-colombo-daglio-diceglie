package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.PlayerView;

import java.util.ArrayList;
import java.util.Map;

import static it.polimi.ingsw.client.user_interface.cli.CliUtils.*;

public class KillShotTrackBox extends MiniBox {

    public KillShotTrackBox(int x, int y, int height, int width) {
        super(x, y, height, width);
    }

    @Override
    public void update(MatchView match) {
        if (!match.isFrenzyOn()){
            stringBox = prepareBorder(height, width);
        } else {
            stringBox = prepareBorder(height, width, RED);
        }
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
            this.insertText(SKULL+" ", match.getTrack().size()*2 + 2+i*2, 3);
        }
    }
}
