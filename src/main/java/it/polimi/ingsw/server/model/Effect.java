package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.exceptions.ApplyEffectImmediatelyException;
import it.polimi.ingsw.server.model.enums.Command;

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
    private int setTempPlayer;

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

        this.setTempPlayer= -1;

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

    public void setSetTempPlayer(int setTempPlayer) {
        this.setTempPlayer = setTempPlayer;
    }

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

    public void start(Player p, Match m) throws ApplyEffectImmediatelyException {

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
            //TEMP PLAYER
            case 3:
                if(m.getCurrentAction().getChosenPlayer() != null){
                    startingSquare= m.getCurrentAction().getChosenPlayer().getSquarePosition();
                }
                else{
                    System.out.println("MANCA IL TEMP PLAYER");
                }



            default: System.out.println("NUMERO SBAGLIATO STARTINGPOINT"); break;


        }

        switch (reciprocalPosition){
            //DON'T CARE
            case -1:
                selectableSquares.addAll(m.getLayout().getSquares());
                break;
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
                throw new ApplyEffectImmediatelyException();
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
            selectableCommands.remove(Command.BACK);
        }

        p.setSelectableCommands(selectableCommands);

    }

    public Effect applyOn(Player source, Player targetP, Square targetS, Match m) {
        Player movingPlayer= null;
        Square destination= null;

        List<Player> playersToShoot= new ArrayList<>();

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

        switch (whoToMove){
            //NOBODY
            case -1: break;
            //ME
            case 0: movingPlayer= source; break;
            //SELECTED PLAYER
            case 1: movingPlayer= targetP; break;
            //LAST DAMAGED
            case 2: movingPlayer= m.getCurrentAction().getLastDamaged(); break;
            //chosen player
            case 3: movingPlayer= m.getCurrentAction().getChosenPlayer(); break;

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
                playersToShoot.remove(source);
                break;
            //ALL IN TEMP SQUARE
            case 5:
                if(m.getCurrentAction().getChosenSquare() != null){
                    playersToShoot.addAll(m.getPlayersOn(new ArrayList<Square>(Arrays.asList(m.getCurrentAction().getChosenSquare()))));
                }
                break;
            //ALL NEAR ME
            case 6:
                playersToShoot.addAll(m.getPlayersOn(m.getLayout().getSquaresInDistanceRange(source.getSquarePosition(), 1, 1)));
                break;
            //ALL IN TEMP SQUARE EXCEPT LAST DAMAGED
            case 7:
                if(m.getCurrentAction().getChosenSquare() != null){
                    playersToShoot.addAll(m.getPlayersOn(new ArrayList<Square>(Arrays.asList(m.getCurrentAction().getChosenSquare()))));
                }
                playersToShoot.remove(m.getCurrentAction().getLastDamaged());
                break;

            //SHOOTER
            case 8:
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
            }
        }

        if(markNumber>0){
            for(Player toShoot: playersToShoot){
                toShoot.getDamageTrack().addMark(source, markNumber);
            }
        }

        switch (setTempSquare){
            case -1: break;//sb.append("don't set");
            case 0: m.getCurrentAction().setChosenSquare(targetS); break; //sb.append("selected square");
            case 1: m.getCurrentAction().setChosenSquare(targetP.getSquarePosition()); break; //sb.append("selected player's square");
            case 2: m.getCurrentAction().setChosenSquare(source.getSquarePosition()); break; //sb.append("my square");
            default: System.out.println("NUMERO SBAGLIATO SETTEMPSQUARE"); break;
        }

        switch (setTempPlayer){
            case -1: break; //don't set
            case 0: m.getCurrentAction().setChosenPlayer(targetP); break; //selected player
            case 1: m.getCurrentAction().setChosenPlayer(source); break; //me
            default: System.out.println("NUMERO SBAGLIATO SETTEMPPLAYER"); break;
        }

        return effectToAdd;

    }
}

