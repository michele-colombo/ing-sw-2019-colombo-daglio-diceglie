package it.polimi.ingsw.client;

import it.polimi.ingsw.client.ClientExceptions.ConnectionInitializationException;
import it.polimi.ingsw.client.ClientExceptions.ForwardingException;
import it.polimi.ingsw.client.ClientExceptions.WrongSelectionException;
import it.polimi.ingsw.client.network.NetworkInterfaceClient;
import it.polimi.ingsw.client.network.RmiClient;
import it.polimi.ingsw.client.network.SocketClient;
import it.polimi.ingsw.client.user_interface.UserInterface;
import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.communication.events.*;
import it.polimi.ingsw.communication.message.*;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerState;

import java.util.*;

public class Client implements MessageVisitor {

    /**
     * the client's network
     */
    private NetworkInterfaceClient network;

    /**
     * client's user interface
     */
    private UserInterface userInterface;

    /**
     * client's name
     */
    private String name;

    /**
     * true if client is connected
     */
    private boolean connected;

    /**
     * current match
     */
    private MatchView match;

    /**
     * the state of connections (of all the players)
     */
    private Map<String, Boolean> connections;


    /**
     * Build a Client
     * @param userInterface the interface of the client
     */
    public Client(UserInterface userInterface){
        this.userInterface = userInterface;
        this.connected = false;
        connections = new HashMap<>();
    }

    /**
     * Create the connectoin for this client
     * @param connection type of connection: socket or rmi
     */
    public void createConnection(String connection){
        try {
            switch (connection.toLowerCase()) {

                case "socket":
                    network = new SocketClient(this);

                    break;
                case "rmi":
                    network = new RmiClient(this);
                break;


                default: //non deve succedere
                    break;
            }
            connected = true;
            userInterface.showLogin();

        }
        catch (ConnectionInitializationException e){
            userInterface.printError("Impossible to initiate connection");
            userInterface.showConnectionSelection();
        }

    }

    /**
     * Log in with a name
     * @param name name chosen
     */
    public void chooseName(String name){
        this.name = name;
        try{
            EventVisitable loginEvent = new LoginEvent(name);
            network.forward(loginEvent);
        } catch(ForwardingException e){
            userInterface.printError(e.getMessage());
            restart();
        }
        catch (NullPointerException e){
            userInterface.printError("Connection lost");
            restart();
        }
    }

    /**
     * display LoginMessage when arrives
     * @param message
     */
    @Override
    public synchronized void visit(LoginMessage message){
        userInterface.printLoginMessage(message.getResponse(), message.getLoginSuccessful());
    }

    /**
     * display the GenericMessage when arrives
     * @param message
     */
    @Override
    public synchronized void visit(GenericMessage message){
        //user_interface.printDisconnectionMessage(message.toString());
    }

    /**
     * update the connections status
     * @param connectionUpdateMessage
     */
    @Override
    public synchronized void visit(ConnectionUpdateMessage connectionUpdateMessage) {

        System.out.println("Connection update received");
        connections.clear();
        for (Map.Entry<String, Boolean> entry : connectionUpdateMessage.getConnectionStates().entrySet()){
            connections.put(entry.getKey(), entry.getValue());
        }
        userInterface.updateConnection(); //aggiunta per la Gui, potrebbe non funzionare per la Cli
    }

    /**
     * display the gameboard at the beginning of a match
     * @param startMatchUpdateMessage
     */
    public synchronized void visit(StartMatchUpdateMessage startMatchUpdateMessage) {
        //System.out.println("Start match update received");
        if (match == null){
            match = new MatchView(name, startMatchUpdateMessage.getLayoutConfiguration(), startMatchUpdateMessage.getNames(), startMatchUpdateMessage.getColors(), connections);
        }
        userInterface.updateStartMatch(match);
        //todo
    }

