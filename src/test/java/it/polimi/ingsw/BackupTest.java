package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.polimi.ingsw.testUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class BackupTest {

    @Test
    public void equalsBetweenBackupsFromIdenticalFiles(){
        //backup_1 and backup_1b contain exactly the same text
        Backup backup_a = Backup.initFromFile(BACKUP_TEST, "backup_1");
        Backup backup_b = Backup.initFromFile(BACKUP_TEST, "backup_1");

        assertEquals(backup_a, backup_b);
        assertTrue(backup_a.equals(backup_b));
    }

    @Test
    public void equalsBetweenDifferentBackups(){
        //backup_1 and backup_1b differs from
        Backup backup_a = Backup.initFromFile(BACKUP_TEST, "backup_1");
        Backup backup_b = Backup.initFromFile(BACKUP_TEST, "backup_1b");

        assertNotEquals(backup_a, backup_b);
    }

    @Test
    public void equalsWeaponsInDifferentOrder(){
        //backup_1 and backup_1c differs by the order of weapons in players
        //weapons order is not relevant, therefore they are equal
        Backup backup_a = Backup.initFromFile(BACKUP_TEST, "backup_1");
        Backup backup_b = Backup.initFromFile(BACKUP_TEST, "backup_1c");

        assertEquals(backup_a, backup_b);
    }

    @Test
    public void equalsWeaponsDifferentlyLoaded(){
        //backup_1 and backup_1d differs by the state of load of weapons
        //that is relevant, therefore they are not equal
        Backup backup_a = Backup.initFromFile(BACKUP_TEST, "backup_1");
        Backup backup_b = Backup.initFromFile(BACKUP_TEST, "backup_1d");

        assertNotEquals(backup_a, backup_b);
    }

    @Test
    public void equalsDifferentOrder(){
        //backup_1e contains many things in different order, but it is equal to backup_1
        Backup backup_a = Backup.initFromFile(BACKUP_TEST, "backup_1");
        Backup backup_b = Backup.initFromFile(BACKUP_TEST, "backup_1e");

        assertEquals(backup_a, backup_b);
    }

    @Test
    public void equalsPlayerDifferentOrder(){
        //players are in a different order (relevant for equality) so backups are different
        Backup backup_a = Backup.initFromFile(BACKUP_TEST, "backup_1");
        Backup backup_b = Backup.initFromFile(BACKUP_TEST, "backup_1f");

        assertNotEquals(backup_a, backup_b);
    }

    @Test
    public void equalsDifferentDamageList(){
        //damages are in a different order (relevant for equality), therefore backups are different
        Backup backup_a = Backup.initFromFile(BACKUP_TEST, "backup_1");
        Backup backup_b = Backup.initFromFile(BACKUP_TEST, "backup_1g");

        assertNotEquals(backup_a, backup_b);
    }

    @Test
    public void equalsDifferentName(){
        //a name is different, therefore backups are different
        Backup backup_a = Backup.initFromFile(BACKUP_TEST, "backup_1");
        Backup backup_b = Backup.initFromFile(BACKUP_TEST, "backup_1h");

        assertNotEquals(backup_a, backup_b);
    }

    @Test
    public void anotherEqualsTest(){
        //another 'random' equals test
        Backup backup_a = Backup.initFromFile(BACKUP_TEST, "backup_2a");
        Backup backup_b = Backup.initFromFile(BACKUP_TEST, "backup_2b");

        assertEquals(backup_a, backup_b);
    }

}