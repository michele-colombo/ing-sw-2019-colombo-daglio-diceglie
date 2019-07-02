package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents the message of game over
 */
public class GameOverMessage implements MessageVisitable, Serializable {
    /**
     * Final rank of the game; maps each player with his position
     */
    private Map<String, Integer> rank;
    /**
     * Final points of the game; maps each player with his points
     */
    private Map<String, Integer> points;

    /**
     * Creates GameOverMessage with given parameters
     * @param rank final rank of the game
     * @param points final points of each player
     */
    public GameOverMessage(Map<String, Integer> rank, Map<String, Integer> points) {
        this.rank = rank;
        this.points = points;
    }

    /**
     *
     * @return rank
     */
    public Map<String, Integer> getRank() {
        return rank;
    }

    /**
     *
     * @return points
     */
    public Map<String, Integer> getPoints() {
        return points;
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
