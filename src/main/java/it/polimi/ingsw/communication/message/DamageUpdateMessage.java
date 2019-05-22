package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.util.List;
import java.util.Map;

public class DamageUpdateMessage extends MessageVisitable {
    private String name;
    private List<String> damageList;
    private Map<String, Integer> markMap;
    private int skulls;
    private boolean isFrenzy;

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }


}