    /**
     * update the layout with new ammos and weapons
     * @param layoutUpdateMessage
     */
    public synchronized void visit(LayoutUpdateMessage layoutUpdateMessage) {
        //System.out.println("Layout update received");
        LayoutView layout = match.getLayout();
        DecksView decks = match.getDecks();

        List<WeaponView> blueWeapons = new ArrayList<>();
        for (String name : layoutUpdateMessage.getBlueWeapons()){
            blueWeapons.add(decks.getWeaponFromName(name));
        }
        layout.setBlueWeapons(blueWeapons);


        List<WeaponView> redWeapons = new ArrayList<>();
        for (String name : layoutUpdateMessage.getRedWeapons()){
            redWeapons.add(decks.getWeaponFromName(name));
        }
        layout.setRedWeapons(redWeapons);

        List<WeaponView> yellowWeapons = new ArrayList<>();
        for (String name : layoutUpdateMessage.getYellowWeapons()){
            yellowWeapons.add(decks.getWeaponFromName(name));
        }
        layout.setYellowWeapons(yellowWeapons);

        SquareView tempSquare;
        for (Map.Entry<String, String> entry : layoutUpdateMessage.getAmmoTilesInSquares().entrySet()){
            tempSquare = layout.getSquareFromString(entry.getKey());
            tempSquare.setAmmo(decks.getAmmoTileFromString(entry.getValue()));
        }
        userInterface.updateLayout();
    }

    /**
     * update the killshot track
     * @param killshotTrackUpdate
     */
    public synchronized void visit(KillshotTrackUpdateMessage killshotTrackUpdate) {
        //System.out.println("Killshot track update received");
        match.setSkulls(killshotTrackUpdate.getSkulls());
        match.setFrenzyOn(killshotTrackUpdate.isFrenzyOn());
        match.getMyPlayer().setPoints(killshotTrackUpdate.getYourPoints());

        List<Map<PlayerView, Integer>> tempTrack = new ArrayList<>();
        for (Map<String, Integer> map : killshotTrackUpdate.getTrack()){
            Map<PlayerView, Integer> temp = new HashMap<>();
            for (Map.Entry<String, Integer> entry : map.entrySet()){
                temp.put(match.getPlayerFromName(entry.getKey()), entry.getValue());
            }
            tempTrack.add(temp);
        }
        match.setTrack(tempTrack);
        userInterface.updateKillshotTrack();
    }

    /**
     * change the current player
     * @param currentPlayerUpdate
     */
    public synchronized void visit(CurrentPlayerUpdateMessage currentPlayerUpdate) {
        match.setCurrentPlayer(match.getPlayerFromName(currentPlayerUpdate.getCurrentPlayer()));
        System.out.println(currentPlayerUpdate.getCurrentPlayer());
        userInterface.updateCurrentPlayer();
    }

    /**
     * Change the status of a player (position, state, wallet)
     * @param playerUpdateMessage
     */
    public synchronized void visit(PlayerUpdateMessage playerUpdateMessage) {
        System.out.println("Player update received");
        PlayerView playerToUpdate = match.getPlayerFromName(playerUpdateMessage.getName());
        playerToUpdate.setState(playerUpdateMessage.getState());
        playerToUpdate.setSquarePosition(match.getLayout().getSquareFromString(playerUpdateMessage.getPosition()));
        playerToUpdate.setWallet(playerUpdateMessage.getWallet());
        userInterface.updatePlayer(playerToUpdate);
    }

    /**
     * displays a payment request
     * @param paymentUpdateMessage
     */
    public synchronized void visit(PaymentUpdateMessage paymentUpdateMessage) {
        System.out.println("Payment update received");
        match.getMyPlayer().setPending(paymentUpdateMessage.getPending());
        match.getMyPlayer().setCredit(paymentUpdateMessage.getCredit());
        userInterface.updatePayment();
    }

