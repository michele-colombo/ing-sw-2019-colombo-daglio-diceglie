package it.polimi.ingsw.client.user_interface.cli;

import java.util.List;

import static it.polimi.ingsw.client.user_interface.cli.CliUtils.prepareBorder;

/**
 * Generic window to display a list of things to the user
 */
public class TitleAndListWindow extends Window {
    /**
     * the title of the window
     */
    private String title;
    /**
     * the list of strings to display
     */
    private List<String> elements;

    /**
     * Builds the playing window
     * @param width the width of the playing window
     * @param height the height of the playing window
     * @param title the title of the window
     * @param titleColor the color of the title
     * @param elements the list of text elements to insert
     */
    public TitleAndListWindow(int width, int height, String title, String titleColor, List<String> elements) {
        super(width, height);
        this.title = title;
        this.elements = elements;

        MiniBox mb = new MiniBox(0,0, this.height, this.width);
        mb.insertSubBox(prepareBorder(mb.getHeight(), mb.getWidth()), 0, 0);
        mb.insertCenteredText(title.toUpperCase(), this.getWidth(), 1, titleColor);
        int i=0;
        for (String element : elements){
            mb.insertText(element, 2, i+3);
            i++;
        }
        this.addMiniBox(mb);
        this.build();
    }
}
