package it.polimi.ingsw;

public class Move implements MicroAction {
    private int movements;

    public Move(int movements) {
        this.movements = movements;
    }

    @Override
    public void act(Match match, Player p) {

    }
}
