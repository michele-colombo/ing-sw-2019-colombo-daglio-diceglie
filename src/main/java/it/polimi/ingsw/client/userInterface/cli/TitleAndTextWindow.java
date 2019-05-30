package it.polimi.ingsw.client.userInterface.cli;

import static it.polimi.ingsw.client.userInterface.cli.CliUtils.*;

public class TitleAndTextWindow extends Window {
    private String title;
    private String text;

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
