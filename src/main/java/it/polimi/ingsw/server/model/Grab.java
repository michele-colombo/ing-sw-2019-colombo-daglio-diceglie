package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PlayerState;

import java.util.List;

public class Grab implements MicroAction {
    private int movements;

    public Grab(int movements) {
        this.movements = movements;
    }

    @Override
    public void act(Match match, Player p) {
        p.setState(PlayerState.GRAB_THERE);
        p.resetSelectables();
        List<Square> temp;
        temp = match.getLayout().getSquaresInDistanceRange(p.getSquarePosition(), 0, movements);
        temp.removeAll(match.getLayout().getEmptySquares());
        p.setSelectableSquares(temp);
    }

    @Override
    public String toString(){
        String s = "";
        for (int i=0; i<movements; i++){
            s = s + ">";
        }
        s = s + "G";
        return s;
    }

}
