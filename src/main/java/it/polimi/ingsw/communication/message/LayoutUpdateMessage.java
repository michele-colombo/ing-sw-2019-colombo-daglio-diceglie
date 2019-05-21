package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.client.VisitorClient;

import java.util.List;
import java.util.Map;

public class LayoutUpdateMessage extends MessageVisitable {
    private List<String> blueWeapon;
    private List<String> redWeapon;
    private List<String> yellowWeapon;

    private Map<String, String> ammoTilesInSquares;

    @Override
    public void accept(VisitorClient visitorClient) {
        visitorClient.visit(this);
    }
}
