import java.rmi.Naming;
import java.rmi.RemoteException;

public class UnoServer {
	
	public static void main (String[] args) {
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry ready.");			
		} catch (RemoteException e) {
			System.out.println("RMI registry already running.");			
		}
		try {
			Naming.rebind("UnoManager", new UnoManager());
			System.out.println ("UnoServer is ready.");
		} catch (Exception e) {
			System.out.println ("UnoServer failed:");
			e.printStackTrace();
		}
	}
	
	
}
