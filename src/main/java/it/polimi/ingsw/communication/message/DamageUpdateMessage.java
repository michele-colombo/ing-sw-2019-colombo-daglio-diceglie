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

    public DamageUpdateMessage(String name, List<String> damageList, Map<String, Integer> markMap, int skulls, boolean isFrenzy) {
        this.name = name;
        this.damageList = damageList;
        this.markMap = markMap;
        this.skulls = skulls;
        this.isFrenzy = isFrenzy;
    }

    public String getName() {
        return name;
    }

    public List<String> getDamageList() {
        return damageList;
    }

    public Map<String, Integer> getMarkMap() {
        return markMap;
    }

    public int getSkulls() {
        return skulls;
    }

    public boolean isFrenzy() {
        return isFrenzy;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }


}
