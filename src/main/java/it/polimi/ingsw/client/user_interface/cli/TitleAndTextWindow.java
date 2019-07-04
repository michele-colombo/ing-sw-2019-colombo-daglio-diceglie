package it.polimi.ingsw.client.user_interface.cli;

import static it.polimi.ingsw.client.user_interface.cli.CliUtils.*;

/**
 * Generic window to display a text to the user
 */
public class TitleAndTextWindow extends Window {
    /**
     * the title of the window
     */
    private String title;
    /**
     * the text content of the window
     */
    private String text;

    /**
     /**
     * Builds the text window
     * @param width the width of the playing window
     * @param height the height of the playing window
     * @param title the title of the window
     * @param titleColor the color of the title
     * @param text the text to insert
     */
    public TitleAndTextWindow(int width, int height, String title, String titleColor, String text) {
        super(width, height);
        this.title = title;
        this.text = text;

        MiniBox mb = new MiniBox(0,0, this.height, this.width);
        mb.insertSubBox(prepareBorder(mb.getHeight(), mb.getWidth()), 0, 0);
        mb.insertCenteredText(title.toUpperCase(), this.getWidth(), 1, titleColor);
        mb.insertTextMultiline(text, 2, (mb.getHeight()-4)/2, mb.getWidth()-4);
        this.addMiniBox(mb);
        this.build();
    }
}
