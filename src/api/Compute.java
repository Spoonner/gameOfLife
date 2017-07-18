package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

import engine.CellSpace;

public interface Compute extends Remote {
	
	public CellSpace getCS() throws RemoteException;
}
