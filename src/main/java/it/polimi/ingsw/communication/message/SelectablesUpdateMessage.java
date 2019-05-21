package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.client.VisitorClient;

import java.util.List;

public class SelectablesUpdateMessage extends MessageVisitable {
    private List<String> selectableWeapons;
    private List<String> selectableSquares;
    private List<String> selectableModes;
    private List<String> selectableCommands;
    private List<String> selectableActions;
    private List<String> selectableColors;
    private List<String> selectablePlayers;
    private List<String> selectablePowerUps;

    @Override
    public void accept(VisitorClient visitorClient) {
        visitorClient.visit(this);
    }
}
