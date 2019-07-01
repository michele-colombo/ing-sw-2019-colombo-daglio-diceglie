package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Cash;

/**
 * Represents Mode of weapons on client
 */
public class ModeView {
    /**
     * Title of the ModeView
     */
    private String title;
    /**
     * Description of the ModeView
     */
    private String description;
    /**
     * The costs (in ammos) of the ModeView
     */
    private Cash cost;

    /**
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return cost
     */
    public Cash getCost() {
        return cost;
    }
}
