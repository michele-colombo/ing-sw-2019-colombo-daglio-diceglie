package it.polimi.ingsw;


import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PlayingTest {
    private static final String backupPath = "./src/main/resources/backups/";
    public static void main(String[] args){

        GameModel gm = new GameModel();


        System.out.println("Do you want to checkStart a new game or resume one from file?");
        System.out.println("type new or resume");
        String choice = new Scanner(System.in).nextLine();
        if(choice.toLowerCase().equals("new")){

            Player p1 = new Player("antonio");
            Player p2 = new Player("gianfranco");
            Player p3 = new Player("enrico");
            Player p4 = new Player("matteo");
            Player p5 = new Player("evila");
            try{
                gm.addPlayer(p1);
                gm.addPlayer(p2);
                gm.addPlayer(p3);
                gm.addPlayer(p4);
                gm.addPlayer(p5);
            } catch(NameAlreadyTakenException | AlreadyLoggedException | GameFullException | NameNotFoundException e){
            }
            gm.startMatch();
        } else if(choice.toLowerCase().equals("resume")) {
            System.out.println("Insert name of file (without .json):");
            String name = new Scanner(System.in).nextLine();
            gm.resumeMatchFromFile(backupPath, name);
        }

        Match match = gm.getMatch();

        while (true){
            System.out.print("waiting for this players: ");
            for (Player waitingPlayer : gm.getWaitingFor()){
                System.out.print(waitingPlayer.getName());
            }
            for (Player p : match.getPlayers()){
                System.out.print("\n");
                if (p.hasSelectables()){
                    System.out.println("PLAYER: "+p.getName()+"\n");
                    System.out.println(p.getState());
                    if (p.getSquarePosition()!= null) System.out.println(p.getSquarePosition().getShortDescription());
                    System.out.println("wallet: "+p.getWallet());
                    System.out.println("pending: "+p.getPending());

                    System.out.println("Damages: " + p.getDamageTrack().getDamageList().size());
                    System.out.println("Marks: ");
                    for (Map.Entry<Player, Integer> entry : p.getDamageTrack().getMarkMap().entrySet()){
                        System.out.println("\t"+entry.getKey().getName()+": "+entry.getValue());
                    }

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
                            } catch (NumberFormatException e){
                                System.out.println("command must be like: type-#");
                                System.out.println("where # is a number and type is: wp, sq, pow, act, ...");
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
                            } catch (NumberFormatException e){
                                System.out.println("command must be like: type-#");
                                System.out.println("where # is a number and type is: wp, sq, pow, act, ...");
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
                            } catch (NumberFormatException e){
                                System.out.println("command must be like: type-#");
                                System.out.println("where # is a number and type is: wp, sq, pow, act, ...");
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
                            } catch (NumberFormatException e){
                                System.out.println("command must be like: type-#");
                                System.out.println("where # is a number and type is: wp, sq, pow, act, ...");
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
                                    gm.reloadWeapon(p, wp);
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
                            } catch (NumberFormatException e){
                                System.out.println("command must be like: type-#");
                                System.out.println("where # is a number and type is: wp, sq, pow, act, ...");
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
                            } catch (NumberFormatException e){
                                System.out.println("command must be like: type-#");
                                System.out.println("where # is a number and type is: wp, sq, pow, act, ...");
                                break;
                            }
                            if (cmd == Command.OK){
                                switch (p.getState()){
                                    case PAYING:
                                        gm.completePayment(p);
                                        break;
                                    case CHOOSE_ACTION:
                                        gm.endTurn();
                                        break;
                                    case CHOOSE_MODE:
                                        gm.confirmModes(p);
                                        break;
                                    case USE_POWERUP:
                                        gm.dontUsePowerUp(p);
                                        break;
                                    case SHOOT_TARGET:
                                        gm.shootTarget(p, null, null);
                                        break;
                                    default:
                                        System.out.println("selected OK in the wrong state");
                                        break;
                                }
                            } else if (cmd == Command.BACK){
                                gm.restore();
                            }
                            break;
                        case "pl":
                            Player pl;
                            try {
                                pl = p.getSelectablePlayers().get(Integer.parseInt(tokens[1].trim()));
                            } catch (IndexOutOfBoundsException e){
                                System.out.println("wrong player selection");
                                break;
                            } catch (NumberFormatException e){
                                System.out.println("command must be like: type-#");
                                System.out.println("where # is a number and type is: wp, sq, pow, act, ...");
                                break;
                            }
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
                            } catch (NumberFormatException e){
                                System.out.println("command must be like: type-#");
                                System.out.println("where # is a number and type is: wp, sq, pow, act, ...");
                                break;
                            }
                            switch (p.getState()){
                                case PAYING_ANY:
                                    gm.payAny(p, col);
                                    break;
                                default:
                                    System.out.println("selected a color in the wrong state");
                                    break;
                            }
                            break;
                        case "save":
                            Backup b1 = new Backup(match);
                            System.out.println("Insert file name (without .json):");
                            String name = new Scanner(System.in).nextLine();
                            b1.saveOnFile(name);
                            break;
                        case "back":
                            System.out.println("Insert file to restore (only for test)");
                            String name1 = new Scanner((System.in)).nextLine();
                            Backup backup = Backup.initFromFile(name1);
                            backup.restore(gm.getMatch());
                            gm.actionCompleted();
                            break;
                        case "situa":
                            for (Player tempPlayer : match.getPlayers()){
                                System.out.println(tempPlayer.getName()+" in "+tempPlayer.getSquarePosition());
                                System.out.print("\tweapons:");
                                for (Weapon w : tempPlayer.getWeapons()){
                                    if (tempPlayer.isLoaded(w)) System.out.print("\t- "+w.getName()+"(LOADED)");
                                    else System.out.print("\t- "+w.getName()+"(UNLOADED)");
                                }
                                System.out.println();
                                System.out.print("\tDamages: " + tempPlayer.getDamageTrack().getDamageList().size()+"\n");
                                System.out.print("\tMarks: ");
                                for (Map.Entry<Player, Integer> entry : tempPlayer.getDamageTrack().getMarkMap().entrySet()){
                                    System.out.print("\t- "+entry.getKey().getName()+": "+entry.getValue());
                                }
                                System.out.println();
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
