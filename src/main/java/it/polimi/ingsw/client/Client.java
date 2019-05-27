package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.NetworkInterfaceClient;
import it.polimi.ingsw.client.network.RmiClient;
import it.polimi.ingsw.client.network.SocketClient;
import it.polimi.ingsw.client.userInterface.ClientView;
import it.polimi.ingsw.client.userInterface.Gui;
import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.communication.events.*;
import it.polimi.ingsw.communication.message.*;
import it.polimi.ingsw.server.model.AmmoTile;
import it.polimi.ingsw.server.model.enums.AmmoColor;
import it.polimi.ingsw.server.model.enums.Command;
import it.polimi.ingsw.server.model.enums.PlayerState;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client extends UnicastRemoteObject implements ClientInterface {

    private NetworkInterfaceClient network;
    private ClientView clientView;
    private String name;
    private String ip;
    private int port;
    private boolean connected;
    private MatchView match;
    private Map<String, Boolean> connections;


    public Client(ClientView clientView) throws RemoteException {
        this.clientView = clientView;
        this.ip = ClientMain.config.getIp();
        this.port = ClientMain.config.getPort();
        this.connected = false;
        connections = new HashMap<>();
    }

    public void login(String choice, String name){
        createConnection(choice);
        chooseName(name);
    }

    public void createConnection(String connection){
        try {
            switch (connection) {
                case "socket":
                    network = new SocketClient(this.ip, this.port, this);
                    connected = true;
                    break;
                case "rmi":
                    network = new RmiClient(this.ip, this.port, this);
                    connected = true;
                    break;
                default: //non deve succedere
                    break;
            }

            //network.startNetwork();
        }
        catch (IOException e){
            System.out.println("Error while creating the network. Try again logging in");
            connected = false;
            //clientView.askLogin();
        }
    }

    public void chooseName(String name){
        this.name = name;
        try{

            EventVisitable loginEvent = new LoginEvent(name);
            network.forward(loginEvent);

        } catch(IOException e){
            System.out.println("Error while forwarding the login event!");
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void visit(LoginMessage message){
        clientView.printLoginMessage(message.toString(), message.getLoginSuccessful());
    }

    @Override
    public synchronized void visit(DisconnectionMessage message){
        clientView.printDisconnectionMessage(message.toString());
    }

    @Override
    public synchronized void visit(GenericMessage message){
        clientView.printDisconnectionMessage(message.toString());
    }

    @Override
    public synchronized void visit(ConnectionUpdateMessage connectionUpdateMessage) {
        System.out.println("Connection update received");
        connections = new HashMap<>(connectionUpdateMessage.getConnectionStates());
        clientView.updateConnection(); //aggiunta per la Gui, potrebbe non funzionare per la Cli
    }

    @Override
    public synchronized void visit(StartMatchUpdateMessage startMatchUpdateMessage) {
        System.out.println("Start match update received");
        match = new MatchView(name, startMatchUpdateMessage.getLayoutConfiguration(), startMatchUpdateMessage.getNames(), startMatchUpdateMessage.getColors(), connections);
        clientView.initialize(match);
        clientView.startMatchUpdate(match);
        //todo
    }

    @Override
    public synchronized void visit(LayoutUpdateMessage layoutUpdateMessage) {
        System.out.println("Layout update received");
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
        clientView.updateLayout();
    }

    @Override
    public synchronized void visit(KillshotTrackUpdateMessage killshotTrackUpdate) {
        System.out.println("Killshot track update received");
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
        clientView.updateKillshotTrack();
    }

    @Override
    public synchronized void visit(CurrentPlayerUpdateMessage currentPlayerUpdate) {
        System.out.println("current player update received");
        match.setCurrentPlayer(match.getPlayerFromName(currentPlayerUpdate.getCurrentPlayer()));
        clientView.updateCurrentPlayer();
    }

    @Override
    public synchronized void visit(PlayerUpdateMessage playerUpdateMessage) {
        System.out.println("Player update received");
        PlayerView playerToUpdate = match.getPlayerFromName(playerUpdateMessage.getName());
        playerToUpdate.setState(playerUpdateMessage.getState());
        playerToUpdate.setSquarePosition(match.getLayout().getSquareFromString(playerUpdateMessage.getPosition()));
        playerToUpdate.setWallet(playerUpdateMessage.getWallet());
        clientView.updatePlayer(playerToUpdate);
    }

    @Override
    public synchronized void visit(PaymentUpdateMessage paymentUpdateMessage) {
        System.out.println("Payment update received");
        match.getMyPlayer().setPending(paymentUpdateMessage.getPending());
        match.getMyPlayer().setCredit(paymentUpdateMessage.getCredit());
        clientView.updatePayment();
    }

    @Override
    public synchronized void visit(WeaponsUpdateMessage weaponsUpdateMessage) {
        System.out.println("Weapons update received");
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
        clientView.updateWeapons(player);
    }

    @Override
    public synchronized void visit(PowerUpUpdateMessage powerUpUpdateMessage) {
        System.out.println("PowerUp update received");
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
        clientView.updatePowerUp(player);
    }

    @Override
    public synchronized void visit(DamageUpdateMessage damageUpdateMessage) {
        System.out.println("Damage update received");
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
        clientView.updateDamage(playerToUpdate);
    }

    @Override
    public synchronized void visit(SelectablesUpdateMessage selectablesUpdateMessage) {
        System.out.println("Selectables update received");
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

        clientView.showAndAskSelection();
    }

    private void sendEvent(EventVisitable event){
        try {
            network.forward(event);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

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

    public MatchView getMatch() {
        return match;
    }

    public boolean isConnected(){
        return connected;
    }

    public Map<String, Boolean> getConnections(){
        return connections;
    }

    public void restart(){
        //todo whan restarting
    }

}
