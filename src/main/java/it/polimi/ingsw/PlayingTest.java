package it.polimi.ingsw;


import it.polimi.ingsw.exceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.exceptions.GameFullException;
import it.polimi.ingsw.exceptions.NameAlreadyTakenException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayingTest {
    public static void main(String[] args){
        Player p1 = new Player("primo giocatore", PlayerColor.YELLOW);
        Player p2 = new Player("secondo giocatore", PlayerColor.BLUE);
        Player p3 = new Player("terzo giocatore", PlayerColor.GREEN);

        GameModel gm = new GameModel();
        try{
            gm.addPlayer(p1);
            gm.addPlayer(p2);
            gm.addPlayer(p3);
        } catch(NameAlreadyTakenException e){

        } catch (ColorAlreadyTakenException e){

        } catch(GameFullException e){

        }

        gm.initMatch();
        Match match = gm.getMatch();

/*
        List<Weapon> tempWeapons = new ArrayList<>();
        tempWeapons.add(new Weapon("distruttore", new Cash(2,0,0), Color.BLUE));
        tempWeapons.add(new Weapon("mitragliatrice", new Cash(1, 1, 0), Color.BLUE));
        tempWeapons.add(new Weapon("torpedine", new Cash(1,1,0), Color.BLUE));
        tempWeapons.add(new Weapon("cannone Vortex", new Cash(1,1,0), Color.RED));
        tempWeapons.add(new Weapon("vulcanizzatore", new Cash(1,1,0), Color.RED));
        tempWeapons.add(new Weapon("razzo termico", new Cash(0,2,1), Color.RED));
        tempWeapons.add(new Weapon("lanciafiamme", new Cash(0,1,0), Color.RED));
        tempWeapons.add(new Weapon("tempWeaponsfucile laser", new Cash(1,0,2), Color.YELLOW));
        tempWeapons.add(new Weapon("spada fotonica", new Cash(0,1,1), Color.YELLOW));
        tempWeapons.add(new Weapon("fucile a pompa", new Cash(0,0,2), Color.YELLOW));
        tempWeapons.add(new Weapon("cyberguanto", new Cash(1,0,1), Color.YELLOW));
        tempWeapons.add(new Weapon("onda d'urto", new Cash(0,0,1), Color.YELLOW));
        match.getStackManager().initWeaponStack(tempWeapons);
*/

        match.getStackManager().initWeaponStack(new WeaponBuilder().getWeapons());

        List<PowerUp> tempPowerups = new ArrayList<>();
        for (Color color : Color.getAmmoColors()){
            for (int i=0; i<2; i++){
                tempPowerups.add(new PowerUp(color, PowerUpType.TAGBACK_GRENADE, "Tagback granade"));
                tempPowerups.add(new PowerUp(color, PowerUpType.TARGETING_SCOPE, "Targeting scope"));
                tempPowerups.add(new PowerUp(color, PowerUpType.ACTION_POWERUP, "Newton"));
                tempPowerups.add(new PowerUp(color, PowerUpType.ACTION_POWERUP, "Teleporter"));
            }
        }
        match.getStackManager().initPowerUpStack(tempPowerups);

        List<AmmoTile> tempAmmoTiles = new ArrayList<>();
        for (int i=0; i<4; i++){
            tempAmmoTiles.add(new AmmoTile(new Cash(2, 1, 0), false));
            tempAmmoTiles.add(new AmmoTile(new Cash(0, 1, 2), false));
            tempAmmoTiles.add(new AmmoTile(new Cash(0, 2, 1), false));
            tempAmmoTiles.add(new AmmoTile(new Cash(1, 0, 2), false));
            tempAmmoTiles.add(new AmmoTile(new Cash(2, 0, 1), false));
            tempAmmoTiles.add(new AmmoTile(new Cash(1, 1, 1), false));
            tempAmmoTiles.add(new AmmoTile(new Cash(1, 1, 0), true));
            tempAmmoTiles.add(new AmmoTile(new Cash(1, 0, 1), true));
            tempAmmoTiles.add(new AmmoTile(new Cash(0, 1, 1), true));
        }
        match.getStackManager().initAmmoTilesStack(tempAmmoTiles);

        gm.startMatch();

        //semplicita' per raccoglere subito
        for(Player p: match.getPlayers()){
            p.getWallet().deposit(new Cash(3, 3, 3));
        }

        while (true){
            for (Player p : match.getPlayers()){
                if (p.hasSelectables()){
                    System.out.println("PLAYER: "+p.getName()+"\n");
                    System.out.println(p.getState());
                    if (p.getSquarePosition()!= null) System.out.println(p.getSquarePosition().getShortDescription());
                    System.out.println("wallet: "+p.getWallet());
                    System.out.println("pending: "+p.getPending());

                    System.out.println("Damages: " + p.getDamageTrack().getDamageList().size() + "Marks:" + p.getDamageTrack().getMarkMap().size());

                    System.out.println("credit: "+p.getCredit());
                    System.out.println("powerups:");
                    for (PowerUp po : p.getPowerUps()){
                        System.out.println(po.toString());
                    }
                    System.out.println("weapons:");
                    for (Weapon w : p.getWeapons()){
                        if (p.isLoaded(w)) System.out.println("- "+w.getName()+"(LOADED)");
                        else System.out.println("- "+w.getName()+"(UNLOADED)");
                    }
                    System.out.println("select something from the following list: ");
                    System.out.println(p.selectablesToString());

                    Scanner scanner = new Scanner(System.in);
                    String inputStr = scanner.nextLine();
                    String[] tokens = inputStr.split("-");

                    switch (tokens[0]){
                        case "sq":
                            Square sq;
                            try {
                                sq = p.getSelectableSquares().get(Integer.parseInt(tokens[1].trim()));
                            } catch (IndexOutOfBoundsException e){
                                System.out.println("wrong square selection");
                                break;
                            }
                            switch (p.getState()){
                                case GRAB_THERE:
                                    gm.grabThere(p, sq);
                                    break;
                                case MOVE_THERE:
                                    gm.moveMeThere(p, sq);
                                    break;
                                case SHOOT_TARGET:
                                    gm.shootTarget(p, null, sq);
                                    break;
                                case USE_POWERUP:
                                    gm.choosePowerUpTarget(p, null, sq);
                                    break;
                                default:
                                    System.out.println("selected a square in the wrong state");
                                    break;
                            }
                            break;
                        case "pow":
                            PowerUp po;
                            try {
                                po = p.getSelectablePowerUps().get(Integer.parseInt(tokens[1].trim()));
                            } catch (IndexOutOfBoundsException e){
                                System.out.println("wrong powerup selection");
                                break;
                            }
                            switch (p.getState()){
                                case SPAWN:
                                    gm.spawn(p, po);
                                    break;
                                case PAYING:
                                    gm.payWith(p, po);
                                    break;
                                case PAYING_ANY:
                                    gm.payAny(p, po);
                                    break;
                                case USE_POWERUP:
                                    gm.usePowerUp(p, po);
                                    break;
                                default:
                                    System.out.println("selected a powerup in the wrong state");
                                    break;
                            }
                            break;
                        case "act":
                            Action act;
                            try {
                                act = p.getSelectableActions().get(Integer.parseInt(tokens[1].trim()));
                            } catch (IndexOutOfBoundsException e){
                                System.out.println("wrong action selection");
                                break;
                            }
                            switch (p.getState()){
                                case CHOOSE_ACTION:
                                    gm.performAction(p, act);
                                    break;
                                default:
                                    System.out.println("selected an action in the wrong state");
                                    break;
                            }
                            break;
                        case "wp":
                            Weapon wp;
                            try {
                                wp = p.getSelectableWeapons().get(Integer.parseInt(tokens[1].trim()));
                            } catch (IndexOutOfBoundsException e){
                                System.out.println("wrong weapon selection");
                                break;
                            }
                            switch (p.getState()){
                                case GRAB_WEAPON:
                                    gm.grabWeapon(p, wp);
                                    break;
                                case DISCARD_WEAPON:
                                    gm.discardWeapon(p, wp);
                                    break;
                                case SHOOT_WEAPON:
                                    //todo
                                    gm.shootWeapon(p, wp);
                                    break;
                                case RELOAD:
                                    gm.realoadWeapon(p, wp);
                                    break;
                                default:
                                    System.out.println("selected a weapon in the wrong state");
                                    break;
                            }
                            break;
                        case "mod":
                            Mode mod;
                            try {
                                mod = p.getSelectableModes().get(Integer.parseInt(tokens[1].trim()));
                            } catch (IndexOutOfBoundsException e){
                                System.out.println("wrong mode selection");
                                break;
                            }
                            switch (p.getState()){
                                case CHOOSE_MODE:
                                    gm.addMode(p, mod);
                                    break;
                                default:
                                    System.out.println("selected a mode in the wrong state");
                                    break;
                            }
                            break;
                        case "cmd":
                            //todo: complete with all possible commands
                            Command cmd;
                            try {
                                cmd = p.getSelectableCommands().get(Integer.parseInt(tokens[1].trim()));
                            } catch (IndexOutOfBoundsException e){
                                System.out.println("wrong command selection");
                                break;
                            }
                            switch (p.getState()){
                                case PAYING:
                                    if (cmd == Command.OK) gm.completePayment(p);
                                    break;
                                case CHOOSE_ACTION:
                                    if (cmd == Command.OK) gm.endTurn();
                                    break;
                                case CHOOSE_MODE:
                                    if (cmd == Command.OK) gm.confirmModes(p);
                                    break;
                                case USE_POWERUP:
                                    if (cmd == Command.OK) gm.dontUsePowerUp(p);
                                    break;
                                default:
                                    System.out.println("selected a command in the wrong state");
                                    break;
                            }
                            break;
                        case "pl":
                            Player pl;
                            try {
                                pl = p.getSelectablePlayers().get(Integer.parseInt(tokens[1].trim()));
                            } catch (IndexOutOfBoundsException e){
                                System.out.println("wrong player selection");
                                break;
                            };
                            switch (p.getState()){
                                case SHOOT_TARGET:
                                    gm.shootTarget(p, pl, null);
                                    break;
                                case USE_POWERUP:
                                    gm.choosePowerUpTarget(p, pl, null);
                                    break;
                                default:
                                    System.out.println("selected a player in the wrong state");
                                    break;
                            }
                            break;
                        case "col":
                            Color col;
                            try {
                                col = p.getSelectableColors().get(Integer.parseInt(tokens[1].trim()));
                            } catch (IndexOutOfBoundsException e){
                                System.out.println("wrong color selection");
                                break;
                            };
                            switch (p.getState()){
                                case PAYING_ANY:
                                    gm.payAny(p, col);
                                    break;
                                default:
                                    System.out.println("selected a color in the wrong state");
                                    break;
                            }
                            break;
                        default:
                            System.out.println("input not valid\n");
                    }
                }
            }
        }

    }
}
