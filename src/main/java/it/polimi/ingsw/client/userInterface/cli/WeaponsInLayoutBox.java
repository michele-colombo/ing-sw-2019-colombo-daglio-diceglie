package it.polimi.ingsw.client.userInterface.cli;

import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.WeaponView;
import it.polimi.ingsw.server.model.enums.AmmoColor;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.userInterface.cli.CliUtils.*;

public class WeaponsInLayoutBox extends MiniBox {
    AmmoColor color;
    public WeaponsInLayoutBox(int x, int y, int height, int width, AmmoColor color) {
        super(x, y, height, width);
        this.color = color;
    }

    @Override
    public void update(MatchView match) {
        List<WeaponView> myWeapons;
        switch (color){
            case BLUE:
                myWeapons = new ArrayList<>(match.getLayout().getBlueWeapons());
                break;
            case RED:
                myWeapons = new ArrayList<>(match.getLayout().getRedWeapons());
                break;
            case YELLOW:
                myWeapons = new ArrayList<>(match.getLayout().getYellowWeapons());
                break;
            default:
                myWeapons = new ArrayList<>();
        }
        String strColor = printColorOf(color);
        if (myWeapons.isEmpty()){
            insertText("There are no weapons", 1, height/2, strColor);
            insertText("in the "+color.toString().toLowerCase()+" spawnpoint", 1, height/2+1, strColor);
        } else {
            insertSubBox(prepareBorder(2*myWeapons.size()+1, width, strColor), 0,0);
            int i = 0;
            for (WeaponView w : myWeapons){
                insertText(w.getName().toUpperCase(), 2, 1+i*2, strColor);
                insertLine(printCash(w.getDiscountedCost()), 5+w.getName().length(), 1+i*2);
                if (i<myWeapons.size()-1){
                    insertMiddleRule(2+i*2, strColor);
                }
                i++;
            }
        }
    }
}
