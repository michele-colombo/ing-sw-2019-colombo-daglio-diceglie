package it.polimi.ingsw;

public interface EffectApplier {
    public Effect applyOn(Player source, Player targetP, Square targetS, Match m);
}
