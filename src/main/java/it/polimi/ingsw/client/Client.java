package it.polimi.ingsw.client;

import it.polimi.ingsw.client.ClientExceptions.ConnectionInitializationException;
import it.polimi.ingsw.client.ClientExceptions.ForwardingException;
import it.polimi.ingsw.client.ClientExceptions.WrongSelectionException;
import it.polimi.ingsw.client.model_view.*;
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
import java.util.logging.Logger;

/**
 * The central node of the client, connects the user interface, the network interface and the small match representation
 * It is responsible of taking updates from the server, updating the small match, presenting to the user the match status
 * taking user's choices and sending them to the server
 */
public class Client implements MessageVisitor {
    /**
     * Used to create a socket connection
     */
    private static final String SOCKET_CHOICE = "socket";
    /**
     * Used to create a RMI connection
     */
    private static final String RMI_CHOICE = "rmi";
    /**
     * String representation of idle state
     */
    private static final String IDLE_STATE = "You can't do anything at the moment. ";
    /**
     * String representation of choose action state
     */
    private static final String CHOOSE_ACTION_STATE = "You are doing an action. ";
    /**
     * String representation of move there state
     */
    private static final String MOVE_THERE_STATE = "You are moving somewhere. ";
    /**
     * String representation of discard weapon state
     */
    private static final String DISCARD_WEAPON_STATE = "You are discarding a weapon. ";
    /**
     * String representation of reload state
     */
    private static final String RELOAD_STATE = "You are reloading. ";
    /**
     * String representation of paying state
     */
    private static final String PAYING_STATE = "You are paying. ";
    /**
     * String representation of paying any state state
     */
    private static final String PAYING_ANY_STATE = "You are paying. ";
    /**
     * String representation of shoot weapon state
     */
    private static final String SHOOT_WEAPON_STATE = "You are shooting. ";
    /**
     * String representation of choose mode state
     */
    private static final String CHOOSE_MODE_STATE = "You are choosing a mode. ";
    /**
     * String representation of shoot target state state
     */
    private static final String SHOOT_TARGET_STATE = "You are shooting. ";
    /**
     * String representation of spawn state state state
     */
    private static final String SPAWN_STATE = "You are spawning. ";
    /**
     * String representation of use power up state state
     */
    private static final String USE_POWERUP_STATE = "You are using (or receiving) a power up. ";
    /**
     * String representation of grab there state
     */
    private static final String GRAB_THERE_STATE = "You are grabbing. ";
    /**
     * String representation of grab weapon state
     */
    private static final String GRAB_WEAPON_STATE = "You are grabbing.";
    /**
     * String representation of default state
     */
    private static final String DEFAULT = "";
    /**
     * Used when socket connection is established properly
     */
    private static final String SOCKET_CONNECTION_CREATED = "Socket connection created";
    /**
     * Used when rmi connection is established properly
     */
    private static final String RMI_CONNECTION_CREATED = "RMI connection created";
    /**
     * Used when ConnectionInitializationException is caught
     */
    private static final String IMPOSSIBLE_TO_INITIATE_CONNECTION = "Impossible to initiate connection";
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

    private static final Logger logger = Logger.getLogger(Client.class.getName());


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
     * Create the connection for this client
     * @param connection type of connection: socket or rmi
     */
    public synchronized void createConnection(String connection){
        match= null;
        try {
            switch (connection.toLowerCase()) {

                case SOCKET_CHOICE:
                    network = new SocketClient(this);
                    logger.info(SOCKET_CONNECTION_CREATED);
                    break;
                case RMI_CHOICE:
                    network = new RmiClient(this);
                    logger.info(RMI_CONNECTION_CREATED);
                break;


                default: break;
            }
            connected = true;
            userInterface.showLogin();

        }
        catch (ConnectionInitializationException e){
            userInterface.printError(IMPOSSIBLE_TO_INITIATE_CONNECTION);
            userInterface.showConnectionSelection();
        }

    }

    /**
     * Log in with a name
     * @param name name chosen
     */
    public synchronized void chooseName(String name){
        this.name = name.trim();
        EventVisitable loginEvent = new LoginEvent(name);
        sendEvent(loginEvent);
    }

    /**
     * display LoginMessage when arrives
     * @param message message received
     */
    @Override
    public synchronized void visit(LoginMessage message){
        if (message.getLoginSuccessful()){
            this.name = message.getName();
        }
        userInterface.printLoginMessage(message.getResponse(), message.getLoginSuccessful());
    }