    /**
     * update weapons of a player
     * @param weaponsUpdateMessage
     */
    public synchronized void visit(WeaponsUpdateMessage weaponsUpdateMessage) {
        //System.out.println("Weapons update received");
        if (weaponsUpdateMessage.getName().equals(name)){
            MyPlayer me = match.getMyPlayer();
            Map<WeaponView, Boolean> temp = new HashMap<>();
            for (String s : weaponsUpdateMessage.getLoadedWeapons()){
                temp.put(match.getDecks().getWeaponFromName(s), true);
            }
            for (String s : weaponsUpdateMessage.getUnloadedWeapons()){
                temp.put(match.getDecks().getWeaponFromName(s), false);
            }
            me.setWeapons(temp);
        }
        PlayerView player = match.getPlayerFromName(weaponsUpdateMessage.getName());
        player.setNumLoadedWeapons(weaponsUpdateMessage.getNumeWeapons());
        List<WeaponView> unloadedWeapons = new ArrayList<>();
        for (String s : weaponsUpdateMessage.getUnloadedWeapons()){
            unloadedWeapons.add(match.getDecks().getWeaponFromName(s));
        }
        player.setUnloadedWeapons(unloadedWeapons);
        userInterface.updateWeapons(player);
    }

    /**
     * update powerups of a player
     * @param powerUpUpdateMessage
     */
    public synchronized void visit(PowerUpUpdateMessage powerUpUpdateMessage) {
        //System.out.println("PowerUp update received");
        if (powerUpUpdateMessage.getName().equals(name)){
            MyPlayer me = match.getMyPlayer();
            List<PowerUpView> powerUps = new ArrayList<>();
            for (String s : powerUpUpdateMessage.getPowerUps()){
                powerUps.add(match.getDecks().getPowerUpFromString(s));
            }
            me.setPowerUps(powerUps);
        }
        PlayerView player = match.getPlayerFromName(powerUpUpdateMessage.getName());
        player.setNumPowerUps(powerUpUpdateMessage.getNumPowerUps());
        userInterface.updatePowerUp(player);
    }

