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
        weaponInConstruction= new Weapon("T.H.O.R.", new Cash(1, 1, 0), BLUE);

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


        //building vortex cannon
        weaponInConstruction= new Weapon("Vortex cannon", new Cash(1, 1, 0), RED);

        m1= new Mode(true, -1, "basic effect", "Choose a square you can see, but not your " +
                "square. Call it \"the vortex\". Choose a target on the vortex " +
                "or 1 move away from it. Move it onto the vortex and give it " +
                "2 damage.", new Cash());
        m1.addEffect(new Effect(0,1,1,-1,-1,0,1,-1,-1,-1,0,0,0));
        m1.addEffect(new Effect(2,-1,0,1,-1,1,1,1,2,0,2,0,-1));

        m2= new Mode(true, -1, "with black hole", "Choose up to 2 other targets on the\n" +
                "vortex or 1 move away from it. Move them onto the vortex\n" +
                "and give them each 1 damage.", new Cash(0, 1, 0));
        m2.addEffect(new Effect(2,-1,0,1,1,1,1,1,2,0,1,0,-1));
        m2.addEffect(new Effect(2,-1,0,1,1,1,0,1,2,0,1,0,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);

        //building furnace
        weaponInConstruction= new Weapon("Furnace", new Cash(1, 1, 0), RED);

        m1= new Mode(true, -1, "basic mode", "Choose a room you can see, but not the room " +
                "you are in. Deal 1 damage to everyone in that room.", new Cash());
        m1.addEffect(new Effect(0,1,-1,-1,3,0,1,-1,-1,2,1,0,-1));

        m2= new Mode(true, -1, "in cozy fire mode", "Choose a square exactly one move " +
                "away. Deal 1 damage and 1 mark to everyone on that " +
                "square.", new Cash());
        m2.addEffect(new Effect(0,-1,1,1,-1,0,1,-1,-1,1,1,1,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);

        //building heatseeker

        weaponInConstruction= new Weapon("Heatseeker", new Cash(0, 2, 1), RED);

        m1= new Mode(true, -1, "effect", "Choose 1 target you cannot see and deal 3 damage\n" +
                "to it.", new Cash());
        m1.addEffect(new Effect( 0,0,-1,-1,-1,1,1,-1,-1,0,3,0,-1));

        weaponInConstruction.addMode(m1);

        weapons.add(weaponInConstruction);

        //building hellion
        weaponInConstruction= new Weapon("Hellion", new Cash(0, 1, 1), RED);

        m1= new Mode(true, -1, "basic mode", "Deal 1 damage to 1 target you can see at least " +
                "1 move away. Then give 1 mark to that target and everyone " +
                "else on that square.", new Cash());
        m1.addEffect(new Effect(0,1,1,-1,-1,1,1,-1,-1,0,1,0,1));
        m1.addEffect(new Effect(1,-1,0,0,1,-1,1,-1,-1,5,0,1,-1));

        m2= new Mode(true, -1, "in nano-tracer mode", "Deal 1 damage to 1 target you can " +
                "see at least 1 move away. Then give 2 marks to that target " +
                "and everyone else on that square.", new Cash(0, 1, 0));
        m2.addEffect(new Effect(0,1,1,-1,-1,1,1,-1,-1,0,1,0,1));
        m2.addEffect(new Effect(1,-1,0,0,1,-1,1,-1,-1,5,0,1,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);

        //building flamethrower

        weaponInConstruction= new Weapon("Flamethrower", new Cash(0, 1, 0), RED);

        m1= new Mode(true, -1, "basic mode", "Choose a square 1 move away and possibly a second square " +
                "1 more move away in the same direction. On each square, you may " +
                "choose 1 target and give it 1 damage.", new Cash());
        m1.addEffect(new Effect(0,-1,1,1,-1,1,1,-1,-1,0,1,0,-1));
        m1.addEffect(new Effect(0,3,2,2,1,1,0,-1,-1,0,1,0,-1));

        m2= new Mode(true, -1, "in barbecue mode", "Choose 2 squares as above. Deal 2 damage to " +
                "everyone on the first square and 1 damage to everyone on the second " +
                "square.", new Cash(0, 0, 2));
        m2.addEffect(new Effect(0,-1,1,1,-1,0,1,-1,-1,1,2,0,-1));
        m2.addEffect(new Effect(0,3,2,2,2,0,0,-1,-1,1,1,0,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);

        //buildin granade launcher

        weaponInConstruction= new Weapon("Granade launcher", new Cash(0, 1, 0), RED);

        m1= new Mode(true, -1, "basic effect", "Deal 1 damage to 1 target you can see. Then you may move " +
                "the target 1 square.", new Cash());
        m1.addEffect(new Effect(0,1,-1,-1,-1,1,1,-1,-1,0,1,0,-1));
        m1.addEffect(new Effect(1,-1,1,1,-1,0,1,2,0,-1,0,0,-1));

        m2= new Mode(false, -1, "with extra grenade", "Deal 1 damage to every player on a square you can " +
                "see. You can use this before or after the basic effect's move.", new Cash(0, 1, 0));
        m2.addEffect(new Effect(0,1,-1,-1,-1,0,1,-1,-1,1,1,0,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);

        //building rocket launcher

        weaponInConstruction= new Weapon("Rocket launcher", new Cash(0, 2, 0), RED);

        m1= new Mode(true, -1, "basic effect", "Deal 2 damage to 1 target you can see that is not on your " +
                "square. Then you may move the target 1 square.", new Cash());
        m1.addEffect(new Effect(0,1,1,-1,-1,1,1,-1,-1,0,2,0,1));
        m1.addEffect(new Effect(1,-1,0,1,-1,0,1,2,0,-1,0,0,-1));

        m2= new Mode(false, -1, "with rocket jump", "Move 1 or 2 squares. This effect can be used either " +
                "before or after the basic effect.", new Cash(1, 0, 0));
        m2.addEffect(new Effect(0,-1,1,2,-1,0,1,0,0,-1,0,0,-1));

        m3= new Mode(false, 0, "with fragmenting warhead", "During the basic effect, deal 1 damage to " +
                "every player on your target's original square – including the target, " +
                "even if you move it.", new Cash(0, 0, 1));
        m3.addEffect(new Effect(0, -1, -1, -1, -1, -1, 1, -1, -1, 3, 1, 0, -1));
        m3.addEffect(new Effect(0, -1, -1, -1, -1, -1, 1, -1, -1, 7, 1, 0, -1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);
        weaponInConstruction.addMode(m3);

        weapons.add(weaponInConstruction);

        //building railgun

        weaponInConstruction= new Weapon("Railgun", new Cash(1, 0, 2), YELLOW);

        m1= new Mode(true, -1, "basic mode", "Choose a cardinal direction and 1 target in that direction.\n" +
                "Deal 3 damage to it.", new Cash());
        m1.addEffect(new Effect(0,2,-1,-1,-1,1,1,-1,-1,0,3,0,-1));

        m2= new Mode(true, -1, "in piercing mode", "Choose a cardinal direction and 1 or 2 targets in that " +
                "direction. Deal 2 damage to each.", new Cash());
        m2.addEffect(new Effect(0,2,-1,-1,-1,1,1,-1,-1,0,2,0,-1));
        m2.addEffect(new Effect(0,3,-1,-1,1,1,0,-1,-1,0,2,0,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);

        //building cyberblade
        weaponInConstruction= new Weapon("Cyberblade", new Cash(0, 1, 1), YELLOW);

        m1= new Mode(true, -1, "basic effect", "Deal 2 damage to 1 target on your square.", new Cash());
        m1.addEffect(new Effect(0,-1,0,0,-1,1,1,-1,-1,0,2,0,-1));

        m2= new Mode(false, -1, "with shadowstep", "Move 1 square before or after the basic effect.", new Cash());
        m2.addEffect(new Effect(0,-1,1,1,-1,0,1,0,0,-1,0,0,-1));

        m3= new Mode(false, 0, "with slice and dice", "Deal 2 damage to a different target on your square. " +
                "The shadowstep may be used before or after this effect.", new Cash(0, 0, 1));
        m3.addEffect(new Effect(0,-1,0,0,1,1,1,-1,-1,0,2,0,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);
        weaponInConstruction.addMode(m3);

        weapons.add(weaponInConstruction);

        //building zx-2

        weaponInConstruction= new Weapon("ZX-2", new Cash(0, 1, 1), YELLOW);

        m1= new Mode(true, -1, "basic mode", "Deal 1 damage and 2 marks to " +
                "1 target you can see.", new Cash());
        m1.addEffect(new Effect(0,1,-1,-1,-1,1,1,-1,-1,0,1,2,-1));

        m2= new Mode(true, -1, "in scanner mode", "Choose up to 3 targets you " +
                "can see and deal 1 mark to each.", new Cash());
        m2.addEffect(new Effect(0,1,-1,-1,-1,1,1,-1,-1,0,0,1,-1));
        m2.addEffect(new Effect(0,1,-1,-1,1,1,0,-1,-1,0,0,1,-1));
        m2.addEffect(new Effect(0,1,-1,-1,1,1,0,-1,-1,0,0,1,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);

        //building shotgun

        weaponInConstruction= new Weapon("Shotgun", new Cash(0, 0, 2), YELLOW);
        m1= new Mode(true, -1, "basic mode", "Deal 3 damage to 1 target on " +
                "your square. If you want, you may then move " +
                "the target 1 square.", new Cash());
        m1.addEffect(new Effect(0,-1,0,0,-1,1,1,-1,-1,0,3,0,-1));
        m1.addEffect(new Effect(1,-1,0,1,-1,0,1,2,0,-1,0,0,-1));

        m2= new Mode(true, -1, "in long barrel mode", "Deal 2 damage to " +
                "1 target on any square exactly one move " +
                "away.", new Cash());
        m2.addEffect(new Effect(0,-1,1,1,-1,1,1,-1,-1,0,2,0,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);

        //building power glove

        weaponInConstruction= new Weapon("Power glove", new Cash(1, 0, 1), YELLOW);

        m1= new Mode(true, -1, "basic mode", "Choose 1 target on any square " +
                "exactly 1 move away. Move onto that square " +
                "and give the target 1 damage and 2 marks.", new Cash());
        m1.addEffect(new Effect(0,-1,1,1,-1,1,1,0,1,0,1,2,-1));

        m2= new Mode(true, -1, "in rocket fist mode", "Choose a square " +
                "exactly 1 move away. Move onto that square. " +
                "You may deal 2 damage to 1 target there. " +
                "If you want, you may move 1 more square in " +
                "that same direction (but only if it is a legal " +
                "move). You may deal 2 damage to 1 target " +
                "there, as well.", new Cash(1, 0, 0));
        m2.addEffect(new Effect(0,-1,1,1,-1,1,1,0,1,0,2,0,2));
        m2.addEffect(new Effect(2,3,2,2,1,1,0,0,1,0,2,0,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);

        //building shockwave
        weaponInConstruction= new Weapon("Shockwave", new Cash(0, 0, 1), YELLOW);

        m1= new Mode(true, -1, "basic mode", "Choose up to 3 targets on " +
                "different squares, each exactly 1 move away. " +
                "Deal 1 damage to each target.", new Cash());
        m1.addEffect(new Effect(0,-1,1,1,-1,1,1,-1,-1,0,1,0,-1));
        m1.addEffect(new Effect(0,-1,1,1,2,1,0,-1,-1,0,1,0,-1));
        m1.addEffect(new Effect(0,-1,1,1,2,1,0,-1,-1,0,1,0,-1));

        m2= new Mode(true, -1, "in tsunami mode", "Deal 1 damage to all " +
                "targets that are exactly 1 move away.", new Cash(0, 0, 1));
        m2.addEffect(new Effect(0, -1, -1, -1, -1, -1, 1, -1, -1, 6, 1, 0, -1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);

        //building sledgehammer

        weaponInConstruction= new Weapon("Sledgehammer", new Cash(0, 0, 1), YELLOW);

        m1= new Mode(true, -1, "basic mode", "Deal 2 damage to 1 target on " +
                "your square.", new Cash());
        m1.addEffect(new Effect(0,-1,0,0,-1,1,1,-1,-1,0,2,0,-1));

        m2= new Mode(true, -1, "in pulverize mode", "Deal 3 damage to 1 target " +
                "on your square, then move that target 0, 1, " +
                "or 2 squares in one direction.", new Cash(0, 1, 0));
        m2.addEffect(new Effect(0,-1,0,0,-1,1,1,-1,-1,0,2,0,-1));
        m2.addEffect(new Effect(1,-1,0,2,-1,0,1,2,0,-1,0,0,-1));

        weaponInConstruction.addMode(m1);
        weaponInConstruction.addMode(m2);

        weapons.add(weaponInConstruction);


//FINITOOO!!

    }

    public List<Weapon> getWeapons() {
        return weapons;
    }
}
