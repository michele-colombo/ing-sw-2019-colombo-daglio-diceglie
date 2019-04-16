package it.polimi.ingsw;

public class Effect {
    private EffectApplier effectApplier;
    private EffectStarter effectStarter;

    public void start(Player p, Match m){
        effectStarter.start(p, m);
    }

    public Effect applyOn(Player source, Player targetP, Square targetS, Match m){
        return effectApplier.applyOn(source, targetP, targetS, m);
    }
}
