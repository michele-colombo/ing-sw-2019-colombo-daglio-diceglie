package it.polimi.ingsw;

public class Grab implements MicroAction {
    private int movements;

    public Grab(int movements) {
        this.movements = movements;
    }

    @Override
    public void act(Match match, Player p) {

    }

}
