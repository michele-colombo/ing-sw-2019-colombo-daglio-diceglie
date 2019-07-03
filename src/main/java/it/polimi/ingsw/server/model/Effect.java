package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.ApplyEffectImmediatelyException;
import it.polimi.ingsw.server.model.enums.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Effect {

    public static final int WHO_TO_DAMAGE_SEL_PLAYER = 0;
    public static final int STARTING_POINT_ME = 0;
    public static final int STARTING_POINT_LAST_DAMAGED = 1;
    public static final int STARTING_POINT_TEMP_SQUARE = 2;
    public static final int STARTING_POINT_TEMP_PLAYER = 3;
    public static final int DONT_CARE = -1;
    public static final int RECIPROCAL_POS_NOT_VISIBLE = 0;
    public static final int RECIPROCAL_POS_VISIBLE = 1;
    public static final int RECIPROCAL_POS_CARDINAL_DIR = 2;
    public static final int RECIPROCAL_POS_LINE_LAST_DAM = 3;
    public static final int DIVERSITY_ALREADY_DAM = 0;
    public static final int DIVERSITY_NOT_DAM_YET_PLAYERS = 1;
    public static final int DIVERSITY_NOT_DAM_YET_SQUARES = 2;
    public static final int DIVERSITY_NOT_MY_ROOM = 3;
    public static final int WHAT_NOTHING_TO_SEL_NO_LIMIT = -1;
    public static final int WHAT_SQUARE_NO_LIMIT = 0;
    public static final int WHAT_PLAYER_NO_LIMIT = 1;
    public static final int WHAT_NOTHING_TO_SEL_SOMEONE_ON_MY_SQUARE = 2;
    public static final int WHAT_SQUARE_SOMEONE_IN_ROOM = 3;
    public static final int WHAT_SQUARE_SOMEONE_ON_IT = 4;
    public static final int WHAT_NOTHING_SOMEONE_NEAR_ME = 5;
    public static final int SET_TEMP_SQUARE_DONT = -1;
    public static final int SET_TEMP_SQUARE_SEL_SQ = 0;
    public static final int SET_TEMP_SQUARE_SEL_PLAYERS_SQ = 1;
    public static final int SEL_TEMP_SQUARE_MY_SQ = 2;
    public static final int WHO_TO_MOVE_NOBODY = -1;
    public static final int WHO_TO_MOVE_ME = 0;
    public static final int WHO_TO_MOVE_SEL_PLAYER = 1;
    public static final int WHO_TO_MOVE_LAST_DAMAGED = 2;
    public static final int WHO_TO_MOVE_CHOSEN_PLAYER = 3;
    public static final int WHARE_TO_MOVE_NOWHERE = -1;
    public static final int WHERE_TO_MOVE_SEL_SQ = 0;
    public static final int WHERE_TO_MOVE_SEL_PLAYERS_SQ = 1;
    public static final int WHERE_TO_MOVE_TEMP_SQUARE = 2;
    public static final int WHERE_TO_MOVE_MY_SQUARE = 3;
    public static final int WHO_TO_DAMAGE_NOBODY = -1;
    public static final int WHO_TO_DAMAGE_ALL_IN_SEL_SQ = 1;
    public static final int WHO_TO_DAMAGE_ALL_IN_SEL_ROOM = 2;
    public static final int WHO_TO_DAMAGE_LAST_DAMAGED = 3;
    public static final int WHO_TO_DAMAGE_ALL_IN_MY_SQUARE = 4;
    public static final int WHO_TO_DAMAGE_ALL_IN_TEMP_SQ = 5;
    public static final int WHO_TO_DAMAGE_ALL_NEAR_ME = 6;
    public static final int WHO_TO_DAMAGE_ALL_TEMP_SQ_EXCPT_LAST_DAM = 7;
    public static final int WHO_TO_DAMAGE_SHOOTER = 8;
    public static final int SET_TEMP_PLAYER_DONT = -1;
    public static final int SET_TEMP_PLAYER_SEL_PLAYER = 0;
    public static final int SET_TEMP_PLAYER_ME = 1;
    /**
     * point from where you calculate selectable targets
     */
    private int startingPoint;

    /**
     * reciprocal position between starting point and target
     */
    private int reciprocalPosition;

    /**
     * minimum distance from startingpoint and target
     */
    private int distMin;
    /**
     * maximum distance from starting point and target
     */
    private int distMax;

    /**
     * constraint for selectionability of target
     */
    private int diversity;
    /**
     * the nature of the target
     */
    private int what;
    /**
     * optionality of the effect
     */
    private int optionality;


    /**
     * player to move
     */
    private int whoToMove;

    /**
     * final square of the moving target
     */
    private int whereToMove;

    /**
     * players that will be damaged
     */
    private int whoToDamage;

    /**
     * number of damages to give
     */
    private int damageNumber;

    /**
     * number of marks to give
     */
    private int markNumber;

    /**
     * set a temporary square for next effects
     */
    private int setTempSquare;

    /**
     * set a temporary player for next effects
     */
    private int setTempPlayer;

    /**
     * effect to add after current effect
     */
    private Effect effectToAdd;

    private static final Logger logger =  Logger.getLogger(Effect.class.getName());

    /**
     * builds an effect from all its attributes
     * @param startingPoint
     * @param reciprocalPosition
     * @param distMin
     * @param distMax
     * @param diversity
     * @param what
     * @param optionality
     * @param whoToMove
     * @param whereToMove
     * @param whoToDamage
     * @param damageNumber
     * @param markNumber
     * @param setTempSquare
     */
    public Effect(int startingPoint, int reciprocalPosition, int distMin, int distMax, int diversity, int what, int optionality, int whoToMove, int whereToMove, int whoToDamage, int damageNumber, int markNumber, int setTempSquare) {
        this.startingPoint = startingPoint;
        this.reciprocalPosition = reciprocalPosition;
        this.distMin = distMin;
        this.distMax = distMax;
        this.diversity = diversity;
        this.what = what;
        this.optionality = optionality;
        this.whoToMove = whoToMove;
        this.whereToMove = whereToMove;
        this.whoToDamage = whoToDamage;
        this.damageNumber = damageNumber;
        this.markNumber = markNumber;
        this.setTempSquare = setTempSquare;

        this.setTempPlayer= -1;

        this.effectToAdd = null;
    }

    @Override
    public String toString(){
        StringBuilder sb= new StringBuilder();
        sb.append(startingPoint + " ; ");
        sb.append(reciprocalPosition + " ; ");
        sb.append(distMin + " ; ");
        sb.append(distMax + " ; ");
        sb.append(diversity + " ; ");
        sb.append(what + " ; ");
        sb.append(optionality + " ; ");
        sb.append(whoToMove + " ; ");
        sb.append(whereToMove + " ; ");
        sb.append(whoToDamage + " ; ");
        sb.append(damageNumber + " ; ");
        sb.append(markNumber + " ; ");
        sb.append(setTempSquare);

        return sb.toString();
    }

    /**
     * generate a string humanly readable
     * @return the string tat describes thee effect
     */
    public String humanString(){
        StringBuilder sb= new StringBuilder();

        sb.append("startingPoint: ");
        switch(startingPoint){
            case 0: sb.append("me"); break;
            case 1: sb.append("last damaged"); break;
            case 2: sb.append("temp sqare"); break;
            case 3: sb.append(("temp player")); break;
            default: sb.append("ERROR"); break;
        }

        sb.append("\nreciprocalPosition: ");
        switch(reciprocalPosition){
            case -1: sb.append("Don't care"); break;
            case 0: sb.append("not visible"); break;
            case 1: sb.append("visible"); break;
            case 2: sb.append("cardinal direction"); break;
            case 3: sb.append("in line with last damaged"); break;
            default: sb.append("ERROR"); break;
        }

        sb.append("\ndistMin: ");
        switch(distMin){
            case -1: sb.append("Don't care"); break;
            default: sb.append(distMin); break;
        }

        sb.append("\ndistMax: ");
        switch(distMax){
            case -1: sb.append("Don't care"); break;
            default: sb.append(distMax); break;
        }

        sb.append("\ndiversity: ");
        switch(diversity){
            case -1: sb.append("Don't care"); break;
            case 0: sb.append("already damaged player"); break;
            case 1: sb.append("not damaged yet player"); break;
            case 2: sb.append("not damaged yet square"); break;
            case 3: sb.append("not my room"); break;
            default: sb.append("ERROR"); break;
        }

        sb.append("\nwhat: ");
        switch(what){
            case -1: sb.append("Nothing to select"); break;
            case 0: sb.append("square"); break;
            case 1: sb.append("player"); break;

            default: sb.append("ERROR"); break;
        }

        sb.append("\noptionality: ");
        switch(optionality){
            case 0: sb.append("optional"); break;
            case 1: sb.append("mandatory"); break;
            default: sb.append("ERROR"); break;
        }

        sb.append("\nwhoToMove: ");
        switch(whoToMove){
            case -1: sb.append("nobody"); break;
            case 0: sb.append("me"); break;
            case 1: sb.append("selected player"); break;
            case 2: sb.append("last damaged"); break;
            case 3: sb.append("temp player"); break;
            default: sb.append("ERROR"); break;
        }

        sb.append("\nwhereToMove: ");
        switch(whereToMove){
            case -1: sb.append("nowhere"); break;
            case 0: sb.append("selected square"); break;
            case 1: sb.append("selected player's square"); break;
            case 2: sb.append("temp square"); break;
            case 3: sb.append("my square"); break;
            default: sb.append("ERROR"); break;
        }

        sb.append("\nwhoToDamage: ");
        switch(whoToDamage){
            case -1: sb.append("nobody"); break;
            case 0: sb.append("selected player"); break;
            case 1: sb.append("all in selected square"); break;
            case 2: sb.append("all in selected room (selected square's room)"); break;
            case 3: sb.append("last damaged"); break;
            case 4: sb.append("all in my square"); break;
            case 5: sb.append("all in temp square"); break;
            case 6: sb.append("all near me"); break;
            case 7: sb.append("all in temp square except last damaged"); break;
            case 8: sb.append("shooter"); break;
            default: sb.append("ERROR"); break;
        }

        sb.append("\ndamageNumber: ");
        switch(damageNumber){
            case -1: sb.append("Cambiami, mettimi a zero"); break;
            case 0: sb.append("no damages (0)"); break;
            default: sb.append(damageNumber); break;
        }

        sb.append("\nmarkNumber: ");
        switch(markNumber){
            case -1: sb.append("Cambiami, mettimi a zero"); break;
            case 0: sb.append("no marks (0)"); break;
            default: sb.append(markNumber); break;
        }

        sb.append("\nsetTempSquare: ");
        switch(setTempSquare){
            case -1: sb.append("don't set"); break;
            case 0: sb.append("selected square"); break;
            case 1: sb.append("selected player's square"); break;
            case 2: sb.append("my square"); break;
            default: sb.append("ERROR"); break;
        }

        return sb.toString();


    }

    /**
     * starts the effect and sets selectable lists from effect's criteria
     * @param p player "source" of the effect
     * @param m match of the game
     * @throws ApplyEffectImmediatelyException if effect has to apply itself
     */
    public void start(Player p, Match m) throws ApplyEffectImmediatelyException {

        m.getCurrentPlayer().resetSelectables();

        Square startingSquare= m.getCurrentPlayer().getSquarePosition();

        List<Square> selectableSquares= new ArrayList<>();
        List<Player> selectablePlayers= new ArrayList<>();
        List<Command> selectableCommands= new ArrayList<>();

        switch (startingPoint){
            //ME
            case STARTING_POINT_ME: startingSquare= p.getSquarePosition(); break;
            //LAST DAMAGED
            case STARTING_POINT_LAST_DAMAGED:
                if(m.getCurrentAction().getLastDamaged() != null){
                    startingSquare= m.getCurrentAction().getLastDamaged().getSquarePosition();
                }
                else{
                    System.out.println("NON C'E' UN DANNEGGIATO");
                }
                break;
            //TEMP SQUARE
            case STARTING_POINT_TEMP_SQUARE:
                if(m.getCurrentAction().getChosenSquare() != null){
                    startingSquare= m.getCurrentAction().getChosenSquare();
                }
                else{
                    System.out.println("MANCA IL TEMP SQUARE");
                }
                break;
            //TEMP PLAYER
            case STARTING_POINT_TEMP_PLAYER:
                if(m.getCurrentAction().getChosenPlayer() != null){
                    startingSquare= m.getCurrentAction().getChosenPlayer().getSquarePosition();
                }
                else{
                    System.out.println("MANCA IL TEMP PLAYER");
                }
                break;



            default: System.out.println("NUMERO SBAGLIATO STARTINGPOINT"); break;


        }

        switch (reciprocalPosition){
            //DON'T CARE
            case DONT_CARE:
                selectableSquares.addAll(m.getLayout().getSquares());
                break;
            //NOT VISIBLE
            case RECIPROCAL_POS_NOT_VISIBLE:
                selectableSquares.addAll(m.getLayout().getSquares());
                selectableSquares.removeAll(m.getLayout().getVisibleSquares(startingSquare));
                break;
            //VISIBLE
            case RECIPROCAL_POS_VISIBLE:
                selectableSquares.addAll(m.getLayout().getVisibleSquares(startingSquare));
                break;
            //CARDINAL DIRECTION
            case RECIPROCAL_POS_CARDINAL_DIR:
                selectableSquares.addAll(m.getLayout().getCardinalSquares(startingSquare));
                break;
            //LINE WITH LAST DAMAGED
            case RECIPROCAL_POS_LINE_LAST_DAM:
                if(m.getCurrentAction().getLastDamaged() != null){
                    selectableSquares.addAll(m.getLayout().getSquaresInDirection(startingSquare, m.getCurrentAction().getLastDamaged().getSquarePosition()));
                }
                else{
                    System.out.println("MANCA L'ULTIMO DANNEGGIATO PER TROVARE IN LINEA");
                }
                break;
            default: System.out.println("NUMERO SBAGLIATO RECIPROCAL POSITION"); break;

        }

        if(distMin > DONT_CARE){
            selectableSquares.retainAll(m.getLayout().getFurtherSquares(startingSquare, distMin));
        }

        if(distMax > DONT_CARE){
            selectableSquares.retainAll(m.getLayout().getCloserSquares(startingSquare, distMax));
        }

        selectablePlayers.addAll(m.getPlayersOn(selectableSquares));
        selectablePlayers.remove(p);

        switch (diversity){
            //DON'T CARE
            case DONT_CARE: break;
            //ALREADY DAMAGED
            case DIVERSITY_ALREADY_DAM:
                selectablePlayers.retainAll(m.getCurrentAction().getDamaged());
                break;
            //NOT DAMAGED YET PLAYERS
            case DIVERSITY_NOT_DAM_YET_PLAYERS:
                selectablePlayers.removeAll(m.getCurrentAction().getDamaged());
                break;
            //NOT DAMAGED YET SQUARES
            case DIVERSITY_NOT_DAM_YET_SQUARES:
                List<Square> damagedSquares= new ArrayList<>();
                for(Player dam: m.getCurrentAction().getDamaged()){
                    damagedSquares.add(dam.getSquarePosition());
                }

                selectableSquares.removeAll(damagedSquares);
                selectablePlayers.retainAll(m.getPlayersOn(selectableSquares));
                break;
            //NOT MY ROOM
            case DIVERSITY_NOT_MY_ROOM:
                selectableSquares.removeAll(p.getSquarePosition().getRoom().getSquaresInRoom());
                selectablePlayers.clear();
                selectablePlayers.addAll(m.getPlayersOn(selectableSquares));
                selectablePlayers.remove(p);
                break;
            default:
                System.out.println("NUMERO SBAGLIATO DIVERSITY");
                break;
        }

        switch (what){
            //NOTHING TO SELECT WITH NO LIMITATIONS
            case WHAT_NOTHING_TO_SEL_NO_LIMIT: // throw exception
                throw new ApplyEffectImmediatelyException();
            //SQUARE WITH NO LIMITATIONS
            case WHAT_SQUARE_NO_LIMIT:
                if(selectableSquares.isEmpty()){
                    selectableCommands.add(Command.BACK);
                }
                else {
                    p.setSelectableSquares(selectableSquares);
                }
                break;
            //PLAYER
            case WHAT_PLAYER_NO_LIMIT:
                if (selectablePlayers.isEmpty()){
                    selectableCommands.add(Command.BACK);
                }
                else{
                    p.setSelectablePlayers(selectablePlayers);
                }
                break;
            //NOTHING TO SELECT, SOMEONE ON MY SQUARE
            case WHAT_NOTHING_TO_SEL_SOMEONE_ON_MY_SQUARE:
                if(m.getPlayersOn(Collections.singletonList( m.getCurrentPlayer().getSquarePosition()) ).equals(Collections.singletonList(m.getCurrentPlayer()))){
                    selectableCommands.add(Command.BACK);
                }
                else {
                    throw new ApplyEffectImmediatelyException();
                }
                break;

            //SQUARE, SOMEONE IN SELECTED ROOM
            case WHAT_SQUARE_SOMEONE_IN_ROOM:
                List<Square> toRemove= new ArrayList<>();
                for(Square s: selectableSquares){
                    if(m.getPlayersOn(s.getRoom().getSquaresInRoom()).equals(Collections.singletonList(m.getCurrentPlayer())) || m.getPlayersOn(s.getRoom().getSquaresInRoom()).isEmpty()){
                        toRemove.add(s);
                    }
                }
                selectableSquares.removeAll(toRemove);

                if(selectableSquares.isEmpty()){
                    selectableCommands.add(Command.BACK);
                }
                else{
                    p.setSelectableSquares(selectableSquares);
                }
                break;
            //SQUARE, SOMEONE ON IT
            case WHAT_SQUARE_SOMEONE_ON_IT:
                List<Square> tempToRemove= new ArrayList<>();
                for(Square s: selectableSquares){
                    if(m.getPlayersOn(Collections.singletonList(s)).equals(Collections.singletonList(m.getCurrentPlayer())) || m.getPlayersOn(Collections.singletonList(s)).isEmpty()){
                        tempToRemove.add(s);
                    }
                }
                selectableSquares.removeAll(tempToRemove);
                if(selectableSquares.isEmpty()){
                    selectableCommands.add(Command.BACK);
                }
                else{
                    p.setSelectableSquares(selectableSquares);
                }
                break;
            //NOTHING, SOMEONE NEAR ME
            case WHAT_NOTHING_SOMEONE_NEAR_ME:
                if(m.getPlayersOn( m.getLayout().getSquaresInDistanceRange(p.getSquarePosition(), 1, 1) ).isEmpty()){
                    selectableCommands.add(Command.BACK);
                }
                else {
                    throw new ApplyEffectImmediatelyException();
                }
                break;

            default:
                System.out.println("NUMERO SBAGLIATO WHAT");
                break;
        }

        if(optionality == 0){
            selectableCommands.add(Command.OK);
            selectableCommands.remove(Command.BACK);
        }

        p.setSelectableCommands(selectableCommands);

    }

    /**
     * applyes from source to target player or square the effect
     * @param source source of the effect
     * @param targetP nullable player target
     * @param targetS nullable square target
     * @param m match
     * @return effect to add to effect list
     */
    public Effect applyOn(Player source, Player targetP, Square targetS, Match m) {
        Player movingPlayer= null;
        Square destination= null;

        List<Player> playersToShoot= new ArrayList<>();

        switch (setTempSquare){
            //DON'T SET
            case SET_TEMP_SQUARE_DONT: break;
            //SELECTED SQUARE
            case SET_TEMP_SQUARE_SEL_SQ: m.getCurrentAction().setChosenSquare(targetS); break;
            //SELECTED PLAYER'S SQUARE
            case SET_TEMP_SQUARE_SEL_PLAYERS_SQ:
                if(targetP != null) {
                    m.getCurrentAction().setChosenSquare(targetP.getSquarePosition());
                }
                break;
            //MY SQUARE
            case SEL_TEMP_SQUARE_MY_SQ: m.getCurrentAction().setChosenSquare(source.getSquarePosition()); break;

            default: System.out.println("NUMERO SBAGLIATO SETTEMPSQUARE");
        }

        switch (whoToMove){
            //NOBODY
            case WHO_TO_MOVE_NOBODY: break;
            //ME
            case WHO_TO_MOVE_ME: movingPlayer= source; break;
            //SELECTED PLAYER
            case WHO_TO_MOVE_SEL_PLAYER: movingPlayer= targetP; break;
            //LAST DAMAGED
            case WHO_TO_MOVE_LAST_DAMAGED: movingPlayer= m.getCurrentAction().getLastDamaged(); break;
            //chosen player
            case WHO_TO_MOVE_CHOSEN_PLAYER: movingPlayer= m.getCurrentAction().getChosenPlayer(); break;

            default: System.out.println("NUMERO SBAGLIATO WHOTOMOVE"); break;
        }

        switch (whereToMove){
            //NOWHERE
            case WHARE_TO_MOVE_NOWHERE: break;
            //SELECTED SQUARE
            case WHERE_TO_MOVE_SEL_SQ: destination= targetS; break;
            //SELECTED PLAYER'S SQUARE
            case WHERE_TO_MOVE_SEL_PLAYERS_SQ:
                if(targetP != null){
                    destination= targetP.getSquarePosition();
                }
                else{
                    destination= null;
                }
                break;
             //TEMP SQUARE
            case WHERE_TO_MOVE_TEMP_SQUARE:
                destination= m.getCurrentAction().getChosenSquare();
                break;
            //MY SQUARE
            case WHERE_TO_MOVE_MY_SQUARE:
                destination= source.getSquarePosition();
                break;
            default:
                System.out.println("NUMERO SBAGLIATO WHERETOMOVE");
                break;

        }

        if(movingPlayer!= null && destination!= null){
            movingPlayer.setSquarePosition(destination);

            m.notifyPlayerUpdate(movingPlayer);
        }

        switch (whoToDamage){
            //NOBODY
            case WHO_TO_DAMAGE_NOBODY: break;
            //SELECTED PLAYER
            case WHO_TO_DAMAGE_SEL_PLAYER:
                if(targetP != null){
                    playersToShoot.add(targetP);
                }
                break;
            //ALL IN SELECTED SQUARE
            case WHO_TO_DAMAGE_ALL_IN_SEL_SQ:
                if(targetS != null){
                    playersToShoot.addAll(m.getPlayersOn(new ArrayList<Square>(Arrays.asList(targetS))));
                }
                break;
            //ALL IN SELECTED ROOM
            case WHO_TO_DAMAGE_ALL_IN_SEL_ROOM:
                if(targetS!= null){
                    playersToShoot.addAll(m.getPlayersOn(targetS.getRoom().getSquaresInRoom()));
                }
                break;
            //LAST DAMAGED
            case WHO_TO_DAMAGE_LAST_DAMAGED:
                if (m.getCurrentAction().getLastDamaged() != null ){
                    playersToShoot.add(m.getCurrentAction().getLastDamaged());
                }
                break;
            //ALL IN MY SQUARE
            case WHO_TO_DAMAGE_ALL_IN_MY_SQUARE:
                playersToShoot.addAll(m.getPlayersOn(new ArrayList<Square>(Arrays.asList(source.getSquarePosition()))));
                playersToShoot.remove(source);
                break;
            //ALL IN TEMP SQUARE
            case WHO_TO_DAMAGE_ALL_IN_TEMP_SQ:
                if(m.getCurrentAction().getChosenSquare() != null){
                    playersToShoot.addAll(m.getPlayersOn(new ArrayList<Square>(Arrays.asList(m.getCurrentAction().getChosenSquare()))));
                }
                break;
            //ALL NEAR ME
            case WHO_TO_DAMAGE_ALL_NEAR_ME:
                playersToShoot.addAll(m.getPlayersOn(m.getLayout().getSquaresInDistanceRange(source.getSquarePosition(), 1, 1)));
                break;
            //ALL IN TEMP SQUARE EXCEPT LAST DAMAGED
            case WHO_TO_DAMAGE_ALL_TEMP_SQ_EXCPT_LAST_DAM:
                if(m.getCurrentAction().getChosenSquare() != null){
                    playersToShoot.addAll(m.getPlayersOn(new ArrayList<Square>(Arrays.asList(m.getCurrentAction().getChosenSquare()))));
                }
                playersToShoot.remove(m.getCurrentAction().getLastDamaged());
                break;

            //SHOOTER
            case WHO_TO_DAMAGE_SHOOTER:
                playersToShoot.clear();
                playersToShoot.add(m.getCurrentPlayer());
                break;

            default: System.out.println("NUMERO SBAGLIATO WHOTODAMAGE");

        }

        playersToShoot.remove(source);

        if(damageNumber>0){
            for(Player toShoot: playersToShoot) {
                toShoot.getDamageTrack().addDamage(source, damageNumber);
                m.getCurrentAction().addDamaged(toShoot);
                toShoot.getDamageTrack().setMarkToDamages(false);

                m.notifyDamageUpdate(toShoot);
            }
        }

        if(markNumber>0){
            for(Player toShoot: playersToShoot){
                toShoot.getDamageTrack().addMark(source, markNumber);

                m.notifyDamageUpdate(toShoot);
            }
        }


        switch (setTempPlayer){
            case SET_TEMP_PLAYER_DONT: break; //don't set
            case SET_TEMP_PLAYER_SEL_PLAYER: m.getCurrentAction().setChosenPlayer(targetP); break; //selected player
            case SET_TEMP_PLAYER_ME: m.getCurrentAction().setChosenPlayer(source); break; //me
            default: System.out.println("NUMERO SBAGLIATO SETTEMPPLAYER"); break;
        }

        return effectToAdd;

    }
}

