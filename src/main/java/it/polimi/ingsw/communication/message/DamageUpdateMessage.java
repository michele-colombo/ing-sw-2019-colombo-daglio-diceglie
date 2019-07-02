package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents the update of a player's DamageTrack
 */
public class DamageUpdateMessage implements MessageVisitable, Serializable {
    /**
     * Player's name
     */
    private String name;
    /**
     * Contains damagers' names
     */
    private List<String> damageList;
    /**
     * Map players' name with their mark on DamageTrack updated
     */
    private Map<String, Integer> markMap;
    /**
     * Skull on DamageTrack updated
     */
    private int skulls;
    /**
     * True if player is in frenzy mode, else false
     */
    private boolean isFrenzy;

    /**
     * Creates DamageUpdateMessage with given parameters
     * @param name player's name updated
     * @param damageList damage on damageTrack updated
     * @param markMap marks on damageTrack updated
     * @param skulls skulls on damageTrack updated
     * @param isFrenzy player's frenzy situation
     */
    public DamageUpdateMessage(String name, List<String> damageList, Map<String, Integer> markMap, int skulls, boolean isFrenzy) {
        this.name = name;
        this.damageList = damageList;
        this.markMap = markMap;
        this.skulls = skulls;
        this.isFrenzy = isFrenzy;
    }

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return damageList
     */
    public List<String> getDamageList() {
        return damageList;
    }

    /**
     *
     * @return markMap
     */
    public Map<String, Integer> getMarkMap() {
        return markMap;
    }

    /**
     *
     * @return skulls
     */
    public int getSkulls() {
        return skulls;
    }

    /**
     *
     * @return isFrenzy
     */
    public boolean isFrenzy() {
        return isFrenzy;
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
