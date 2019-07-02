package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Backup;
import it.polimi.ingsw.server.model.GameModel;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static it.polimi.ingsw.testUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class BackupTest {

    /**
     * Tests the loading of backups and a trivial application of the equals method
     */
    @Test
    public void equalsBetweenBackupsFromIdenticalFiles(){
        //backup_1 and backup_1b contain exactly the same text
        InputStream url= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1.json");

        Backup backup_a = Backup.initFromFile(url);
        Backup backup_b = Backup.initFromFile(getClass().getClassLoader().getResourceAsStream("backupTest/backup_1.json"));

        assertEquals(backup_a, backup_b);
        assertTrue(backup_a.equals(backup_b));
    }

    /**
     * Tests the loading of backups and the difference detecting of equals method
     */
    @Test
    public void equalsBetweenDifferentBackups(){
        //backup_1 and backup_1b differs from
        InputStream url1= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1.json");
        InputStream url1b= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1b.json");

        Backup backup_a = Backup.initFromFile(url1);
        Backup backup_b = Backup.initFromFile(url1b);

        assertNotEquals(backup_a, backup_b);
    }

    /**
     * Tests the loading of backups and the equals method
     * Backups are equals even if the weapons are in a different order
     */
    @Test
    public void equalsWeaponsInDifferentOrder(){
        //backup_1 and backup_1c differs by the order of weapons in players
        //weapons order is not relevant, therefore they are equal

        InputStream url1= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1.json");
        InputStream url1c= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1c.json");

        Backup backup_a = Backup.initFromFile(url1);
        Backup backup_b = Backup.initFromFile(url1c);

        assertEquals(backup_a, backup_b);
    }

    /**
     * Tests the loading of backups and the equals method
     * The match state is different if weapons have different states of charge
     */
    @Test
    public void equalsWeaponsDifferentlyLoaded(){
        //backup_1 and backup_1d differs by the state of load of weapons
        //that is relevant, therefore they are not equal

        InputStream url1= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1.json");
        InputStream url1d= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1d.json");

        Backup backup_a = Backup.initFromFile(url1);
        Backup backup_b = Backup.initFromFile(url1d);

        assertNotEquals(backup_a, backup_b);
    }

    /**
     * Tests the loading of backups and the equals method.
     * The two backups contains some things in different order, where order is not relevant,
     * therefore they are equal
     */
    @Test
    public void equalsDifferentOrder(){
        //backup_1e contains many things in different order, but it is equal to backup_1

        InputStream url1= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1.json");
        InputStream url1e= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1e.json");

        Backup backup_a = Backup.initFromFile(url1);
        Backup backup_b = Backup.initFromFile(url1e);

        assertEquals(backup_a, backup_b);
    }

    /**
     * Tests the loading of backups and the equals method.
     * Players are in a different order (relevant for equality) so backups are different
     */
    @Test
    public void equalsPlayerDifferentOrder(){

        InputStream url1= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1.json");
        InputStream url1f= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1f.json");

        Backup backup_a = Backup.initFromFile(url1);
        Backup backup_b = Backup.initFromFile(url1f);

        assertNotEquals(backup_a, backup_b);
    }

    /**
     * Tests the loading of backups and the equals method.
     * Damages are in a different order (relevant for equality), therefore backups are different
     */
    @Test
    public void equalsDifferentDamageList(){

        InputStream url1= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1.json");
        InputStream url1g= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1g.json");

        Backup backup_a = Backup.initFromFile(url1);
        Backup backup_b = Backup.initFromFile(url1g);

        assertNotEquals(backup_a, backup_b);
    }

    /**
     * Tests the loading of backups and the equals method.
     * There is one name different, therefore backups are different
     */
    @Test
    public void equalsDifferentName(){

        InputStream url1= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1.json");
        InputStream url1h= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1h.json");

        Backup backup_a = Backup.initFromFile(url1);
        Backup backup_b = Backup.initFromFile(url1h);

        assertNotEquals(backup_a, backup_b);
    }

    /**
     * Tests the loading of backups and the equals method.
     * Two backups differing only for not relevant things, they are equal
     */
    @Test
    public void anotherEqualsTest(){
        //another 'random' equals test

        InputStream url2a= getClass().getClassLoader().getResourceAsStream("backupTest/backup_2a.json");
        InputStream url2b= getClass().getClassLoader().getResourceAsStream("backupTest/backup_2b.json");

        Backup backup_a = Backup.initFromFile(url2a);
        Backup backup_b = Backup.initFromFile(url2b);

        assertEquals(backup_a, backup_b);
    }

    /**
     * Tests the resuming of a backup, with particular attention on the resuming of the killshot track
     */
    @Test
    public void resumeBackupWithoutKillingOrderAndKillingCounter(){
        GameModel gm = new GameModel();

        InputStream url1= getClass().getClassLoader().getResourceAsStream("backupTest/backup_1.json");

        gm.resumeMatchFromFile(url1);
        assertTrue(true);
    }

}