package engine;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import api.*;

public class Engine extends UnicastRemoteObject implements Compute{
	private CellSpace cellspace;
	
	public Engine() throws RemoteException {
		super();
	}

	public CellSpace getCS(){
		if (cellspace == null) {
			cellspace = new CellSpace(12,100,100);
		}
		return cellspace;
	}
	
	public static void main(String[] args) {
		System.setProperty("java.security.policy", "D:\\rmi.policy");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		String name = "rmi://localhost/Compute";
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			Compute engine = new Engine();
			Naming.rebind(name, engine);
			System.out.println("ComputeEngine bound");
		} catch (Exception e) {
			System.err.println("ComputeEngine exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
