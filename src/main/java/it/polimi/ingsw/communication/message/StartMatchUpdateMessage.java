package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.server.model.enums.PlayerColor;

import java.util.List;

public class StartMatchUpdateMessage extends MessageVisitable{
    private int layoutConfiguration;
    private List<String> names;
    private List<PlayerColor> colors;

    public StartMatchUpdateMessage(int layoutConfiguration, List<String> names, List<PlayerColor> colors) {
        this.layoutConfiguration = layoutConfiguration;
        this.names = names;
        this.colors = colors;
    }

    public int getLayoutConfiguration() {
        return layoutConfiguration;
    }

    public List<String> getNames() {
        return names;
    }

    public List<PlayerColor> getColors() {
        return colors;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
