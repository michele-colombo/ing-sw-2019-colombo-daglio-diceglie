package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PlayerState;

import java.util.List;

/**
 * Represents the task of grabbing, which involves movement to a certain square.
 * Actual grabbing is demanded to the collect method of squares.
 */
public class Grab implements MicroAction {
    /**
     * number of movements (on the map) allowed before grabbing
     */
    private int movements;

    /**
     * Builds the miro action, specifying the movements
     * @param movements the number of movements allowed before grabbing
     */
    public Grab(int movements) {
        this.movements = movements;
    }

    /**
     * Prepare the list of selectable squares, according to the movements allowed
     * @param match the current match
     * @param p the player which is grabbing
     */
    @Override
    public void act(Match match, Player p) {
        p.setState(PlayerState.GRAB_THERE);
        p.resetSelectables();
        List<Square> temp;
        temp = match.getLayout().getSquaresInDistanceRange(p.getSquarePosition(), 0, movements);
        temp.removeAll(match.getLayout().getEmptySquares());
        p.setSelectableSquares(temp);
    }

    /**
     * Gets the string identifier of the microaction
     * @return a string the identifies the microaction
     */
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
