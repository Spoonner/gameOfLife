package client;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.util.ArrayList;

import api.CellSpaceInterface;
import api.Compute;

public class Client {

	public static void main(String[] args) {
		System.setProperty("java.security.policy", "D:\\rmi.policy");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		try {
			String[] ipList = new String[3];
			ArrayList<CellSpaceInterface> csList = new ArrayList<CellSpaceInterface>();
			ipList[0] = "localhost";
			ipList[1] = "127.0.0.2";
			ipList[2] = "127.0.0.3";
//			ipList[3] = "127.0.0.4";
			for (int i=0;i<ipList.length;i++){
				String name = "rmi://"+ipList[i]+"/Compute";
	            Compute compute = (Compute) Naming.lookup(name);
	            csList.add(compute.getCS());
			}
            
            GameOfLife gol = new GameOfLife(csList, 1);
        } catch (Exception e) {
            System.err.println("ComputePi exception: " + e.getMessage());
            e.printStackTrace();
        }
	}

}
