package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.server.model.enums.PlayerColor;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the update the match starts with
 */
public class StartMatchUpdateMessage implements MessageVisitable, Serializable {
    /**
     * Chosen layout configuration
     */
    private int layoutConfiguration;
    /**
     * Names of all players
     */
    private List<String> names;
    /**
     * Colors of all players
     */
    private List<PlayerColor> colors;

    /**
     * Creates a StartMatchUpdate with given parameters
     * @param layoutConfiguration chosen layout configuration
     * @param names names of all players
     * @param colors colors of all players
     */
    public StartMatchUpdateMessage(int layoutConfiguration, List<String> names, List<PlayerColor> colors) {
        this.layoutConfiguration = layoutConfiguration;
        this.names = names;
        this.colors = colors;
    }

    /**
     *
     * @return layoutConfiguration
     */
    public int getLayoutConfiguration() {
        return layoutConfiguration;
    }

    /**
     *
     * @return names
     */
    public List<String> getNames() {
        return names;
    }

    /**
     *
     * @return colors
     */
    public List<PlayerColor> getColors() {
        return colors;
    }

    /**
     * Method used to properly recognize dynamic type of Message
     * @param messageVisitor messageVisitor who "visits" this message
     */
    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
