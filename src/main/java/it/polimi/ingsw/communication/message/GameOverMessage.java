package it.polimi.ingsw.communication.message;

import it.polimi.ingsw.communication.MessageVisitor;

import java.util.Map;

public class GameOverMessage extends MessageVisitable {
    Map<String, Integer> rank;
    Map<String, Integer> points;

    public GameOverMessage(Map<String, Integer> rank, Map<String, Integer> points) {
        this.rank = rank;
        this.points = points;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }

    public Map<String, Integer> getRank() {
        return rank;
    }

    public Map<String, Integer> getPoints() {
        return points;
    }
}
