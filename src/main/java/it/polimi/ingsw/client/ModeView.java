package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Cash;

public class ModeView {
    private String title;
    private String description;
    private Cash cost;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Cash getCost() {
        return cost;
    }
}
