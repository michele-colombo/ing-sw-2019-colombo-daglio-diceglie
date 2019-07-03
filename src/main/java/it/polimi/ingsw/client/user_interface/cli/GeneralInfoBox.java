package it.polimi.ingsw.client.user_interface.cli;

import it.polimi.ingsw.client.MatchView;

/**
 * Contains the title of the playing window
 */
public class GeneralInfoBox extends MiniBox {
    /**
     * Builds the box specifying all its parameters (except its content)
     * @param x the x position int the window (from the left side)
     * @param y the y position in the window (from the top)
     * @param height the height of this box
     * @param width the width of this box
     */
    public GeneralInfoBox(int x, int y, int height, int width) {
        super(x, y, height, width);
    }

    /**
     * Updates teh content of the box with the title
     * @param match the match from which retrieve information
     */
    @Override
    public void update(MatchView match) {
        String title = "ADRENALINE";
        insertText(title, (width-title.length())/2, 1);
    }
}
