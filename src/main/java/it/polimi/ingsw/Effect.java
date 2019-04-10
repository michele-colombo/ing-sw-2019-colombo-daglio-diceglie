package it.polimi.ingsw;

public class Effect {
    private EffectApplier effectApplier;
    private EffectStarter effectStarter;

    public void start(Player p){
        effectStarter.start(p);
    }

    public Effect applyOn(Player source, Player targetP, Square targetS){
        return effectApplier.applyOn(source, targetP, targetS);
    }
}
