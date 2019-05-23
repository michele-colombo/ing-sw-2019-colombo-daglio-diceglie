package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.util.List;
import java.util.Map;

public class LayoutUpdateMessage extends MessageVisitable {
    private List<String> blueWeapons;
    private List<String> redWeapons;
    private List<String> yellowWeapons;

    private Map<String, String> ammoTilesInSquares;

    public LayoutUpdateMessage(List<String> blueWeapons, List<String> redWeapons, List<String> yellowWeapons, Map<String, String> ammoTilesInSquares) {
        this.blueWeapons = blueWeapons;
        this.redWeapons = redWeapons;
        this.yellowWeapons = yellowWeapons;
        this.ammoTilesInSquares = ammoTilesInSquares;
    }

    public List<String> getBlueWeapons() {
        return blueWeapons;
    }

    public List<String> getRedWeapons() {
        return redWeapons;
    }

    public List<String> getYellowWeapons() {
        return yellowWeapons;
    }

    public Map<String, String> getAmmoTilesInSquares() {
        return ammoTilesInSquares;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
