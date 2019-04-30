package it.polimi.ingsw;

import java.util.List;
/*
public class Effect {
    private List<EffectApplier> effectApplier;
    private EffectStarter effectStarter;

    public Effect(EffectStarter starter, List<EffectApplier> applier){
        this.effectApplier= applier;
        this.effectStarter= starter;
    }

    public void start(Player p, Match m){
        effectStarter.start(p, m);
    }

    public Effect applyOn(Player source, Player targetP, Square targetS, Match m) {
        for (EffectApplier e : effectApplier) {
            e.applyOn(source, targetP, targetS, m);
        }
        return null;
    }

}

*/
import java.util.ArrayList;
import java.util.List;

public class Effect {
    private int reciprocalPosition;
    private int distMin;
    private int distMax;
    private int startingPoint;
    private int diversity;
    private int what;
    private int optionality;


    private int damageNumber;
    private int markNumber;
    private int move;
    private int whom;

    public Effect(int startingPoint, int reciprocalPosition, int distMin, int distMax, int diversity, int what, int optionality, int move, int whom, int damageNumber, int markNumber) {
        this.reciprocalPosition = reciprocalPosition;
        this.distMin = distMin;
        this.distMax = distMax;
        this.startingPoint = startingPoint;
        this.diversity = diversity;
        this.what = what;
        this.optionality = optionality;
        this.damageNumber = damageNumber;
        this.markNumber = markNumber;
        this.move = move;
        this.whom = whom;
    }

    public void start(Player p, Match m){
        List<Square> squareFound= new ArrayList<>();
        List<Player> targetFound;

        Square startingSquare= m.getLayout().getSquare(0,0);

        //Qui c'e' il controllo che tutti i parametri sono nell;intervallo giusto

        switch(startingPoint){
            //ME
            case 0: startingSquare= p.getSquarePosition(); break;
            //LASTDAMAGED
            case 1: startingSquare= m.getCurrentAction().getLastDamaged().getSquarePosition(); break;
            //CHOSENSQUARE
            case 2: startingSquare= m.getCurrentAction().getChosenSquare(); break;
        }

        switch (reciprocalPosition){
            //NOT VISIBLE
            case 0: squareFound.addAll( m.getLayout().getSquares() );
                squareFound.removeAll(m.getLayout().getVisibleSquares(startingSquare));
                break;
            //VISIBLE
            case 1: squareFound= m.getLayout().getVisibleSquares(startingSquare); break;
            //DIREZIONE CARDINALE
            case 2: squareFound= m.getLayout().getCardinalSquares(startingSquare); break;
            //LINE WITH LAST DAMAGED
            case 3: squareFound= m.getLayout().getSquaresInDirection(startingSquare, m.getCurrentAction().getLastDamaged().getSquarePosition()); break;
            //DON'T CARE
            case 4: squareFound.addAll( m.getLayout().getSquares() ); break;



        }

        if (distMin != -1){
            squareFound.retainAll(m.getLayout().getFurtherSquares(startingSquare, distMin));
        }

        if(distMax != -1){
            squareFound.retainAll(m.getLayout().getCloserSquares(startingSquare, distMax));
        }


        targetFound= m.getPlayersOn(squareFound);



        switch (diversity){
            //ALREADY DAMAGED
            case 0: targetFound.retainAll(m.getCurrentAction().getDamaged()); break;
            //OTHER TARGET
            case 1: targetFound.removeAll(m.getCurrentAction().getDamaged()); break;

            //OTHER SQUARE
            case 2: for(Player damaged : m.getCurrentAction().getDamaged()){
                squareFound.remove(damaged.getSquarePosition());
            }
                targetFound= m.getPlayersOn(squareFound);
                break;

            //NOT MY ROOM
            case 3: squareFound.removeAll( p.getSquarePosition().getRoom().getSquaresInRoom() );
                targetFound= m.getPlayersOn(squareFound); break;
            //DONT CARE
            case 4: break;

        }

        targetFound.remove(p);

        switch (what){
            //SQUARE
            case 0: p.setSelectableSquares( squareFound); break;
            //PLAYER
            case 1: p.setSelectablePlayers( targetFound ); break;
        }

        switch (optionality){
            //OPTIONAL
            case 0: p.setSelectableCommands(Command.OK); break;
            //MANDATORY
            case 1: //niente
                break;
        }






    }

    public Effect applyOn(Player source, Player targetP, Square targetS, Match m){




        switch (move){
            //ME
            case 0: source.setSquarePosition(targetS); return null;
            //SELECTED
            case 1: targetP.setSquarePosition(targetS); return null;
            //DON'T MOVE
            case 2: break;
        }

        List<Player> targetsToDamage= new ArrayList<>();
        List<Square> targetsToDamagePosition= new ArrayList<>();

        switch(whom){
            //SELECTED PLAYER
            case 0:  targetsToDamage.add(targetP);
                break;

            // ALL IN SQUARE
            case 1:  targetsToDamagePosition.add(targetS);
                targetsToDamage.addAll(m.getPlayersOn(targetsToDamagePosition));
                break;

            // ALL IN ROOM
            case 2: targetsToDamagePosition.addAll( targetS.getRoom().getSquaresInRoom());
                targetsToDamage.addAll(m.getPlayersOn(targetsToDamagePosition));
                break;

        }

        for(Player p: targetsToDamage){
            p.getDamageTrack().addDamage(source, damageNumber);
            m.getCurrentAction().addDamaged(p);

            p.getDamageTrack().addMark(source, markNumber);
        }

        return  null;



    }
}

