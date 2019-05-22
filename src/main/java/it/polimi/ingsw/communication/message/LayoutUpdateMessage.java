package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.util.List;
import java.util.Map;

public class LayoutUpdateMessage extends MessageVisitable {
    private List<String> blueWeapon;
    private List<String> redWeapon;
    private List<String> yellowWeapon;

    private Map<String, String> ammoTilesInSquares;

    public LayoutUpdateMessage(List<String> blueWeapon, List<String> redWeapon, List<String> yellowWeapon, Map<String, String> ammoTilesInSquares) {
        this.blueWeapon = blueWeapon;
        this.redWeapon = redWeapon;
        this.yellowWeapon = yellowWeapon;
        this.ammoTilesInSquares = ammoTilesInSquares;
    }

    public List<String> getBlueWeapon() {
        return blueWeapon;
    }

    public List<String> getRedWeapon() {
        return redWeapon;
    }

    public List<String> getYellowWeapon() {
        return yellowWeapon;
    }

    public Map<String, String> getAmmoTilesInSquares() {
        return ammoTilesInSquares;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }
}
