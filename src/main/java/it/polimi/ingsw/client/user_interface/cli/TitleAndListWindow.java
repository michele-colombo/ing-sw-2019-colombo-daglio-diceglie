package it.polimi.ingsw.client.user_interface.cli;

import java.util.List;

import static it.polimi.ingsw.client.user_interface.cli.CliUtils.prepareBorder;

public class TitleAndListWindow extends Window {
    private String title;
    private List<String> elements;

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
