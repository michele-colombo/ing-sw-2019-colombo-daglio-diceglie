package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class BackupTest {
    private static final String testBackupPath = "./src/test/resources/backupTest/";

    public void addPlayers(GameModel gm, List<Player> players){
        for (Player p : players){
            try {
                gm.addPlayer(p);
            } catch (NameAlreadyTakenException | GameFullException | AlreadyLoggedException | NameNotFoundException e){

            }
        }
    }


    @Test
    public void equalsBetweenBackupsFromIdenticalFiles(){
        //backup_1 and backup_1b contain exactly the same text
        Backup backup_a = Backup.initFromFile(testBackupPath, "backup_1");
        Backup backup_b = Backup.initFromFile(testBackupPath, "backup_1");

        assertEquals(backup_a, backup_b);
        assertTrue(backup_a.equals(backup_b));
    }

    @Test
    public void equalsBetweenDifferentBackups(){
        //backup_1 and backup_1b differs from
        Backup backup_a = Backup.initFromFile(testBackupPath, "backup_1");
        Backup backup_b = Backup.initFromFile(testBackupPath, "backup_1b");

        assertNotEquals(backup_a, backup_b);
    }

    @Test
    public void equalsWeaponsInDifferentOrder(){
        //backup_1 and backup_1c differs by the order of weapons in players
        //weapons order is not relevant, therefore they are equal
        Backup backup_a = Backup.initFromFile(testBackupPath, "backup_1");
        Backup backup_b = Backup.initFromFile(testBackupPath, "backup_1c");

        assertEquals(backup_a, backup_b);
    }

    @Test
    public void equalsWeaponsDifferentlyLoaded(){
        //backup_1 and backup_1d differs by the state of load of weapons
        //that is relevant, therefore they are not equal
        Backup backup_a = Backup.initFromFile(testBackupPath, "backup_1");
        Backup backup_b = Backup.initFromFile(testBackupPath, "backup_1d");

        assertNotEquals(backup_a, backup_b);
    }

    @Test
    public void equalsDifferentOrder(){
        //backup_1e contains many things in different order, but it is equal to backup_1
        Backup backup_a = Backup.initFromFile(testBackupPath, "backup_1");
        Backup backup_b = Backup.initFromFile(testBackupPath, "backup_1e");

        assertEquals(backup_a, backup_b);
    }

    @Test
    public void equalsPlayerDifferentOrder(){
        //players are in a different order (relevant for equality) so backups are different
        Backup backup_a = Backup.initFromFile(testBackupPath, "backup_1");
        Backup backup_b = Backup.initFromFile(testBackupPath, "backup_1f");

        assertNotEquals(backup_a, backup_b);
    }

    @Test
    public void equalsDifferentDamageList(){
        //damages are in a different order (relevant for equality), therefore backups are different
        Backup backup_a = Backup.initFromFile(testBackupPath, "backup_1");
        Backup backup_b = Backup.initFromFile(testBackupPath, "backup_1g");

        assertNotEquals(backup_a, backup_b);
    }




}