    /**
     * display the GenericMessage when arrives
     * @param message message received
     */
    @Override
    public synchronized void visit(GenericMessage message){
        //user_interface.printDisconnectionMessage(message.toString());
    }

    /**
     * update the connections status
     * @param connectionUpdateMessage message received
     */
    @Override
    public synchronized void visit(ConnectionUpdateMessage connectionUpdateMessage) {
        connections.clear();
        for (Map.Entry<String, Boolean> entry : connectionUpdateMessage.getConnectionStates().entrySet()){
            connections.put(entry.getKey(), entry.getValue());
        }
        userInterface.updateConnection();
    }

    /**
     * display the gameboard at the beginning of a match
     * @param startMatchUpdateMessage message received
     */
    public synchronized void visit(StartMatchUpdateMessage startMatchUpdateMessage) {
        if (match == null){
            match = new MatchView(name, startMatchUpdateMessage.getLayoutConfiguration(), startMatchUpdateMessage.getNames(), startMatchUpdateMessage.getColors(), connections);
        }
        userInterface.updateStartMatch(match);
    }

    /**
     * update the layout with new ammos and weapons
     * @param layoutUpdateMessage message received
     */
    public synchronized void visit(LayoutUpdateMessage layoutUpdateMessage) {
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
     * @param killshotTrackUpdate message received
     */
    public synchronized void visit(KillshotTrackUpdateMessage killshotTrackUpdate) {
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
     * @param currentPlayerUpdate message received
     */
    public synchronized void visit(CurrentPlayerUpdateMessage currentPlayerUpdate) {
        match.setCurrentPlayer(match.getPlayerFromName(currentPlayerUpdate.getCurrentPlayer()));
        userInterface.updateCurrentPlayer();
    }

    /**
     * Change the status of a player (position, state, wallet)
     * @param playerUpdateMessage message received
     */
    public synchronized void visit(PlayerUpdateMessage playerUpdateMessage) {
        PlayerView playerToUpdate = match.getPlayerFromName(playerUpdateMessage.getName());
        playerToUpdate.setState(playerUpdateMessage.getState());
        playerToUpdate.setSquarePosition(match.getLayout().getSquareFromString(playerUpdateMessage.getPosition()));
        playerToUpdate.setWallet(playerUpdateMessage.getWallet());
        userInterface.updatePlayer(playerToUpdate);
    }

    /**
     * displays a payment request
     * @param paymentUpdateMessage message received
     */
    public synchronized void visit(PaymentUpdateMessage paymentUpdateMessage) {
        match.getMyPlayer().setPending(paymentUpdateMessage.getPending());
        match.getMyPlayer().setCredit(paymentUpdateMessage.getCredit());
        userInterface.updatePayment();
    }

    /**
     * update weapons of a player
     * @param weaponsUpdateMessage message received
     */
    public synchronized void visit(WeaponsUpdateMessage weaponsUpdateMessage) {
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
     * @param powerUpUpdateMessage message received
     */
    public synchronized void visit(PowerUpUpdateMessage powerUpUpdateMessage) {
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
     * @param damageUpdateMessage message received
     */
    public synchronized void visit(DamageUpdateMessage damageUpdateMessage) {
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
     * @param selectablesUpdateMessage message received
     */
    public synchronized void visit(SelectablesUpdateMessage selectablesUpdateMessage) {
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
     * @param gameOverMessage message received
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
        }
    }

    /**
     * send an event through the network
     * @param event Event to send
     */
    private synchronized void sendEvent(EventVisitable event){
        if (network != null) {
            try {
                network.forward(event);
            } catch (ForwardingException e) {
                userInterface.printError(e.getMessage());
                restart();
            }
        } else {
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

        if (match != null) {
            WeaponView weapon = match.getDecks().getWeaponFromName(selected);
            if (weapon != null) {
                indexSel = match.getMyPlayer().getSelectableWeapons().indexOf(weapon);
                if (indexSel < 0) {
                    throw new WrongSelectionException();
                } else {
                    event = new WeaponSelectedEvent(indexSel);
                    found = true;
                    sendEvent(event);
                }
            }

            SquareView square = match.getLayout().getSquareFromString(selected);
            if (square != null) {
                indexSel = match.getMyPlayer().getSelectableSquares().indexOf(square);
                if (indexSel < 0) {
                    throw new WrongSelectionException();
                } else {
                    event = new SquareSelectedEvent(indexSel);
                    found = true;
                    sendEvent(event);
                }
            }

            if (match.getMyPlayer().getState() == PlayerState.CHOOSE_MODE) {
                WeaponView currWeapon = match.getMyPlayer().getCurrWeapon();
                if (currWeapon != null) {
                    ModeView mode = currWeapon.getModeFromString(selected);
                    if (mode != null) {
                        indexSel = match.getMyPlayer().getSelectableModes().indexOf(mode);
                        if (indexSel < 0) {
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
                if (indexSel < 0) {
                    throw new WrongSelectionException();
                } else {
                    event = new CommandSelectedEvent(indexSel);
                    found = true;
                    sendEvent(event);
                }
            } catch (IllegalArgumentException e) {
            }

            indexSel = match.getMyPlayer().getSelectableActions().indexOf(selected);
            if (indexSel >= 0) {
                event = new ActionSelectedEvent(indexSel);
                found = true;
                sendEvent(event);
            }

            try {
                AmmoColor color = AmmoColor.valueOf(selected);
                indexSel = match.getMyPlayer().getSelectableColors().indexOf(color);
                if (indexSel < 0) {
                    throw new WrongSelectionException();
                } else {
                    event = new ColorSelectedEvent(indexSel);
                    found = true;
                    sendEvent(event);
                }
            } catch (IllegalArgumentException e) {
            }

            PlayerView player = match.getPlayerFromName(selected);
            if (player != null) {
                indexSel = match.getMyPlayer().getSelectablePlayers().indexOf(player);
                if (indexSel < 0) {
                    throw new WrongSelectionException();
                } else {
                    event = new PlayerSelectedEvent(indexSel);
                    found = true;
                    sendEvent(event);
                }
            }

            PowerUpView powerUp = match.getDecks().getPowerUpFromString(selected);
            if (powerUp != null) {
                indexSel = match.getMyPlayer().getSelectablePowerUps().indexOf(powerUp);
                if (indexSel < 0) {
                    throw new WrongSelectionException();
                } else {
                    event = new PowerUpSelectedEvent(indexSel);
                    found = true;
                    sendEvent(event);
                }
            }


            if (!found){
                throw new WrongSelectionException();
            }
        }
    }

    /**
     * Gets matchView
     * @return the current matchView
     */
    public MatchView getMatch() {
        return match;
    }

    /**
     * Returns isConnected
     * @return if client is connected
     */
    public boolean isConnected(){
        return connected;
    }

    /**
     * Gets connections
     * @return the connection status of all players
     */
    public Map<String, Boolean> getConnections(){
        return connections;
    }

    /**
     * restart the client (from connection selection)
     */
    public synchronized void restart(){
        shutDown();
        userInterface.showConnectionSelection();
    }

    /**
     * close every active thread
     */
    public synchronized void shutDown(){
        connected = false;
        if(network != null) {
            network.closeConnection();
            network= null;
        }
    }

    /**
     * Gets state description a state
     * @param state state to be returned as a String
     * @return String description of state
     */
    public static String getStateDescription(PlayerState state){
        String result;
        switch (state){
            case IDLE:
                result = IDLE_STATE;
                break;
            case CHOOSE_ACTION:
                result = CHOOSE_ACTION_STATE;
                break;
            case MOVE_THERE:
                result = MOVE_THERE_STATE;
                break;
            case GRAB_THERE:
                result = GRAB_THERE_STATE;
                break;
            case GRAB_WEAPON:
                result = GRAB_WEAPON_STATE;
                break;
            case DISCARD_WEAPON:
                result = DISCARD_WEAPON_STATE;
                break;
            case RELOAD:
                result = RELOAD_STATE;
                break;
            case PAYING:
                result = PAYING_STATE;
                break;
            case PAYING_ANY:
                result = PAYING_ANY_STATE;
                break;
            case SHOOT_WEAPON:
                result = SHOOT_WEAPON_STATE;
                break;
            case CHOOSE_MODE:
                result = CHOOSE_MODE_STATE;
                break;
            case SHOOT_TARGET:
                result = SHOOT_TARGET_STATE;
                break;
            case SPAWN:
                result = SPAWN_STATE;
                break;
            case USE_POWERUP:
                result = USE_POWERUP_STATE;
                break;
            default:
                result = DEFAULT;
        }
        return result;
    }


}
