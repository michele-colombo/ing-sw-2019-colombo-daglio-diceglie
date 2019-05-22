package it.polimi.ingsw.communication.events;

import it.polimi.ingsw.communication.EventVisitor;

public class SquareSelectedEvent extends EventVisitable{
    private int selection;
    private String nickname;

    public SquareSelectedEvent(int selection, String nickname) {
        this.selection = selection;
        this.nickname = nickname;
    }

    public int getSelection() {
        return selection;
    }

    public String getNickname() {
        return nickname;
    }

    public void accept(EventVisitor eventVisitor){
        eventVisitor.visit(this);
    }
}
