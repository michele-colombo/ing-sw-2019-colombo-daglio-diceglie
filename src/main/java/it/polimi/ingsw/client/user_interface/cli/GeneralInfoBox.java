package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.MatchView;

public class GeneralInfoBox extends MiniBox {
    public GeneralInfoBox(int x, int y, int height, int width) {
        super(x, y, height, width);
    }

    @Override
    public void update(MatchView match) {
        String title = "ADRENALINA";
        insertText(title, (width-title.length())/2, 1);
        insertText("A line to display server messages", 1, 2);
        insertText("A line for other messages", 1, 3);
    }
}
