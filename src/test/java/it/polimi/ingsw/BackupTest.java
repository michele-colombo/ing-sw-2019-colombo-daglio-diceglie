package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class BackupTest {

    @Test
    public void backupTest(){
        Match match = new Match(2,8);

        Player p1 = new Player("Paul", GREY);
        Player p2 = new Player("John", YELLOW);
        Player p3 = new Player("Benjamin", GREEN);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);

        p1.addPoints(30);
        p2.addPoints(12);
        p3.addPoints(5);
        p1.setSquarePosition(match.getLayout().getSquare(0,2));
        p2.setSquarePosition(match.getLayout().getSquare(1,2));
        p3.setSquarePosition(match.getLayout().getSquare(3,2));

        Backup backup = new Backup(match.getPlayers());
        p1.addPoints(5);
        p2.addPoints(10);
        p3.addPoints(5);

        List<Player> toBackup = new ArrayList<>();
        toBackup.add(p1);
        toBackup.add(p2);
        backup.restoreAll(toBackup);
        assertEquals(30, p1.getPoints());
        assertEquals(12, p2.getPoints());
        assertEquals(10, p3.getPoints());

        backup = new Backup(toBackup);
        p2.setSquarePosition(match.getLayout().getSquare(0,2));
        backup.restore(p2);
        assertEquals(match.getLayout().getSquare(1,2), p2.getSquarePosition());

        PowerUp po1 = new PowerUp(Color.BLUE, PowerUpType.TARGETING_SCOPE);
        PowerUp po2 = new PowerUp(Color.YELLOW, PowerUpType.TAGBACK_GRANADE);
        p1.addPowerUp(po1);
        p1.addPowerUp(po2);
        backup = new Backup(toBackup);
        p1.removePowerUp(po1);
        backup.restore(p1);
        assertEquals(2, p1.getPowerUps().size());
    }
}