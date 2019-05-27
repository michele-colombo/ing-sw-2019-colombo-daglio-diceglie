package it.polimi.ingsw.server;

import it.polimi.ingsw.communication.EventVisitor;
import it.polimi.ingsw.communication.events.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerViewInterface extends EventVisitor, Remote {


     void visit(LoginEvent loginEvent) throws RemoteException;

     void visit(SquareSelectedEvent squareSelectedEvent) throws RemoteException;


     void visit(ActionSelectedEvent actionSelectedEvent) throws RemoteException;


     void visit(PlayerSelectedEvent playerSelectedEvent) throws RemoteException;
     void visit(WeaponSelectedEvent weaponSelectedEvent) throws RemoteException;
     void visit(ModeSelectedEvent modeSelectedEvent) throws RemoteException;

     void visit(CommandSelectedEvent commandSelectedEvent) throws RemoteException;
     void visit(ColorSelectedEvent colorSelectedEvent) throws RemoteException;
     void visit(PowerUpSelectedEvent powerUpSelectedEvent) throws RemoteException;





}
