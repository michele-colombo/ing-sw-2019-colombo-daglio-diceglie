package it.polimi.ingsw;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayingTest {
    public static void main(String[] args){
        Player p1 = new Player("primo giocatore", PlayerColor.YELLOW);
        Player p2 = new Player("secondo giocatore", PlayerColor.BLUE);
        Player p3 = new Player("terzo giocatore", PlayerColor.GREEN);

        GameModel gm = new GameModel();
        gm.addPlayer(p1);
        gm.addPlayer(p2);
        gm.addPlayer(p3);

        gm.initMatch();
        Match match = gm.getMatch();

        List<Weapon> tempWeapons = new ArrayList<>();
        tempWeapons.add(new Weapon("distruttore", new Cash(2,0,0), Color.BLUE));
        tempWeapons.add(new Weapon("mitragliatrice", new Cash(1, 1, 0), Color.BLUE));
        tempWeapons.add(new Weapon("torpedine", new Cash(1,1,0), Color.BLUE));
        tempWeapons.add(new Weapon("cannone Vortex", new Cash(1,1,0), Color.RED));
        tempWeapons.add(new Weapon("vulcanizzatore", new Cash(1,1,0), Color.RED));
        tempWeapons.add(new Weapon("razzo termico", new Cash(0,2,1), Color.RED));
        tempWeapons.add(new Weapon("lanciafiamme", new Cash(0,1,0), Color.RED));
        tempWeapons.add(new Weapon("fucile laser", new Cash(1,0,2), Color.YELLOW));
        tempWeapons.add(new Weapon("spada fotonica", new Cash(0,1,1), Color.YELLOW));
        tempWeapons.add(new Weapon("fucile a pompa", new Cash(0,0,2), Color.YELLOW));
        tempWeapons.add(new Weapon("cyberguanto", new Cash(1,0,1), Color.YELLOW));
        tempWeapons.add(new Weapon("onda d'urto", new Cash(0,0,1), Color.YELLOW));
        match.getStackManager().initWeaponStack(tempWeapons);

        List<PowerUp> tempPowerups = new ArrayList<>();
        for (Color color : Color.getAmmoColors()){
            tempPowerups.add(new PowerUp(color, PowerUpType.TAGBACK_GRANADE));
            tempPowerups.add(new PowerUp(color, PowerUpType.TARGETING_SCOPE));
            tempPowerups.add(new PowerUp(color, PowerUpType.YOUR_TURN_POWERUP));
            tempPowerups.add(new PowerUp(color, PowerUpType.YOUR_TURN_POWERUP));
            tempPowerups.add(new PowerUp(color, PowerUpType.TAGBACK_GRANADE));
            tempPowerups.add(new PowerUp(color, PowerUpType.TARGETING_SCOPE));
            tempPowerups.add(new PowerUp(color, PowerUpType.YOUR_TURN_POWERUP));
            tempPowerups.add(new PowerUp(color, PowerUpType.YOUR_TURN_POWERUP));
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

        while (true){
            for (Player p : match.getPlayers()){
                if (p.hasSelectables()){
                    System.out.println("PLAYER: "+p.getName()+"\n");
                    System.out.println(p.getState());
                    if (p.getSquarePosition()!= null) System.out.println(p.getSquarePosition().getShortDescription());
                    System.out.println("wallet: "+p.getWallet());
                    System.out.println("pending: "+p.getPending());
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
                            Square sq = p.getSelectableSquares().get(Integer.parseInt(tokens[1].trim()));
                            switch (p.getState()){
                                case GRAB_THERE:
                                    gm.grabThere(p, sq);
                                    break;
                                case MOVE_THERE:
                                    gm.moveMeThere(p, sq);
                                    break;
                                case SHOOT_TARGET:
                                    //todo
                                    break;
                                default:
                                    System.out.println("selected a square in the wrong state");
                                    break;
                            }
                            break;
                        case "pow":
                            PowerUp po = p.getSelectablePowerUps().get(Integer.parseInt(tokens[1].trim()));
                            switch (p.getState()){
                                case SPAWN:
                                    gm.spawn(p, po);
                                    break;
                                case PAYING:
                                    gm.payWith(p, po);
                                    break;
                                default:
                                    System.out.println("selected a powerup in the wrong state");
                                    break;
                            }
                            break;
                        case "act":
                            Action act = p.getSelectableActions().get(Integer.parseInt(tokens[1].trim()));
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
                            Weapon wp = p.getSelectableWeapons().get(Integer.parseInt(tokens[1].trim()));
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
                            Mode mod = p.getSelectableModes().get(Integer.parseInt(tokens[1].trim()));
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
                            Command cmd = p.getSelectableCommands().get(Integer.parseInt(tokens[1].trim()));
                            switch (p.getState()){
                                case PAYING:
                                    if (cmd == Command.OK) gm.completePayment(p);
                                    break;
                                default:
                                    System.out.println("selected a command in the wrong state");
                                    break;
                            }
                            break;
                        case "pl":
                            Player pl = p.getSelectablePlayers().get(Integer.parseInt(tokens[1].trim()));
                            switch (p.getState()){
                                case SHOOT_TARGET:
                                    //todo
                                    break;
                                default:
                                    System.out.println("selected a player in the wrong state");
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
