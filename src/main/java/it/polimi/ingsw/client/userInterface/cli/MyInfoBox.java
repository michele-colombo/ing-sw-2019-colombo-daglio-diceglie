package it.polimi.ingsw.client.userInterface.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.MatchView;
import it.polimi.ingsw.client.MyPlayer;
import it.polimi.ingsw.server.model.Cash;

import static it.polimi.ingsw.client.userInterface.cli.CliUtils.*;

public class MyInfoBox extends MiniBox {
    public MyInfoBox(int x, int y, int height, int width) {
        super(x, y, height, width);
    }

    @Override
    public void update(MatchView match) {
        MyPlayer me = match.getMyPlayer();
        insertSubBox(prepareBorder(height, width), 0,0);
        String title = "YOU: "+me.getName()+"   (points: "+me.getPoints()+")";
        insertText(title, (width-title.length())/2, 1, printColorOf(me.getColor()));
        if (!me.getPending().equals(new Cash(0,0,0))){
            String payString = "to pay: ";
            insertText(payString, 1, 2);
            insertLine(printCash(me.getPending()), payString.length(), 2 );
            String creditString = "paid: ";
            insertText(creditString, 16, 2);
            insertLine(printCash(me.getCredit()), creditString.length()+16, 2 );
            insertTextMultiline(Client.getStateDescription(me.getState()), 1, 3, width-2);
        } else {
            insertTextMultiline(Client.getStateDescription(me.getState()), 1, 2, width-2);
        }
    }
}
