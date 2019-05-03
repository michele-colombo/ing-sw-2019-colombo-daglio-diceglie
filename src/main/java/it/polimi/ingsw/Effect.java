package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.ApplyEffectImmediatelyException;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Effect {

    private int startingPoint;
    private int reciprocalPosition;
    private int distMin;
    private int distMax;
    private int diversity;
    private int what;
    private int optionality;

    private int whoToMove;
    private int whereToMove;
    private int whoToDamage;
    private int damageNumber;
    private int markNumber;
    private int setTempSquare;

    private Effect effectToAdd;

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

        this.effectToAdd = null;
    }

    public Effect(int startingPoint, int reciprocalPosition, int distMin, int distMax, int diversity, int what, int optionality, int whoToMove, int whereToMove, int whoToDamage, int damageNumber, int markNumber, int setTempSquare, Effect effectToAdd) {
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
        this.effectToAdd = effectToAdd;
    }

    public void start(Player p, Match m){

        m.getCurrentPlayer().resetSelectables();

        Square startingSquare= m.getCurrentPlayer().getSquarePosition();

        List<Square> selectableSquares= new ArrayList<>();
        List<Player> selectablePlayers= new ArrayList<>();
        List<Command> selectableCommands= new ArrayList<>();

        switch (startingPoint){
            //ME
            case 0: startingSquare= p.getSquarePosition(); break;
            //LAST DAMAGED
            case 1:
                if(m.getCurrentAction().getLastDamaged() != null){
                    startingSquare= m.getCurrentAction().getLastDamaged().getSquarePosition();
                }
                else{
                    System.out.println("NON C'E' UN DANNEGGIATO");
                }
                break;
            //TEMP SQUARE
            case 2:
                if(m.getCurrentAction().getChosenSquare() != null){
                    startingSquare= m.getCurrentAction().getChosenSquare();
                }
                else{
                    System.out.println("MANCA IL TEMP SQUARE");
                }
                break;

            default: System.out.println("NUMERO SBAGLIATO STARTINGPOINT"); break;


        }

        switch (reciprocalPosition){
            //DON'T CARE
            case -1: break;
            //NOT VISIBLE
            case 0:
                selectableSquares.addAll(m.getLayout().getSquares());
                selectableSquares.removeAll(m.getLayout().getVisibleSquares(startingSquare));
                break;
            //VISIBLE
            case 1:
                selectableSquares.addAll(m.getLayout().getVisibleSquares(startingSquare));
                break;
            //CARDINAL DIRECTION
            case 2:
                selectableSquares.addAll(m.getLayout().getCardinalSquares(startingSquare));
                break;
            //LINE WITH LAST DAMAGED
            case 3:
                if(m.getCurrentAction().getLastDamaged() != null){
                    selectableSquares.addAll(m.getLayout().getSquaresInDirection(startingSquare, m.getCurrentAction().getLastDamaged().getSquarePosition()));
                }
                else{
                    System.out.println("MANCA L'ULTIMO DANNEGGIATO PER TROVARE IN LINEA");
                }
                break;
            default: System.out.println("NUMERO SBAGLIATO RECIPROCAL POSITION"); break;

        }

        if(distMin > -1){
            selectableSquares.retainAll(m.getLayout().getFurtherSquares(startingSquare, distMin));
        }

        if(distMax > -1){
            selectableSquares.retainAll(m.getLayout().getCloserSquares(startingSquare, distMax));
        }

        selectablePlayers.addAll(m.getPlayersOn(selectableSquares));
        selectablePlayers.remove(p);

        switch (diversity){
            //DON'T CARE
            case -1: break;
            //ALREADY DAMAGED
            case 0:
                selectablePlayers.retainAll(m.getCurrentAction().getDamaged());
                break;
            //NOT DAMAGED YET PLAYERS
            case 1:
                selectablePlayers.removeAll(m.getCurrentAction().getDamaged());
                break;
            //NOT DAMAGED YET SQUARES
            case 2:
                List<Square> damagedSquares= new ArrayList<>();
                for(Player dam: m.getCurrentAction().getDamaged()){
                    damagedSquares.add(dam.getSquarePosition());
                }

                selectableSquares.removeAll(damagedSquares);
                selectablePlayers.retainAll(m.getPlayersOn(selectableSquares));
                break;
            //NOT MY ROOM
            case 3:
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
            //NOTHING TO SELECT
            case -1: // throw exception
                // throw new ApplyEffectImmediatelyException();
                break;
            //SQUARE
            case 0:
                if(selectableSquares.isEmpty()){
                    selectableCommands.add(Command.BACK);
                }
                else {
                    p.setSelectableSquares(selectableSquares);
                }
                break;
            //PLAYER
            case 1:
                if (selectablePlayers.isEmpty()){
                    selectableCommands.add(Command.BACK);
                }
                else{
                    p.setSelectablePlayers(selectablePlayers);
                }
                break;

            default:
                System.out.println("NUMERO SBAGLIATO WHAT");
                break;
        }

        if(optionality == 0){
            selectableCommands.add(Command.OK);
        }

        p.setSelectableCommands(selectableCommands);

    }

    public Effect applyOn(Player source, Player targetP, Square targetS, Match m) {
        Player movingPlayer= null;
        Square destination= null;

        List<Player> playersToShoot= new ArrayList<>();

        switch (whoToMove){
            //NOBODY
            case -1: break;
            //ME
            case 0: movingPlayer= source; break;
            //SELECTED PLAYER
            case 1: movingPlayer= targetP; break;
            //LAST DAMAGED
            case 2: movingPlayer= m.getCurrentAction().getLastDamaged(); break;

            default: System.out.println("NUMERO SBAGLIATO WHOTOMOVE"); break;
        }

        switch (whereToMove){
            //NOWHERE
            case -1: break;
            //SELECTED SQUARE
            case 0: destination= targetS; break;
            //SELECTED PLAYER'S SQUARE
            case 1:
                if(targetP != null){
                    destination= targetP.getSquarePosition();
                }
                else{
                    destination= null;
                }
                break;
             //TEMP SQUARE
            case 2:
                destination= m.getCurrentAction().getChosenSquare();
                break;
            //MY SQUARE
            case 3:
                destination= source.getSquarePosition();
                break;
            default:
                System.out.println("NUMERO SBAGLIATO WHERETOMOVE");
                break;

        }

        if(movingPlayer!= null && destination!= null){
            movingPlayer.setSquarePosition(destination);
        }

        switch (whoToDamage){
            //NOBODY
            case  -1: break;
            //SELECTED PLAYER
            case 0:
                if(targetP != null){
                    playersToShoot.add(targetP);
                }
                break;
            //ALL IN SELECTED SQUARE
            case 1:
                if(targetS != null){
                    playersToShoot.addAll(m.getPlayersOn(new ArrayList<Square>(Arrays.asList(targetS))));
                }
                break;
            //ALL IN SELECTED ROOM
            case 2:
                if(targetS!= null){
                    playersToShoot.addAll(m.getPlayersOn(targetS.getRoom().getSquaresInRoom()));
                }
                break;
            //LAST DAMAGED
            case 3:
                if (m.getCurrentAction().getLastDamaged() != null ){
                    playersToShoot.add(m.getCurrentAction().getLastDamaged());
                }
                break;
            //ALL IN MY SQUARE
            case 4:
                playersToShoot.addAll(m.getPlayersOn(new ArrayList<Square>(Arrays.asList(source.getSquarePosition()))));
                break;
            //ALL IN TEMP SQUARE
            case 5:
                if(m.getCurrentAction().getChosenSquare() != null){
                    playersToShoot.addAll(m.getPlayersOn(new ArrayList<Square>(Arrays.asList(m.getCurrentAction().getChosenSquare()))));
                }
                break;

            default: System.out.println("NUMERO SBAGLIATO WHOTODAMAGE");

        }

        if(damageNumber>0){
            for(Player toShoot: playersToShoot) {
                toShoot.getDamageTrack().addDamage(source, damageNumber);
                m.getCurrentAction().addDamaged(toShoot);
            }
        }

        if(markNumber>0){
            for(Player toShoot: playersToShoot){
                toShoot.getDamageTrack().addMark(source, markNumber);
            }
        }


        switch (setTempSquare){
            //DON'T SET
            case -1: break;
            //SELECTED SQUARE
            case 0: m.getCurrentAction().setChosenSquare(targetS); break;
            //SELECTED PLAYER'S SQUARE
            case 1:
                if(targetP != null) {
                    m.getCurrentAction().setChosenSquare(targetP.getSquarePosition());
                }
                break;
            //MY SQUARE
            case 2: m.getCurrentAction().setChosenSquare(source.getSquarePosition()); break;

            default: System.out.println("NUMERO SBAGLIATO SETTEMPSQUARE");
        }

        return effectToAdd;

    }
}

