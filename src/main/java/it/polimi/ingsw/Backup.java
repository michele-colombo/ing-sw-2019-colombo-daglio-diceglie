package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * It contains information in order to make a rollback during a match
 */
public class Backup {
    /**
     * In contains each player and his own points
     */
    private Map<Player, Integer> points;
    /**
     * It contains each player and his own weapons, and if they're loaded or not
     */
    private Map<Player, Map<Weapon, Boolean>> weapons;
    /**
     * It contains each player and his own power up
     */
    private Map<Player, List<PowerUp>> powerUps;
    /**
     * It contains each player and his own position
     */
    private Map<Player, Square> positions;

    public Backup(List<Player> players){
        points = backupPoints(players);
        weapons = backupWeapons(players);
        powerUps = backupPowerUps(players);
        positions = backupPosition(players);
    }

    /**
     * It returns a map containing each player and his own points
     * @param players The players of which it creates a backup
     * @return An HashMap
     */
    private Map<Player, Integer> backupPoints(List<Player> players){
        Map<Player, Integer> backup = new HashMap<>();
        for(Player p : players){
            backup.put(p, p.getPoints());
        }
        return backup;
    }

    /**
     * It returns a map containing each player and his own weapons, and if they're loaded or not
     * @param players The players of which it creates a backup
     * @return An HashMap
     */
    private Map<Player, Map<Weapon, Boolean>> backupWeapons(List<Player> players){
        Map<Player, Map<Weapon, Boolean>> backup = new HashMap<>();
        for(Player p : players){
            backup.put(p, new HashMap<>(p.getWeaponAsMap()));
        }
        return backup;
    }

    /**
     * It returns a map containing each player and his own power up
     * @param players The players of which it creates a backup
     * @return An HashMap
     */
    private Map<Player, List<PowerUp>> backupPowerUps(List<Player> players){
        Map<Player, List<PowerUp>> backup = new HashMap<>();
        for(Player p : players){
            List<PowerUp> powerUps = new ArrayList<>(p.getPowerUps());
            backup.put(p, p.getPowerUps());
        }
        return backup;
    }

    /**
     * It returns a map containing each player and his own position
     * @param players The players of which it creates a backup
     * @return An HashMap
     */
    private Map<Player, Square> backupPosition(List<Player> players){
        Map<Player, Square> backup = new HashMap<>();
        for(Player p : players){
            backup.put(p, p.getSquarePosition());
        }
        return backup;
    }

    /**
     * It restores a backup to a player
     * @param p The player to whom it restores a backup
     */
    public void restore(Player p){
        p.setPoints(points.get(p));
        p.setWeapons(weapons.get(p));
        p.setPowerUps(powerUps.get(p));
        p.setSquarePosition(positions.get(p));
    }

    /**
     * It restores a backup to each player
     * @param players The players to whom it restores a backup
     */
    public void restoreAll(List<Player> players){
        for(Player p : players){
            restore(p);
        }
    }
}
