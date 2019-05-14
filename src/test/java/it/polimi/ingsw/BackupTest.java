package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class BackupTest {
    public void addPlayers(GameModel gm, List<Player> players){
        for (Player p : players){
            try {
                gm.addPlayer(p);
            } catch (NameAlreadyTakenException | GameFullException | AlreadyLoggedException | NameNotFoundException e){

            }
        }
    }
}