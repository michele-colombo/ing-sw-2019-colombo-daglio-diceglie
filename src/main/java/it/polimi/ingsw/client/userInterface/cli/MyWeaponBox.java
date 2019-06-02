package it.polimi.ingsw.client.userInterface.cli;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.WeaponView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.client.userInterface.cli.CliUtils.*;

public class MyWeaponBox extends MiniBox {
    public MyWeaponBox(int x, int y, int height, int width) {
        super(x, y, height, width);
    }

    @Override
    public void update(MatchView match) {
        Map<WeaponView, Boolean> myWeapons = new HashMap<>(match.getMyPlayer().getWeapons());
        //List<WeaponView> myUnloadedWeapons = new ArrayList<>(match.getMyPlayer().getUnloadedWeapons());
        insertSubBox(prepareBorder(height, width), 0, 0);
        if (myWeapons.isEmpty()){
            String noWeapons = "You have no weapons";
            insertText(noWeapons, (width-noWeapons.length())/2, height/2);
        } else {
            int i = 0;
            for (Map.Entry<WeaponView, Boolean> w : myWeapons.entrySet()){
                String weaponString = w.getKey().getName().toUpperCase();
                if (w.getValue() == true){
                    insertText(weaponString, 2, 1+i, OK_COLOR);
                } else {
                    weaponString = weaponString + " -> ";
                    insertText(weaponString, 2, 1+i, WRONG_COLOR);
                    insertLine(printCash(w.getKey().getCost()), weaponString.length()+2, 1+i);
                }
                i++;
            }
        }
    }
}
