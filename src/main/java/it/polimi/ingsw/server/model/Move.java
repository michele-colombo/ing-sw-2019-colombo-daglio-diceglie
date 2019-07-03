package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PlayerState;

/**
 * Represents the task of moving to a certain square.
 */
public class Move implements MicroAction {
    /**
     * the number of movements allowed on the map in this single move
     */
    private int movements;

    /**
     * Builds the micro action with the correct number of movements
     * @param movements
     */
    public Move(int movements) {
        this.movements = movements;
    }

    /**
     * Prepares the selectable squares allowed for moving to
     * @param match the current match
     * @param p the player which is taking the action
     */
    @Override
    public void act(Match match, Player p) {
        p.setState(PlayerState.MOVE_THERE);
        p.resetSelectables();
        p.setSelectableSquares(match.getLayout().getSquaresInDistanceRange(p.getSquarePosition(), 0, movements));
    }

    /**
     * Gets a string that uniquely describes this microaction
     * @return a unique string (according to the number of movements)
     */
    @Override
    public String toString(){
        String s = "";
        for (int i=0; i<movements; i++){
            s = s + ">";
        }
        return s;
    }
}
