package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Color.*;

public class WeaponBuilder {
    private List<Weapon> weapons;

    public WeaponBuilder(){
        weapons= new ArrayList<>();

        Weapon weaponInConstruction;
        Mode m1;
        Mode m2;
        Mode m3;

        //building lock rifle
        weaponInConstruction= new Weapon("Lock rifle", new Cash(2, 0, 0), BLUE);

        m1= new Mode(true, -1, "basic effect", "Deal 2 damage and 1 mark to 1 target " +
                "you can see.", new Cash());
        m1.addEffect(new Effect(0,1,-1,-1,-1,1,1,-1,-1,0,2,1,-1));

        m2 = new Mode(false, 0, "with second lock", "Deal 1 mark to a different target " +
                "you can see.", new Cash(0, 1, 0));

        m2.addEffect(new Effect(0,1,-1,-1,1,1,1,-1,-1,0,0,1,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);
        weapons.add(weaponInConstruction);


        //building machine gun

        weaponInConstruction= new Weapon("Machine gun", new Cash(1, 1, 0), BLUE);

        m1= new Mode(true, -1, "basic effect", "Choose 1 or 2 targets you can see and deal " +
                "1 damage to each.", new Cash());

        m1.addEffect(new Effect(0,1,-1,-1,-1,1,1,-1,-1,0,1,0,-1));
        m1.addEffect(new Effect(00,1,-1,-1,1,1,0,-1,-1,0,1,0,-1));

        m2= new Mode(false, 0, "with focus shot", "Deal 1 additional damage to one of those " +
                "targets", new Cash(0, 0, 1));
        m2.addEffect(new Effect(0,-1,-1,-1,0,1,1,-1,-1,0,1,0,-1));

        m3= new Mode(false, 1, "with turret tripod", "Deal 1 additional damage to the other " +
                "of those targets and/or deal 1 damage to a different target " +
                "you can see.", new Cash(1, 0, 0));
        m3.addEffect(new Effect(0,1,-1,-1,-1,1,1,-1,-1,0,1,0,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);
        weaponInConstruction.addMode(m3);

        weapons.add(weaponInConstruction);

        //building thor
        weaponInConstruction= new Weapon("T.H.O.R", new Cash(1, 1, 0), BLUE);

        m1= new Mode(true, -1, "basic effect", "Deal 2 damage to 1 target you can see.", new Cash());
        m1.addEffect(new Effect(0,1,-1,-1,-1,1,1,-1,-1,0,2,0,-1));

        m2= new Mode(false, 0, "with chain reaction", "Deal 1 damage to a second target that " +
                "your first target can see.", new Cash(1, 0, 0));
        m2.addEffect(new Effect(1,1,-1,-1,1,1,1,-1,-1,0,1,0,-1));

        m3= new Mode(false, 1, "with high voltage", "Deal 2 damage to a third target that " +
                "your second target can see. You cannot use this effect " +
                "unless you first use the chain reaction.", new Cash(1, 0, 0));
        m3.addEffect(new Effect(1,1,-1,-1,1,1,1,-1,-1,0,2,0,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);
        weaponInConstruction.addMode(m3);

        weapons.add(weaponInConstruction);

        //building plasma gun
        weaponInConstruction= new Weapon("Plasma gun", new Cash(1, 0, 1), BLUE);

        m1= new Mode(true, -1, "basic effect", "Deal 2 damage to 1 target you can see.", new Cash());

        m1.addEffect(new Effect(0,1,-1,-1,-1,1,1,-1,-1,0,2,0,-1));

        m2= new Mode(false, -1, "with phase glide", "Move 1 or 2 squares. This effect can be " +
                "used either before or after the basic effect.", new Cash());
        m2.addEffect(new Effect(0,-1,1,2,-1,0,1,0,0,-1,0,0,-1 ));

        m3= new Mode(false, 0, "with charged shot", "Deal 1 additional damage to your " +
                "target.", new Cash(1, 0, 0));

        m3.addEffect(new Effect(0,-1,-1,-1,-1,-1,1,-1,-1,3,1,0,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);
        weaponInConstruction.addMode(m3);

        weapons.add(weaponInConstruction);

        //building whisper
        weaponInConstruction= new Weapon("Whisper", new Cash(2, 0, 1), BLUE);

        m1= new Mode(true, -1, "effect", "Deal 3 damage and 1 mark to 1 target you can see. " +
                "Your target must be at least 2 moves away from you.", new Cash());
        m1.addEffect(new Effect(0,1,2,-1,-1,1,1,-1,-1,0,3,1,-1));

        weaponInConstruction.addMode(m1);

        weapons.add(weaponInConstruction);

        //building electroscythe

        weaponInConstruction= new Weapon("Electoscythe", new Cash(1, 0, 0), BLUE);

        m1= new Mode(true, -1, "basic effect", "Deal 1 damage to every other player " +
                "on your square.", new Cash());
        m1.addEffect(new Effect(0,-1,0,0,-1,-1,1,-1,-1,4,1,0,-1));
        weaponInConstruction.addMode(m1);

        m2= new Mode(true, -1, "in reaper mode", "Deal 2 damage to every other player " +
                "on your square.", new Cash(1, 1, 0));
        m2.addEffect(new Effect(0,-1,0,0,-1,-1,1,-1,-1,4,2,0,-1));
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);

        //building tractor beam

        weaponInConstruction= new Weapon("Tractor Beam", new Cash(1, 0, 0), BLUE);

        m1= new Mode(true, -1, "basic mode", "Move a target 0, 1, or 2 squares to a square " +
                "you can see, and give it 1 damage.", new Cash());
        m1.addEffect(new Effect(0,1,-1,-1,-1,0,1,-1,-1,-1,0,0,0));

        m1.addEffect(new Effect(2,-1,0,2,-1,1,1,1,2,0,1,0,-1));

        m2= new Mode(true, -1, "in punisher mode", "Choose a target 0, 1, or 2 moves away " +
                "from you. Move the target to your square " +
                "and deal 3 damage to it.", new Cash(0, 1, 1));
        m2.addEffect(new Effect(0,-1,0,2,-1,1,1,1,3,0,3,0,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);

    }

    public List<Weapon> getWeapons() {
        return weapons;
    }
}
