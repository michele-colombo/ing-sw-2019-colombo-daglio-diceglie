package it.polimi.ingsw.client.model_view;

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
     * Gets title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets cost
     * @return cost
     */
    public Cash getCost() {
        return cost;
    }
}
