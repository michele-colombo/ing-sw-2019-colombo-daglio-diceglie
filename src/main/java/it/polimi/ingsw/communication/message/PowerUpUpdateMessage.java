package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class PowerUpUpdateMessage extends MessageVisitable {
    private String name;
    private List<String> powerUps;
    private int numPowerUps;

    public PowerUpUpdateMessage(String name, List<String> powerUps, int numPowerUps) {
        this.name = name;
        this.powerUps = powerUps;
        this.numPowerUps = numPowerUps;
    }

    public PowerUpUpdateMessage(String name, int numPowerUps) {
        this.name = name;
        this.powerUps = new ArrayList<>();
        this.numPowerUps = numPowerUps;
    }

    public String getName() {
        return name;
    }

    public List<String> getPowerUps() {
        return powerUps;
    }

    public int getNumPowerUps() {
        return numPowerUps;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) throws RemoteException {
        messageVisitor.visit(this);
    }
}