    /**
     * update damage tracks (damages and marks)
     * @param damageUpdateMessage
     */
    public synchronized void visit(DamageUpdateMessage damageUpdateMessage) {
        //System.out.println("Damage update received");
        PlayerView playerToUpdate = match.getPlayerFromName(damageUpdateMessage.getName());
        playerToUpdate.setSkulls(damageUpdateMessage.getSkulls());
        playerToUpdate.setFrenzy(damageUpdateMessage.isFrenzy());

        List<PlayerView> tempDamageList = new ArrayList<>();
        for (String damagerName : damageUpdateMessage.getDamageList()){
            tempDamageList.add(match.getPlayerFromName(damagerName));
        }
        playerToUpdate.setDamageList(tempDamageList);

        Map<PlayerView, Integer> tempMarkMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : damageUpdateMessage.getMarkMap().entrySet()){
            tempMarkMap.put(match.getPlayerFromName(entry.getKey()), entry.getValue());
        }
        playerToUpdate.setMarkMap(tempMarkMap);
        userInterface.updateDamage(playerToUpdate);
    }

    /**
     * update selectable items
     * @param selectablesUpdateMessage
     */
    public synchronized void visit(SelectablesUpdateMessage selectablesUpdateMessage) {
        //System.out.println("Selectables update received");
        MyPlayer me = match.getMyPlayer();

        List<WeaponView> selWeapons = new ArrayList<>();
        for (String s : selectablesUpdateMessage.getSelectableWeapons()){
            WeaponView weapon = match.getDecks().getWeaponFromName(s);
            if (weapon != null){
                selWeapons.add(weapon);
            }
        }
        me.setSelectableWeapons(selWeapons);

        List<SquareView> selSquares = new ArrayList<>();
        for (String s : selectablesUpdateMessage.getSelectableSquares()){
            SquareView square = match.getLayout().getSquareFromString(s);
            if (square!= null){
                selSquares.add(square);
            }
        }
        me.setSelectableSquares(selSquares);

        List<ModeView> selModes = new ArrayList<>();
        WeaponView currWeapon = match.getDecks().getWeaponFromName(selectablesUpdateMessage.getCurrWeapon());
        if (currWeapon != null) {
            for (String s : selectablesUpdateMessage.getSelectableModes()) {
                ModeView mode = currWeapon.getModeFromString(s);
                if (mode != null){
                    selModes.add(mode);
                }
            }
            me.setCurrWeapon(currWeapon);
        }
        me.setSelectableModes(selModes);

        List<Command> selCommands = new ArrayList<>();
        for (String s : selectablesUpdateMessage.getSelectableCommands()){
            try {
                selCommands.add(Command.valueOf(s));
            } catch (IllegalArgumentException e){

            }
        }
        me.setSelectableCommands(selCommands);

        me.setSelectableActions(selectablesUpdateMessage.getSelectableActions());

        List<AmmoColor> selColors = new ArrayList<>();
        for (String s : selectablesUpdateMessage.getSelectableColors()){
            try {
                selColors.add(AmmoColor.valueOf(s));
            } catch (IllegalArgumentException e){

            }
        }
        me.setSelectableColors(selColors);

        List<PlayerView> selPlayers = new ArrayList<>();
        for (String s : selectablesUpdateMessage.getSelectablePlayers()){
            PlayerView player = match.getPlayerFromName(s);
            if (player != null){
                selPlayers.add(player);
            }
        }
        me.setSelectablePlayers(selPlayers);

        List<PowerUpView> selPowerUps = new ArrayList<>();
        for (String s : selectablesUpdateMessage.getSelectablePowerUps()){
            PowerUpView powerUp = match.getDecks().getPowerUpFromString(s);
            if (powerUp != null){
                selPowerUps.add(powerUp);
            }
        }
        me.setSelectablePowerUps(selPowerUps);
        userInterface.updateSelectables();
        userInterface.showAndAskSelection();
    }

    /**
     * give points to players and displays game over message
     * @param gameOverMessage
     */
    @Override
    public synchronized void visit(GameOverMessage gameOverMessage) {

        Map<PlayerView, Integer> rank = new HashMap<>();
        Map<PlayerView, Integer> points = new HashMap<>();
        if (match != null){
            for (Map.Entry<String, Integer> entry : gameOverMessage.getRank().entrySet()){
                rank.put(match.getPlayerFromName(entry.getKey()), entry.getValue());
            }
            for (Map.Entry<String, Integer> entry : gameOverMessage.getPoints().entrySet()){
                points.put(match.getPlayerFromName(entry.getKey()), entry.getValue());
            }
            userInterface.showGameOver(rank, points);
            shutDown();
        } else {
            System.out.println("[ERROR] end without start");
        }
    }

    /**
     * send an event through the network
     * @param event Event to send
     */
    private void sendEvent(EventVisitable event){
        try {
            network.forward(event);
        } catch (ForwardingException e){
            userInterface.printError(e.getMessage());
            restart();
        }
    }

    /**
     * select a something from selectable items
     * @param selected the string identifier of the selected item
     * @throws WrongSelectionException if the selection is invalid
     */
    public synchronized void selected(String selected) throws WrongSelectionException {
        int indexSel;
        EventVisitable event;
        boolean found = false;

        WeaponView weapon = match.getDecks().getWeaponFromName(selected);
        if (weapon != null){
            indexSel = match.getMyPlayer().getSelectableWeapons().indexOf(weapon);
            if (indexSel<0) {
                throw new WrongSelectionException();
            }
            else {
                event = new WeaponSelectedEvent(indexSel);
                found = true;
                sendEvent(event);
            }
        }

        SquareView square = match.getLayout().getSquareFromString(selected);
        if (square != null){
            indexSel = match.getMyPlayer().getSelectableSquares().indexOf(square);
            if (indexSel<0) {
                throw new WrongSelectionException();
            }
            else {
                event = new SquareSelectedEvent(indexSel);
                found = true;
                sendEvent(event);
            }
        }

        if (match.getMyPlayer().getState() == PlayerState.CHOOSE_MODE){
            WeaponView currWeapon = match.getMyPlayer().getCurrWeapon();
            if (currWeapon != null){
                ModeView mode = currWeapon.getModeFromString(selected);
                if (mode != null){
                    indexSel = match.getMyPlayer().getSelectableModes().indexOf(mode);
                    if (indexSel<0){
                        throw new WrongSelectionException();
                    } else {
                        event = new ModeSelectedEvent(indexSel);
                        found = true;
                        sendEvent(event);
                    }
                }
            }
        }

        try {
            Command command = Command.valueOf(selected);
            indexSel = match.getMyPlayer().getSelectableCommands().indexOf(command);
            if (indexSel<0) {
                throw new WrongSelectionException();
            }
            else {
                event = new CommandSelectedEvent(indexSel);
                found = true;
                sendEvent(event);
            }
        } catch (IllegalArgumentException e ){}

        indexSel = match.getMyPlayer().getSelectableActions().indexOf(selected);
        if (indexSel >= 0) {
            event = new ActionSelectedEvent(indexSel);
            found = true;
            sendEvent(event);
        }

        try {
            AmmoColor color = AmmoColor.valueOf(selected);
            indexSel = match.getMyPlayer().getSelectableColors().indexOf(color);
            if (indexSel<0) {
                throw new WrongSelectionException();
            }
            else {
                event = new ColorSelectedEvent(indexSel);
                found = true;
                sendEvent(event);
            }
        } catch (IllegalArgumentException e ){}

        PlayerView player = match.getPlayerFromName(selected);
        if (player != null){
            indexSel = match.getMyPlayer().getSelectablePlayers().indexOf(player);
            if (indexSel<0) {
                throw new WrongSelectionException();
            }
            else {
                event = new PlayerSelectedEvent(indexSel);
                found = true;
                sendEvent(event);
            }
        }

        PowerUpView powerUp = match.getDecks().getPowerUpFromString(selected);
        if (powerUp != null){
            indexSel = match.getMyPlayer().getSelectablePowerUps().indexOf(powerUp);
            if (indexSel<0) {
                throw new WrongSelectionException();
            }
            else {
                event = new PowerUpSelectedEvent(indexSel);
                found = true;
                sendEvent(event);
            }
        }

        if (!found){
            throw new WrongSelectionException();
        }
    }

    /**
     *
     * @return the current matchview
     */
    public MatchView getMatch() {
        return match;
    }

    /**
     *
     * @return if client is connected
     */
    public boolean isConnected(){
        return connected;
    }

    /**
     *
     * @return the connection status of all players
     */
    public Map<String, Boolean> getConnections(){
        return connections;
    }

    /**
     * restart the client (from connection selection)
     */
    public void restart(){
        shutDown();
        userInterface.showConnectionSelection();
    }

    /**
     * close every active thread
     */
    public void shutDown(){
        System.out.println("I'm in Client.shutDown()");
        match = null;
        if(network != null) {
            network.closeConnection();
            network= null;
        }
    }

    public static String getStateDescription(PlayerState state){
        String result = "";
        switch (state){
            case IDLE:
                result = "You can't do anything at the moment.";
                break;
            case CHOOSE_ACTION:
                result = "Choose an action to do.";
                break;
            case MOVE_THERE:
                result = "Choose a square to move into.";
                break;
            case GRAB_THERE:
                result = "Choose a square to grab from.";
                break;
            case GRAB_WEAPON:
                result = "Choose a weapon to grab.";
                break;
            case DISCARD_WEAPON:
                result = "You must discard one of your weapon: choose one.";
                break;
            case RELOAD:
                result = "Choose a weapon to reload, or select OK to skip.";
                break;
            case PAYING:
                result = "You have to pay for something. Select a powerUp(s) or press OK to pay with ammos.";
                break;
            case PAYING_ANY:
                result = "Choose an ammo (or powerup) to pay";
                break;
            case SHOOT_WEAPON:
                result = "Choose a weapon to shoot with";
                break;
            case CHOOSE_MODE:
                result = "Select a mode to build the shoot. When you have done, select OK.";
                break;
            case SHOOT_TARGET:
                result = "Choose a target (either a player or a square) to shoot";
                break;
            case SPAWN:
                result = "Choose a powerup to discard (and spawn on the corresponding spawn point)";
                break;
            case USE_POWERUP:
                result = "Choose a powerup to use or select OK to skip";
                break;
            default:
                result = "";
        }
        return result;
    }


}
