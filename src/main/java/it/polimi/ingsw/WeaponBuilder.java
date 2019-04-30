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

        m1= new Mode(true, null, "basic effect", "Deal 2 damage and 1 mark to 1 target " +
                "you can see.", new Cash());
        m2 = new Mode(false, m1, "with second lock", "Deal 1 mark to a different target " +
                "you can see.", new Cash(0, 1, 0));

        m1.addEffect(new Effect(0, 1, 0, -1, 4, 1, 1, 2, 0, 2, 1));
        m2.addEffect(new Effect(0, 1, 0, -1, 1, 1, 1, 2, 0, 0, 1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);


        weapons.add(weaponInConstruction);

        System.out.println(weaponInConstruction.getDescription());

        //building machine gun

        weaponInConstruction= new Weapon("Machine gun", new Cash(1, 1, 0), BLUE);

        m1= new Mode(true, null, "basic effect", "Choose 1 or 2 targets you can see and deal " +
                "1 damage to each.", new Cash());

        m1.addEffect(new Effect(0, 1, 0, -1, 4, 1, 1, 2, 0, 1, 0));
        m1.addEffect(new Effect(0, 1, 0, -1, 1, 1, 0, 2, 0, 1, 0));

        m2= new Mode(false, m1, "with focus shot", "Deal 1 additional damage to one of those " +
                "targets", new Cash(0, 0, 1));
        m2.addEffect(new Effect(0, 4, -1, -1, 0, 1, 1, 2, 0, 1, 0));

        m3= new Mode(false, m2, "with turret tripod", "Deal 1 additional damage to the other " +
                "of those targets and/or deal 1 damage to a different target " +
                "you can see.", new Cash(1, 0, 0));
        m3.addEffect(new Effect(0, 1, -1, -1, 4, 1, 1, 2, 0, 1, 0));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);
        weaponInConstruction.addMode(m3);

        weapons.add(weaponInConstruction);

        //building thor
        weaponInConstruction= new Weapon("T.H.O.R", new Cash(1, 1, 0), BLUE);

        m1= new Mode(true, null, "basic effect", "Deal 2 damage to 1 target you can see.", new Cash());
        m1.addEffect(new Effect(0, 1, 0, -1, 4, 1, 1, 2, 0, 2, 0));

        m2= new Mode(false, m1, "with chain reaction", "Deal 1 damage to a second target that " +
                "your first target can see.", new Cash(1, 0, 0));
        m2.addEffect(new Effect(1, 1, 0, -1, 1, 1, 1, 2, 0, 1, 0));

        m3= new Mode(false, m2, "with high voltage", "Deal 2 damage to a third target that " +
                "your second target can see. You cannot use this effect " +
                "unless you first use the chain reaction.", new Cash(1, 0, 0));
        m3.addEffect(new Effect(1, 1, -1, -1, 1, 1, 1, 2, 0, 2, 0));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);
        weaponInConstruction.addMode(m3);

        weapons.add(weaponInConstruction);

        //building plasma gun
        weaponInConstruction= new Weapon("Plasma gun", new Cash(1, 0, 1), BLUE);

        m1= new Mode(true, null, "basic effect", "Deal 2 damage to 1 target you can see.", new Cash());

        m1.addEffect(new Effect(0, 1, 0, -1, 4, 1, 1, 2, 0, 2, 0));

        m2= new Mode(false, null, "with phase glide", "Move 1 or 2 squares. This effect can be " +
                "used either before or after the basic effect.", new Cash());
        m2.addEffect(new Effect(0, 4, 1, 2, 4, 0, 1, 0, -1, 0, 0 ));

        m3= new Mode(false, m1, "with charged shot", "Deal 1 additional damage to your " +
                "target.", new Cash(1, 0, 0));

        m3.addEffect(new Effect(0, 4, -1, -1, 0, 1, 1, 2, 0, 1, 0));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);
        weaponInConstruction.addMode(m3);

        weapons.add(weaponInConstruction);

        //building whisper
        weaponInConstruction= new Weapon("Whisper", new Cash(2, 0, 1), BLUE);

        m1= new Mode(true, null, "effect", "Deal 3 damage and 1 mark to 1 target you can see. " +
                "Your target must be at least 2 moves away from you.", new Cash());
        m1.addEffect(new Effect(0, 1, 2, -1, 4, 1, 1, 2, 0, 3, 1));

        weaponInConstruction.addMode(m1);

        weapons.add(weaponInConstruction);





    }

    public List<Weapon> getWeapons() {
        return weapons;
    }
}
