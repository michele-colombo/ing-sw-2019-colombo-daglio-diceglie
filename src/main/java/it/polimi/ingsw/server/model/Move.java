package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PlayerState;

public class Move implements MicroAction {
    private int movements;

    public Move(int movements) {
        this.movements = movements;
    }

    @Override
    public void act(Match match, Player p) {
        p.setState(PlayerState.MOVE_THERE);
        p.resetSelectables();
        p.setSelectableSquares(match.getLayout().getSquaresInDistanceRange(p.getSquarePosition(), 0, movements));
    }

    @Override
    public String toString(){
        String s = "";
        for (int i=0; i<movements; i++){
            s = s + ">";
        }
        return s;
    }
}
