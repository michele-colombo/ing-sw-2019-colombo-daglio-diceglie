package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.MessageVisitor;
import it.polimi.ingsw.communication.message.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends MessageVisitor, Remote {

    void visit(LoginMessage loginMessage) throws RemoteException;

    void visit(DisconnectionMessage disconnectionMessage) throws RemoteException;

    void visit(GenericMessage genericMessage) throws RemoteException;

    //void visit(UpdateMessage updateMessage) throws RemoteException;

    void visit(LayoutUpdateMessage layoutUpdateMessage) throws RemoteException;

    void visit(KillshotTrackUpdateMessage killshotTrackUpdate) throws RemoteException;

    void visit(CurrentPlayerUpdateMessage currentPlayerUpdate) throws RemoteException;

    void visit(StartMatchUpdateMessage startMatchUpdateMessage) throws RemoteException;

    void visit(PlayerUpdateMessage playerUpdateMessage) throws RemoteException;

    void visit(PaymentUpdateMessage paymentUpdateMessage) throws RemoteException;

    void visit(WeaponsUpdateMessage weaponsUpdateMessage) throws RemoteException;

    void visit(PowerUpUpdateMessage powerUpUpdateMessage) throws RemoteException;

    void visit(SelectablesUpdateMessage selectablesUpdateMessage) throws RemoteException;

    void visit(DamageUpdateMessage damageUpdateMessage) throws RemoteException;

    void visit(ConnectionUpdateMessage connectionUpdateMessage) throws RemoteException;

}
